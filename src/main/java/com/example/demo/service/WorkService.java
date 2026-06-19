package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AssignmentRequest;
import com.example.demo.dto.AssignmentResponse;
import com.example.demo.dto.AssignmentUpdateRequest;
import com.example.demo.dto.GuideResponse;
import com.example.demo.dto.OrderInfoResponse;
import com.example.demo.dto.OrderSpecialRequestProjection;
import com.example.demo.dto.ServiceInfoResponse;
import com.example.demo.dto.SpecialRequestTypeResponse;
import com.example.demo.dto.WorkDetailProjection;
import com.example.demo.dto.WorkFilter;
import com.example.demo.dto.WorkGuideDetailProjection;
import com.example.demo.dto.WorkGuideProjection;
import com.example.demo.dto.WorkListProjection;
import com.example.demo.dto.WorkListResponse;
import com.example.demo.dto.WorkOrderListProjection;
import com.example.demo.dto.WorkOrderListResponse;
import com.example.demo.dto.WorkOrderProjection;
import com.example.demo.dto.WorkSearchResponse;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Work;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.WorkRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkService {
  private final WorkRepository workRepository;
  private final AssignmentRepository assignmentRepository;

  public WorkSearchResponse findAll(
    WorkFilter filter,
    Pageable pageable) {

    filter.setPrivateFilter(buildPrivateFilter(filter));
    List<Long> allWorkIds;
    if (filter.getTourDate() != null) {
      allWorkIds = workRepository.findAllWorkIdsByTourDate(
          filter,
          filter.getTourDate());
    } else {
      allWorkIds = workRepository.findAllWorkIds(filter);
    }

    long totalAdultCount = 0;
    long totalChildCount = 0;
  
    if (!allWorkIds.isEmpty()) {
      List<WorkOrderProjection> allRows = workRepository.findOrdersByWorkIds(allWorkIds);

      Map<Long, WorkOrderProjection> uniqueOrders = allRows.stream()
          .collect(Collectors.toMap(
              WorkOrderProjection::getOrderId,
              Function.identity(),
              (first, second) -> first));
      totalAdultCount = uniqueOrders.values()
          .stream()
          .mapToLong(o -> Optional.ofNullable(o.getAdultCount())
              .orElse(0))
          .sum();
      totalChildCount = uniqueOrders.values()
          .stream()
          .mapToLong(o -> Optional.ofNullable(o.getChildCount())
              .orElse(0))
          .sum();
    }

    Page<WorkListProjection> workPage;
    if (filter.getTourDate() != null) {
      workPage = workRepository.findWorkPageByTourDate(
          filter,
          filter.getTourDate(),
          pageable);
    } else {
      workPage = workRepository.findWorkPage(filter, pageable);
    }

    if (workPage.isEmpty()) {
      return WorkSearchResponse.builder()
          .content(Collections.emptyList())
          .totalElements(0)
          .totalPages(0)
          .page(pageable.getPageNumber())
          .size(pageable.getPageSize())
          .totalAdultCount(0L)
          .totalChildCount(0L)
          .build();
    }

    List<Long> workIds = workPage.getContent()
        .stream()
        .map(WorkListProjection::getId)
        .toList();

    List<WorkOrderProjection> rows = workRepository.findOrdersByWorkIds(workIds);

    List<WorkGuideProjection> guideRows = workRepository.findGuidesByWorkIds(workIds);

    Map<Long, List<GuideResponse>> guideMap = guideRows.stream()
        .collect(Collectors.groupingBy(
            WorkGuideProjection::getWorkId,
            Collectors.collectingAndThen(
                Collectors.toList(),
                guides -> {
                  WorkGuideProjection selected = guides.stream()
                      .filter(g -> "leader".equalsIgnoreCase(
                          g.getGuideRole()))
                      .findFirst()
                      .orElse(guides.get(0));
                  return List.of(
                      GuideResponse.builder()
                          .id(selected.getGuideId())
                          .fullName(selected.getGuideFullName())
                          .email(selected.getGuideEmail())
                          .phone(selected.getGuidePhone())
                          .isActive(selected.getGuideIsActive())
                          .avatar(selected.getGuideAvatar())
                          .build());
                })));
    Map<Long, Map<Long, OrderInfoResponse>> workOrderMap = new LinkedHashMap<>();

    Map<Long, Set<Long>> orderServiceMap = new HashMap<>();

    for (WorkOrderProjection row : rows) {

      Long workId = row.getWorkId();
      Long orderId = row.getOrderId();

      Map<Long, OrderInfoResponse> orderMap = workOrderMap.computeIfAbsent(
          workId,
          k -> new LinkedHashMap<>());

      OrderInfoResponse order = orderMap.computeIfAbsent(
          orderId,
          k -> buildOrder(row));

      Long serviceId = row.getServiceId();

      if (serviceId != null) {

        Set<Long> serviceIds = orderServiceMap.computeIfAbsent(
            orderId,
            k -> new HashSet<>());

        if (serviceIds.add(serviceId)) {

          order.getServices().add(
              new ServiceInfoResponse(
                  serviceId,
                  row.getServiceName(),
                  row.getAreaId(),
                  row.getAreaName()));
        }
      }
    }

    List<WorkListResponse> content = workPage.getContent()
        .stream()
        .map(work -> {

          WorkListResponse dto = new WorkListResponse();

          dto.setId(work.getId());
          dto.setWorkNumber(work.getWorkNumber());
          dto.setStatus(work.getStatus());
          dto.setTourDate(work.getTourDate());

          dto.setOrders(
              new ArrayList<>(
                  workOrderMap
                      .getOrDefault(
                          work.getId(),
                          Collections.emptyMap())
                      .values()));
          dto.setGuides(
              guideMap.getOrDefault(
                  work.getId(),
                  Collections.emptyList()));
          return dto;
        })
        .toList();

    return WorkSearchResponse.builder()
        .content(content)
        .page(workPage.getNumber())
        .size(workPage.getSize())
        .totalElements(workPage.getTotalElements())
        .totalPages(workPage.getTotalPages())
        .totalAdultCount(totalAdultCount)
        .totalChildCount(totalChildCount)
        .build();
  }

  private Boolean buildPrivateFilter(WorkFilter filter) {
    if (Boolean.TRUE.equals(filter.getIsPrivate())
        && Boolean.TRUE.equals(filter.getIsShared())) {
      return null;
    }
    if (Boolean.TRUE.equals(filter.getIsPrivate())) {
      return true;
    }
    if (Boolean.TRUE.equals(filter.getIsShared())) {
      return false;
    }
    return null;
  }

  private OrderInfoResponse buildOrder(
      WorkOrderProjection row) {

    OrderInfoResponse dto = new OrderInfoResponse();

    dto.setId(row.getOrderId());
    dto.setOrderNumber(row.getOrderNumber());

    dto.setAdultCount(row.getAdultCount());
    dto.setChildCount(row.getChildCount());

    dto.setRef1(row.getRef1());

    dto.setResellerId(row.getResellerId());
    dto.setResellerName(row.getResellerName());

    dto.setPicContactId(row.getPicContactId());
    dto.setPicName(row.getPicName());

    dto.setIsPrivate(row.getIsPrivate());

    dto.setServices(new ArrayList<>());

    return dto;
  }

  public WorkDetailProjection getWorkById(Long id) {
    return workRepository.findWorkDetailById(id)
        .orElseThrow(() -> new RuntimeException("Work not found with id: " + id));
  }

  public List<WorkOrderListResponse> getWorkOrderListByWorkId(Long workId, String status) {
    List<WorkOrderListProjection> orders = workRepository.findOrdersByWorkId(workId, status);
    if (orders == null || orders.isEmpty()) {
      throw new RuntimeException("Work order not found for work id: " + workId);
    }
    List<OrderSpecialRequestProjection> specialRequests = workRepository.findSpecialRequestsByWorkId(workId);

    Map<Long, List<SpecialRequestTypeResponse>> specialRequestMap = specialRequests.stream()
        .collect(Collectors.groupingBy(
            OrderSpecialRequestProjection::getOrderId,
            Collectors.mapping(
                item -> {
                  SpecialRequestTypeResponse dto = new SpecialRequestTypeResponse();
                  dto.setId(item.getSpecialRequestId());
                  dto.setCode(item.getSpecialRequestCode());
                  dto.setLabel(item.getSpecialRequestLabel());
                  return dto;
                },
                Collectors.toList())));

    return orders.stream()
        .map(order -> WorkOrderListResponse.builder()
            .orderId(order.getOrderId())
            .reseller(order.getReseller())
            .originalAgent(order.getOriginalAgent())
            .ref1(order.getRef1())
            .adultCount(order.getAdultCount())
            .childCount(order.getChildCount())
            .totalFeeAmount(order.getTotalFeeAmount())
            .status(order.getStatus())
            .specialRequests(
                specialRequestMap.getOrDefault(
                    order.getOrderId(),
                    Collections.emptyList()))
            .build())
        .toList();
  }

  public List<WorkGuideDetailProjection> getWorkGuidesByWorkId(Long workId) {
    List<WorkGuideDetailProjection> guides = workRepository.findGuidesByWorkId(workId);
    return guides;
  }

  public AssignmentResponse createAssignment(
      AssignmentRequest request) {

    boolean existed = assignmentRepository
        .existsByWorkIdAndGuideIdAndDeletedAtIsNull(
            request.getWorkId(),
            request.getGuideId());

    if (existed) {
      throw new RuntimeException(
          "Guide already assigned to this work");
    }

    Assignment assignment = new Assignment();

    assignment.setWorkId(request.getWorkId());
    assignment.setGuideId(request.getGuideId());

    assignment.setStatus(request.getStatus());

    assignment.setRole(request.getRole());

    assignment.setNote(request.getNote());

    assignment.setIsCalendarInvitation(
        Boolean.TRUE.equals(
            request.getIsCalendarInvitation()));

    assignment.setCreatedAt(LocalDateTime.now());
    assignment.setUpdatedAt(LocalDateTime.now());

    Assignment saved = assignmentRepository.save(assignment);

    // Update work status -> offered
    Work work = workRepository.findById(request.getWorkId())
        .orElseThrow(() -> new RuntimeException("Work not found"));

    if (!"OFFERED".equalsIgnoreCase(work.getStatus())) {
      work.setStatus("OFFERED");
      work.setUpdatedAt(LocalDateTime.now());
      workRepository.save(work);
    }

    return AssignmentResponse.builder()
        .id(saved.getId())
        .workId(saved.getWorkId())
        .guideId(saved.getGuideId())
        .status(saved.getStatus())
        .role(saved.getRole())
        .note(saved.getNote())
        .isCalendarInvitation(
            saved.getIsCalendarInvitation())
        .build();
  }

  public AssignmentResponse updateAssignment(
      AssignmentUpdateRequest request) {

    Assignment assignment = assignmentRepository
        .findById(request.getId())
        .orElseThrow(() -> new RuntimeException(
            "Assignment not found: "
                + request.getId()));

    if (request.getStatus() != null) {
      assignment.setStatus(request.getStatus());

      switch (request.getStatus().toLowerCase()) {
        case "accepted":
          assignment.setAcceptedAt(LocalDateTime.now());

          // Update work status -> accepted
          if ("accepted".equalsIgnoreCase(request.getStatus().toLowerCase())) {
            Work work = workRepository
                .findById(assignment.getWorkId())
                .orElseThrow(() -> new RuntimeException(
                    "Work not found: "
                        + assignment.getWorkId()));
            work.setStatus("ACCEPTED");
            work.setUpdatedAt(LocalDateTime.now());
            workRepository.save(work);
          }
          break;

        case "rejected":
          assignment.setRejectedAt(LocalDateTime.now());
          assignment.setRejectionReason(request.getRejectionReason());

          // Update work status -> IN_PREP
          if ("rejected".equalsIgnoreCase(request.getStatus().toLowerCase())) {
            Work work = workRepository
                .findById(assignment.getWorkId())
                .orElseThrow(() -> new RuntimeException(
                    "Work not found: "
                        + assignment.getWorkId()));
            work.setStatus("IN_PREP");
            work.setUpdatedAt(LocalDateTime.now());
            workRepository.save(work);
          }
          break;

        case "closed":
          assignment.setClosedAt(LocalDateTime.now());
          break;
      }
    }

    if (request.getRole() != null) {
      assignment.setRole(request.getRole());
    }

    if (request.getNote() != null) {
      assignment.setNote(request.getNote());
    }

    if (request.getIsCalendarInvitation() != null) {
      assignment.setIsCalendarInvitation(
          request.getIsCalendarInvitation());
    }

    assignment.setUpdatedAt(LocalDateTime.now());

    Assignment saved = assignmentRepository.save(assignment);

    return AssignmentResponse.builder()
        .id(saved.getId())
        .workId(saved.getWorkId())
        .guideId(saved.getGuideId())
        .status(saved.getStatus())
        .role(saved.getRole())
        .note(saved.getNote())
        .rejectionReason(saved.getRejectionReason())
        .isCalendarInvitation(
            saved.getIsCalendarInvitation())
        .build();
  }
}

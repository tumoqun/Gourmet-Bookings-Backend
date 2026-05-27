package com.example.demo.service;

import com.example.demo.dto.OrderCreateRequest;
import com.example.demo.dto.OrderAdditionalServiceRequest;
import com.example.demo.dto.OrderServiceRequest;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderAdditionalService;
import com.example.demo.entity.OrderSpecialRequest;
import com.example.demo.entity.OrderSpecialRequestId;
import com.example.demo.entity.OrderStatus;
import com.example.demo.entity.OrderFinancialLine;
import com.example.demo.entity.OrderStatusHistory;
import com.example.demo.entity.SpecialRequestType;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderServiceRepository;
import com.example.demo.repository.OrderFinancialLineRepository;
import com.example.demo.repository.OrderStatusRepository;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.DistanceBandRepository;
import com.example.demo.repository.ServiceTypeRepository;
import com.example.demo.repository.SpecialRequestTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    private static final String STATUS_REQUESTED = "REQUESTED";
    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_CANCELLED = "CANCELLED";

    private final OrderRepository orderRepository;
    private final OrderServiceRepository orderServiceRepository;
    private final OrderFinancialLineRepository orderFinancialLineRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ServiceRepository serviceRepository;
    private final AreaRepository areaRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final DistanceBandRepository distanceBandRepository;
    private final com.example.demo.repository.OrderAdditionalServiceRepository additionalServiceRepository;
    private final com.example.demo.repository.OrderSpecialRequestRepository specialRequestRepository;
    private final SpecialRequestTypeRepository specialRequestTypeRepository;
    private final com.example.demo.repository.WorkRepository workRepository;
    private final com.example.demo.repository.AssignmentRepository assignmentRepository;
    private final com.example.demo.repository.GuideRepository guideRepository;
    private final com.example.demo.repository.ResellerRepository resellerRepository;
    private final com.example.demo.repository.AgentRepository agentRepository;
    private final com.example.demo.repository.ResellerContactRepository resellerContactRepository;

    public List<Order> findAllActive() {
        return orderRepository.findAllActive();
    }

    public List<com.example.demo.dto.OrderResponse> findAllActiveResponses() {
        List<Order> orders = orderRepository.findAllActive();
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());

        List<com.example.demo.entity.OrderService> allOrderServices = orderServiceRepository.findByOrderIdIn(orderIds);
        Map<Long, List<com.example.demo.entity.OrderService>> servicesByOrder = allOrderServices.stream()
                .collect(Collectors.groupingBy(os -> os.getOrder().getId()));

        List<com.example.demo.entity.OrderAdditionalService> allAdditional = additionalServiceRepository.findEnabledByOrderIdIn(orderIds);
        Map<Long, List<com.example.demo.entity.OrderAdditionalService>> additionalByOrder = allAdditional.stream()
                .collect(Collectors.groupingBy(a -> a.getOrder().getId()));

        List<com.example.demo.entity.OrderSpecialRequest> allSpecial = specialRequestRepository.findByOrderIdIn(orderIds);
        Map<Long, List<com.example.demo.entity.OrderSpecialRequest>> specialByOrder = allSpecial.stream()
                .collect(Collectors.groupingBy(s -> s.getOrder().getId()));

        List<Object[]> guidePairs = guideRepository.findGuideNamesByOrderIds(orderIds);
        Map<Long, String> guideByOrder = guidePairs.stream()
                .collect(Collectors.toMap(
                        p -> ((Number) p[0]).longValue(),
                        p -> (String) p[1],
                        (a, b) -> a
                ));

        return orders.stream().map(order -> {
            com.example.demo.dto.OrderResponse response = new com.example.demo.dto.OrderResponse();
            response.setId(order.getId());
            response.setOrderNumber(order.getOrderNumber());
            response.setStatus(order.getStatus() == null ? null : new com.example.demo.dto.OrderStatusResponse(
                    order.getStatus().getId(),
                    order.getStatus().getCode(),
                    order.getStatus().getLabel()
            ));
            response.setOrderChannel(order.getOrderChannel());
            response.setIsTentative(order.getIsTentative());
            response.setCreatedByName(order.getCreatedByName());
            response.setPicEmail(order.getPicEmail());
            response.setCopyEmail(order.getCopyEmail());
            response.setRef1(order.getRef1());
            response.setRef2(order.getRef2());
            response.setVoucherNumber(order.getVoucherNumber());
            response.setGuestEmail(order.getGuestEmail());
            response.setAdultCount(order.getAdultCount());
            response.setChildCount(order.getChildCount());
            response.setDietaryRestrictions(order.getDietaryRestrictions());
            response.setCurrencyCode(order.getCurrencyCode());
            response.setTotalFeeAmount(order.getTotalFeeAmount());
            response.setRequestedAt(order.getRequestedAt());
            response.setSubmittedAt(order.getSubmittedAt());
            response.setCreatedAt(order.getCreatedAt());
            response.setUpdatedAt(order.getUpdatedAt());

            // Reseller
            if (order.getReseller() != null) {
                com.example.demo.entity.Reseller r = order.getReseller();
                response.setReseller(new com.example.demo.dto.ResellerResponse(r.getId(), r.getName(), r.getStatus()));
            }

            // PIC Contact
            if (order.getPicContact() != null) {
                com.example.demo.entity.ResellerContact c = order.getPicContact();
                com.example.demo.dto.ResellerResponse resellerDto = c.getReseller() == null ? null :
                        new com.example.demo.dto.ResellerResponse(c.getReseller().getId(), c.getReseller().getName(), c.getReseller().getStatus());
                response.setPicContact(new com.example.demo.dto.ResellerContactResponse(c.getId(), resellerDto, c.getName(), c.getEmail(), c.getIsPrimary()));
            }

            // Original Agent
            if (order.getOriginalAgent() != null) {
                com.example.demo.entity.Agent a = order.getOriginalAgent();
                com.example.demo.dto.ResellerResponse resellerDto = a.getReseller() == null ? null :
                        new com.example.demo.dto.ResellerResponse(a.getReseller().getId(), a.getReseller().getName(), a.getReseller().getStatus());
                response.setOriginalAgent(new com.example.demo.dto.AgentResponse(a.getId(), resellerDto, a.getName(), a.getEmail()));
            }

            // Order Services
            List<com.example.demo.entity.OrderService> orderServices = servicesByOrder.getOrDefault(order.getId(), Collections.emptyList());
            response.setOrderServices(orderServices.stream().map(os -> {
                com.example.demo.dto.OrderServiceResponse osr = new com.example.demo.dto.OrderServiceResponse();
                osr.setId(os.getId());
                osr.setServiceNameSnapshot(os.getServiceNameSnapshot());
                osr.setTargetDate(os.getTargetDate());
                osr.setStartTime(os.getStartTime());
                osr.setTimeSlotCode(os.getTimeSlotCode());
                osr.setIsPrivate(os.getIsPrivate());
                osr.setTimezone(os.getTimezone());
                osr.setArea(toAreaResponse(os.getArea()));
                osr.setServiceType(toServiceTypeResponse(os.getServiceType()));
                com.example.demo.entity.Service svc = os.getService();
                osr.setService(new com.example.demo.dto.ServiceResponse(
                        svc.getId(),
                        toAreaResponse(svc.getArea()),
                        toServiceTypeResponse(svc.getServiceType()),
                        svc.getName(),
                        svc.getIsPrivateAvailable(),
                        svc.getIsActive()
                ));
                osr.setIsAdminModified(Boolean.TRUE.equals(os.getIsAdminModified()));
                osr.setOriginalServiceId(os.getOriginalServiceId());
                osr.setOriginalServiceNameSnapshot(os.getOriginalServiceNameSnapshot());
                return osr;
            }).collect(Collectors.toList()));

            // Additional Services
            List<com.example.demo.entity.OrderAdditionalService> adds = additionalByOrder.getOrDefault(order.getId(), Collections.emptyList());
            response.setAdditionalServices(adds.stream().map(oas -> {
                com.example.demo.dto.OrderAdditionalServiceResponse r = new com.example.demo.dto.OrderAdditionalServiceResponse();
                r.setId(oas.getId());
                r.setKind(oas.getKind());
                r.setIsEnabled(oas.getIsEnabled());
                r.setLocation(oas.getLocation());
                r.setHandoffText(oas.getHandoffText());
                r.setSuggestedTime(oas.getSuggestedTime());
                r.setFeeAmount(oas.getFeeAmount());
                r.setCurrencyCode(oas.getCurrencyCode());
                if (oas.getServiceType() != null) {
                    r.setServiceType(toServiceTypeResponse(oas.getServiceType()));
                }
                if (oas.getDistanceBand() != null) {
                    r.setDistanceBand(toDistanceBandResponse(oas.getDistanceBand()));
                }
                return r;
            }).collect(Collectors.toList()));

            // Special Requests
            List<com.example.demo.entity.OrderSpecialRequest> specs = specialByOrder.getOrDefault(order.getId(), Collections.emptyList());
            response.setSpecialRequests(specs.stream().map(osr -> {
                com.example.demo.entity.SpecialRequestType srt = osr.getSpecialRequestType();
                return new com.example.demo.dto.SpecialRequestTypeResponse(srt.getId(), srt.getCode(), srt.getLabel());
            }).collect(Collectors.toList()));

            // Guide
            response.setGuide(guideByOrder.get(order.getId()));

            return response;
        }).collect(Collectors.toList());
    }

    private com.example.demo.dto.AreaResponse toAreaResponse(com.example.demo.entity.Area area) {
        return area == null ? null : new com.example.demo.dto.AreaResponse(area.getId(), area.getCode(), area.getName());
    }

    private com.example.demo.dto.ServiceTypeResponse toServiceTypeResponse(com.example.demo.entity.ServiceType serviceType) {
        return serviceType == null ? null : new com.example.demo.dto.ServiceTypeResponse(serviceType.getId(), serviceType.getCode(), serviceType.getName());
    }

    private com.example.demo.dto.DistanceBandResponse toDistanceBandResponse(com.example.demo.entity.DistanceBand distanceBand) {
        return distanceBand == null ? null : new com.example.demo.dto.DistanceBandResponse(
            distanceBand.getId(),
            distanceBand.getLabel(),
            distanceBand.getSortOrder()
        );
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public List<Order> findByStatusId(Long statusId) {
        return orderRepository.findByStatusId(statusId);
    }

    public List<Order> findByResellerId(Long resellerId) {
        return orderRepository.findByResellerIdAndRequestedAtGreaterThanOrderByRequestedAtDesc(resellerId, LocalDateTime.now().minusMonths(12));
    }

    public List<com.example.demo.entity.OrderService> findServicesByOrderId(Long orderId) {
        return orderServiceRepository.findByOrderId(orderId);
    }

    public Order createOrder(OrderCreateRequest request) {
        Order order = new Order();
        order.setOrderNumber(request.getOrderNumber());
        order.setOrderChannel(request.getOrderChannel());
        order.setIsTentative(Boolean.TRUE.equals(request.getIsTentative()));
        order.setCreatedByName(request.getCreatedByName());
        order.setPicEmail(request.getPicEmail());
        order.setCopyEmail(request.getCopyEmail());
        order.setRef1(request.getRef1());
        order.setRef2(request.getRef2());
        order.setVoucherNumber(request.getVoucherNumber());
        order.setGuestEmail(request.getGuestEmail());
        order.setAdultCount(request.getAdultCount());
        order.setChildCount(request.getChildCount());
        order.setDietaryRestrictions(request.getDietaryRestrictions());
        order.setCurrencyCode(request.getCurrencyCode());
        order.setTotalFeeAmount(request.getTotalFeeAmount());
        order.setRequestedAt(request.getRequestedAt());

        if (request.getResellerId() != null) {
            order.setReseller(resellerRepository.findById(request.getResellerId()).orElse(null));
        }
        if (request.getPicContactId() != null) {
            order.setPicContact(resellerContactRepository.findById(request.getPicContactId()).orElse(null));
        }
        if (request.getOriginalAgentId() != null) {
            order.setOriginalAgent(agentRepository.findById(request.getOriginalAgentId()).orElse(null));
        }

        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber(generateOrderNumber());
        }

        OrderStatus defaultStatus = orderStatusRepository.findByCode(STATUS_REQUESTED)
            .orElseThrow(() -> new RuntimeException("Default order status not found"));
        order.setStatus(defaultStatus);

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        saveOrderServices(savedOrder, request.getOrderServices());
        BigDecimal additionalFeeTotal = saveAdditionalServices(savedOrder, request.getAdditionalServices());
        saveSpecialRequests(savedOrder, request.getSpecialRequestTypeIds());
        if (additionalFeeTotal.compareTo(BigDecimal.ZERO) > 0) {
            savedOrder.setTotalFeeAmount((savedOrder.getTotalFeeAmount() == null ? BigDecimal.ZERO : savedOrder.getTotalFeeAmount()).add(additionalFeeTotal));
            savedOrder = orderRepository.save(savedOrder);
        }
        log.info("Created order with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    private void saveOrderServices(Order order, List<OrderServiceRequest> serviceRequests) {
        if (serviceRequests == null || serviceRequests.isEmpty()) {
            return;
        }

        for (OrderServiceRequest request : serviceRequests) {
            com.example.demo.entity.OrderService orderService = new com.example.demo.entity.OrderService();
            orderService.setOrder(order);
            orderService.setService(serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + request.getServiceId())));
            orderService.setArea(areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new RuntimeException("Area not found with id: " + request.getAreaId())));
            orderService.setServiceType(serviceTypeRepository.findById(request.getServiceTypeId())
                .orElseThrow(() -> new RuntimeException("Service type not found with id: " + request.getServiceTypeId())));
            orderService.setServiceNameSnapshot(request.getServiceNameSnapshot());
            orderService.setTargetDate(request.getTargetDate());
            orderService.setStartTime(request.getStartTime());
            orderService.setTimeSlotCode(request.getTimeSlotCode());
            orderService.setIsPrivate(Boolean.TRUE.equals(request.getIsPrivate()));
            orderService.setTimezone(request.getTimezone());
            orderServiceRepository.save(orderService);
        }
    }

    private BigDecimal saveAdditionalServices(Order order, List<OrderAdditionalServiceRequest> serviceRequests) {
        if (serviceRequests == null || serviceRequests.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal additionalFeeTotal = BigDecimal.ZERO;

        for (OrderAdditionalServiceRequest request : serviceRequests) {
            OrderAdditionalService additionalService = new OrderAdditionalService();
            additionalService.setOrder(order);
            additionalService.setKind(request.getKind());
            additionalService.setIsEnabled(request.getIsEnabled() == null || Boolean.TRUE.equals(request.getIsEnabled()));
            additionalService.setLocation(request.getLocation());
            additionalService.setHandoffText(request.getHandoffText());
            additionalService.setSuggestedTime(request.getSuggestedTime());
            additionalService.setFeeAmount(BigDecimal.ZERO);
            additionalService.setCurrencyCode(order.getCurrencyCode());

            if (request.getServiceTypeId() != null) {
                additionalService.setServiceType(serviceTypeRepository.findById(request.getServiceTypeId())
                    .orElseThrow(() -> new RuntimeException("Service type not found with id: " + request.getServiceTypeId())));
            }

            if (request.getDistanceBandId() != null) {
                com.example.demo.entity.DistanceBand distanceBand = distanceBandRepository.findById(request.getDistanceBandId())
                    .orElseThrow(() -> new RuntimeException("Distance band not found with id: " + request.getDistanceBandId()));
                additionalService.setDistanceBand(distanceBand);
                BigDecimal feeAmount = calculateDistanceFee(request.getDistanceBandId());
                additionalService.setFeeAmount(feeAmount);
                additionalFeeTotal = additionalFeeTotal.add(feeAmount);
            }

            additionalServiceRepository.save(additionalService);
        }

        return additionalFeeTotal;
    }

    private void saveSpecialRequests(Order order, List<Long> specialRequestTypeIds) {
        if (specialRequestTypeIds == null || specialRequestTypeIds.isEmpty()) {
            return;
        }

        for (Long specialRequestTypeId : specialRequestTypeIds) {
            SpecialRequestType specialRequestType = specialRequestTypeRepository.findById(specialRequestTypeId)
                .orElseThrow(() -> new RuntimeException("Special request type not found with id: " + specialRequestTypeId));

            OrderSpecialRequest specialRequest = new OrderSpecialRequest();
            specialRequest.setId(new OrderSpecialRequestId(order.getId(), specialRequestTypeId));
            specialRequest.setOrder(order);
            specialRequest.setSpecialRequestType(specialRequestType);
            specialRequestRepository.save(specialRequest);
        }
    }

    private BigDecimal calculateDistanceFee(Long distanceBandId) {
        if (distanceBandId == null) {
            return BigDecimal.ZERO;
        }

        return switch (distanceBandId.intValue()) {
            case 1 -> BigDecimal.valueOf(100);
            case 2 -> BigDecimal.valueOf(150);
            case 3 -> BigDecimal.valueOf(200);
            default -> BigDecimal.ZERO;
        };
    }

    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingOrder.setOrderChannel(order.getOrderChannel());
        existingOrder.setIsTentative(order.getIsTentative());
        existingOrder.setReseller(order.getReseller());
        existingOrder.setPicContact(order.getPicContact());
        existingOrder.setPicEmail(order.getPicEmail());
        existingOrder.setCopyEmail(order.getCopyEmail());
        existingOrder.setOriginalAgent(order.getOriginalAgent());
        existingOrder.setRef1(order.getRef1());
        existingOrder.setRef2(order.getRef2());
        existingOrder.setVoucherNumber(order.getVoucherNumber());
        existingOrder.setGuestEmail(order.getGuestEmail());
        existingOrder.setAdultCount(order.getAdultCount());
        existingOrder.setChildCount(order.getChildCount());
        existingOrder.setDietaryRestrictions(order.getDietaryRestrictions());
        existingOrder.setCurrencyCode(order.getCurrencyCode());
        existingOrder.setTotalFeeAmount(order.getTotalFeeAmount());
        existingOrder.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("Updated order with ID: {}", updatedOrder.getId());
        return updatedOrder;
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setDeletedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        log.info("Soft deleted order with ID: {}", id);
    }

    public Order submitOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        OrderStatus confirmedStatus = orderStatusRepository.findByCode(STATUS_CONFIRMED)
            .orElseThrow(() -> new RuntimeException("Confirmed status not found"));

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(order.getStatus());
        history.setToStatus(confirmedStatus);
        history.setChangedByUser(order.getCreatedByUser());
        history.setCreatedAt(LocalDateTime.now());

        order.setStatus(confirmedStatus);
        order.setSubmittedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("Submitted order with ID: {}", id);

        createFinancialLines(order);

        return order;
    }

    public Order cancelOrder(Long id, String note) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        OrderStatus cancelledStatus = orderStatusRepository.findByCode(STATUS_CANCELLED)
            .orElseThrow(() -> new RuntimeException("Cancelled status not found"));

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(order.getStatus());
        history.setToStatus(cancelledStatus);
        history.setChangedByUser(order.getCreatedByUser());
        history.setNote(note);
        history.setCreatedAt(LocalDateTime.now());

        order.setStatus(cancelledStatus);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("Cancelled order with ID: {}", id);

        return order;
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void createFinancialLines(Order order) {
        if (order.getTotalFeeAmount() != null && order.getTotalFeeAmount().doubleValue() > 0) {
            OrderFinancialLine serviceFee = new OrderFinancialLine();
            serviceFee.setOrder(order);
            serviceFee.setLineType("SERVICE_FEE");
            serviceFee.setDescription("Service Fee");
            serviceFee.setAmount(order.getTotalFeeAmount());
            serviceFee.setCurrencyCode(order.getCurrencyCode());
            serviceFee.setTaxAmount(null);
            serviceFee.setIsTaxIncluded(false);

            orderFinancialLineRepository.save(serviceFee);
            log.info("Created financial line for order ID: {}", order.getId());
        }
    }
}

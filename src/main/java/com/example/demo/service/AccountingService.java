package com.example.demo.service;

import com.example.demo.dto.AccountingFilter;
import com.example.demo.dto.AccountingGuideProjection;
import com.example.demo.dto.AccountingItemResponse;
import com.example.demo.dto.AccountingPageResponse;
import com.example.demo.dto.AccountingRowProjection;
import com.example.demo.repository.AccountingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dedicated service for the Accounting page.
 * Completely separate from WorkService — uses AccountingRepository only.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AccountingService {

  private final AccountingRepository accountingRepository;

  public AccountingPageResponse findAll(AccountingFilter filter, Pageable pageable) {

    // Normalise filter strings so empty strings behave as null in SQL
    String ref         = blankToNull(filter.getRef());
    String guideName   = blankToNull(filter.getGuideName());
    String serviceName = blankToNull(filter.getServiceName());
    String status      = blankToNull(filter.getStatus());

    Long      resellerId = filter.getResellerId();
    Boolean   isPrivate  = filter.getIsPrivate();
    var       tourDate   = filter.getTourDate();

    // 1. Count total (for pagination metadata)
    long total = accountingRepository.countAccounting(
        resellerId, ref, guideName, serviceName, tourDate, status, isPrivate);

    if (total == 0) {
      return AccountingPageResponse.builder()
          .content(Collections.emptyList())
          .totalElements(0)
          .totalPages(0)
          .page(pageable.getPageNumber())
          .size(pageable.getPageSize())
          .build();
    }

    int  pageSize = pageable.getPageSize();
    long offset   = pageable.getOffset();
    int  totalPages = (int) Math.ceil((double) total / pageSize);

    // 2. Paginated list of work IDs
    List<Long> workIds = accountingRepository.findAccountingWorkIds(
        resellerId, ref, guideName, serviceName, tourDate, status, isPrivate,
        pageSize, offset);

    if (workIds.isEmpty()) {
      return AccountingPageResponse.builder()
          .content(Collections.emptyList())
          .totalElements(total)
          .totalPages(totalPages)
          .page(pageable.getPageNumber())
          .size(pageSize)
          .build();
    }

    // 3. Main row data (one row per work)
    List<AccountingRowProjection> rows = accountingRepository.findAccountingRows(workIds);

    // 4. All guide names per work
    List<AccountingGuideProjection> guideRows = accountingRepository.findAccountingGuides(workIds);

    // Group guide names by workId, preserving insertion order
    Map<Long, List<String>> guideMap = new LinkedHashMap<>();
    for (AccountingGuideProjection gp : guideRows) {
      guideMap
          .computeIfAbsent(gp.getWorkId(), k -> new ArrayList<>())
          .add(gp.getGuideName());
    }

    // 5. Assemble response — preserve order of workIds from the page query
    Map<Long, AccountingRowProjection> rowMap = rows.stream()
        .collect(Collectors.toMap(AccountingRowProjection::getWorkId, r -> r));

    List<AccountingItemResponse> content = workIds.stream()
        .filter(rowMap::containsKey)
        .map(id -> {
          AccountingRowProjection row = rowMap.get(id);
          return AccountingItemResponse.builder()
              .workId(row.getWorkId())
              .workNumber(row.getWorkNumber())
              .status(row.getWorkStatus())
              .tourDate(row.getTourDate())
              .tourStartTime(row.getTourStartTime())
              .notes(row.getNotes())
              .guideNames(guideMap.getOrDefault(id, Collections.emptyList()))
              .ref1(row.getRef1())
              .resellerName(row.getResellerName())
              .isPrivate(row.getIsPrivate())
              .adultCount(row.getAdultCount())
              .childCount(row.getChildCount())
              .serviceName(row.getServiceName())
              .build();
        })
        .toList();

    return AccountingPageResponse.builder()
        .content(content)
        .totalElements(total)
        .totalPages(totalPages)
        .page(pageable.getPageNumber())
        .size(pageSize)
        .build();
  }

  // ── helpers ──────────────────────────────────────────────────────────────

  private static String blankToNull(String s) {
    return (s == null || s.isBlank()) ? null : s.trim();
  }
}

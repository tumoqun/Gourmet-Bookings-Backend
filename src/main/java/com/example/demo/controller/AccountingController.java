package com.example.demo.controller;

import com.example.demo.dto.AccountingFilter;
import com.example.demo.dto.AccountingPageResponse;
import com.example.demo.service.AccountingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dedicated controller for the Accounting page.
 * Exposes GET /api/accounting — separate from /api/works.
 */
@RestController
@RequestMapping("/api/accounting")
@RequiredArgsConstructor
@Slf4j
public class AccountingController {

  private final AccountingService accountingService;

  /**
   * Returns a paginated list of work rows for the Accounting table.
   *
   * <p>Supported query params (all optional):
   * <ul>
   *   <li>{@code resellerId} — filter by reseller ID</li>
   *   <li>{@code ref}        — partial match on order ref1</li>
   *   <li>{@code guideName}  — partial match on guide full name</li>
   *   <li>{@code serviceName}— partial match on service name</li>
   *   <li>{@code tourDate}   — exact date match (yyyy-MM-dd)</li>
   *   <li>{@code status}     — exact work status (case-insensitive)</li>
   *   <li>{@code isPrivate}  — true / false</li>
   *   <li>{@code page}       — 0-based page number (Spring Pageable)</li>
   *   <li>{@code size}       — page size (default 25)</li>
   * </ul>
   */
  @GetMapping
  @PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
  public ResponseEntity<AccountingPageResponse> getAccounting(
      @ModelAttribute AccountingFilter filter,
      Pageable pageable) {

    return ResponseEntity.ok(accountingService.findAll(filter, pageable));
  }
}

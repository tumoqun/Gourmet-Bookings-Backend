package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Paginated response envelope for GET /api/accounting.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountingPageResponse {

  private List<AccountingItemResponse> content;
  private long totalElements;
  private int totalPages;
  private int page;
  private int size;
}

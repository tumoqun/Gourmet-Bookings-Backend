package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkSearchResponse {
  private List<WorkListResponse> content;

  private long totalElements;

  private int totalPages;

  private int page;

  private int size;

  private Long totalAdultCount;

  private Long totalChildCount;
}

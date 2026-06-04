package com.example.demo.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkListResponse {
  private Long id;
  private String workNumber;
  private String status;
  private LocalDate tourDate;
  private List<GuideResponse> guides = new ArrayList<>();
  private List<OrderInfoResponse> orders = new ArrayList<>();
}

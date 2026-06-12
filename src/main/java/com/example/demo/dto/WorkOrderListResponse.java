package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderListResponse {
  private Long orderId;

  private String reseller;

  private String originalAgent;

  private String ref1;

  private Integer adultCount;

  private Integer childCount;

  private BigDecimal totalFeeAmount;

  private String status;

  private List<SpecialRequestTypeResponse> specialRequests;
}

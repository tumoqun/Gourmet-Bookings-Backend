package com.example.demo.dto;

import java.math.BigDecimal;

public interface WorkOrderListProjection {
  Long getOrderId();

  String getReseller();

  String getOriginalAgent();

  String getRef1();

  Integer getAdultCount();

  Integer getChildCount();

  BigDecimal getTotalFeeAmount();

  String getStatus();
}

package com.example.demo.dto;

public interface OrderSpecialRequestProjection {
  Long getOrderId();

  Long getSpecialRequestId();

  String getSpecialRequestCode();

  String getSpecialRequestLabel();
}

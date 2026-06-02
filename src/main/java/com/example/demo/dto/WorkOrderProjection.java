package com.example.demo.dto;

public interface WorkOrderProjection {
  Long getWorkId();

  Long getOrderId();

  String getOrderNumber();

  Integer getAdultCount();

  Integer getChildCount();

  String getRef1();

  Long getResellerId();

  String getResellerName();

  Long getPicContactId();

  String getPicName();

  Boolean getIsPrivate();

  Long getServiceId();

  String getServiceName();

  Long getAreaId();

  String getAreaName();
}

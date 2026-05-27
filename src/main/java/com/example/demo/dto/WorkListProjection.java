package com.example.demo.dto;

import java.time.LocalDate;

public interface WorkListProjection {
    Long getId();
    String getWorkNumber();
    String getStatus();
    LocalDate getTourDate();
    Long getOrderId();
    String getOrderNumber();
    Long getResellerId();
    String getResellerName();
    Long getPicContactId();
    String getPicName();
    // String getRef1();
    Integer getAdultCount();
    Integer getChildCount();
    Boolean getIsPrivate();
    Long getServiceId();
    String getServiceName();
    Long getAreaId();
    String getAreaName();
}

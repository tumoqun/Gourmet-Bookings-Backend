package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoResponse {
  private Long id;
  private String orderNumber;

  private Integer adultCount;
  private Integer childCount;

  private String ref1;

  private Long resellerId;
  private String resellerName;

  private Long picContactId;
  private String picName;

  private Boolean isPrivate;

  private List<ServiceInfoResponse> services = new ArrayList<>();
}

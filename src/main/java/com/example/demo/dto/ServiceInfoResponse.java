package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInfoResponse {
  private Long serviceId;
    private String serviceName;

    private Long areaId;
    private String areaName;
}

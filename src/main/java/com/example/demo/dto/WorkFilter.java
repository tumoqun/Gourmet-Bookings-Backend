package com.example.demo.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkFilter {
  private Long resellerId;

  private String ref;

  private Long personInChargeId;

  private Long areaId;

  private String serviceName;

  private String guideName;

  private String status;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate tourDate;

  private Boolean isPrivate;

  private Boolean isShared;

  private Boolean privateFilter;
}

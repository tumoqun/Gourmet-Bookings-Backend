package com.example.demo.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountingFilter {

  private Long resellerId;

  private String ref;

  private String guideName;

  private String serviceName;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate tourDate;

  /** e.g. "SCHEDULED", "ENDED" — compared case-insensitively */
  private String status;

  /** true = private only, false = shared only, null = all */
  private Boolean isPrivate;
}

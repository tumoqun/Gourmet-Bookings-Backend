package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Per-row DTO returned by GET /api/accounting.
 * Contains all fields required by the Accounting table in the frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountingItemResponse {

  private Long workId;
  private String workNumber;

  /** Work status, e.g. "SCHEDULED", "ENDED" */
  private String status;

  private LocalDate tourDate;

  /** May be null if not yet scheduled */
  private LocalTime tourStartTime;

  private String notes;

  /** All guide names assigned to this work */
  private List<String> guideNames;

  /** ref1 from the first linked order */
  private String ref1;

  /** Reseller name from the first linked order */
  private String resellerName;

  /** true = Private, false = Shared */
  private Boolean isPrivate;

  /** Total adult guests across all linked orders */
  private Integer adultCount;

  /** Total child guests across all linked orders */
  private Integer childCount;

  /** Service name from the first order_service of the first linked order */
  private String serviceName;
}

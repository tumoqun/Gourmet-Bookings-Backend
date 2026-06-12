package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponse {
  private Long id;

  private Long workId;

  private Long guideId;

  private String status;

  private String role;

  private String note;

  private Boolean isCalendarInvitation;

  private String rejectionReason;
}

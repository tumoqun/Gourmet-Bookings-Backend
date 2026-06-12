package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignmentUpdateRequest {
  @NotNull(message = "Assignment ID is required")
  private Long id;

  private String status;

  private String note;

  private Boolean isCalendarInvitation;

  private String role;

  private String rejectionReason;
}

package com.example.demo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequest {
  @NotNull(message = "Work ID is required")
  private Long workId;

  @NotNull(message = "Guide ID is required")
  private Long guideId;

  @NotBlank(message = "Status is required")
  private String status;

  private String note;
  private Boolean isCalendarInvitation;
  private String role;
}

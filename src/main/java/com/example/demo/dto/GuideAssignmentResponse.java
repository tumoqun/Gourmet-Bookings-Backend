package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GuideAssignmentResponse {
    private Long id;
    private Long workId;
    private String workNumber;
    private Long guideId;
    private String guideName;
    private String status;
    private LocalDateTime acceptedAt;
    private LocalDateTime tourStartedAt;
    private LocalDateTime tourEndedAt;
}

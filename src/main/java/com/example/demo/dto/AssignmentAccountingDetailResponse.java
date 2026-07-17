package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentAccountingDetailResponse {
    /** All guides assigned to this work. */
    private List<GuideBasicInfo> guides;

    private String serviceName;
    private Integer durationMinutes;
    private String tourType;
    private Integer guestCount;

    private String statusCode;
    private String statusName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private BigDecimal standardHoursEarned;
    private BigDecimal extraHoursEarned;
    private BigDecimal tourHoursEarned;

    private BigDecimal hourlySalary;
    private BigDecimal taxableSalaryEarned;

    private BigDecimal travelExpenses;
    private BigDecimal tourReceipts;
}

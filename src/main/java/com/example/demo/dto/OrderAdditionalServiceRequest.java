package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAdditionalServiceRequest {
    
    @NotBlank(message = "Kind is required")
    private String kind;
    
    private Boolean isEnabled = false;
    private String location;
    private String handoffText;
    private Long serviceTypeId;
    private String vehicleType;
    private Long distanceBandId;
    private LocalTime suggestedTime;
    private BigDecimal feeAmount;
    private String currencyCode;
}

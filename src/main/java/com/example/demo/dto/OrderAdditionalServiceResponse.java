package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAdditionalServiceResponse {
    
    private Long id;
    private String kind;
    private Boolean isEnabled;
    private String location;
    private ServiceTypeResponse serviceType;
    private DistanceBandResponse distanceBand;
    private LocalTime suggestedTime;
    private BigDecimal feeAmount;
    private String currencyCode;
}

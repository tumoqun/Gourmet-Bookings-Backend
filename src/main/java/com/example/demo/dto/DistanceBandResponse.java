package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceBandResponse {
    
    private Long id;
    private String label;
    private Integer sortOrder;
    private BigDecimal feeAmount;
}

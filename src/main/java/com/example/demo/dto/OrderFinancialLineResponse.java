package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFinancialLineResponse {
    
    private Long id;
    private String lineType;
    private String description;
    private BigDecimal amount;
    private BigDecimal taxAmount;
    private String currencyCode;
    private Boolean isTaxIncluded;
}

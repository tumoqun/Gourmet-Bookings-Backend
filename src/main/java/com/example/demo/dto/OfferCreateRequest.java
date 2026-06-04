package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferCreateRequest {

    private Long serviceId;
    private LocalDate targetDate;
    private LocalTime startTime;
    private BigDecimal netPrice;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal puDoFee;
    private BigDecimal commissionPercent;
    private BigDecimal commissionAmount;
    private BigDecimal subtotal;
    private BigDecimal estimatedTax;
    private BigDecimal totalAmount;
    private String pricingNotes;
    private Boolean hostConfirmationRequired;
}

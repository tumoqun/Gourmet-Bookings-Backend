package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    
    @NotBlank(message = "Order number is required")
    private String orderNumber;
    
    @NotNull(message = "Status ID is required")
    private Long statusId;
    
    private String orderChannel;
    
    private Boolean isTentative = false;
    
    private Long createdById;
    private String createdByName;
    
    private Long resellerId;
    private Long picContactId;
    private String picEmail;
    private String copyEmail;
    private Long originalAgentId;
    private String ref1;
    private String ref2;
    
    private String voucherNumber;
    private String guestEmail;
    private Integer adultCount;
    private Integer childCount;
    private String dietaryRestrictions;
    
    private String currencyCode;
    private BigDecimal totalFeeAmount;
    
    private List<OrderServiceRequest> orderServices;
    private List<OrderAdditionalServiceRequest> additionalServices;
    private List<Long> specialRequestTypeIds;
}

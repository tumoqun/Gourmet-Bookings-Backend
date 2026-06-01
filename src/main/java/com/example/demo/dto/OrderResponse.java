package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private Long id;
    private String orderNumber;
    private OrderStatusResponse status;
    private String orderChannel;
    private Boolean isTentative;
    private Boolean isPrivate;
    private UserResponse createdByUser;
    private String createdByName;
    private ResellerResponse reseller;
    private ResellerContactResponse picContact;
    private String picEmail;
    private String copyEmail;
    private AgentResponse originalAgent;
    private String ref1;
    private String ref2;
    private String voucherNumber;
    private String guestEmail;
    private Integer adultCount;
    private Integer childCount;
    private String dietaryRestrictions;
    private String currencyCode;
    private BigDecimal totalFeeAmount;
    private LocalDateTime requestedAt;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<OrderServiceResponse> orderServices;
    private List<OrderAdditionalServiceResponse> additionalServices;
    private List<SpecialRequestTypeResponse> specialRequests;
    private List<OrderFinancialLineResponse> financialLines;
    private String guide;
}

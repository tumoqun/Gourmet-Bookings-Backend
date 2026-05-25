package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "order_additional_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAdditionalService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(name = "kind", nullable = false, length = 20)
    private String kind;
    
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = false;
    
    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "handoff_text", length = 200)
    private String handoffText;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distance_band_id")
    private DistanceBand distanceBand;
    
    @Column(name = "suggested_time")
    private LocalTime suggestedTime;
    
    @Column(name = "fee_amount", precision = 10, scale = 2)
    private BigDecimal feeAmount;
    
    @Column(name = "currency_code", length = 3)
    private String currencyCode;
}

package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "order_special_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSpecialRequest {
    
    @EmbeddedId
    private OrderSpecialRequestId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("specialRequestTypeId")
    @JoinColumn(name = "special_request_type_id", nullable = false)
    private SpecialRequestType specialRequestType;
}

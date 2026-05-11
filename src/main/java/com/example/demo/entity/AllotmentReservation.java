package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "allotment_reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllotmentReservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allotment_id", nullable = false)
    private Allotment allotment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_service_id", nullable = false)
    private OrderService orderService;
    
    @Column(name = "adult_count")
    private Integer adultCount;
    
    @Column(name = "child_count")
    private Integer childCount;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status;
}

package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "distance_bands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceBand {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "label", nullable = false, length = 100)
    private String label;
    
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "fee_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal feeAmount;
}

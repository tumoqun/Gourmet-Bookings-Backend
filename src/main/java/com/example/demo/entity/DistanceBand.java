package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
}

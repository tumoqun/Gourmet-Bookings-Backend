package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "allotments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allotment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @Column(name = "date", nullable = false)
    private LocalDate serviceDate;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacityTotal;
    
    @Column(name = "reserved_count", nullable = false)
    private Integer reservedTotal = 0;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status;
}

package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "order_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @Column(name = "service_name_snapshot", nullable = false, length = 200)
    private String serviceNameSnapshot;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;
    
    @Column(name = "target_date")
    private LocalDate targetDate;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "time_slot_code", length = 50)
    private String timeSlotCode;
    
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate = false;
    
    @Column(name = "timezone", length = 50)
    private String timezone;
}

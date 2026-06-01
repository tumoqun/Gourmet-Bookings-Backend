package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

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
    

    @Column(name = "timezone", length = 50)
    private String timezone;

    // Admin override fields (V3)
    @Column(name = "is_admin_modified", nullable = false)
    private Boolean isAdminModified = false;

    @Column(name = "original_service_id")
    private Long originalServiceId;

    @Column(name = "original_service_name_snapshot", length = 200)
    private String originalServiceNameSnapshot;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}

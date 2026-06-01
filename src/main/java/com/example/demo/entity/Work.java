package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "works")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "work_orders",
        joinColumns = @JoinColumn(name = "work_id"),
        inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private Set<Order> orders = new HashSet<>();

    @Column(name = "work_number", nullable = false, unique = true, length = 50)
    private String workNumber;

    @Column(name = "status", nullable = false, length = 50)
    private String status = "scheduled";

    @Column(name = "tour_date", nullable = false)
    private LocalDate tourDate;

    @Column(name = "tour_start_time")
    private LocalTime tourStartTime;

    @Column(name = "tour_end_time")
    private LocalTime tourEndTime;

    @Column(name = "tour_started_at")
    private LocalDateTime tourStartedAt;

    @Column(name = "tour_ended_at")
    private LocalDateTime tourEndedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "location_name", length = 200)
    private String locationName;

    @Column(name = "location_address", columnDefinition = "TEXT")
    private String locationAddress;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}


package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "itinerary_stops")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "itinerary_id", nullable = false)
    private Long itineraryId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;

    @Column(name = "stop_type", nullable = false, length = 50)
    private String stopType;

    @Column(name = "scheduled_time")
    private LocalTime scheduledTime;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "special_notes", columnDefinition = "TEXT")
    private String specialNotes;

    @Column(name = "status", nullable = true, length = 50)
    private String status;

    @Column(name = "added_by", nullable = true)
    private String addedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}


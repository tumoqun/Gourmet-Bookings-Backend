package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  private Assignment assignment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_id")
  private Supplier supplier;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "itinerary_stop_id")
  private ItineraryStop itineraryStop;

  @Column(name = "amount", nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Column(name = "expense_date", nullable = false)
  private LocalDate expenseDate;

  @Column(name = "expense_time")
  private LocalTime expenseTime;

  @Column(name = "notes", columnDefinition = "TEXT")
  private String notes;

  @Column(name = "image_url", length = 500)
  private String imageUrl;

  @Column(name = "is_verified", nullable = false)
  private Boolean isVerified = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "verified_by_id")
  private User verifiedBy;

  @Column(name = "verified_at")
  private LocalDateTime verifiedAt;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
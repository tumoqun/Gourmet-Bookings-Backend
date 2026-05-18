package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_book_id", nullable = false)
    private Long priceBookId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "guest_type", nullable = false, length = 50)
    private String guestType;

    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "currency_code", length = 3)
    private String currencyCode = "USD";

    @Column(name = "min_pax")
    private Integer minPax;

    @Column(name = "max_pax")
    private Integer maxPax;

    @Column(name = "seasonal_adjustment_percent", precision = 5, scale = 2)
    private BigDecimal seasonalAdjustmentPercent = BigDecimal.ZERO;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_until")
    private LocalDate effectiveUntil;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}


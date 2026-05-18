package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_group_id", nullable = false)
    private Long customerGroupId;

    @Column(name = "reseller_id")
    private Long resellerId;

    @Column(name = "group_name", length = 200)
    private String groupName;

    @Column(name = "total_pax")
    private Integer totalPax = 0;

    @Column(name = "currency_code", length = 3)
    private String currencyCode = "USD";

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}


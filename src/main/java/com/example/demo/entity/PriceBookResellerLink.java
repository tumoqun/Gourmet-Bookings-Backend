package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_book_reseller_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceBookResellerLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_book_id", nullable = false)
    private Long priceBookId;

    @Column(name = "reseller_id", nullable = false)
    private Long resellerId;

    @Column(name = "link_type", nullable = false, length = 50)
    private String linkType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}


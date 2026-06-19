package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private OrderStatus status;
    
    @Column(name = "order_channel", length = 50)
    private String orderChannel;
    
    @Column(name = "is_tentative", nullable = false)
    private Boolean isTentative = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdByUser;
    
    @Column(name = "created_by_name", length = 200)
    private String createdByName;
    
    @Column(name = "order_group_id")
    private Long orderGroupId;

    @Column(name = "customer_group_id")
    private Long customerGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reseller_id")
    private Reseller reseller;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pic_contact_id")
    private ResellerContact picContact;
    
    @Column(name = "pic_email", length = 150)
    private String picEmail;
    
    @Column(name = "copy_email", length = 500)
    private String copyEmail;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_agent_id")
    private Agent originalAgent;
    
    @Column(name = "ref1", length = 100)
    private String ref1;
    
    @Column(name = "ref2", length = 100)
    private String ref2;
    
    @Column(name = "voucher_number", length = 100)
    private String voucherNumber;
    
    @Column(name = "guest_email", length = 150)
    private String guestEmail;

    @Column(name = "leader_phone", length = 50)
    private String leaderPhone;

    @Column(name = "guest_group_notes", columnDefinition = "TEXT")
    private String guestGroupNotes;
    
    @Column(name = "adult_count")
    private Integer adultCount;
    
    @Column(name = "child_count")
    private Integer childCount;
    
    @Column(name = "dietary_restrictions", length = 500)
    private String dietaryRestrictions;
    
    @Column(name = "currency_code", length = 3)
    private String currencyCode;
    
    @Column(name = "total_fee_amount", precision = 10, scale = 2)
    private BigDecimal totalFeeAmount;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate = false;

    @ManyToMany(mappedBy = "orders")
    private Set<Work> works = new HashSet<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

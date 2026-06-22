package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_guests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "guest_type", length = 50)
    private String guestType;

    @Column(name = "is_vip")
    private Boolean isVip = false;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 50)
    private String gender;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "allergies", length = 500)
    private String allergies;

    @Column(name = "special_occasion", length = 500)
    private String specialOccasion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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

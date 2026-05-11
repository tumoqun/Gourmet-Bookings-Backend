package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "reseller_contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResellerContact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reseller_id", nullable = false)
    private Reseller reseller;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "email", nullable = false, length = 150)
    private String email;
    
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;
}

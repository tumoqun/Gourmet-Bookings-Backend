package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "order_financial_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFinancialLine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(name = "line_type", nullable = false, length = 50)
    private String lineType;
    
    @Column(name = "description", length = 200)
    private String description;
    
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(name = "currency_code", length = 3)
    private String currencyCode;
    
    @Column(name = "is_tax_included", nullable = false)
    private Boolean isTaxIncluded = false;
}

package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSpecialRequestId implements Serializable {
    
    private Long orderId;
    private Long specialRequestTypeId;
}

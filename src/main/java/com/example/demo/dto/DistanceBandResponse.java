package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceBandResponse {
    
    private Long id;
    private String label;
    private Integer sortOrder;
}

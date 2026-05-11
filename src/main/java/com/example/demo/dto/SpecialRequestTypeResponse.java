package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialRequestTypeResponse {
    
    private Long id;
    private String code;
    private String label;
}

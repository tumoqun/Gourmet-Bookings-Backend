package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse {
    
    private Long id;
    private ResellerResponse reseller;
    private String name;
    private String email;
}

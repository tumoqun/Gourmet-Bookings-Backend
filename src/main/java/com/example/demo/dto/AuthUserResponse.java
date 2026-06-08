package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthUserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private Long guideId;
    private List<String> permissions;
}

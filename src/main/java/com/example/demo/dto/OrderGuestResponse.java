package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderGuestResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String guestType;
    private Boolean isVip;
    private Integer age;
    private String gender;
    private String nationality;
    private String phoneNumber;
    private String allergies;
    private String specialOccasion;
}

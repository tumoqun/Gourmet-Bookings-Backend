package com.example.demo.dto;

import lombok.Data;

@Data
public class GuestResponse {
  private Long id;

  private String firstName;

  private String lastName;

  private String phoneNumber;

  private Integer age;

  private String gender;

  private String allergies;

  private String specialOccasion;

  private String dietaryRestrictions;
}

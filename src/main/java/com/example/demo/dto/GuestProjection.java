package com.example.demo.dto;

public interface GuestProjection {
  Long getId();

  String getFirstName();

  String getLastName();

  String getPhoneNumber();

  Integer getAge();

  String getGender();

  String getAllergies();

  String getSpecialOccasion();

  String getDietaryRestrictions();

  Long getOrderId();
}

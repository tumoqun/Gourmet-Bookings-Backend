package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class WorkOrderGuestResponse {
  private Long orderId;

  private Integer adultCount;

  private Integer childCount;

  private String guestGroupNotes;

  private String leaderPhone;

  private Double averageAge;

  private List<GuestResponse> guests;
}

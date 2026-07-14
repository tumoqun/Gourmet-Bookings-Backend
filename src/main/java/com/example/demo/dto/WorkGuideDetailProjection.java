package com.example.demo.dto;

public interface WorkGuideDetailProjection {
  Long getId();

  Long getGuideId();

  String getName();

  String getPhone();

  String getAvatar();

  String getRole();

  String getStatus();

  String getNote();

  String getRejectionReason();

  Boolean getIsCalendarInvitation();
}

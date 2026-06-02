package com.example.demo.dto;

public interface WorkGuideProjection {
  Long getWorkId();

  Long getGuideId();

  String getGuideFullName();

  String getGuideEmail();

  String getGuidePhone();

  String getGuideRole();

  String getGuideAvatar();

  Boolean getGuideIsActive();
}

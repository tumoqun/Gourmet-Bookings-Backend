package com.example.demo.dto;

/**
 * Projects (work_id, guide_full_name) pairs so the service layer
 * can build the complete guide name list per work row.
 */
public interface AccountingGuideProjection {
  Long getWorkId();
  String getGuideName();
}

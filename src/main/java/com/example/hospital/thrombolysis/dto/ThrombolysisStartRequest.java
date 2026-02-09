package com.example.hospital.thrombolysis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ThrombolysisStartRequest {
  @NotNull
  private Long recordId;

  @NotBlank
  private String thrombolysisStartTime;

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public String getThrombolysisStartTime() {
    return thrombolysisStartTime;
  }

  public void setThrombolysisStartTime(String thrombolysisStartTime) {
    this.thrombolysisStartTime = thrombolysisStartTime;
  }
}

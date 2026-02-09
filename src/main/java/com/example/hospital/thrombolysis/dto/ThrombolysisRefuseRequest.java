package com.example.hospital.thrombolysis.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ThrombolysisRefuseRequest {
  @NotNull
  private Long recordId;
  private List<String> refuseReasons;
  private String refuseRemark;

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public List<String> getRefuseReasons() {
    return refuseReasons;
  }

  public void setRefuseReasons(List<String> refuseReasons) {
    this.refuseReasons = refuseReasons;
  }

  public String getRefuseRemark() {
    return refuseRemark;
  }

  public void setRefuseRemark(String refuseRemark) {
    this.refuseRemark = refuseRemark;
  }
}

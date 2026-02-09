package com.example.hospital.thrombolysis.dto;

import com.example.hospital.thrombolysis.ThrombolysisRecord;
import com.example.hospital.thrombolysis.ThrombolysisStatus;

public class ThrombolysisRecordResponse {
  private Long recordId;
  private String status;
  private ComputedMetrics computed;

  public ThrombolysisRecordResponse(Long recordId, ThrombolysisStatus status, ComputedMetrics computed) {
    this.recordId = recordId;
    this.status = status == null ? null : status.name();
    this.computed = computed;
  }

  public Long getRecordId() {
    return recordId;
  }

  public String getStatus() {
    return status;
  }

  public ComputedMetrics getComputed() {
    return computed;
  }

  public static ThrombolysisRecordResponse fromRecord(ThrombolysisRecord record) {
    ComputedMetrics metrics = new ComputedMetrics(record.getDntMinutes(), record.getOntMinutes(),
      record.getOnsetToArrivalMinutes());
    return new ThrombolysisRecordResponse(record.getId(), record.getStatus(), metrics);
  }

  public static class ComputedMetrics {
    private Integer dntMinutes;
    private Integer ontMinutes;
    private Integer onsetToArrivalMinutes;

    public ComputedMetrics(Integer dntMinutes, Integer ontMinutes, Integer onsetToArrivalMinutes) {
      this.dntMinutes = dntMinutes;
      this.ontMinutes = ontMinutes;
      this.onsetToArrivalMinutes = onsetToArrivalMinutes;
    }

    public Integer getDntMinutes() {
      return dntMinutes;
    }

    public Integer getOntMinutes() {
      return ontMinutes;
    }

    public Integer getOnsetToArrivalMinutes() {
      return onsetToArrivalMinutes;
    }
  }
}

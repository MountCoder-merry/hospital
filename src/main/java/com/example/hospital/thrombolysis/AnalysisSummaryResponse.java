package com.example.hospital.thrombolysis;

import java.util.List;

public class AnalysisSummaryResponse {
  private Summary summary;
  private Series series;

  public AnalysisSummaryResponse(Summary summary, Series series) {
    this.summary = summary;
    this.series = series;
  }

  public Summary getSummary() {
    return summary;
  }

  public Series getSeries() {
    return series;
  }

  public static class Summary {
    private Double avgDntMinutes;

    public Summary(Double avgDntMinutes) {
      this.avgDntMinutes = avgDntMinutes;
    }

    public Double getAvgDntMinutes() {
      return avgDntMinutes;
    }
  }

  public static class Series {
    private List<PhaseDuration> phaseDurations;
    private List<RefuseReasonStat> refuseReasons;

    public Series(List<PhaseDuration> phaseDurations, List<RefuseReasonStat> refuseReasons) {
      this.phaseDurations = phaseDurations;
      this.refuseReasons = refuseReasons;
    }

    public List<PhaseDuration> getPhaseDurations() {
      return phaseDurations;
    }

    public List<RefuseReasonStat> getRefuseReasons() {
      return refuseReasons;
    }
  }

  public static class PhaseDuration {
    private String phase;
    private Double avgMinutes;
    private Double medianMinutes;

    public PhaseDuration(String phase, Double avgMinutes, Double medianMinutes) {
      this.phase = phase;
      this.avgMinutes = avgMinutes;
      this.medianMinutes = medianMinutes;
    }

    public String getPhase() {
      return phase;
    }

    public Double getAvgMinutes() {
      return avgMinutes;
    }

    public Double getMedianMinutes() {
      return medianMinutes;
    }
  }

  public static class RefuseReasonStat {
    private String reason;
    private long count;

    public RefuseReasonStat(String reason, long count) {
      this.reason = reason;
      this.count = count;
    }

    public String getReason() {
      return reason;
    }

    public long getCount() {
      return count;
    }
  }
}

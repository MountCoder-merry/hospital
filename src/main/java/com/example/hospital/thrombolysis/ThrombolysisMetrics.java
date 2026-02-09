package com.example.hospital.thrombolysis;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public final class ThrombolysisMetrics {
  private ThrombolysisMetrics() {}

  public static void validateStartAfterArrival(LocalDateTime arrivalTime, LocalDateTime startTime) {
    if (arrivalTime == null || startTime == null) {
      return;
    }
    if (!startTime.isAfter(arrivalTime)) {
      throw new IllegalArgumentException("thrombolysisStartTime must be after arrivalTime");
    }
  }

  public static void populateDerivedFields(ThrombolysisRecord record) {
    Objects.requireNonNull(record, "record");
    record.setDntMinutes(minutesBetween(record.getArrivalTime(), record.getThrombolysisStartTime()));
    record.setOntMinutes(minutesBetween(record.getOntTime(), record.getThrombolysisStartTime()));
    record.setOnsetToArrivalMinutes(minutesBetween(record.getOntTime(), record.getArrivalTime()));
  }

  private static Integer minutesBetween(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) {
      return null;
    }
    return Math.toIntExact(Duration.between(start, end).toMinutes());
  }
}

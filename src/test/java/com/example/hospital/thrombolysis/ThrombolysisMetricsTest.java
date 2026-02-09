package com.example.hospital.thrombolysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ThrombolysisMetricsTest {
  @Test
  void populatesDerivedFields() {
    ThrombolysisRecord record = new ThrombolysisRecord();
    record.setOntTime(LocalDateTime.of(2025, 1, 1, 9, 0));
    record.setArrivalTime(LocalDateTime.of(2025, 1, 1, 10, 0));
    record.setThrombolysisStartTime(LocalDateTime.of(2025, 1, 1, 10, 30));

    ThrombolysisMetrics.populateDerivedFields(record);

    assertEquals(30, record.getDntMinutes());
    assertEquals(90, record.getOntMinutes());
    assertEquals(60, record.getOnsetToArrivalMinutes());
  }

  @Test
  void ignoresMissingTimes() {
    ThrombolysisRecord record = new ThrombolysisRecord();
    ThrombolysisMetrics.populateDerivedFields(record);

    assertNull(record.getDntMinutes());
    assertNull(record.getOntMinutes());
    assertNull(record.getOnsetToArrivalMinutes());
  }

  @Test
  void validatesStartAfterArrival() {
    LocalDateTime arrival = LocalDateTime.of(2025, 1, 1, 10, 0);
    LocalDateTime start = LocalDateTime.of(2025, 1, 1, 9, 0);

    assertThrows(IllegalArgumentException.class,
      () -> ThrombolysisMetrics.validateStartAfterArrival(arrival, start));
  }
}

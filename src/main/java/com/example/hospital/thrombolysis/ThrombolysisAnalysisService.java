package com.example.hospital.thrombolysis;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class ThrombolysisAnalysisService {
  private final ThrombolysisRecordRepository repository;

  public ThrombolysisAnalysisService(ThrombolysisRecordRepository repository) {
    this.repository = repository;
  }

  public AnalysisSummaryResponse summarize(
    LocalDateTime startTime,
    LocalDateTime endTime,
    Long doctorId,
    Long departmentId
  ) {
    List<ThrombolysisRecord> records = repository.findByCreateTimeBetween(startTime, endTime);

    List<ThrombolysisRecord> filtered = records.stream()
      .filter(record -> doctorId == null || Objects.equals(record.getDoctorId(), doctorId))
      .filter(record -> departmentId == null || Objects.equals(record.getDepartmentId(), departmentId))
      .toList();

    List<Long> dntValues = filtered.stream()
      .map(ThrombolysisRecord::getDntMinutes)
      .filter(Objects::nonNull)
      .map(Integer::longValue)
      .toList();

    double avgDnt = average(dntValues);

    List<Long> arrivalToNotify = durations(filtered, ThrombolysisRecord::getArrivalTime,
      ThrombolysisRecord::getNotifyDoctorTime);
    List<Long> notifyToArrival = durations(filtered, ThrombolysisRecord::getNotifyDoctorTime,
      ThrombolysisRecord::getDoctorArrivalTime);
    List<Long> arrivalToThrombolysis = durations(filtered, ThrombolysisRecord::getArrivalTime,
      ThrombolysisRecord::getThrombolysisStartTime);

    List<AnalysisSummaryResponse.PhaseDuration> phases = List.of(
      new AnalysisSummaryResponse.PhaseDuration("到院→通知", average(arrivalToNotify), median(arrivalToNotify)),
      new AnalysisSummaryResponse.PhaseDuration("通知→到达", average(notifyToArrival), median(notifyToArrival)),
      new AnalysisSummaryResponse.PhaseDuration("到达→溶栓", average(arrivalToThrombolysis), median(arrivalToThrombolysis))
    );

    Map<String, Long> refuseCounts = new HashMap<>();
    filtered.stream()
      .map(ThrombolysisRecord::getRefuseReason)
      .filter(Objects::nonNull)
      .forEach(reason -> {
        String[] parts = reason.split(",");
        for (String part : parts) {
          String trimmed = part.trim();
          if (!trimmed.isEmpty()) {
            refuseCounts.merge(trimmed, 1L, Long::sum);
          }
        }
      });

    List<AnalysisSummaryResponse.RefuseReasonStat> refuseReasons = refuseCounts.entrySet()
      .stream()
      .map(entry -> new AnalysisSummaryResponse.RefuseReasonStat(entry.getKey(), entry.getValue()))
      .sorted(Comparator.comparingLong(AnalysisSummaryResponse.RefuseReasonStat::getCount).reversed())
      .toList();

    AnalysisSummaryResponse.Summary summary = new AnalysisSummaryResponse.Summary(avgDnt);
    AnalysisSummaryResponse.Series series = new AnalysisSummaryResponse.Series(phases, refuseReasons);
    return new AnalysisSummaryResponse(summary, series);
  }

  private static List<Long> durations(
    List<ThrombolysisRecord> records,
    java.util.function.Function<ThrombolysisRecord, LocalDateTime> startGetter,
    java.util.function.Function<ThrombolysisRecord, LocalDateTime> endGetter
  ) {
    return records.stream()
      .map(record -> minutesBetween(startGetter.apply(record), endGetter.apply(record)))
      .filter(Objects::nonNull)
      .toList();
  }

  private static Long minutesBetween(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) {
      return null;
    }
    return Duration.between(start, end).toMinutes();
  }

  private static double average(List<Long> values) {
    if (values.isEmpty()) {
      return 0;
    }
    return values.stream().mapToLong(Long::longValue).average().orElse(0);
  }

  private static double median(List<Long> values) {
    if (values.isEmpty()) {
      return 0;
    }
    List<Long> sorted = new ArrayList<>(values);
    sorted.sort(Long::compareTo);
    int size = sorted.size();
    if (size % 2 == 1) {
      return sorted.get(size / 2);
    }
    long lower = sorted.get(size / 2 - 1);
    long upper = sorted.get(size / 2);
    return (lower + upper) / 2.0;
  }
}

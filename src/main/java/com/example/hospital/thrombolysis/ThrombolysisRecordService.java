package com.example.hospital.thrombolysis;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ThrombolysisRecordService {
  private final ThrombolysisRecordRepository repository;

  public ThrombolysisRecordService(ThrombolysisRecordRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public ThrombolysisRecord saveRecord(ThrombolysisRecord record) {
    ThrombolysisMetrics.validateStartAfterArrival(record.getArrivalTime(), record.getThrombolysisStartTime());
    ThrombolysisMetrics.populateDerivedFields(record);
    return repository.save(record);
  }

  @Transactional
  public ThrombolysisRecord startThrombolysis(Long recordId, LocalDateTime startTime) {
    ThrombolysisRecord record = repository.findById(recordId)
      .orElseThrow(() -> new IllegalArgumentException("record not found"));
    ThrombolysisMetrics.validateStartAfterArrival(record.getArrivalTime(), startTime);
    record.setThrombolysisStartTime(startTime);
    record.setStatus(ThrombolysisStatus.THROMBOLYZED);
    ThrombolysisMetrics.populateDerivedFields(record);
    return repository.save(record);
  }

  @Transactional
  public ThrombolysisRecord refuseThrombolysis(Long recordId, String reason, String remark) {
    ThrombolysisRecord record = repository.findById(recordId)
      .orElseThrow(() -> new IllegalArgumentException("record not found"));
    record.setRefuseReason(reason);
    record.setRefuseRemark(remark);
    record.setStatus(ThrombolysisStatus.REFUSED);
    return repository.save(record);
  }
}

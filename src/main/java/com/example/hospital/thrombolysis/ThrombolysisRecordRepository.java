package com.example.hospital.thrombolysis;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThrombolysisRecordRepository extends JpaRepository<ThrombolysisRecord, Long> {
  List<ThrombolysisRecord> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
}

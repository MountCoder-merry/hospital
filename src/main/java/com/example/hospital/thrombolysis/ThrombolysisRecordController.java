package com.example.hospital.thrombolysis;

import com.example.hospital.thrombolysis.dto.ThrombolysisRecordResponse;
import com.example.hospital.thrombolysis.dto.ThrombolysisRefuseRequest;
import com.example.hospital.thrombolysis.dto.ThrombolysisSaveRequest;
import com.example.hospital.thrombolysis.dto.ThrombolysisStartRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/thrombolysis")
@Validated
public class ThrombolysisRecordController {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private final ThrombolysisRecordService recordService;
  private final ThrombolysisRecordRepository recordRepository;

  public ThrombolysisRecordController(
    ThrombolysisRecordService recordService,
    ThrombolysisRecordRepository recordRepository
  ) {
    this.recordService = recordService;
    this.recordRepository = recordRepository;
  }

  @PostMapping("/save")
  public ThrombolysisRecordResponse save(@Valid @RequestBody ThrombolysisSaveRequest request) {
    ThrombolysisRecord record = request.getRecordId() == null
      ? new ThrombolysisRecord()
      : recordRepository.findById(request.getRecordId())
        .orElseThrow(() -> new IllegalArgumentException("record not found"));

    record.setPatientId(request.getPatientId());
    record.setDoctorId(request.getDoctorId());
    record.setDepartmentId(request.getDepartmentId());
    record.setArrivalTime(parseDateTime(request.getArrivalTime()));
    record.setNotifyDoctorTime(parseDateTime(request.getNotifyDoctorTime()));
    record.setDoctorArrivalTime(parseDateTime(request.getDoctorArrivalTime()));
    record.setOntTime(parseDateTime(request.getOntTime()));
    record.setContactErTime(parseDateTime(request.getContactErTime()));
    record.setThrombolysisStartTime(parseDateTime(request.getThrombolysisStartTime()));

    ThrombolysisRecord saved = recordService.saveRecord(record);
    return ThrombolysisRecordResponse.fromRecord(saved);
  }

  @PostMapping("/start")
  public ThrombolysisRecordResponse start(@Valid @RequestBody ThrombolysisStartRequest request) {
    LocalDateTime startTime = parseRequiredDateTime(request.getThrombolysisStartTime());
    ThrombolysisRecord record = recordService.startThrombolysis(request.getRecordId(), startTime);
    return ThrombolysisRecordResponse.fromRecord(record);
  }

  @PostMapping("/refuse")
  public ThrombolysisRecordResponse refuse(@Valid @RequestBody ThrombolysisRefuseRequest request) {
    String reason = joinReasons(request.getRefuseReasons());
    ThrombolysisRecord record = recordService.refuseThrombolysis(request.getRecordId(), reason, request.getRefuseRemark());
    return ThrombolysisRecordResponse.fromRecord(record);
  }

  private static LocalDateTime parseDateTime(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return LocalDateTime.parse(value, FORMATTER);
  }

  private static LocalDateTime parseRequiredDateTime(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("thrombolysisStartTime is required");
    }
    return LocalDateTime.parse(value, FORMATTER);
  }

  private static String joinReasons(List<String> reasons) {
    if (reasons == null || reasons.isEmpty()) {
      return null;
    }
    return reasons.stream()
      .filter(Objects::nonNull)
      .map(String::trim)
      .filter(value -> !value.isEmpty())
      .distinct()
      .reduce((left, right) -> left + "," + right)
      .orElse(null);
  }
}

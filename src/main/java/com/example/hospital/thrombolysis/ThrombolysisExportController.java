package com.example.hospital.thrombolysis;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
public class ThrombolysisExportController {
  private final ThrombolysisExportService exportService;

  public ThrombolysisExportController(ThrombolysisExportService exportService) {
    this.exportService = exportService;
  }

  @GetMapping("/excel")
  public ResponseEntity<byte[]> exportExcel(
    @RequestParam("startTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime startTime,
    @RequestParam("endTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime endTime
  ) {
    byte[] data = exportService.exportRecords(startTime, endTime);
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=thrombolysis_records.xlsx")
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .body(data);
  }
}

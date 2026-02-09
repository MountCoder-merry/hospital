package com.example.hospital.thrombolysis;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class ThrombolysisAnalysisController {
  private final ThrombolysisAnalysisService analysisService;

  public ThrombolysisAnalysisController(ThrombolysisAnalysisService analysisService) {
    this.analysisService = analysisService;
  }

  @GetMapping("/summary")
  public AnalysisSummaryResponse summary(
    @RequestParam("startTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime startTime,
    @RequestParam("endTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime endTime,
    @RequestParam(value = "doctorId", required = false) Long doctorId,
    @RequestParam(value = "departmentId", required = false) Long departmentId
  ) {
    return analysisService.summarize(startTime, endTime, doctorId, departmentId);
  }
}

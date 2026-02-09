package com.example.hospital.thrombolysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ThrombolysisExportService {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private final ThrombolysisRecordRepository repository;

  public ThrombolysisExportService(ThrombolysisRecordRepository repository) {
    this.repository = repository;
  }

  public byte[] exportRecords(LocalDateTime startTime, LocalDateTime endTime) {
    List<ThrombolysisRecord> records = repository.findByCreateTimeBetween(startTime, endTime);

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      Sheet sheet = workbook.createSheet("records");
      Row header = sheet.createRow(0);
      String[] columns = {
        "记录ID",
        "患者ID",
        "医生ID",
        "科室ID",
        "到达时间",
        "通知医生时间",
        "医生到达时间",
        "发病时间",
        "联系急诊时间",
        "开始溶栓时间",
        "状态",
        "DNT(分钟)",
        "ONT(分钟)",
        "发病到就诊(分钟)",
        "创建时间"
      };
      for (int i = 0; i < columns.length; i++) {
        header.createCell(i).setCellValue(columns[i]);
      }

      int rowIndex = 1;
      for (ThrombolysisRecord record : records) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(valueOf(record.getId()));
        row.createCell(1).setCellValue(valueOf(record.getPatientId()));
        row.createCell(2).setCellValue(valueOf(record.getDoctorId()));
        row.createCell(3).setCellValue(valueOf(record.getDepartmentId()));
        row.createCell(4).setCellValue(formatDateTime(record.getArrivalTime()));
        row.createCell(5).setCellValue(formatDateTime(record.getNotifyDoctorTime()));
        row.createCell(6).setCellValue(formatDateTime(record.getDoctorArrivalTime()));
        row.createCell(7).setCellValue(formatDateTime(record.getOntTime()));
        row.createCell(8).setCellValue(formatDateTime(record.getContactErTime()));
        row.createCell(9).setCellValue(formatDateTime(record.getThrombolysisStartTime()));
        row.createCell(10).setCellValue(record.getStatus() == null ? "" : record.getStatus().name());
        row.createCell(11).setCellValue(valueOf(record.getDntMinutes()));
        row.createCell(12).setCellValue(valueOf(record.getOntMinutes()));
        row.createCell(13).setCellValue(valueOf(record.getOnsetToArrivalMinutes()));
        row.createCell(14).setCellValue(formatDateTime(record.getCreateTime()));
      }

      for (int i = 0; i < columns.length; i++) {
        sheet.autoSizeColumn(i);
      }

      workbook.write(outputStream);
      return outputStream.toByteArray();
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to export thrombolysis records", ex);
    }
  }

  private static String formatDateTime(LocalDateTime value) {
    if (value == null) {
      return "";
    }
    return value.format(DATE_TIME_FORMATTER);
  }

  private static String valueOf(Number value) {
    if (value == null) {
      return "";
    }
    return String.valueOf(value);
  }
}

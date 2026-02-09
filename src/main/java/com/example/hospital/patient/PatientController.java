package com.example.hospital.patient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
@Validated
public class PatientController {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private final PatientService service;

  public PatientController(PatientService service) {
    this.service = service;
  }

  @PostMapping
  public PatientCreateResponse create(@Valid @RequestBody PatientCreateRequest request) {
    Patient patient = new Patient();
    patient.setName(request.getName());
    patient.setGender(request.getGender());
    patient.setDepartmentId(request.getDepartmentId());
    Patient saved = service.create(patient);
    return new PatientCreateResponse(saved.getId(), saved.getCreateTime().format(FORMATTER));
  }

  @GetMapping("/{id}")
  public PatientDetailResponse detail(@PathVariable Long id) {
    Patient patient = service.get(id);
    return new PatientDetailResponse(patient.getId(), patient.getName(), patient.getGender(), patient.getDepartmentId(),
      patient.getCreateTime().format(FORMATTER));
  }

  public static class PatientCreateRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String gender;

    @NotNull
    private Long departmentId;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getGender() {
      return gender;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }

    public Long getDepartmentId() {
      return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
      this.departmentId = departmentId;
    }
  }

  public static class PatientCreateResponse {
    private Long id;
    private String createdAt;

    public PatientCreateResponse(Long id, String createdAt) {
      this.id = id;
      this.createdAt = createdAt;
    }

    public Long getId() {
      return id;
    }

    public String getCreatedAt() {
      return createdAt;
    }
  }

  public static class PatientDetailResponse {
    private Long id;
    private String name;
    private String gender;
    private Long departmentId;
    private String createdAt;

    public PatientDetailResponse(Long id, String name, String gender, Long departmentId, String createdAt) {
      this.id = id;
      this.name = name;
      this.gender = gender;
      this.departmentId = departmentId;
      this.createdAt = createdAt;
    }

    public Long getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public String getGender() {
      return gender;
    }

    public Long getDepartmentId() {
      return departmentId;
    }

    public String getCreatedAt() {
      return createdAt;
    }
  }
}

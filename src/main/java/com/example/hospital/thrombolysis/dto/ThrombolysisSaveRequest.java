package com.example.hospital.thrombolysis.dto;

import jakarta.validation.constraints.NotNull;

public class ThrombolysisSaveRequest {
  private Long recordId;

  @NotNull
  private Long patientId;

  private Long doctorId;
  private Long departmentId;
  private String arrivalTime;
  private String notifyDoctorTime;
  private String doctorArrivalTime;
  private String ontTime;
  private String contactErTime;
  private String thrombolysisStartTime;

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public Long getPatientId() {
    return patientId;
  }

  public void setPatientId(Long patientId) {
    this.patientId = patientId;
  }

  public Long getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(Long doctorId) {
    this.doctorId = doctorId;
  }

  public Long getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Long departmentId) {
    this.departmentId = departmentId;
  }

  public String getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(String arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public String getNotifyDoctorTime() {
    return notifyDoctorTime;
  }

  public void setNotifyDoctorTime(String notifyDoctorTime) {
    this.notifyDoctorTime = notifyDoctorTime;
  }

  public String getDoctorArrivalTime() {
    return doctorArrivalTime;
  }

  public void setDoctorArrivalTime(String doctorArrivalTime) {
    this.doctorArrivalTime = doctorArrivalTime;
  }

  public String getOntTime() {
    return ontTime;
  }

  public void setOntTime(String ontTime) {
    this.ontTime = ontTime;
  }

  public String getContactErTime() {
    return contactErTime;
  }

  public void setContactErTime(String contactErTime) {
    this.contactErTime = contactErTime;
  }

  public String getThrombolysisStartTime() {
    return thrombolysisStartTime;
  }

  public void setThrombolysisStartTime(String thrombolysisStartTime) {
    this.thrombolysisStartTime = thrombolysisStartTime;
  }
}

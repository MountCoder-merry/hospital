package com.example.hospital.thrombolysis;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "thrombolysis_record")
public class ThrombolysisRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "patient_id", nullable = false)
  private Long patientId;

  @Column(name = "doctor_id")
  private Long doctorId;

  @Column(name = "department_id")
  private Long departmentId;

  @Column(name = "arrival_time")
  private LocalDateTime arrivalTime;

  @Column(name = "notify_doctor_time")
  private LocalDateTime notifyDoctorTime;

  @Column(name = "doctor_arrival_time")
  private LocalDateTime doctorArrivalTime;

  @Column(name = "ont_time")
  private LocalDateTime ontTime;

  @Column(name = "contact_er_time")
  private LocalDateTime contactErTime;

  @Column(name = "thrombolysis_start_time")
  private LocalDateTime thrombolysisStartTime;

  @Column(name = "refuse_reason", length = 500)
  private String refuseReason;

  @Column(name = "refuse_remark", length = 500)
  private String refuseRemark;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private ThrombolysisStatus status = ThrombolysisStatus.PENDING;

  @Column(name = "dnt_minutes")
  private Integer dntMinutes;

  @Column(name = "ont_minutes")
  private Integer ontMinutes;

  @Column(name = "onset_to_arrival_minutes")
  private Integer onsetToArrivalMinutes;

  @CreationTimestamp
  @Column(name = "create_time", nullable = false, updatable = false)
  private LocalDateTime createTime;

  @UpdateTimestamp
  @Column(name = "update_time", nullable = false)
  private LocalDateTime updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public LocalDateTime getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(LocalDateTime arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public LocalDateTime getNotifyDoctorTime() {
    return notifyDoctorTime;
  }

  public void setNotifyDoctorTime(LocalDateTime notifyDoctorTime) {
    this.notifyDoctorTime = notifyDoctorTime;
  }

  public LocalDateTime getDoctorArrivalTime() {
    return doctorArrivalTime;
  }

  public void setDoctorArrivalTime(LocalDateTime doctorArrivalTime) {
    this.doctorArrivalTime = doctorArrivalTime;
  }

  public LocalDateTime getOntTime() {
    return ontTime;
  }

  public void setOntTime(LocalDateTime ontTime) {
    this.ontTime = ontTime;
  }

  public LocalDateTime getContactErTime() {
    return contactErTime;
  }

  public void setContactErTime(LocalDateTime contactErTime) {
    this.contactErTime = contactErTime;
  }

  public LocalDateTime getThrombolysisStartTime() {
    return thrombolysisStartTime;
  }

  public void setThrombolysisStartTime(LocalDateTime thrombolysisStartTime) {
    this.thrombolysisStartTime = thrombolysisStartTime;
  }

  public String getRefuseReason() {
    return refuseReason;
  }

  public void setRefuseReason(String refuseReason) {
    this.refuseReason = refuseReason;
  }

  public String getRefuseRemark() {
    return refuseRemark;
  }

  public void setRefuseRemark(String refuseRemark) {
    this.refuseRemark = refuseRemark;
  }

  public ThrombolysisStatus getStatus() {
    return status;
  }

  public void setStatus(ThrombolysisStatus status) {
    this.status = status;
  }

  public Integer getDntMinutes() {
    return dntMinutes;
  }

  public void setDntMinutes(Integer dntMinutes) {
    this.dntMinutes = dntMinutes;
  }

  public Integer getOntMinutes() {
    return ontMinutes;
  }

  public void setOntMinutes(Integer ontMinutes) {
    this.ontMinutes = ontMinutes;
  }

  public Integer getOnsetToArrivalMinutes() {
    return onsetToArrivalMinutes;
  }

  public void setOnsetToArrivalMinutes(Integer onsetToArrivalMinutes) {
    this.onsetToArrivalMinutes = onsetToArrivalMinutes;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }
}

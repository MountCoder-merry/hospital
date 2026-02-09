package com.example.hospital.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "patient")
public class Patient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "gender", nullable = false, length = 1)
  private String gender;

  @Column(name = "department_id", nullable = false)
  private Long departmentId;

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

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }
}

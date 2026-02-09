package com.example.hospital.patient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {
  private final PatientRepository repository;

  public PatientService(PatientRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public Patient create(Patient patient) {
    return repository.save(patient);
  }

  public Patient get(Long id) {
    return repository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("patient not found"));
  }
}

CREATE TABLE thrombolysis_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id BIGINT NOT NULL,
  doctor_id BIGINT,
  department_id BIGINT,
  arrival_time DATETIME,
  notify_doctor_time DATETIME,
  doctor_arrival_time DATETIME,
  ont_time DATETIME,
  contact_er_time DATETIME,
  thrombolysis_start_time DATETIME,
  refuse_reason VARCHAR(500),
  refuse_remark VARCHAR(500),
  status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
  dnt_minutes INT,
  ont_minutes INT,
  onset_to_arrival_minutes INT,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_status CHECK (status IN ('PENDING', 'THROMBOLYZED', 'REFUSED'))
);

CREATE INDEX idx_record_patient_id ON thrombolysis_record(patient_id);
CREATE INDEX idx_record_create_time ON thrombolysis_record(create_time);
CREATE INDEX idx_record_status ON thrombolysis_record(status);

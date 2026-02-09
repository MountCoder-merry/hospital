# 急诊溶栓管理系统设计输出

## 里程碑拆分（MVP → V1 → V2）

### MVP（4-6 周）
- **患者与溶栓记录主流程**：患者建档、时间节点录入、溶栓/拒绝决策。
- **核心时间计算**：ONT、DNT、发病到就诊时间自动计算并持久化。
- **基础权限**：医生/管理员两级角色（只读 vs 可编辑）。
- **基础分析**：DNT/ONT 平均值、溶栓率统计（表格 + 简单图表）。
- **导出**：按时间范围导出 CSV/Excel。

### V1（6-10 周）
- **完整统计图表**：ECharts 折线/柱状/饼图。
- **多维筛选**：时间范围、科室、医生维度筛选。
- **质控指标**：到院→通知、通知→到达、到达→溶栓等细化指标。
- **审计日志**：关键时间节点变更记录与追溯。

### V2（10-16 周）
- **高级报表**：多院区、分病区对比分析。
- **科研输出**：数据脱敏/匿名导出。
- **移动端适配**：响应式支持/移动端小程序方案。
- **API 生态**：与 HIS/EMR 系统对接的数据同步。

## API 详细契约（请求/响应/错误码）

### 统一说明
- **Base URL**: `/api`
- **时间格式**: `YYYY-MM-DD HH:mm` (UTC+8)
- **分页**：`page` 从 1 开始，`pageSize` 默认 20
- **错误响应结构**：
  ```json
  {
    "code": "VALIDATION_ERROR",
    "message": "thrombolysis_start_time must be after arrival_time",
    "detail": {
      "field": "thrombolysis_start_time"
    }
  }
  ```

### 1) 创建患者（最小字段）
**POST** `/api/patient`

**请求**
```json
{
  "name": "张三",
  "gender": "M",
  "departmentId": 2
}
```

**响应**
```json
{
  "id": 1001,
  "createdAt": "2025-01-01 10:20"
}
```

**错误码**
- `VALIDATION_ERROR`
- `DUPLICATE_ID_CARD`

### 2) 获取患者详情
**GET** `/api/patient/{id}`

**响应**
```json
{
  "id": 1001,
  "name": "张三",
  "gender": "M",
  "birthDate": "1968-01-12",
  "idCard": "320101196801120011",
  "phone": "13800000000",
  "departmentId": 2,
  "attendingDoctorId": 12,
  "createdAt": "2025-01-01 10:20"
}
```

**错误码**
- `NOT_FOUND`

### 3) 保存溶栓记录
**POST** `/api/thrombolysis/save`

**请求**
```json
{
  "patientId": 1001,
  "arrivalTime": "2025-01-01 10:10",
  "notifyDoctorTime": "2025-01-01 10:15",
  "doctorArrivalTime": "2025-01-01 10:25",
  "ontTime": "2025-01-01 09:00",
  "contactErTime": "2025-01-01 10:16",
  "thrombolysisStartTime": null
}
```

**响应**
```json
{
  "recordId": 5001,
  "status": "PENDING",
  "computed": {
    "dntMinutes": null,
    "ontMinutes": null,
    "onsetToArrivalMinutes": 70
  }
}
```

**错误码**
- `VALIDATION_ERROR`
- `NOT_FOUND`

### 4) 开始溶栓
**POST** `/api/thrombolysis/start`

**请求**
```json
{
  "recordId": 5001,
  "thrombolysisStartTime": "2025-01-01 10:40"
}
```

**响应**
```json
{
  "recordId": 5001,
  "status": "THROMBOLYZED",
  "computed": {
    "dntMinutes": 30,
    "ontMinutes": 100,
    "onsetToArrivalMinutes": 70
  }
}
```

**错误码**
- `VALIDATION_ERROR`
- `NOT_FOUND`
- `INVALID_STATE`

### 5) 拒绝溶栓
**POST** `/api/thrombolysis/refuse`

**请求**
```json
{
  "recordId": 5001,
  "refuseReasons": ["超出时间窗", "影像不符合"],
  "refuseRemark": "家属不同意签字"
}
```

**响应**
```json
{
  "recordId": 5001,
  "status": "REFUSED"
}
```

**错误码**
- `VALIDATION_ERROR`
- `NOT_FOUND`
- `INVALID_STATE`

### 6) 分析汇总
**GET** `/api/analysis/summary`

**查询参数**
- `startTime` / `endTime`
- `departmentId`
- `doctorId`

**响应**
```json
{
  "summary": {
    "avgDntMinutes": 38
  },
  "series": {
    "phaseDurations": [
      {"phase": "到院→通知", "avgMinutes": 5, "medianMinutes": 4},
      {"phase": "通知→到达", "avgMinutes": 10, "medianMinutes": 9},
      {"phase": "到达→溶栓", "avgMinutes": 25, "medianMinutes": 24}
    ],
    "refuseReasons": [
      {"reason": "超出时间窗", "count": 10},
      {"reason": "影像不符合", "count": 5}
    ]
  }
}
```

**错误码**
- `VALIDATION_ERROR`

### 7) 导出
**GET** `/api/export/excel`

**查询参数**
- `startTime` / `endTime`
- `format` = `xlsx` | `csv`

**响应**
- 文件流（Excel 示例：记录字段 + DNT/ONT/发病到就诊分钟）

**错误码**
- `VALIDATION_ERROR`

### 错误码定义（建议）
- `VALIDATION_ERROR` 参数不合法
- `NOT_FOUND` 资源不存在
- `INVALID_STATE` 状态不允许（如已拒绝再开始溶栓）
- `FORBIDDEN` 权限不足
- `INTERNAL_ERROR` 服务器异常

### 统一校验与计算规则
- 溶栓开始时间必须晚于到院时间，否则返回 `VALIDATION_ERROR`。
- 保存/开始溶栓时自动计算并落库：\n
  - DNT = 到院时间 → 溶栓时间（分钟）\n
  - ONT = 发病时间 → 溶栓时间（分钟）\n
  - 发病到就诊 = 发病时间 → 到院时间（分钟）

### Postman / curl 示例
```bash
curl -X POST http://localhost:8080/api/patient \\
  -H 'Content-Type: application/json' \\
  -d '{\"name\":\"张三\",\"gender\":\"M\",\"departmentId\":2}'
```

```bash
curl -X POST http://localhost:8080/api/thrombolysis/save \\
  -H 'Content-Type: application/json' \\
  -d '{\"patientId\":1001,\"arrivalTime\":\"2025-01-01 10:10\",\"notifyDoctorTime\":\"2025-01-01 10:15\",\"doctorArrivalTime\":\"2025-01-01 10:25\",\"ontTime\":\"2025-01-01 09:00\",\"contactErTime\":\"2025-01-01 10:16\"}'
```

```bash
curl -X POST http://localhost:8080/api/thrombolysis/start \\
  -H 'Content-Type: application/json' \\
  -d '{\"recordId\":5001,\"thrombolysisStartTime\":\"2025-01-01 10:40\"}'
```

```bash
curl -X POST http://localhost:8080/api/thrombolysis/refuse \\
  -H 'Content-Type: application/json' \\
  -d '{\"recordId\":5001,\"refuseReasons\":[\"超出时间窗\"],\"refuseRemark\":\"家属不同意签字\"}'
```

## DB 表结构最终版（含索引/约束）

### 1) patient
```sql
CREATE TABLE patient (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  gender CHAR(1) NOT NULL,
  birth_date DATE,
  id_card VARCHAR(30) UNIQUE,
  phone VARCHAR(20),
  department_id BIGINT NOT NULL,
  attending_doctor_id BIGINT,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_gender CHECK (gender IN ('M','F'))
);
CREATE INDEX idx_patient_department ON patient(department_id);
```

### 2) thrombolysis_record
```sql
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
  CONSTRAINT fk_record_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
  CONSTRAINT chk_status CHECK (status IN ('PENDING','THROMBOLYZED','REFUSED'))
);
CREATE INDEX idx_record_patient_id ON thrombolysis_record(patient_id);
CREATE INDEX idx_record_create_time ON thrombolysis_record(create_time);
CREATE INDEX idx_record_status ON thrombolysis_record(status);
```

### 3) thrombolysis_refuse_reason (规范化原因)
```sql
CREATE TABLE thrombolysis_refuse_reason (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  record_id BIGINT NOT NULL,
  reason VARCHAR(100) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_refuse_record FOREIGN KEY (record_id) REFERENCES thrombolysis_record(id)
);
CREATE INDEX idx_refuse_record ON thrombolysis_refuse_reason(record_id);
```

### 4) audit_log
```sql
CREATE TABLE audit_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  record_id BIGINT NOT NULL,
  operator_id BIGINT NOT NULL,
  field_name VARCHAR(50) NOT NULL,
  old_value VARCHAR(200),
  new_value VARCHAR(200),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_audit_record FOREIGN KEY (record_id) REFERENCES thrombolysis_record(id)
);
CREATE INDEX idx_audit_record ON audit_log(record_id);
```

## 前端路由与页面组件清单

### 路由
- `/patients` 患者列表页
- `/patients/:id` 患者详情页
- `/thrombolysis/:patientId` 急诊溶栓记录页
- `/analysis` 数据分析页
- `/export` 数据导出页

### 页面组件

1. **患者列表页**
   - PatientSearchForm
   - PatientTable
   - PatientCreateDialog

2. **急诊溶栓记录页**
   - TimelineTimePicker（统一时间选择器）
   - ThrombolysisActionBar（开始溶栓/拒绝溶栓/保存）
   - RefuseReasonDialog
   - TimeMetricsPanel（DNT/ONT 等指标）

3. **数据分析页**
   - FilterPanel（时间范围/科室/医生）
   - DntTrendChart（折线）
   - OntTrendChart（折线）
   - PhaseDurationBarChart（柱状）
   - RefuseReasonPieChart（饼图）

4. **数据导出页**
   - ExportFilterForm
   - ExportHistoryTable

## 每个模块的验收标准（可测试）

### 1) 患者管理模块
- 可新增患者，必填字段校验正确。
- 输入身份证号重复时提示错误。
- 可按姓名/日期筛选患者列表。

### 2) 溶栓记录模块
- 时间字段支持按钮记录当前时间，并允许权限用户修改。
- 保存后自动计算 ONT/DNT/发病到就诊时间。
- “开始溶栓”后状态变更为已溶栓，且溶栓时间 > 到院时间。
- “拒绝溶栓”可多选原因并保存备注。

### 3) 质控分析模块
- 选择时间范围后，图表实时刷新。
- 溶栓率、平均 DNT/ONT 展示正确。
- 饼图原因分布与拒绝记录一致。

### 4) 数据导出模块
- 指定时间范围导出 CSV/XLSX 文件成功。
- 导出内容包含完整时间节点与计算指标。

### 5) 权限与审计模块
- 普通医生只能编辑自己创建的记录。
- 管理员可查看所有记录并修改。
- 审计日志包含字段变更前后值。

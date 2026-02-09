export interface Patient {
  id: number;
  name: string;
  gender: "M" | "F";
  departmentId: number;
}

export interface ThrombolysisRecordPayload {
  patientId: number;
  arrivalTime?: string | null;
  notifyDoctorTime?: string | null;
  doctorArrivalTime?: string | null;
  ontTime?: string | null;
  contactErTime?: string | null;
  thrombolysisStartTime?: string | null;
}

export interface ThrombolysisComputedMetrics {
  dntMinutes: number | null;
  ontMinutes: number | null;
  onsetToArrivalMinutes: number | null;
}

export interface ThrombolysisRecordResponse {
  recordId: number;
  status: string;
  computed: ThrombolysisComputedMetrics;
}

export interface ThrombolysisRefusePayload {
  recordId: number;
  refuseReasons: string[];
  refuseRemark?: string;
}

export interface ThrombolysisStartPayload {
  recordId: number;
  thrombolysisStartTime: string;
}

export interface PhaseDurationStat {
  phase: string;
  avgMinutes: number;
  medianMinutes: number;
}

export interface RefuseReasonStat {
  reason: string;
  count: number;
}

export interface AnalysisSummaryResponse {
  summary: {
    avgDntMinutes: number;
  };
  series: {
    phaseDurations: PhaseDurationStat[];
    refuseReasons: RefuseReasonStat[];
  };
}

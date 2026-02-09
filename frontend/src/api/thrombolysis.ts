import apiClient from "./client";
import type {
  Patient,
  ThrombolysisRecordPayload,
  ThrombolysisRecordResponse,
  AnalysisSummaryResponse,
  ThrombolysisRefusePayload,
  ThrombolysisStartPayload
} from "../types";

export const createPatient = (payload: Omit<Patient, "id">) =>
  apiClient.post<{ id: number; createdAt: string }>("/patient", payload);

export const saveThrombolysisRecord = (payload: ThrombolysisRecordPayload) =>
  apiClient.post<ThrombolysisRecordResponse>("/thrombolysis/save", payload);

export const startThrombolysis = (payload: ThrombolysisStartPayload) =>
  apiClient.post<ThrombolysisRecordResponse>("/thrombolysis/start", payload);

export const refuseThrombolysis = (payload: ThrombolysisRefusePayload) =>
  apiClient.post<{ recordId: number; status: string }>("/thrombolysis/refuse", payload);

export const fetchAnalysisSummary = (params: {
  startTime: string;
  endTime: string;
  doctorId?: number;
  departmentId?: number;
}) => apiClient.get<AnalysisSummaryResponse>("/analysis/summary", { params });

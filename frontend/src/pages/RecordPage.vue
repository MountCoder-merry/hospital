<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>急诊溶栓记录 - 患者 {{ patientId }}</span>
        <el-button @click="goBack">返回列表</el-button>
      </div>
    </template>

    <el-form :model="form" label-width="160px" class="form-grid">
      <el-form-item label="发病时间 (ONT)">
        <el-date-picker
          v-model="form.ontTime"
          type="datetime"
          placeholder="选择时间"
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DD HH:mm"
        />
      </el-form-item>

      <el-form-item label="到达医院时间">
        <div class="inline">
          <el-button @click="recordNow('arrivalTime')">记录当前时间</el-button>
          <el-date-picker
            v-model="form.arrivalTime"
            type="datetime"
            placeholder="选择时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
          />
        </div>
      </el-form-item>

      <el-form-item label="开始溶栓时间">
        <div class="inline">
          <el-button @click="recordNow('thrombolysisStartTime')">记录当前时间</el-button>
          <el-date-picker
            v-model="form.thrombolysisStartTime"
            type="datetime"
            placeholder="选择时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
          />
        </div>
      </el-form-item>

      <el-form-item label="电话通知医生时间">
        <el-date-picker
          v-model="form.notifyDoctorTime"
          type="datetime"
          placeholder="选择时间"
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DD HH:mm"
        />
      </el-form-item>

      <el-form-item label="医生到达急诊时间">
        <el-date-picker
          v-model="form.doctorArrivalTime"
          type="datetime"
          placeholder="选择时间"
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DD HH:mm"
        />
      </el-form-item>

      <el-form-item label="溶栓医生联系急诊时间">
        <el-date-picker
          v-model="form.contactErTime"
          type="datetime"
          placeholder="选择时间"
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DD HH:mm"
        />
      </el-form-item>
    </el-form>

    <el-divider />

    <el-row :gutter="16" class="metrics">
      <el-col :span="8">
        <el-statistic title="DNT (分钟)" :value="metrics.dntMinutes ?? '-'" />
      </el-col>
      <el-col :span="8">
        <el-statistic title="ONT (分钟)" :value="metrics.ontMinutes ?? '-'" />
      </el-col>
      <el-col :span="8">
        <el-statistic title="发病到就诊 (分钟)" :value="metrics.onsetToArrivalMinutes ?? '-'" />
      </el-col>
    </el-row>

    <el-divider />

    <div class="actions">
      <el-button type="primary" @click="saveRecord">保存</el-button>
      <el-button type="success" @click="startThrombolysisAction">开始溶栓</el-button>
      <el-button type="danger" @click="refuseDialogVisible = true">拒绝溶栓</el-button>
    </div>
  </el-card>

  <RefuseDialog
    v-model="refuseDialogVisible"
    v-model:reasons="refuseReasons"
    v-model:remark="refuseRemark"
    @confirm="submitRefuse"
  />
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import RefuseDialog from "../components/RefuseDialog.vue";
import {
  saveThrombolysisRecord,
  startThrombolysis,
  refuseThrombolysis
} from "../api/thrombolysis";
import type {
  ThrombolysisComputedMetrics,
  ThrombolysisRecordPayload
} from "../types";

const props = defineProps<{ patientId: string }>();
const router = useRouter();

const form = reactive<ThrombolysisRecordPayload>({
  patientId: Number(props.patientId),
  arrivalTime: null,
  notifyDoctorTime: null,
  doctorArrivalTime: null,
  ontTime: null,
  contactErTime: null,
  thrombolysisStartTime: null
});

const metrics = reactive<ThrombolysisComputedMetrics>({
  dntMinutes: null,
  ontMinutes: null,
  onsetToArrivalMinutes: null
});

const recordId = ref<number | null>(null);
const refuseDialogVisible = ref(false);
const refuseReasons = ref<string[]>([]);
const refuseRemark = ref("");

const goBack = () => router.push("/patients");

const recordNow = (field: keyof ThrombolysisRecordPayload) => {
  const now = new Date();
  const pad = (value: number) => String(value).padStart(2, "0");
  const formatted = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(
    now.getHours()
  )}:${pad(now.getMinutes())}`;
  form[field] = formatted;
};

const updateMetrics = (data?: ThrombolysisComputedMetrics) => {
  if (!data) {
    metrics.dntMinutes = null;
    metrics.ontMinutes = null;
    metrics.onsetToArrivalMinutes = null;
    return;
  }
  metrics.dntMinutes = data.dntMinutes;
  metrics.ontMinutes = data.ontMinutes;
  metrics.onsetToArrivalMinutes = data.onsetToArrivalMinutes;
};

const saveRecord = async () => {
  try {
    const response = await saveThrombolysisRecord(form);
    recordId.value = response.data.recordId;
    updateMetrics(response.data.computed);
    ElMessage.success("记录已保存");
  } catch (error) {
    ElMessage.error((error as Error).message);
  }
};

const startThrombolysisAction = async () => {
  if (!recordId.value) {
    ElMessage.warning("请先保存记录");
    return;
  }
  if (!form.thrombolysisStartTime) {
    ElMessage.warning("请先填写开始溶栓时间");
    return;
  }
  try {
    const response = await startThrombolysis({
      recordId: recordId.value,
      thrombolysisStartTime: form.thrombolysisStartTime
    });
    updateMetrics(response.data.computed);
    ElMessage.success("已开始溶栓");
  } catch (error) {
    ElMessage.error((error as Error).message);
  }
};

const submitRefuse = async () => {
  if (!recordId.value) {
    ElMessage.warning("请先保存记录");
    return;
  }
  try {
    await refuseThrombolysis({
      recordId: recordId.value,
      refuseReasons: refuseReasons.value,
      refuseRemark: refuseRemark.value
    });
    refuseDialogVisible.value = false;
    ElMessage.success("已提交拒绝原因");
  } catch (error) {
    ElMessage.error((error as Error).message);
  }
};

</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 12px 24px;
}

.inline {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.actions {
  display: flex;
  gap: 12px;
}

.metrics {
  margin-top: 8px;
}
</style>

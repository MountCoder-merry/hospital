<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>质控分析</span>
        <el-button type="primary" @click="loadSummary">刷新</el-button>
      </div>
    </template>

    <el-form :inline="true" class="filters">
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DD HH:mm"
        />
      </el-form-item>
      <el-form-item label="科室">
        <el-input-number v-model="departmentId" :min="1" placeholder="科室ID" />
      </el-form-item>
      <el-form-item label="医生">
        <el-input-number v-model="doctorId" :min="1" placeholder="医生ID" />
      </el-form-item>
    </el-form>

    <el-row :gutter="16" class="summary">
      <el-col :span="8">
        <el-statistic title="平均 DNT (分钟)" :value="summary.avgDntMinutes" />
      </el-col>
    </el-row>

    <el-divider />

    <div class="charts">
      <div class="chart" ref="lineChartRef"></div>
      <div class="chart" ref="barChartRef"></div>
      <div class="chart" ref="pieChartRef"></div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from "vue";
import { ElMessage } from "element-plus";
import * as echarts from "echarts";
import { fetchAnalysisSummary } from "../api/thrombolysis";
import type { AnalysisSummaryResponse } from "../types";

const lineChartRef = ref<HTMLDivElement | null>(null);
const barChartRef = ref<HTMLDivElement | null>(null);
const pieChartRef = ref<HTMLDivElement | null>(null);

const lineChart = ref<echarts.ECharts | null>(null);
const barChart = ref<echarts.ECharts | null>(null);
const pieChart = ref<echarts.ECharts | null>(null);

const dateRange = ref<[string, string] | null>(null);
const departmentId = ref<number | null>(null);
const doctorId = ref<number | null>(null);

const summary = ref<AnalysisSummaryResponse["summary"]>({
  avgDntMinutes: 0
});

const seriesData = ref<AnalysisSummaryResponse["series"]>({
  phaseDurations: [],
  refuseReasons: []
});

const formatDateTime = (date: Date) => {
  const pad = (value: number) => String(value).padStart(2, "0");
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(
    date.getHours()
  )}:${pad(date.getMinutes())}`;
};

const initCharts = () => {
  if (lineChartRef.value) {
    lineChart.value = echarts.init(lineChartRef.value);
  }
  if (barChartRef.value) {
    barChart.value = echarts.init(barChartRef.value);
  }
  if (pieChartRef.value) {
    pieChart.value = echarts.init(pieChartRef.value);
  }
};

const renderCharts = () => {
  const phases = seriesData.value.phaseDurations;
  const phaseLabels = phases.map((item) => item.phase);

  lineChart.value?.setOption({
    title: { text: "各阶段平均耗时" },
    tooltip: { trigger: "axis" },
    xAxis: { type: "category", data: phaseLabels },
    yAxis: { type: "value", name: "分钟" },
    series: [
      {
        type: "line",
        data: phases.map((item) => item.avgMinutes)
      }
    ]
  });

  barChart.value?.setOption({
    title: { text: "各阶段中位数耗时" },
    tooltip: { trigger: "axis" },
    xAxis: { type: "category", data: phaseLabels },
    yAxis: { type: "value", name: "分钟" },
    series: [
      {
        type: "bar",
        data: phases.map((item) => item.medianMinutes)
      }
    ]
  });

  pieChart.value?.setOption({
    title: { text: "未溶栓原因分布", left: "center" },
    tooltip: { trigger: "item" },
    series: [
      {
        type: "pie",
        radius: "60%",
        data: seriesData.value.refuseReasons.map((item) => ({
          name: item.reason,
          value: item.count
        }))
      }
    ]
  });
};

const loadSummary = async () => {
  if (!dateRange.value) {
    ElMessage.warning("请选择时间范围");
    return;
  }
  try {
    const response = await fetchAnalysisSummary({
      startTime: dateRange.value[0],
      endTime: dateRange.value[1],
      departmentId: departmentId.value ?? undefined,
      doctorId: doctorId.value ?? undefined
    });
    summary.value = response.data.summary;
    seriesData.value = response.data.series;
    renderCharts();
  } catch (error) {
    ElMessage.error((error as Error).message);
  }
};

onMounted(() => {
  initCharts();
  const now = new Date();
  const start = new Date();
  start.setHours(0, 0, 0, 0);
  dateRange.value = [formatDateTime(start), formatDateTime(now)];
});

onBeforeUnmount(() => {
  lineChart.value?.dispose();
  barChart.value?.dispose();
  pieChart.value?.dispose();
});
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.filters {
  margin-bottom: 16px;
}

.summary {
  margin-bottom: 12px;
}

.charts {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
}

.chart {
  height: 320px;
}
</style>

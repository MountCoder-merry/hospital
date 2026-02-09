<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>患者列表</span>
        <el-button type="primary" @click="dialogVisible = true">新增患者</el-button>
      </div>
    </template>

    <el-table :data="patients" stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="departmentId" label="科室" width="120" />
      <el-table-column label="操作" width="140">
        <template #default="scope">
          <el-button type="primary" link @click="goToRecord(scope.row.id)">进入记录</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增患者" width="420px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="姓名">
        <el-input v-model="form.name" placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="form.gender" placeholder="请选择">
          <el-option label="男" value="M" />
          <el-option label="女" value="F" />
        </el-select>
      </el-form-item>
      <el-form-item label="科室">
        <el-input-number v-model="form.departmentId" :min="1" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submit">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { createPatient } from "../api/thrombolysis";
import type { Patient } from "../types";

const router = useRouter();

const patients = ref<Patient[]>([
  { id: 1001, name: "张三", gender: "M", departmentId: 2 },
  { id: 1002, name: "李四", gender: "F", departmentId: 3 }
]);

const dialogVisible = ref(false);
const form = reactive<Omit<Patient, "id">>({
  name: "",
  gender: "M",
  departmentId: 1
});

const submit = async () => {
  try {
    const response = await createPatient(form);
    patients.value.push({ id: response.data.id, ...form });
    dialogVisible.value = false;
    ElMessage.success("新增患者成功");
  } catch (error) {
    ElMessage.error((error as Error).message);
  }
};

const goToRecord = (patientId: number) => {
  router.push(`/record/${patientId}`);
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

<template>
  <el-dialog v-model="visible" title="拒绝溶栓原因" width="480px">
    <el-checkbox-group v-model="localReasons" class="reason-list">
      <el-checkbox label="超出时间窗" />
      <el-checkbox label="患者/家属拒绝" />
      <el-checkbox label="出血风险高" />
      <el-checkbox label="影像不符合" />
      <el-checkbox label="其他" />
    </el-checkbox-group>
    <el-input
      v-model="localRemark"
      type="textarea"
      rows="3"
      placeholder="备注信息"
    />
    <template #footer>
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="confirm">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";

const props = defineProps<{
  modelValue: boolean;
  reasons: string[];
  remark: string;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "update:reasons", value: string[]): void;
  (e: "update:remark", value: string): void;
  (e: "confirm"): void;
}>();

const visible = computed({
  get: () => props.modelValue,
  set: (value: boolean) => emit("update:modelValue", value)
});

const localReasons = ref<string[]>([...props.reasons]);
const localRemark = ref(props.remark);

watch(
  () => props.modelValue,
  (value) => {
    if (value) {
      localReasons.value = [...props.reasons];
      localRemark.value = props.remark;
    }
  }
);

watch(localReasons, (value) => emit("update:reasons", value));
watch(localRemark, (value) => emit("update:remark", value));

const close = () => {
  emit("update:modelValue", false);
};

const confirm = () => {
  emit("confirm");
};
</script>

<style scoped>
.reason-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 12px;
}
</style>

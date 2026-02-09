<template>
  <el-container class="app-shell">
    <el-header class="app-header">
      <div class="title">急诊溶栓管理系统</div>
      <el-menu mode="horizontal" :default-active="activePath" router>
        <el-menu-item index="/patients">患者列表</el-menu-item>
        <el-menu-item index="/analysis">质控分析</el-menu-item>
      </el-menu>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const activePath = computed(() => route.path.startsWith("/record") ? "/patients" : route.path);
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
  background: #f5f7fa;
}

.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ffffff;
  border-bottom: 1px solid #ebeef5;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
</style>

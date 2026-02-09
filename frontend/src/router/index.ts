import { createRouter, createWebHistory } from "vue-router";
import PatientsPage from "../pages/PatientsPage.vue";
import RecordPage from "../pages/RecordPage.vue";
import AnalysisPage from "../pages/AnalysisPage.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", redirect: "/patients" },
    { path: "/patients", component: PatientsPage },
    { path: "/record/:patientId", component: RecordPage, props: true },
    { path: "/analysis", component: AnalysisPage }
  ]
});

export default router;

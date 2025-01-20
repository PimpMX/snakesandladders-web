import { createRouter, createWebHistory } from "vue-router";
import IndexPage from "@/components/IndexPage.vue";
import LoginComponent from "@/components/LoginComponent.vue";

const routes = [
    { path: "/", name: "Home", component: IndexPage },
    { path: "/login", name: "Login", component: LoginComponent }, // Updated name
];


const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;

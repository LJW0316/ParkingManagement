import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path:"/login",
        name:"login",
        component:()=>import("../components/LoginPage")
    }
]
const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
});
export default router;
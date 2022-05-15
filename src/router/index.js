import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path:"/",
        name:"login",
        component:()=>import("../views/LoginPage")
    },
    {
        path:"/student",
        name:"student",
        component:()=>import("../components/Student")
    },
    {
        path:"/admin",
        name:"admin",
        component:()=>import("../components/Admin")
    },
    {
        path:"/teacher",
        name:"teacher",
        component:()=>import("../components/Teacher")
    }
]
const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
});
export default router;
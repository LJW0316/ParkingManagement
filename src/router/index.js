import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path:"/admin",
        name:"admin",
        component:()=>import("../layout/AdminLayout"),
        children: [
            {
                path:"/admin/car",
                name:"AdminCar",
                component:()=>import("../views/Admin/Admin")
            },
        ]
    },
]
const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
});
export default router;
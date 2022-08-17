import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path:'/',
        name:'login',
        component:()=>import("../views/LoginPage")
    },
    {
        path:"/admin",
        name:"admin",
        component:()=>import("../layout/AdminLayout"),
        children: [
            {
                path:"/admin/car",
                name:"AdminCar",
                component:()=>import("../views/Admin/AdminCar")
            },
            {
                path:"/admin/order",
                name:"AdminOrder",
                component:()=>import("../views/Admin/AdminOrder")
            },
        ]
    },
]
const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
});
export default router;
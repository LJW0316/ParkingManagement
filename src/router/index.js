import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path:"/",
        name:"login",
        component:()=>import("../views/LoginPage")
    },
    {
        path:"/teacher",
        name:"teacher",
        component:()=>import("../views/TeacherHome"),
        children: [
            {
                path:"/grade",
                name:"gradeInput",
                component:()=>import("../components/Teacher/GradeInput")
            },
            {
                path:"/welcome",
                name:"welcome",
                component:()=>import("../components/Teacher/TeacherMain")
            }
        ]
    }
]
const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
});
export default router;
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
        component:()=>import("../views/TeacherMain"),
        children: [
            {
                path:"/teacher/home",
                name:"teacherHome",
                component:()=>import("../components/Teacher/TeacherHome")
            },
            {
                path:"/teacher/grade",
                name:"gradeInput",
                component:()=>import("../components/Teacher/GradeInput")
            }
        ]
    },
    {
        path:"/student",
        name:"student",
        component:()=>import("../views/StudentMain"),
        children: [
            {
                path:"/student/home",
                name:"studentHome",
                component:()=>import("../components/Student/StudentHome")
            },
        ]
    },
]
const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
});
export default router;
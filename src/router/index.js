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
        component:()=>import("../layout/TeacherLayout"),
        children: [
            {
                path:"/teacher/home",
                name:"teacherHome",
                component:()=>import("../views/Teacher/TeacherHome")
            },
            {
                path:"/teacher/grade",
                name:"gradeInput",
                component:()=>import("../views/Teacher/GradeInput")
            }
        ]
    },
    {
        path:"/admin",
        name:"admin",
        component:()=>import("../layout/AdminLayout"),
        children: [
            {
                path:"/admin/student",
                name:"AdminStudent",
                component:()=>import("../views/Admin/AdminStudent")
            },
            {
                path:"/admin/teacher",
                name:"AdminTeacher",
                component:()=>import("../views/Admin/AdminTeacher")
            },
            {
                path:"/admin/course",
                name:"AdminCourse",
                component:()=>import("../views/Admin/AdminCourse")
            },
            {
                path:"/admin/studentcourse",
                name:"AdminStudentCourse",
                component:()=>import("../views/Admin/AdminStudentCourse")
            },
        ]
    },
    {
        path:"/student",
        name:"student",
        component:()=>import("../layout/StudentLayout"),
        children: [
            {
                path:"/student/addCourse",
                name:"addCourse",
                component:()=>import("../views/Student/AddCourse")
            },
            {
                path:"/student/lookUpCourse",
                name:"lookUpCourse",
                component:()=>import("../views/Student/LookupCourse")
            },
            {
                path:"/student/lookUpGrade",
                name:"lookUpGrade",
                component:()=>import("../views/Student/LookupGrade")
            }
        ]
    }
]
const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
});
export default router;
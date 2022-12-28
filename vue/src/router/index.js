import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store";

/*const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push (location) {
    return originalPush.call(this, location).catch(err => err)
};*/

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        component: () => import('../views/Manage.vue'),
        redirect: "/login",
        children: [
            {path: 'home', name: '首页', component: () => import('../views/Home.vue')},
            {path: 'user', name: '信息员管理', component: () => import('../views/User.vue')},
            {path: 'admin', name: '管理员管理', component: () => import('../views/Admin.vue')},
            {path: 'password', name: '修改密码', component: () => import('../views/Password.vue')},
            {path: 'personInformation', name: '个人信息', component: () => import('../views/PersonInformation.vue')},
            {path: 'studentInformation', name: '信息员信息', component: () => import('../views/StudentInformation.vue')},
            {path: 'campus', name: '校区设置', component: () => import('../views/Campus.vue')},
            {path: 'course', name: '课程设置', component: () => import('../views/Course.vue')},
            {path: 'semester', name: '学期设置', component: () => import('../views/Semester.vue')},
            {path: 'notice', name: '公告', component: () => import('../views/Notice.vue')},
            {path: 'noticeShow', name: '公告展示', component: () => import('../views/NoticeShow.vue')},
            {path: 'attendance', name: '出勤反馈统计', component: () => import('../views/Attendance.vue')},
            {path: 'attendanceSubmit', name: '出勤反馈', component: () => import('../views/AttendanceSubmit.vue')},
            {path: 'information', name: '信息反馈统计', component: () => import('../views/Information.vue')},
            {path: 'informationSubmit', name: '信息反馈', component: () => import('../views/InformationSubmit.vue')},
            {path: 'informerStatistics', name: '信息员统计', component: () => import('../views/InformerStatistics.vue')},
        ]
    },
    {
        path: '/about',
        name: 'About',
        component: () => import('../views/About.vue')
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../views/404.vue')
    }
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
    // 设置当前的路由名称，为了在Header组件中去使用
    localStorage.setItem("currentPathName", to.name);
    // 触发store的数据更新
    store.commit("setPath");
    // 放行路由
    next()
});

export default router

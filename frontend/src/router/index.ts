import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/admin',
    name: "admin-home",
    component: () => import("@/views/admin/AdminHome.vue"),
    children: [
      {
        path: "student-manage",
        name: "student-manage",
        component: () => import("@/views/admin/StudentManage.vue")
      },

      {
        path: "role-manage",
        name: "role-manage",
        component: () => import("@/views/admin/RoleManage.vue")
      }
    ]
  },

  {
    path: "/student",
    name: "student-home",
    component: () => import("@/views/student/StudentHome.vue")
  },

  {
    path: "/login",
    name: "login",
    component: () => import("@/views/LoginView.vue")
  },


  {
    path: "/test",
    name: "test",
    component: () => import("@/views/Test.vue")
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

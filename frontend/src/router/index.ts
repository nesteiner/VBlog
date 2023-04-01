import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "login",
    component: () => import("@/views/LoginView.vue")
  },

  {
    path: "/home",
    name: "home",
    component: () => import("@/views/HomeView.vue"),

    children: [
      {
        path: "/",
        name: "default",
        component: () => import("@/components/ArticleList.vue")
      },
      
      {
        path: "/articleList",
        name: "articleList",
        component: () => import("@/components/ArticleList.vue")
      },

      {
        path: "/postArticle",
        name: "postArticle",
        component: () => import("@/components/PostArticle.vue")
      },

      {
        path: "/blogDetail",
        name: "blogDetail",
        component: () => import("@/components/BlogDetail.vue")
      },

      {
        path: "/editBlog",
        name: "editBlog",
        component: () => import("@/components/PostArticle.vue")
      }
    ]
  },

  {
    path: "/test",
    name: "test",
    component: () => import("@/views/Test.vue")
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router

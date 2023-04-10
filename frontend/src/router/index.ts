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
        path: "",
        name: "default",
        component: () => import("@/components/ArticleList.vue")
      },

      {
        path: "article-list",
        name: "article-list",
        component: () => import("@/components/ArticleList.vue")
      },

      {
        path: "post-article",
        name: "post-article",
        component: () => import("@/components/PostArticle.vue")
      },

      {
        path: "blog-detail",
        name: "blog-detail",
        component: () => import("@/components/BlogDetail.vue")
      },

      {
        path: "edit-blog",
        name: "edit-blog",
        component: () => import("@/components/PostArticle.vue")
      },

      {
        path: "category-manage",
        name: "category-manage",
        component: () => import("@/components/CategoryManage.vue")
      },
      
      {
        path: "user-manage",
        name: "user-manage",
        component: () => import("@/components/UserManage.vue")
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

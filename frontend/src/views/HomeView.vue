<template>
  <el-container class="home">
    <el-header>
      <div class="title">V部落博客管理平台</div>
      <div class="userinfo">
        <el-dropdown @command="handleCommand">
          <span class="dropdown-link user-info">
            {{ username }}
            <i class="el-icon-arrow-down el-icon--right user-info"/>
          </span>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="system-message">系统消息</el-dropdown-item>
              <el-dropdown-item command="my-article">我的文章</el-dropdown-item>
              <el-dropdown-item command="my-home">个人主页</el-dropdown-item>
              <el-dropdown-item command="add-post">添加文章</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- <el-main>
      <router-view/>
    </el-main> -->

    <el-main>
      <el-aside width="200px">
        <el-menu default-active="0" router>
          <template v-for="(item, index) in routes" :key="index">
            <el-sub-menu :index="`${index}`" v-if="item.children !== undefined && item.children.length > 1">
              <template #title>
                <span> {{ item.name }} </span>
              </template>

              <template v-for="child in item.children" :key="child.path">
                <el-menu-item :index="child.path">
                  {{ child.name }}
                </el-menu-item>
              </template>
            </el-sub-menu>

            <template v-else-if="item.children !== undefined && item.children.length == 1">
              <el-menu-item :index="item.children[0].path">
                <template #title>
                  <span> {{ item.children[0].name }} </span>
                </template>
              </el-menu-item>
            </template>

            <template v-else>
              <el-menu-item :index="item.path">
                <template #title>
                  <span> {{ item.name }} </span>
                </template>
              </el-menu-item>
            </template>
          </template>
        </el-menu>
      </el-aside>

      <router-view/>
    </el-main>    

  </el-container>

</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import {currentUsername, isadmin, logout} from "@/api"
import { useRouter } from 'vue-router';
import {ElContainer, ElHeader, ElMain, 
  ElDropdown, ElDropdownMenu, ElDropdownItem,
  ElAside, ElMenu, ElSubMenu, ElMenuItem
} from "element-plus"

interface IRoute {
  path?: string,
  name: string,
  children?: IRoute[]
}

const router = useRouter()

const username = ref("")
const routes = ref([] as IRoute[])
const handleCommand = (command: string) => {
  if (command == "logout") {
    let flag = confirm("确认登出吗")
    if (flag) {
      logout()
      router.replace({name: "login"})
    }
  } else if (command == "my-article") {
    router.push({name: "article-list"})
  } else if (command == "add-post") {
    router.push({name: "post-article"})
  }
}
onMounted(async () => {
  username.value = await currentUsername()
  let _isadmin = await isadmin()
  if (!_isadmin) {
    routes.value = [
      {
        name: "文章管理",
        children: [
          {
            path: "/home/article-list",
            name: "文章列表",
          },

          {
            path: "/home/post-article",
            name: "发表文章"
          }
        ]
      },

      {
        name: "栏目管理",
        path: "/home/category-manage"
      }
    ]
  } else {
    routes.value = [
      {
        path: "/home/article-list",
        name: "文章列表",
      },

      {
        path: "/home/user-manage",
        name: "用户管理"
      },

      {
        name: "栏目管理",
        path: "/home/category-manage"
      }
    ]
  }
})
</script>

<style lang="scss" scoped>
.home {
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;

  .el-header {
    background: #20a0ff;
    color: #333;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .title {
      color: #fff;
      font-size: 22px;
      display: inline;
    }

    .userinfo {
      color: #fff;
      cursor: pointer;
    }
  }

  

  .el-main {
    padding: 0;
    background: #fff;
    color: #000;
    text-align: center;
    display: flex;
    .el-aside {
      background: #ECECEC;
      margin-right: 8px;
    }
  }
}
</style>
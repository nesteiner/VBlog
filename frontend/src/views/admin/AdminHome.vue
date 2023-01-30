<template>
  <div class="admin-home row">
    <Sidebar>
      <SidebarItem @click="handleClickSidebar('student-manage')">学生管理</SidebarItem>
      <SidebarItem @click="handleClickSidebar('role-manage')">角色管理</SidebarItem>
    </Sidebar>

    <div class="content">
      <router-view/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {Sidebar, SidebarItem} from "@/components/sidebar";
import {useRouter} from "vue-router";

const router = useRouter()
function handleClickSidebar(name: string) {
  router.push({name})
}
</script>

<style lang="scss" scoped>
div.admin-home.row {
  display: flex;
  flex-direction: row;

  div.content {
    flex-grow: 1;
    flex-shrink: 1;
    flex-basis: auto;
  }
}
</style>
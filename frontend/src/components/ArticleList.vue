<template>
  <el-container class="article-list">
    <el-main class="main">
      <Tab active-name="已发表">
        <TabItem name="已发表">
          <BlogTable :state="1" show-edit show-delete/>
        </TabItem>

        <TabItem name="草稿箱">
          <BlogTable :state="0" show-edit show-delete/>
        </TabItem>

        <TabItem name="回收站">
          <BlogTable :state="2" show-delete show-restore remove/>
        </TabItem>

        <TabItem name="博客管理" v-if="isadminRef">
          <BlogTable :state="-2" show-delete/>
        </TabItem>
      </Tab>
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
import {ref, onMounted} from "vue"
import { isadmin } from "@/api";
import {ElContainer, ElMain, ElTabs, ElTabPane} from "element-plus"
import {Tab, TabItem} from "@/components/tab"
import BlogTable from "@/components/BlogTable.vue"

const isadminRef = ref(false)

onMounted(async () => {
  isadminRef.value = await isadmin()
})
</script>

<style lang="scss" scoped>
.article-list {
  .header {
    background: #ececec;
    margin-top: 10px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
  }

  .main {
    display: flex;
    flex-direction: column;
    padding-left: 0;
    background: #fff;
    padding-top: 0;
    margin-top: 8px;
  }
}
</style>
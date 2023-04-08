<template>
  <el-row v-loading="loading" v-if="article !== null" class="blog-detail">
    <el-col :span="24" class="back">
      <el-button type="default" :icon="Back" @click="goback">返回</el-button>
    </el-col>

    <el-col :span="24">
      <h3> {{ article.title }} </h3>  
      <Row main-axis-aligment="end" cross-axis-aligment="center">
        <div class="nickname">
          {{ article.author.nickname }}
        </div>

        <span class="view">浏览 {{article.pageView}}</span>
        <span class="edittime"> {{article.editTime}}</span>

        <template v-for="(item, index) in article.tags" :key="index">
          <el-tag class="tag" type="success" size="small">
            {{ item.name }}
          </el-tag>
        </template>
      </Row>
    </el-col>

    <el-col>
      <div class="content" v-html="article.htmlContent"/>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import { findArticle } from '@/api';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElRow, vLoading, ElCol, ElTag, ElButton } from 'element-plus';
import {Back} from "@element-plus/icons"
import {Row} from "scratch-components"
const route = useRoute()
const router = useRouter()

const article = ref<Article | null>(null)
const loading = ref(false)

const goback = () => {
  router.go(-1)
}

onMounted(async () => {
  let aid = parseInt(route.query["id"] as string)
  loading.value = true

  try {
    article.value = await findArticle(aid)
  } catch (error: any) {
    ElMessage.error("页面加载失败")
  } finally {
    loading.value = false
  }
})
</script>

<style lang="scss" scoped>
.blog-detail {
  .back {
    padding-bottom: 0;
  }

  .nickname {
    display: inline; 
    color: #20a0ff; 
    margin-left: 50px; 
    margin-right: 20px; 
    font-size: 12px;
  }

  span.view {
    color: #20a0ff;
    margin-right:20px;
    font-size: 12px;
  }

  span.edittime {
    color: #20a0ff;
    margin-right:20px;
    font-size: 12px;
  }

  .tag {
    margin-left: 8px;
  }

  .content {
    text-align: left;
  }
}
</style>
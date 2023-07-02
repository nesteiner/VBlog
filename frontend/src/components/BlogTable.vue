<template>
  <div class="blog-table">
    <Row>
      <el-input placeholder="通过标题搜索该分类下的博客" 
                prefix-icon="el-icon-search" 
                v-model="keywords" 
                style="width: 400px"
                size="small"/>
      <el-button type="primary"
                 :icon="Search"
                 size="small"
                 @click="searchClick">
        搜索          
      </el-button>
    </Row>

    <el-table :data="articles"
              tooltip-effect="dark"
              style="width: 100%;overflow-x: hidden; overflow-y: hidden;"
              :max-height="390"
              @selection-change="handleSelectionChange"
              v-loading="loading">
            
      <el-table-column type="selection" :width="35" align="left" v-if="showEdit || showDelete"/>
      <el-table-column label="标题" :width="400" align="left">
        <template #default="scope">
          <span style="color: #409eff; cursor: pointer" @click="itemClick(scope.row)"> 
            {{ scope.row.title }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="最近编辑时间" :width="140" align="left">
        <template #default="scope">
          {{ scope.row.editTime }}
        </template>
      </el-table-column>

      <el-table-column label="作者" :width="120" align="left">
        <template #default="scope">
          {{ scope.row.author.nickname }}
        </template>
      </el-table-column>

      <el-table-column label="所属分类" :width="120" align="left">
        <template #default="scope">
          {{ scope.row.category.name }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="left" v-if="showEdit || showDelete">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)" v-if="showEdit">编辑</el-button>
          <el-button size="small" @click="handleRestore(scope.$index, scope.row)" v-if="showRestore">还原</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)" v-if="showDelete">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="blog-table-footer">
      <el-button type="danger" 
                 size="small" 
                 style="margin: 0px" 
                 v-show="articles.length > 0 && showDelete"
                 :disabled="selectedItems.length == 0" 
                 @click="deleteMany">
        批量删除          
      </el-button>

      <el-pagination background 
                     :page-size="pageSize" 
                     layout="prev,pager,next" 
                     :total="totalCount" 
                     @current-change="currentChange"
                     v-show="articles.length > 0"/>
    </div>
  </div>
</template>


<script lang="ts" setup>
import { deleteArticle, findAllArticles, findAllArticlesByState, restoreArticle, updateArticleState } from '@/api';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import {ElMessage, ElInput, ElButton, ElTable, ElTableColumn, ElPagination, vLoading} from "element-plus"
import {Search} from "@element-plus/icons"
import {Row} from "scratch-components"
const router = useRouter()

const props = withDefaults(
  defineProps<{
    state: number,
    showEdit?: boolean,
    showDelete?: boolean,
    showRestore?: boolean,
    remove?: boolean
  }>(),

  {
    showEdit: false,
    showDelete: false,
    showRestore: false,
    remove: false
  }
)

const articles = ref([] as Article[])
const selectedItems = ref([] as Article[])
const loading = ref(false)
const currentPage = ref(1)
const totalCount = ref(-1)
const pageSize = ref(6)
const keywords = ref("")
const dustbinData = ref([] as number[])

const currentChange = async (page: number) => {
  currentPage.value = page
  loading.value = true
  await loadBlogs(currentPage.value - 1, pageSize.value)
}

const itemClick = (row: Article) => {
  router.push({path: "blog-detail", query: {id: row.id}})
}

const searchClick = async () => {
  await loadBlogs(0, pageSize.value)
}

const loadBlogs = async (page: number, size: number) => {
  if (props.state == -2) {
    try {
      let response = await findAllArticles(page, size)
      articles.value = response["content"]
      totalCount.value = response["totalElements"]
    } catch (error: any) {
      ElMessage.error("数据加载失败")
    } finally {
      loading.value = false
    }
  } else {
    try {
      let response = await findAllArticlesByState(keywords.value, props.state, page, size)
      articles.value = response["content"]
      totalCount.value = response["totalElements"]
    } catch (error: any) {
      ElMessage.error("数据加载失败")
    } finally {
      loading.value = false
    }
  }
}

const handleSelectionChange = (val: Article[]) => {
  selectedItems.value = val
}

const handleEdit = (index: number, row: Article) => {
  router.push({path: "edit-blog", query: {from: "edit", id: row.id}})
}

const handleDelete = async (index: number, row: Article) => {
  let flag = confirm("删除这个文章?")
  loading.value = true

  try {
    if (flag && !props.remove) {
      await updateArticleState(row.id!, 2)
      articles.value.splice(index, 1)
    } else if (flag && props.remove) {
      await deleteArticle(row.id!)
      articles.value.splice(index, 1)
    }
  } catch (error: any) {
    ElMessage.error("更新状态失败")
  } finally {
    loading.value = false
  }
  
}

const deleteMany = async () => {
  for (let item of selectedItems.value) {
    dustbinData.value.push(item.id!)
  }

  if (props.remove) {
    deleteToDustbin(2)
  } else {
    deleteToDustbin(3)
  }
}

const handleRestore = async (index: number, row: Article) => {
  let flag = confirm("将该文件还原到原处，是否继续?")
  if (flag) {
    loading.value = true
    try {
      await restoreArticle(row.id!)
      articles.value.splice(index, 1)
    } catch(error: any) {
      ElMessage.error("还原失败")
    } finally {
      loading.value = false
    }
  } else {
    ElMessage.info("已取消")
    loading.value = false
  }
}

const deleteToDustbin = async (state: number) => {
  let flag = confirm("将文件放入回收站，时候继续")
  loading.value = true
  if (flag) {
    try {
      for (let id of dustbinData.value) {
        updateArticleState(id, state)
        let index = articles.value.findIndex((article) => article.id == id)
        if (index != -1) {
          articles.value.splice(index, 1)
        }
      }

      dustbinData.value = []
    } catch (error: any) {
      ElMessage.error("删除失败")
    } finally {
      loading.value = false
    }
  } else {
    ElMessage.info("已取消")
    loading.value = false
  }
}

onMounted(async () => {
  loading.value = true
  await loadBlogs(0, pageSize.value)
})
</script>
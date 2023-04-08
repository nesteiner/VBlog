<template>
  <el-container>
    <el-header>
      <el-input placeholder="请输入栏目名称" v-model="categoryName"/>
      <el-button type="primary" size="default" @click="addCategory">新增栏目</el-button>
    </el-header>

    <el-main>
      <el-table :data="categories" tooltip-effect="dark" @selection-change="handleSelectionChange" v-loading="loading">
        <el-table-column type="selection" :width="55" align="left"/>
        <el-table-column label="编号" prop="id" :width="120" align="left"/>
        <el-table-column label="栏目名称" prop="name" :width="120" align="left"/>
        <el-table-column label="启用时间" prop="date" align="left"/>
        <el-table-column label="操作" align="left">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)" :disabled="scope.row.name == 'default'">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)" :disabled="scope.row.name == 'default'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-button type="danger" 
                :disabled="selectedItems.length == 0"
                @click="deleteAll"
                v-if="categories.length > 0">
        批量删除
      </el-button>
        
    </el-main>
  </el-container>  
</template>

<script lang="ts" setup>
import { deleteArticle, deleteCategory, findAllCategories, insertCategory, updateCategory } from '@/api';
import { ElMessage, vLoading, ElContainer, 
  ElHeader, ElInput, ElMain, ElTable, 
  ElTableColumn, ElButton } from 'element-plus';
import { ref, onMounted } from 'vue';

const categoryName = ref("")
const selectedItems = ref([] as Category[])
const categories = ref([] as Category[])
const loading = ref(false)

const addCategory = async () => {
  loading.value = true
  try {
    let category = await insertCategory({name: categoryName.value})
    categories.value.push(category)

    categoryName.value = ""
  } catch (error: any) {
    ElMessage.error("添加分类失败")
  } finally {
    loading.value = false
  }
}

const deleteAll = async () => {
  loading.value = true
  let flag = confirm(`确定删除 ${selectedItems.value.length} 条数据?`)
  if (flag) {
    try {
      for (let category of selectedItems.value) {
        await deleteArticle(category.id!)
      }
    } catch (error: any) {
      ElMessage.error("全部删除失败")
    } finally {
      loading.value = false
    }
  }

  loading.value =false
}

const handleSelectionChange = (val: Category[]) => {
  selectedItems.value = val
}

const handleEdit = async (index: number, row: Category) => {
  let value = prompt("输入新名称")
  if (value == null) {
    ElMessage.info("数据不能为空")
  } else {
    loading.value = true
    try {
      let newcategory = await updateCategory({
        id: row.id!,
        name: value,
        date: row.date
      })

      categories.value[index] = newcategory
    } catch (error: any) {
      ElMessage.error("更新分类失败")
    } finally {
      loading.value = false 
    }
  }
}

const handleDelete = async (index: number, row: Category) => {
  let flag = confirm(`确认删除 ${row.name} ?`)
  loading.value = true

  if (flag) {
    try {
      await deleteCategory(row.id!)
      categories.value.splice(index, 1)
    } catch (error: any) {
      ElMessage.error(`删除 ${row.name} 失败`)
    } finally {
      loading.value = false
    }
  }

  loading.value = false
}

onMounted(async () => {
  categories.value = await findAllCategories()
})
</script>

<style lang="scss" scoped>
.el-header {
  display: flex;
  align-items: center;
  background: #ececec;
  margin-top: 20px;
  padding-left: 5px;

  .el-input {
    width: 200px;
  }

  .el-button {
    margin-left: 10px;
  }
}

.el-main {
  display: flex;
  flex-direction: column;
  padding-left: 5px;
  background: #ececec;
  margin-top: 20px;
  padding-top: 10px;

  .el-button {
    margin-top: 10px;
    width: 100px;
  }
}
</style>
<template>
  <el-container v-loading="loading" class="post-article">
    <el-header class="header">
      <el-select class="select" v-model="article.category.id" placeholder="请选择文章栏目">
        <template v-for="(item, index) in categories" :key="index">
          <el-option :value="item.id" :label="item.name"/>
        </template>
      </el-select>

      <el-input v-model="article.title" placeholder="请输入标题..."/>
      
      <template v-for="(tag, index) in dynamicTags" :key="index">
        <el-tag closable :disable-transitions="false" @close="handleClose(tag.name)">
          {{ tag.name }}
        </el-tag>
      </template>


      <el-input class="input-new-tag" 
                v-if="taginputVisible" 
                v-model="tagvalue" 
                ref="saveTagInput" 
                size="small"
                @keyup.enter.native="handleInputConfirm"
                @blur="handleInputConfirm"/>

      <el-button v-else class="button-new-tag" type="primary" size="small" @click="showInput">+Tag</el-button>
    </el-header>

    <el-main class="main">
      <div id="editor">
        <MdEditor ref="editor" 
                  v-model="article.markdownContent"
                  @on-html-changed="onHtmlChanged"
                  @on-upload-img="onUploadImage"
                  />
      </div>
      
      <SizedBox :height="15"/>

      <Row main-axis-aligment="end" cross-axis-aligment="center">
        <el-button @click="cancelEdit">放弃修改</el-button>
        <template v-if="from == 'post'">
          <el-button @click="saveBlog(0)">保存到草稿箱</el-button>
          <el-button @click="saveBlog(1)" type="primary">发表文章</el-button>
        </template>

        <template v-else="from == 'edit'">
          <el-button type="primary" @click="saveBlog(1)">保存修改</el-button>
        </template>
      </Row>
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
import { onMounted, ref, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElContainer, ElHeader, ElSelect, ElOption, ElInput, ElTag, ElMain, ElButton, vLoading } from "element-plus";
import { findAllCategories, findArticle, insertArticle, uploadImage } from "@/api";
import { Row, SizedBox } from "scratch-components";
import MdEditor from "md-editor-v3"
import type {ExposeParam} from "md-editor-v3"
import "md-editor-v3/lib/style.css"
import {insertTag, updateArticle} from "@/api"

const router = useRouter()
const route = useRoute()

const categories = ref([] as Category[])
const taginputVisible = ref(false)
const tagvalue = ref("")
const loading = ref(false)
const from = ref("")
const dynamicTags = ref([] as RegisterTagRequest[])

const article = ref<RegisterArticleRequest>({
  id: undefined,
  title: "",
	markdownContent: "",
	htmlContent: "",
	summary: "",
	category: {
    id: -1,
    name: "",
    date: "2023-03-31"
  },
  
	state: 1,
	tags: [] as Tag[]
})

const saveTagInput = ref<InstanceType<typeof ElInput>>()
const editor = ref<ExposeParam>()

const cancelEdit = () => {
  router.go(-1)
}

const onHtmlChanged = (html: string) => {
  article.value.htmlContent = html
}

const onUploadImage = async (files: Array<File>, callback: (urls: Array<string>) => void) => {
  const res = await Promise.all(
    files.map((file) => {
      return uploadImage(file)
    })
  )

  callback(res.map((item) => `http://localhost/api/image/download/${item.id}`))
}

const saveBlog = async (state: number) => {
  if (!isNotNullOrBlank(article.value.title, 
                        article.value.markdownContent, 
                        article.value.category.id)) {
    ElMessage.error("数据不能为空")
    return
  }

  if (article.value.category.id == -1) {
    ElMessage.error("需要选择分类")
    return
  }

  loading.value = true

  try {
    let tags = [] as Tag[]
    for (let item of dynamicTags.value) {
      let tag = await insertTag(item)
      tags.push(tag)
    }

    if (from.value == "edit") {
      let id = parseInt(route.query["id"] as string)
      
      let request: UpdateArticleRequest = {
        id,
        title: article.value.title,
        markdownContent: article.value.markdownContent,
        htmlContent: article.value.htmlContent,
        category: categories.value.find((item) => item.id == article.value.category.id)!,
        tags: tags,
        summary: generateSummary(article.value.htmlContent)
      }


      await updateArticle(
        request
      )
    } else {
      await insertArticle({
        ...article.value,
        state,
        tags,
        category: categories.value.find((item) => item.id == article.value.category.id)!
      })  
    }
    

    router.replace({path: "article-list"})
  } catch (error: any) {
    ElMessage.error("博客发布失败")
  } finally {
    loading.value = false
  }
}

const handleClose = (tag: string) => {
  if (from.value == "post") {
    let index = dynamicTags.value.indexOf({name: tag})
    if (index != -1 || index != null) {
      dynamicTags.value.splice(index, 1)
    }
  } else if (from.value == "edit") {
    let index = article.value.tags.findIndex((item) => item.name == tag)
    if (index != -1) {
      article.value.tags.splice(index, 1)
    }
  }
}

const showInput = () => {
  taginputVisible.value = true
  nextTick(() => {
    // PROBLEM 
    saveTagInput.value?.input?.focus()
  })
}

/* const imageAdd = async (pos: any, file: File) => {
  try {
    let response = await uploadImage(file)
    // PROBLEM
    markdownIt.$imglst2Url(pos, `http://localhost/api/image/download/response["id"]`)
  } catch (error: any) {
    ElMessage.error("上传失败")
  }
}
 */

const isNotNullOrBlank = (...args: any[]) => {
  for (let index = 0; index < args.length; index += 1) {
    let argument = args[index]
    if (argument == null || argument == "" || argument == undefined) {
      return false
    }
  }

  return true
}

const handleInputConfirm = () => {
  if (tagvalue.value != "") {
    let index = dynamicTags.value.findIndex((item) => item.name == tagvalue.value)
    if (index == -1) {
      dynamicTags.value.push({name: tagvalue.value})
    }
  }

  taginputVisible.value = false
  tagvalue.value = ""
}

const stripHtml = (html: string) => {
  let content = html.replace(/<p .*?>/, "")
  content = content.replace(/<br \s*\/?>/, "")
  content = content.replace(/\<.*?>/, "")
  return content
}

const generateSummary = (html: string) => {
  let content = stripHtml(html) 
  let length = content.length
  if (length > 50) {
    return content.substring(0, 50)
  } else {
    return content
  }
}

onMounted(async () => {
  loading.value = true
  try {
    categories.value = await findAllCategories()
    from.value = route.query["from"] as string ?? "post"
   
    if (from.value == "edit") {
      let id = parseInt(route.query["id"] as string)
      try { 
        let existArticle = await findArticle(id)
        article.value.id = id
        article.value.title = existArticle.title
        article.value.tags = existArticle.tags
        article.value.summary = existArticle.summary
        article.value.state = existArticle.state
        article.value.markdownContent = existArticle.markdownContent
        article.value.htmlContent = existArticle.htmlContent
        article.value.category = existArticle.category

        dynamicTags.value = article.value.tags
      } catch (error: any) {
        ElMessage.error("页面加载失败")
      }
    }
  } catch (error: any) {
    ElMessage.error("分类加载失败")
  } finally {
    loading.value = false
  }
})
</script>

<style lang="scss" scoped>
.post-article {
  .header {
    background: #ececec;
    margin-top: 10px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .select {
      width: 150px;
    }

    .el-input {
      width: 400px;
      margin-left: 10px;
    }

    .el-tag {
      margin-left: 10px;
    }

    .button-new-tag {
      margin-left: 10px;
      height: 32px;
      line-height: 30px;
      padding-top: 0;
      padding-bottom: 0;
    }

    .input-new-tag {
      width: 90px;
      margin-left: 10px;
      vertical-align: bottom;
    }
  }
  .main {
    display: flex;
    flex-direction: column;
    padding-left: 5px;
    background: #ececec;
    padding-top: 0;

    #editor {
      width: 100%;
      height: 450px;
      text-align: left;
    }
  }
}
</style>
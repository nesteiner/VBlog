import {instance} from "."

async function insertArticle(request: RegisterArticleRequest) {
  let response = await instance.post("/article", request)
  return response.data["data"]
}

async function deleteArticle(id: number) {
  let response = await instance.delete(`/article/${id}`)
  return response.data["data"]
}

async function updateArticle(article: UpdateArticleRequest) {
  let response = await instance.put("/article", article)
  return response.data["data"]
}

async function findAllArticlesByState(keywords: string | undefined, state: number = 1, page: number = 0, size: number = 6) {
  let response: any | null = null
  if (keywords !== undefined) {
    response = await instance.get(`/article/all?keywords=${keywords}&state=${state}&page=${page}&size=${size}`)
  } else {
    response = await instance.get(`/article/all?state=${state}&page=${page}&size=${size}`)
  }

  return response.data["data"]
}

async function findAllArticles(page: number, size: number) {
  let response = await instance.get(`/admin/article/all?page=${page}&size=${size}`)
  return response.data["data"]
}

async function findArticle(id: number) {
  let response = await instance.get(`/article/${id}`)
  return response.data["data"]
}

async function updateArticleState(id: number, state: number) {
  let response = await instance.put(`/article/dustbin?id=${id}&state=${state}`)
  return response.data["data"]
}

async function restoreArticle(id: number) {
  let response = await instance.put(`/article/restore/${id}`)
  return response.data["data"]
}

async function uploadImage(image: File) {
  let formdata = new FormData()
  formdata.append("file", image)
  let response = await instance.post("/image/upload", formdata)
  return response.data["data"]
}

export {
  insertArticle,
  deleteArticle,
  updateArticle,
  findAllArticles,
  findAllArticlesByState,
  findArticle,
  updateArticleState,
  restoreArticle,
  uploadImage
}
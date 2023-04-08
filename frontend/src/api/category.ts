import {instance} from "."

async function findAllCategories() {
  let response = await instance.get("/category/all")
  return response.data["data"]
}

async function deleteCategory(id: number) {
  let response = await instance.delete(`/category?id=${id}`)
  return response.data["data"]
}

async function insertCategory(request: RegisterCategoryRequest) {
  let response = await instance.post("/category", request)
  return response.data["data"]
}

async function updateCategory(request: Category) {
  let response = await instance.put("/category", request)
  return response.data["data"]
}

export {
  findAllCategories,
  deleteCategory,
  insertCategory,
  updateCategory
}
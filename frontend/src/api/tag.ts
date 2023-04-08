import {instance} from "."

const insertTag = async (request: RegisterTagRequest) => {
  let response = await instance.post("/tag", request)
  return response.data["data"]
}

const deleteTag = async (id: number) => {
  let response = await instance.delete(`/tag/${id}`)
  return response.data["data"]
}

const updateTag = async (request: Tag) => {
  let response = await instance.put("/tag", request)
  return response.data["data"]
}

const findTag = async (id: number) => {
  let response = await instance.get(`/article/${id}`)
  return response.data["data"]
}

export {
  insertTag,
  deleteTag,
  updateTag,
  findTag,
}
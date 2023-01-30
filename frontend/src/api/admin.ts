import {instance} from "@/api/index";
import {Md5} from "ts-md5";

async function findAllUser(type: string, page = 0, size = 10) {
    let response = await instance.get(`/admin/user?type=${type}&page=${page}&size=${size}`)
    return {
        content: response.data["data"]["content"],
        totalPages: response.data["data"]["totalPages"]
    }
}

async function findRoles() {
    let response = await instance.get("/admin/role")
    return response.data["data"]
}

async function insertRole(request: RegisterRoleRequest) {
    let response = await instance.post("/admin/role", request)
    return response.data["data"]
}

async function deleteRole(id: number) {
    let response = await instance.delete(`/admin/role/${id}`)
    return response.data["data"]
}

async function updateRole(requset: UpdateRoleRequest) {
    let response = await instance.put("/admin/role", requset)
    return response.data["data"]
}

async function deleteUserByAdmin(id: number) {
    let response = await instance.delete(`/admin/user/${id}`)
    return response.data["data"]
}

async function updateStudent(request: UpdateStudentRequest) {
    request.passwordHash = Md5.hashStr(request.passwordHash)
    let response = await instance.put("/student", request)
    return response.data["data"]
}

async function registerStudent(request: RegisterStudentRequest) {
    let passwordHash = Md5.hashStr(request.passwordHash)
    let response = await instance.post("/student/register", {
        ...request,
        passwordHash
    })

    return response.data["data"]
}
export {
    findAllUser,
    findRoles,
    insertRole,
    deleteRole,
    updateRole,
    deleteUserByAdmin,
    updateStudent,
    registerStudent
}
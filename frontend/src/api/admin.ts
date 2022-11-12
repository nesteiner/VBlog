import {instance} from "@/api/index";

async function findAllUser() {
    let response = await instance.get("/admin/user")
    return response.data["data"]
}


async function findRoles() {
    let response = await instance.get("/admin/role")

    return response.data["data"]
}

async function changeUserRoles(request: UpdateUserRoleRequest) {
    let response = await instance.put("/admin/user/role", request)

    return response.data["data"]
}

async function changeUserNameByAdmin(request: UpdateUserNameRequest) {
    let response = await instance.put("/admin/user/name", request)

    return response.data["data"]
}

async function deleteUserByAdmin(id: number) {
    let response = await instance.delete(`/admin/user/${id}`)

    return response.data["data"]
}
export {
    findAllUser,
    findRoles,
    changeUserRoles,
    changeUserNameByAdmin,
    deleteUserByAdmin
}
import {instance} from "@/api/index";

async function findAllUsersByNickname(nickname: string) {
	let response = await instance.get(`/admin/user?nickname=${nickname}`)
	return response.data["data"]
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

async function updateRoles(request: UpdateRolesRequest) {
	let response = await instance.put("/admin/user/role", request)
	return response.data["data"]
}

async function deleteUserByAdmin(id: number) {
	let response = await instance.delete(`/admin/user/${id}`)
	return response.data["data"]
}

async function findUser(id: number) {
	let response = await instance.get(`/admin/user/${id}`)
	return response.data["data"]
}

async function updateUserEnabled(request: UpdateEnabledRequest) {
	let response = await instance.put("/admin/user/enabled", request)
	return response.data["data"]
}

async function updateUserRoles(request: UpdateRolesRequest) {
	let response = await instance.put("/admin/user/role", request)
	return response.data["data"]
}

export {
	findAllUsersByNickname,
	findRoles,
	insertRole,
	deleteRole,
	updateRoles,
	deleteUserByAdmin,
	findUser,
	updateUserEnabled,
	updateUserRoles
}
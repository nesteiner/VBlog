import {Md5} from "ts-md5"
import {instance, LOCAL_TOKEN_KEY} from "@/api/index";

async function login(request: LoginRequest) {
    logout()
    let passwordHash = Md5.hashStr(request.passwordHash)
    let jwttoken = await instance.post("/authenticate", {
        username: request.username,
        passwordHash
    }).then(response => response.data["jwttoken"])
        .catch(error => {
            throw error.response.data["message"]
        })

    localStorage.setItem(LOCAL_TOKEN_KEY, `Bearer ${jwttoken}`)
}

function logout() {
    localStorage.removeItem(LOCAL_TOKEN_KEY)
}

async function register(request: RegisterUserRequest) {
    let passwordHash = Md5.hashStr(request.passwordHash)
    let response = await instance.post("/admin/register", {
        ...request,
        passwordHash
    })

    return response.data["data"]
}

async function isadmin() {
    let response = await instance.get("/user/isadmin")
    return response.data["data"]
}

async function currentUsername() {
    let response = await instance.get("/user/name")
    return response.data["data"]
}

async function changeAvatar(userid: number, userface: string) {
    let response = await instance.put("/user/avatar", {
        userid,
        userface
    })

    return response.data["data"]
}

export {
    login,
    logout,
    register,
    isadmin,
    currentUsername,
    changeAvatar
}
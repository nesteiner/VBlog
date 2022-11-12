import {Md5} from "ts-md5"
import {instance} from "@/api/index";

const LOCAL_TOKEN_KEY = "token"
async function login(request: LoginRequest) {
    let passwordHash = Md5.hashStr(request.passwordHash)
    let jwttoken = await instance.post("/authenticate", {
        username: request.username,
        passwordHash
    }).then(response => response.data["jwttoken"])
        .catch(error => {
            throw error.response.data["message"]
        })

    instance.defaults.headers.common["Authorization"] = `Bearer ${jwttoken}`
}

function logout() {
    localStorage.removeItem(LOCAL_TOKEN_KEY)
}

async function register(request: RegisterRequest) {
    let password = request.passwordHash
    request.passwordHash = Md5.hashStr(password)
    let response = await instance.post("/user/register", request)
    return response.data
}

async function findUser() {
    let response = await instance.get("/user")
    return response.data["data"]
}

async function deleteUser() {
    let response = await instance.delete("/user")
    return response.data["data"]
}
function isadmin(user: User) {
    return user.roles.findIndex(x => x.name == "admin") != -1
}

export {
    login,
    logout,
    register,
    findUser,
    isadmin,
    deleteUser
}
import Axios from "axios"
import {Md5} from "ts-md5"

const BASE_URL = "http://localhost/api"
const instance = Axios.create({
    baseURL: BASE_URL
})
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

    let authorization = `Bearer ${jwttoken}`
    localStorage.setItem(LOCAL_TOKEN_KEY, authorization)
}

function logout() {
    localStorage.removeItem(LOCAL_TOKEN_KEY)
}

async function register(request: RegisterRequest) {
    let password = request.passwordHash
    request.passwordHash = Md5.hashStr(password)
    let response = await instance.post("/register", request)
    return response.data
}

async function findUser() {
    let token = localStorage.getItem(LOCAL_TOKEN_KEY) || "no token"
    let response = await instance.get("/user", {
        headers: {
            "Authorization": token
        }
    })

    return response.data["data"]
}

export {
    login,
    logout,
    register,
    findUser
}
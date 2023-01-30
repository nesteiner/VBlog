import {Md5} from "ts-md5"
import {instance, LOCAL_TOKEN_KEY} from "@/api/index";

async function login(request: LoginRequest, type: string) {
    logout()
    let passwordHash = Md5.hashStr(request.passwordHash)
    let jwttoken = await instance.post(`/authenticate?type=${type}`, {
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

export {
    login,
    logout
}
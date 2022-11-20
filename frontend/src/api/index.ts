import Axios from "axios"
import router from "@/router"
const BASE_URL = "http://localhost/api"
const LOCAL_TOKEN_KEY = "TOKEN"
const instance = Axios.create({
    baseURL: BASE_URL
})

instance.interceptors.request.use(
    config => {
        if (config.headers !== undefined) {
            config.headers["Authorization"] = localStorage.getItem(LOCAL_TOKEN_KEY) || "no token"
        }
        return config
    },

    error => {
        return Promise.reject(error.response)
    }
)
instance.interceptors.response.use(
    response => response,
    error => {
        let status = error.response.status
        if(status == 401) {
            router.replace({name: "login"})
        }

        return Promise.reject(error)
    }
)
export {instance, LOCAL_TOKEN_KEY}

export * from "./user"
export * from "./admin"
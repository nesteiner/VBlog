import Axios from "axios"
import router from "@/router"
const BASE_URL = "http://localhost/api"
const instance = Axios.create({
    baseURL: BASE_URL
})

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
export {instance}
export * from "./user"
export * from "./admin"
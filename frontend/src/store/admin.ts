import {defineStore} from "pinia";
import {findAllUser, findRoles} from "@/api";

export const useAdminStore = defineStore("admin-store", {
    state: () => {
        return {
            users: [] as User[],
            roles: [] as Role[]
        }
    },

    getters: {

    },

    actions: {
        async loadUsers() {
            this.users = await findAllUser();
        },

        async loadRoles() {
            this.roles = await findRoles();
        }
    }
})
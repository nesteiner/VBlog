<template>
  <div class="admin-home">
    <nav>
      <div class="container">
        <router-link to="/admin">Home</router-link>
      </div>
      <button @click="handleLogout">Logout</button>
    </nav>

    <template v-for="(user, index) in adminStore.users" :key="index">
      <UserPanel :user="user"
                 :roles="adminStore.roles"
                 @change-roles="handleChangeRoles($event, user.id, index)"
                 @change-name="handleChangeName($event, user.id, index)"
                 @ondelete="handleDelete(user.id, index)"/>
    </template>
  </div>
</template>

<script lang="ts" setup>
import {changeUserNameByAdmin, changeUserRoles, deleteUserByAdmin, logout} from "@/api";
import UserPanel from "@/components/UserPanel.vue";
import {useRouter} from "vue-router";
import {useAdminStore} from "@/store/admin";
import {onMounted} from "vue";

const router = useRouter();
const adminStore = useAdminStore()

function handleLogout() {
  logout();
  router.replace({name: "login"})
}

async function handleChangeRoles(roles: Role[], id: number, index: number) {
  let request: UpdateUserRoleRequest = {
    id,
    roles,
  }

  adminStore.users[index] = await changeUserRoles(request)
}

async function handleChangeName(name: string, id: number, index: number) {
  let request: UpdateUserNameRequest = {
    id,
    name
  }
  try {
    adminStore.users[index] = await changeUserNameByAdmin(request)
  } catch(error: any) {
    if (error.response.status == 500) {
      alert(error.response.data["data"])
    }
  }
}

async function handleDelete(id: number, index: number) {
  await deleteUserByAdmin(id)
  adminStore.users.splice(index, 1)
}

onMounted(async () => {
  await adminStore.loadUsers()
  await adminStore.loadRoles()

})
</script>

<style scoped>

</style>
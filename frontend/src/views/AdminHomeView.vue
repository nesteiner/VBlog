<template>
  <div class="admin-home">
    <template v-for="(user, index) in users" :key="index">
      <UserPanel :user="user"
                 @change-roles="handleChangeRoles($event, user.id, index)"
                 @change-name="handleChangeName($event, user.id, index)"
                 @ondelete="handleDelete(user.id, index)"/>
    </template>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, provide, ref} from "vue";
import {changeUserNameByAdmin, changeUserRoles, deleteUserByAdmin, findAllUser, findRoles} from "@/api";
import UserPanel from "@/components/UserPanel.vue";

const users = ref<User[]>([])
const roles = ref<Role[]>([])
provide("roles", roles)

async function handleChangeRoles(roles: Role[], id: number, index: number) {
  let request: UpdateUserRoleRequest = {
    id,
    roles,
  }

  users.value[index] = await changeUserRoles(request)
}

async function handleChangeName(name: string, id: number, index: number) {
  let request: UpdateUserNameRequest = {
    id,
    name
  }
  try {
    users.value[index] = await changeUserNameByAdmin(request)
  } catch(error: any) {
    if (error.response.status == 500) {
      alert(error.response.data["data"])
    }
  }
}

async function handleDelete(id: number, index: number) {
  await deleteUserByAdmin(id)
  users.value.splice(index, 1)

}
onMounted(async () => {
  users.value = await findAllUser()
  roles.value = await findRoles()
})
</script>

<style scoped>

</style>
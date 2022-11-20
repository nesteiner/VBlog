<template>
  <div class="home">
    <nav>
      <div class="container">
        <router-link to="/">Home</router-link>
      </div>
      <button @click="handleLogout">Logout</button>
    </nav>

    <h1>{{user.name}}</h1>
  </div>
</template>

<script lang="ts" setup>
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {findUser, logout} from "@/api";

const router = useRouter()
const user = ref<User>({
  id: 0,
  name: "loading",
  roles: []
})

function handleLogout() {
  logout()
  router.replace({name: "login"})
}

onMounted(async () => {
  try {
    user.value = await findUser()
  } catch (error: any) {
    if(error.response.status == 401) {
      router.replace({name: "login"})
    }
  }
})
</script>

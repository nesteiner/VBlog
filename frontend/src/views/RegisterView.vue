<template>
  <div class="register-view">
    <input type="text" v-model="username" placeholder="input username"/>
    <br/>
    <input type="password" v-model="password" placeholder="input password"/>
    <br/>
    <button @click="handleRegister">Register</button>
  </div>
</template>

<script lang="ts" setup>
import {ref} from "vue";
import {useRouter} from "vue-router";
import {logout, register} from "@/api";

const router = useRouter()
const username = ref("")
const password = ref("")

async function handleRegister() {
  try {
    let request: RegisterRequest = {
      username: username.value,
      passwordHash: password.value,
    }

    await register(request)
    router.replace({name: "login"})
  } catch (error: any) {
    let errorMessage = error.response.data["message"]
    console.error(errorMessage)
    alert(errorMessage)
  } finally {
    password.value = ""
  }
}
</script>

<style scoped>

</style>
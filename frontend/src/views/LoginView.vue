<template>
  <div class="login-view">
    <input type="text" v-model="username" placeholder="input your username"/>
    <br/>
    <input type="password" v-model="password" placeholder="input your password"/>
    <br/>
    <router-link to="register">No User? Create One</router-link>
    <br/>
    <button @click="handleLogin">Login</button>
  </div>
</template>

<script lang="ts" setup>
import {ref} from "vue";
import {login} from "@/api";
import {useRouter} from "vue-router";

const router = useRouter()
const username = ref("")
const password = ref("")

async function handleLogin() {
  try {
    await login(username.value, password.value)
    router.replace({name: "home"})
  } catch(error: any) {
    alert(`fuck: ${error}`)
  } finally {
    password.value = ""
  }
}
</script>

<style scoped>

</style>
<template>
  <div class="login-view">
    <div class="form-item">
      <span class="description">
        用户名
      </span>
      <input type="text" v-model="username" placeholder="input your username"/>
    </div>

    <div class="form-item">
      <span class="description">
        密码
      </span>
      <input type="password" v-model="password" placeholder="input your password"/>
    </div>

    <div class="form-item">
      <div class="row">
        <input type="radio" value="student" v-model="usertype">student <br/>
        <input type="radio" value="admin" v-model="usertype">admin <br/>
      </div>
    </div>

    <div class="form-item">
      <button @click="handleLogin">Login</button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref} from "vue";
import {login} from "@/api";
import {useRouter} from "vue-router";

const router = useRouter()
const username = ref("")
const password = ref("")
const usertype = ref("student")

async function handleLogin() {
  try {
    let request = {
      username: username.value,
      passwordHash: password.value
    }
    await login(request, usertype.value)
    if (usertype.value == "student") {
      router.replace({name: "student-home"})
    } else if(usertype.value == "admin") {
      router.replace({name: "admin-home"})
    }
  } catch(error: any) {
    alert(`fuck: ${error}`)
  } finally {
    password.value = ""
  }
}
</script>

<style lang="scss" scoped>
div.login-view {
  margin: 0 auto;
  width: fit-content;
  div.form-item {
    margin: 3px;
    text-align: right;
    a {
      display: block;
    }

    div.row {
      width: 100%;
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>
<template>
  <div class="home">
    <h1>{{user.name}}</h1>
  </div>
</template>

<script lang="ts" setup>
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import { findUser } from "@/api";

const router = useRouter()
const user = ref<User>({
  id: 0,
  name: "loading",
})


onMounted(async () => {
  try {
    user.value = await findUser()
  } catch (error: any) {
    if(error.response.status == 401 || error.response.status == 400) {
      router.replace({name: "login"})
    }
  }
})
</script>

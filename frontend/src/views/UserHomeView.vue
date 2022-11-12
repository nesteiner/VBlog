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
  roles: []
})


onMounted(async () => {
  user.value = await findUser()
})
</script>

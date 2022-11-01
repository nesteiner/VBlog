<template>
  <nav>
    <div class="container">
      <router-link to="/">Home</router-link>
    </div>
    <button v-show="showable" @click="handleLogout">Logout</button>
  </nav>
  <router-view/>
</template>

<script lang="ts" setup>
import {useRoute, useRouter} from "vue-router";
import {logout} from "@/api";
import {computed} from "vue";

const router = useRouter()
const route = useRoute()
const showable = computed(() => {
  return route.name != "login" && route.name != "register"
})

function handleLogout() {
  if(showable.value) {
    logout()
    router.replace({name: "login"})
  }
}
</script>
<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>

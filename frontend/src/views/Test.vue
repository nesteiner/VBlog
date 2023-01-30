<template>
  <div class="test-view">
    <!--    1. sidebar-->
    <!--    2. tabbar-->
    <Sidebar>
      <SidebarItem>
        Hello
      </SidebarItem>
      <SidebarItem>
        World
      </SidebarItem>
    </Sidebar>

  </div>
</template>

<script lang="ts" setup>
import {Sidebar, SidebarItem} from "@/components/sidebar"
</script>

<style lang="scss" scoped>

</style>
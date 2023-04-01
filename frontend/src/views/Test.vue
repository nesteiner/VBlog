<template>
  <BlogTable :state="1" active-name="hello" show-delete show-edit show-restore/>
</template>

<script lang="ts" setup>
import BlogTable from "@/components/BlogTable.vue"
</script>

<style lang="scss" scoped>

</style>
<template>
  <div class="test">
    <ImageUpload @input="onInput" default-url="http://localhost/api/image/download/1"/>
  </div>
</template>

<script lang="ts" setup>
import ImageUpload from '@/components/ImageUpload.vue';
import { ref } from 'vue';

const onInput = (file: File) => {
  alert("hello world")
  console.log(file)
}
</script>

<style lang="scss" scoped>

</style>
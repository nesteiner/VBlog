<template>
  <div class="image-upload" @click="chooseImage" :style="{'background-image': `url(${imagedata})`}">
    <span v-if="!imagedata && defaultUrl" class="placeholder">Choose an Image</span>
    <input class="file-input" ref="fileInput" type="file" @input="selectFile">
  </div>
</template>

<script lang="ts" setup>
import {ref} from "vue"

const props = withDefaults(defineProps<{
  defaultUrl?: string | null
}>(), {
  defaultUrl: null
})

const imagedata = ref<string | ArrayBuffer | null>(props.defaultUrl)
const fileInput = ref<HTMLInputElement>()
const emits = defineEmits(["input"])

const chooseImage = () => {
  fileInput.value?.click()
}

const selectFile = () => {
  const input = fileInput.value as HTMLInputElement
  const files = input.files
  if (files && files[0]) {
    const reader = new FileReader()
    reader.onload = e => {
      imagedata.value = e.target?.result ?? null
    }

    reader.readAsDataURL(files[0])
    emits("input", files[0])
  }
}
</script>

<style lang="scss" scoped>
.image-upload {
  width: 70px;
  height: 70px;
  cursor: pointer;
  background-size: cover;
  background-position: center center;

  .placeholder {
    background: #F0F0F0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #333;
    font-size: 18px;

    &:hover {
      background: #E0E0E0;
    }
  }

  .file-input {
    display: none;
  }
}
</style>
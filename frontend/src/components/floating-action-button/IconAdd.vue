<template>
  <svg t="1688031161312" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2263"><path d="M939.939489 459.072557 562.339502 459.072557 562.339502 83.519182 462.055494 83.519182 462.055494 459.072557 84.455507 459.072557 84.455507 559.356564 462.055494 559.356564 462.055494 939.003164 562.339502 939.003164 562.339502 559.356564 939.939489 559.356564Z" fill="#272636" p-id="2264"></path></svg>
</template>

<script lang="ts" setup>
import { computed } from 'vue';

interface PropsInterface {
  width: number,
  height: number,
  fill ?: string
}

const props = withDefaults(defineProps<PropsInterface>(), {
  fill: "white"
})

const width = computed(() => `${props.width}px`)
const height = computed(() => `${props.height}px`)
</script>

<style lang="scss" scoped>
svg {
  width: v-bind(width);
  height: v-bind(height);
  fill: v-bind(fill);

  path {
    fill: v-bind(fill);
  }
}
</style>
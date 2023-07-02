<template>
  <SizedBox :width="50" :height="50" class="floating-action-button" @click="onPressed">
    <Center>
      <IconAdd :width="30" :height="30"/>    
    </Center>
  </SizedBox>
</template>

<script lang="ts" setup>
import IconAdd from "./IconAdd.vue";
import {Center, SizedBox} from "scratch-components"

defineProps<{
  onPressed: () => void
}>()
</script>

<style lang="scss" scoped>
.floating-action-button {
  border-radius: 50%;

  position: fixed;
  bottom: 50px;
  right: 50px;

  cursor: pointer;
  background: blue;
}
</style>
<template>
  <div class="dialog" v-show="show">
    <div class="container">
      <div class="header">
        <button @click="handleClose">close</button>
      </div>

      <div class="body">
        <slot/>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
const props = defineProps<{
  show: boolean
}>()

const emits = defineEmits(["update:show"])
function handleClose() {
  emits("update:show", false)
}
</script>

<style lang="scss" scoped>
div.dialog {
  z-index: 100;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  div.container {
    width: 50%;
    height: 70%;
    background: white;
    display: flex;
    flex-direction: column;

    div.header {
      display: flex;
      justify-content: end;
    }
  }
}
</style>
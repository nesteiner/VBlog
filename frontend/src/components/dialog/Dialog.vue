<template>
  <div class="dialog" v-if="show">
    <div class="content column">
      <div class="header row">
        <svg @click="handleClose" t="1674967844795" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2708">
          <path d="M576 512l277.333333 277.333333-64 64-277.333333-277.333333L234.666667 853.333333 170.666667 789.333333l277.333333-277.333333L170.666667 234.666667 234.666667 170.666667l277.333333 277.333333L789.333333 170.666667 853.333333 234.666667 576 512z" fill="#444444" p-id="2709"/>
        </svg>
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
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;

  z-index: 100;

  display: flex;
  justify-content: center;
  align-items: center;

  background: rgba(0, 0, 0, 0.2);

  div.content.column {
    max-width: 30%;
    max-height: 70%;

    width: fit-content;
    height: fit-content;

    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;

    background: white;
    padding: 10px;

    div.header.row {
      width: 100%;
      display: flex;
      justify-content: flex-end;
      margin-bottom: 10px;

      svg {
        width: 20px;
        height: 20px;
        cursor: pointer;
      }
    }
  }
}
</style>
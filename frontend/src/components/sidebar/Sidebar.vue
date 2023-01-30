<template>
  <div class="sidebar">
    <slot/>
  </div>
</template>

<script lang="ts" setup>

</script>

<style lang="scss" scoped>
div.sidebar {
  width: 15%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  border-right: 1px solid rgba(0, 0, 0, 0.3);
}
</style>
<template>
  <div class="sidebar-item">
    <slot/>
  </div>
</template>

<style lang="scss" scoped>
div.sidebar-item {
  width: 100%;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  &:hover {
    background: rgba(0, 0, 0, 0.1);
    cursor: pointer;
  }

  word-break: break-word;
}
</style>
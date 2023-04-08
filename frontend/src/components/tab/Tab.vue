<template>
  <div class="tab">
    <Row class="names" cross-axis-aligment="center">
      <template v-for="(name, index) in names" :key="index">
        <div @click="clickItem(name)" :class="{active: activeName == name}">
          {{ name }}
        </div>
      </template>
    </Row>

    <div class="content">
      <slot/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref, provide} from "vue"
import { Row } from "scratch-components";
const props = defineProps<{
  activeName: string
}>()

const activeName = ref(props.activeName)
const names = ref([] as string[])

provide("active-name", activeName)
provide("names", names)

const clickItem = (name: string) => {
  activeName.value = name
}
</script>

<style lang="scss" scoped>
div.tab {
  box-sizing: border-box;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 1.5em;
  margin: 0.5px;

  div.names {
    height: 40px;
    line-height: 40px;
    font-weight: 500;
    border-bottom: 1px solid #dcdfe6;

    div {
      min-width: 100px;
      cursor: pointer;
      border: 1px solid #dcdfe6;

      &.active {
        color: rgb(64, 158, 255);
        border-bottom: 1px solid white;
      }
    }
  }

  div.content {
    padding: 25px 25px 25px 10px;
  }
}
</style>
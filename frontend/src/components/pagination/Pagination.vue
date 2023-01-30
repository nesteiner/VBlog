<template>
  <div class="page-container">
    <ul class="page-inner">
      <li class="page" @click="prevOrNext(-1)">
        <span aria-hidden="true">
          <svg t="1655539975347" class="icon left" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2097"><path d="M647.338667 816.128c-8.704 0-17.408-3.413333-24.064-10.069333L313.344 496.128a33.8432 33.8432 0 0 1 0-48.128L623.104 138.069333c13.312-13.312 34.986667-13.312 48.298667 0s13.312 34.986667 0 48.298667L385.706667 472.064 671.402667 757.76a34.2016 34.2016 0 0 1-24.064 58.368z" p-id="2098"></path></svg>
        </span>
      </li>

      <template v-for="(item, index) in pages" :key="index">
        <li class="page" :class="{actived: item === currentPage}" @click="select(item)">
          <span> {{item}} </span>
        </li>
      </template>

      <li class="page" @click="prevOrNext(1)">
        <span aria-hidden="true">
          <svg t="1655539975347" class="icon right" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2097"><path d="M647.338667 816.128c-8.704 0-17.408-3.413333-24.064-10.069333L313.344 496.128a33.8432 33.8432 0 0 1 0-48.128L623.104 138.069333c13.312-13.312 34.986667-13.312 48.298667 0s13.312 34.986667 0 48.298667L385.706667 472.064 671.402667 757.76a34.2016 34.2016 0 0 1-24.064 58.368z" p-id="2098"></path></svg>
        </span>

      </li>

    </ul>
  </div>
</template>

<script lang="ts" setup>
import {computed, ref} from 'vue'
const props = defineProps({
  totalPages: {type: Number, default: 0},
  pageSize: {type: Number, default: 5},
  currentPage: {type: Number, default: 1}
})

const emits = defineEmits(['update:currentPage'])
const pages = computed(() => {
  const c: number = props.currentPage
  const t: number = props.totalPages;

  const halfpage = Math.floor(props.pageSize / 2);

  if(props.totalPages < 10) {
    return Array.from({length: props.totalPages}, (_, index) => index + 1)
  } else{
    if (c <= halfpage + 1) {
      let array: Array<number | string> = Array.from({length: props.pageSize}, (_, i) => i + 1)
      array.push('...')
      array.push(t);
      // return [1, 2, 3, 4, 5, 6, 7, 8, 9, '...', t]  //第一种情况
      return array;
    } else if (c >= t - halfpage) {
      let array = [1, '...']
      for (let i = t - props.pageSize; i <= t; i += 1) {
        array.push(i);
      }
      // return [1, '...', t-8, t-7, t-6, t-5, t-4, t-3, t-2, t-1, t] //第二种情况
      return array;
    } else {
      let array = [1, '...']
      for (let i = c - (halfpage - 1); i <= c + (halfpage -1); i += 1) {
        array.push(i)
      }

      array.push("...")
      array.push(t)
      // return [1, '...', c-3, c-2, c-1, c, c+1, c+2, c+3, '...', t]  //第三种情况
      return array;
    }
  }

})


function select(item: number | string) {
  if(item != '...') {
    // current.value = item
    emits('update:currentPage', item)
  }
}

function prevOrNext(n: number) {
  // if(current.value > 1 && current.value < props.totalPages) {
  //   current.value += n
  // }

  if(props.currentPage > 1&& props.currentPage < props.totalPages) {
    emits('update:currentPage', props.currentPage + n)
  }
}
</script>

<style lang="scss" scoped>
div.page-container {
  display: flex;
  justify-content: center;
  align-items: center;

  ul {
    user-select: none;
    list-style: none;
    font-size: 0;
    padding: 0;
    margin: 0;
    display: flex;
    align-items: center;

    li {
      width: 30px;
      background: white;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 14px;
      height: 32px;
      margin: 0 1px;
      cursor: pointer;

      &:hover {
        color: #4ba4ff;
      }

      &.actived {
        border-color: #2d8cf0;
        background-color: #2d8cf0;
        color: #fff;
      }

      span {
        display: flex;
        align-items: center;
        justify-content: center;

        svg {
          width: 50%;
          height: 50%;

          &:hover {
            fill: #4ba4ff;
          }

          &.right {
            transform: rotateY(180deg);
          }
        }
      }
    }
  }
}
</style>
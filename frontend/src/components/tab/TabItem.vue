<template>
  <div class="tab-item" v-if="name == activeName">
    <slot/>
  </div>
</template>

<script lang="ts" setup>
import { inject, Ref, onMounted } from 'vue';

const props = defineProps<{
  name: string
}>()

const activeName = inject<Ref<string>>("active-name")
const names = inject<Ref<string[]>>("names")

onMounted(() => {
  names?.value.push(props.name)
})
</script>
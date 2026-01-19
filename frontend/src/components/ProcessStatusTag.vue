<!-- src/components/ProcessStatusTag.vue -->
<template>
  <el-tag :type="tagType" :effect="effect">{{ statusText }}</el-tag>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    required: true
  }
})

const tagType = computed(() => {
  switch(props.status) {
    case 'notStarted': return 'danger'
    case 'inProgress': return 'warning'
    case 'completed': return 'success'
    default: return 'info'
  }
})

const statusText = computed(() => {
  switch(props.status) {
    case 'notStarted': return '未开始'
    case 'inProgress': return '进行中'
    case 'completed': return '已完成'
    default: return '未知状态'
  }
})

const effect = computed(() => 'dark')
</script>
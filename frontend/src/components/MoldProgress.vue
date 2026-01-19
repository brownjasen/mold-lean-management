<!-- src/components/MoldProgress.vue -->
<template>
  <div class="mold-progress">
    <el-progress
        :percentage="percentage"
        :color="getColorByPercentage"
        :format="format" />
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  percentage: {
    type: Number,
    default: 0
  }
})

const getColorByPercentage = computed(() => {
  if (props.percentage < 30) return '#f56c6c' // 红色
  if (props.percentage < 80) return '#e6a23c' // 黄色
  return '#67c23a' // 绿色
})

const format = (percentage) => {
  return percentage === 100 ? '完成' : `${percentage}%`
}
</script>
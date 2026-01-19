<!-- src/components/ProcessList.vue -->
<template>
  <div class="process-list">
    <el-table :data="processes" border stripe size="large">
      <el-table-column label="工序名称" prop="name" min-width="150">
        <template #default="{ row }">
          <span style="font-size: 18px; font-weight: 600;">{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="负责人" prop="operator" min-width="120">
        <template #default="{ row }">
          <span style="font-size: 16px;">{{ row.operator }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="120" align="center">
        <template #default="{ row }">
          <ProcessStatusTag :status="row.status" />
        </template>
      </el-table-column>
      <el-table-column label="开始时间" min-width="180">
        <template #default="{ row }">
          <span style="font-size: 16px;">{{ formatTime(row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" min-width="180">
        <template #default="{ row }">
          <span style="font-size: 16px;">{{ formatTime(row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="150" align="center">
        <template #default="{ row }">
          <el-button 
            size="large" 
            v-if="row.status === 'pending'"
            @click="handleStart(row)">
            开始
          </el-button>
          <el-button 
            size="large" 
            type="success"
            v-if="row.status === 'processing'"
            @click="handleComplete(row)">
            完成
          </el-button>
          <el-tag v-if="row.status === 'completed'" type="success" size="large">已完成</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { ElMessage } from 'element-plus'
import ProcessStatusTag from './ProcessStatusTag.vue'
import dayjs from 'dayjs'

const props = defineProps({
  processes: {
    type: Array,
    default: () => []
  }
})

const formatTime = (timestamp) => {
  return timestamp ? dayjs(timestamp).format('YYYY-MM-DD HH:mm') : '-'
}

const handleStart = (row) => {
  row.status = 'processing'
  row.startTime = Date.now()
  ElMessage.success('工序已开始')
}

const handleComplete = (row) => {
  row.status = 'completed'
  row.endTime = Date.now()
  ElMessage.success('工序已完成')
}
</script>

<style scoped>
.process-list {
  margin-top: 20px;
}

:deep(.el-table) {
  font-size: 16px;
}

:deep(.el-table th) {
  background: #f5f7fa;
  font-weight: 600;
  font-size: 16px;
}

:deep(.el-table td) {
  padding: 16px 0;
}
</style>

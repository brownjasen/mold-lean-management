<!-- src/views/Dashboard.vue -->
<template>
  <div class="dashboard">
    <div class="page-header">
      <h1>生产总览</h1>
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-icon total">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="18" height="18" rx="2"/>
              <path d="M3 9h18M9 21V9"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ totalMolds }}</div>
            <div class="stat-label">总模具数</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon processing">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ processingMolds }}</div>
            <div class="stat-label">生产中</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon completed">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
              <polyline points="22 4 12 14.01 9 11.01"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ completedMolds }}</div>
            <div class="stat-label">已完成</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon pending">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ pendingMolds }}</div>
            <div class="stat-label">待开始</div>
          </div>
        </div>
      </div>
    </div>

    <el-card class="table-card">
      <el-table 
        :data="moldList" 
        style="width: 100%" 
        :row-class-name="tableRowClassName"
        stripe
        size="large">
        <el-table-column label="模具编号" prop="moldCode" min-width="150" />
        <el-table-column label="下单时间" min-width="180">
          <template #default="{ row }">
            {{ formatTime(row.orderTime) }}
          </template>
        </el-table-column>
        <el-table-column label="开始生产时间" min-width="180">
          <template #default="{ row }">
            {{ row.startProductionTime ? formatTime(row.startProductionTime) : '未开始' }}
          </template>
        </el-table-column>
        <el-table-column label="生产进度" min-width="200">
          <template #default="{ row }">
            <MoldProgress :percentage="row.progress" />
          </template>
        </el-table-column>
        <el-table-column label="完成时间" min-width="180">
          <template #default="{ row }">
            {{ row.completionTime ? formatTime(row.completionTime) : '未完成' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="large">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="150">
          <template #default="{ row }">
            <el-button type="primary" size="large" @click="goToDetail(row.id)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import MoldProgress from '../components/MoldProgress.vue'
import dayjs from 'dayjs'
import { moldApi } from '../api'

const router = useRouter()

// 模具列表
const moldList = ref([])
const totalMolds = ref(0)
const processingMolds = ref(0)
const completedMolds = ref(0)
const pendingMolds = ref(0)

// 获取数据
const fetchData = async () => {
  try {
    const [listRes, statsRes] = await Promise.all([
      moldApi.getList(),
      moldApi.getStatistics()
    ])
    // 转换 status 为小写，匹配前端的判断逻辑
    const statusMap = {
      'PENDING': 'pending',
      'PROCESSING': 'processing',
      'COMPLETED': 'completed'
    }
    moldList.value = (listRes || []).map(m => ({
      ...m,
      status: statusMap[m.status] || m.status
    }))
    totalMolds.value = statsRes.totalMolds || 0
    processingMolds.value = statsRes.processingMolds || 0
    completedMolds.value = statsRes.completedMolds || 0
    pendingMolds.value = statsRes.pendingMolds || 0
  } catch (error) {
    console.error('获取数据失败:', error)
  }
}

onMounted(() => {
  fetchData()
})

const formatTime = (timestamp) => {
  return dayjs(timestamp).format('YYYY-MM-DD HH:mm')
}

const goToDetail = (id) => {
  router.push({ name: 'moldDetail', params: { id } })
}

const getStatusType = (status) => {
  const types = {
    'processing': 'warning',
    'completed': 'success',
    'pending': 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    'processing': '生产中',
    'completed': '已完成',
    'pending': '待开始'
  }
  return texts[status] || '未知'
}

const tableRowClassName = ({ row }) => {
  if (row.status === 'completed') return 'success-row'
  if (row.status === 'processing') return 'warning-row'
  return ''
}
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 20px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  border: 1px solid #e5e7eb;
  transition: all 0.2s;
}

.stat-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-icon svg {
  width: 24px;
  height: 24px;
}

.stat-icon.total {
  background: #eff6ff;
  color: #3b82f6;
}

.stat-icon.processing {
  background: #fef3c7;
  color: #f59e0b;
}

.stat-icon.completed {
  background: #d1fae5;
  color: #10b981;
}

.stat-icon.pending {
  background: #f3f4f6;
  color: #6b7280;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.table-card {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-card__body) {
  padding: 0;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th.el-table__cell) {
  background: #f9fafb;
  font-weight: 600;
  font-size: 13px;
  color: #6b7280;
  border-bottom: 1px solid #e5e7eb;
  padding: 14px 0;
}

:deep(.el-table td.el-table__cell) {
  padding: 14px 0;
  border-bottom: 1px solid #f3f4f6;
}

:deep(.el-table__row:hover td) {
  background: #f9fafb;
}

:deep(.el-table__row) {
  cursor: pointer;
}

:deep(.success-row) {
  background: #f0fdf4;
}

:deep(.warning-row) {
  background: #fffbeb;
}

:deep(.el-button) {
  border-radius: 6px;
}

:deep(.el-tag) {
  border-radius: 6px;
  border: none;
  font-weight: 500;
}
</style>
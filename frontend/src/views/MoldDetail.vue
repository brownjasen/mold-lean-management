<!-- src/views/MoldDetail.vue -->
<template>
  <div class="mold-detail">
    <el-page-header @back="goBack" class="page-header">
      <template #title>
        <span style="font-size: 18px;">返回列表</span>
      </template>
      <template #content>
        <span style="font-size: 28px; font-weight: 600;">{{ currentMold?.moldCode || '模具详情' }}</span>
      </template>
    </el-page-header>

    <div class="mold-info" v-if="currentMold">
      <el-card class="info-card">
        <el-descriptions :column="3" border size="large">
          <el-descriptions-item label="模具编号" label-class-name="label-style">
            <span style="font-size: 18px; font-weight: 600;">{{ currentMold.moldCode }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="下单时间" label-class-name="label-style">
            <span style="font-size: 18px;">{{ formatTime(currentMold.orderTime) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="当前进度" label-class-name="label-style">
            <MoldProgress :percentage="currentMold.progress" />
          </el-descriptions-item>
          <el-descriptions-item label="开始时间" label-class-name="label-style">
            <span style="font-size: 18px;">{{ formatTime(currentMold.startProductionTime) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="完成时间" label-class-name="label-style">
            <span style="font-size: 18px;">{{ currentMold.completionTime ? formatTime(currentMold.completionTime) : '未完成' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="状态" label-class-name="label-style">
            <el-tag :type="getStatusType(currentMold.status)" size="large">
              {{ getStatusText(currentMold.status) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>

    <!-- 工序模块 -->
    <div class="process-modules">
      <h2 style="font-size: 24px; margin-bottom: 20px;">工序模块</h2>
      <el-tabs type="card" class="process-tabs">
        <el-tab-pane v-for="module in processModules" :key="module.type" :label="module.name">
          <ProcessList :processes="getProcessesByModule(module.type)" />
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 图纸信息 -->
    <div class="blueprint-section" v-if="currentMold">
      <h2 style="font-size: 24px; margin-bottom: 20px;">设计图纸</h2>
      <BlueprintList :blueprints="blueprints || []" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MoldProgress from '../components/MoldProgress.vue'
import ProcessList from '../components/ProcessList.vue'
import BlueprintList from '../components/BlueprintList.vue'
import dayjs from 'dayjs'
import { moldApi, processApi, blueprintApi } from '../api'

const route = useRoute()
const router = useRouter()

const currentMold = ref(null)
const processes = ref([])
const blueprints = ref([])

// 获取数据
const fetchData = async () => {
  try {
    const id = route.params.id
    const [moldRes, processesRes, blueprintsRes] = await Promise.all([
      moldApi.getById(id),
      processApi.getByMoldId(id),
      blueprintApi.getByMoldId(id)
    ])
    currentMold.value = moldRes ? {
      ...moldRes,
      status: moldRes.status?.toLowerCase()
    } : null
    // 转换 moduleType 为小写，匹配前端的模块类型
    const moduleTypeMap = {
      'FRAME': 'frame',
      'MAIN_PARTS': 'mainParts',
      'ACCESSORIES': 'accessories',
      'ASSEMBLY': 'assembly',
      'TESTING': 'testing',
      'REPAIR': 'repair'
    }
    const statusMap = {
      'PENDING': 'notStarted',
      'PROCESSING': 'inProgress',
      'COMPLETED': 'completed'
    }
    processes.value = (processesRes || []).map(p => ({
      ...p,
      moduleType: moduleTypeMap[p.moduleType] || p.moduleType,
      status: statusMap[p.status] || p.status?.toLowerCase()
    }))
    blueprints.value = blueprintsRes || []
  } catch (error) {
    console.error('获取数据失败:', error)
  }
}

onMounted(() => {
  fetchData()
})

const processModules = [
  { type: 'frame', name: '模架' },
  { type: 'mainParts', name: '三大件' },
  { type: 'accessories', name: '辅料' },
  { type: 'assembly', name: '组装' },
  { type: 'testing', name: '试模' },
  { type: 'repair', name: '返修' }
]

const formatTime = (timestamp) => {
  return timestamp ? dayjs(timestamp).format('YYYY-MM-DD HH:mm') : '未开始'
}

const getProcessesByModule = (moduleType) => {
  return processes.value.filter(p => p.moduleType === moduleType)
}

const goBack = () => {
  router.back()
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
</script>

<style scoped>
.mold-detail {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 20px;
}

:deep(.el-page-header__title) {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

:deep(.el-page-header__content) {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
}

.info-card {
  margin-bottom: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-descriptions) {
  font-size: 14px;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  color: #6b7280;
  background: #f9fafb;
}

:deep(.el-descriptions__content) {
  color: #1f2937;
}

:deep(.el-descriptions__cell) {
  padding: 14px 16px;
}

.process-modules {
  margin-bottom: 24px;
}

.process-modules h2 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 16px;
}

:deep(.el-tabs--card) {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-tabs__header) {
  margin: 0;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

:deep(.el-tabs__nav) {
  border: none;
}

:deep(.el-tabs__item) {
  font-size: 14px;
  font-weight: 500;
  color: #6b7280;
  border: none;
  padding: 0 24px;
  height: 44px;
  line-height: 44px;
  transition: all 0.2s;
}

:deep(.el-tabs__item:hover) {
  color: #409eff;
}

:deep(.el-tabs__item.is-active) {
  color: #409eff;
  background: #ffffff;
  border-bottom: 2px solid #409eff;
}

:deep(.el-tabs__content) {
  padding: 0;
}

.blueprint-section h2 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 16px;
}

:deep(.el-tag) {
  border-radius: 6px;
  border: none;
  font-weight: 500;
}

:deep(.el-button) {
  border-radius: 6px;
}
</style>
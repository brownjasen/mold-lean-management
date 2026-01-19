<!-- src/views/MaterialManagement.vue -->
<template>
  <div class="material-management">
    <div class="page-header">
      <h1>物料管理</h1>
      <div class="stats-summary">
        <div class="summary-card">
          <div class="summary-icon total">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
              <polyline points="3.27 6.96 12 12.01 20.73 6.96"/>
              <line x1="12" y1="22.08" x2="12" y2="12"/>
            </svg>
          </div>
          <div class="summary-content">
            <div class="summary-value">{{ totalMaterials }}</div>
            <div class="summary-label">物料种类</div>
          </div>
        </div>
        <div class="summary-card">
          <div class="summary-icon warning">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
              <line x1="12" y1="9" x2="12" y2="13"/>
              <line x1="12" y1="17" x2="12.01" y2="17"/>
            </svg>
          </div>
          <div class="summary-content">
            <div class="summary-value">{{ lowStockCount }}</div>
            <div class="summary-label">库存预警</div>
          </div>
        </div>
        <div class="summary-card">
          <div class="summary-icon success">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
              <polyline points="22 4 12 14.01 9 11.01"/>
            </svg>
          </div>
          <div class="summary-content">
            <div class="summary-value">{{ normalStockCount }}</div>
            <div class="summary-label">库存正常</div>
          </div>
        </div>
      </div>
    </div>

    <el-card class="table-card">
      <el-table :data="materials" border stripe size="large" style="width: 100%">
        <el-table-column label="物料编号" prop="code" min-width="120" />
        <el-table-column label="物料名称" prop="name" min-width="120" />
        <el-table-column label="规格" prop="specification" min-width="200" />
        <el-table-column label="单位" prop="unit" min-width="80" align="center" />
        <el-table-column label="当前库存" min-width="120" align="center">
          <template #default="{ row }">
            <span :class="{ 'stock-warning': row.currentStock < row.safetyStock }" style="font-size: 18px; font-weight: 600;">
              {{ row.currentStock }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="安全库存" prop="safetyStock" min-width="120" align="center">
          <template #default="{ row }">
            <span style="font-size: 16px;">{{ row.safetyStock }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getStockStatusType(row)" size="large">
              {{ getStockStatusText(row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="220">
          <template #default="{ row }">
            <el-button size="large" type="success" @click="handleInStock(row)">入库</el-button>
            <el-button size="large" type="warning" @click="handleOutStock(row)">出库</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 入库/出库对话框 -->
    <el-dialog 
      :title="dialogType === 'in' ? '物料入库' : '物料出库'" 
      v-model="dialogVisible" 
      width="600px">
      <el-form :model="transactionForm" label-width="120px" size="large">
        <el-form-item label="物料名称">
          <span style="font-size: 18px; font-weight: 600;">{{ currentMaterial?.name }}</span>
        </el-form-item>
        <el-form-item label="当前库存">
          <span style="font-size: 18px; color: #409eff;">{{ currentMaterial?.currentStock }}</span>
        </el-form-item>
        <el-form-item label="数量" required>
          <el-input-number v-model="transactionForm.quantity" :min="1" :max="10000" size="large" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="transactionForm.remark" type="textarea" :rows="4" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false" size="large">取消</el-button>
        <el-button type="primary" @click="submitTransaction" size="large">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { materialApi } from '../api'

const materials = ref([])
const totalMaterials = ref(0)
const lowStockCount = ref(0)
const normalStockCount = ref(0)

const dialogVisible = ref(false)
const dialogType = ref('in')
const currentMaterial = ref(null)
const transactionForm = ref({
  quantity: 1,
  remark: ''
})

// 获取数据
const fetchData = async () => {
  try {
    const [listRes, statsRes] = await Promise.all([
      materialApi.getList(),
      materialApi.getStatistics()
    ])
    materials.value = listRes || []
    totalMaterials.value = statsRes.totalMaterials || 0
    lowStockCount.value = statsRes.lowStockCount || 0
    normalStockCount.value = statsRes.normalStockCount || 0
  } catch (error) {
    console.error('获取数据失败:', error)
  }
}

onMounted(() => {
  fetchData()
})

const getStockStatusType = (material) => {
  if (material.currentStock < material.safetyStock) return 'danger'
  if (material.currentStock < material.safetyStock * 2) return 'warning'
  return 'success'
}

const getStockStatusText = (material) => {
  if (material.currentStock < material.safetyStock) return '库存不足'
  if (material.currentStock < material.safetyStock * 2) return '库存偏低'
  return '库存充足'
}

const handleInStock = (material) => {
  currentMaterial.value = material
  dialogType.value = 'in'
  transactionForm.value = { quantity: 1, remark: '' }
  dialogVisible.value = true
}

const handleOutStock = (material) => {
  currentMaterial.value = material
  dialogType.value = 'out'
  transactionForm.value = { quantity: 1, remark: '' }
  dialogVisible.value = true
}

const submitTransaction = async () => {
  try {
    const data = {
      quantity: transactionForm.value.quantity,
      remark: transactionForm.value.remark
    }

    if (dialogType.value === 'in') {
      await materialApi.inStock(currentMaterial.value.id, data)
      ElMessage.success('入库成功！')
    } else {
      await materialApi.outStock(currentMaterial.value.id, data)
      ElMessage.success('出库成功！')
    }

    dialogVisible.value = false
    // 重新获取数据
    await fetchData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}
</script>

<style scoped>
.material-management {
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

.stats-summary {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.summary-card {
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

.summary-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.summary-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.summary-icon svg {
  width: 24px;
  height: 24px;
}

.summary-icon.total {
  background: #eff6ff;
  color: #3b82f6;
}

.summary-icon.warning {
  background: #fef3c7;
  color: #f59e0b;
}

.summary-icon.success {
  background: #d1fae5;
  color: #10b981;
}

.summary-content {
  flex: 1;
}

.summary-value {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  line-height: 1;
  margin-bottom: 4px;
}

.summary-label {
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

.stock-warning {
  color: #ef4444;
  font-weight: 600;
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

:deep(.el-button) {
  border-radius: 6px;
}

:deep(.el-tag) {
  border-radius: 6px;
  border: none;
  font-weight: 500;
}

:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  padding: 20px 24px;
  border-bottom: 1px solid #e5e7eb;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

:deep(.el-dialog__footer) {
  padding: 16px 24px;
  border-top: 1px solid #e5e7eb;
}
</style>
<!-- src/components/BlueprintList.vue -->
<template>
  <div class="blueprint-list">
    <el-row :gutter="30">
      <el-col :span="6" v-for="blueprint in blueprints" :key="blueprint.id">
        <el-card class="blueprint-card" @click="viewBlueprint(blueprint)" shadow="hover">
          <div class="blueprint-thumb">
            <el-icon :size="80" color="#409eff"><Document /></el-icon>
          </div>
          <div class="blueprint-info">
            <div class="name">{{ blueprint.name }}</div>
            <div class="version">版本: {{ blueprint.version }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { Document } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

defineProps({
  blueprints: {
    type: Array,
    default: () => []
  }
})

const viewBlueprint = (blueprint) => {
  ElMessage.success(`正在查看图纸：${blueprint.name}`)
}
</script>

<style scoped>
.blueprint-list {
  margin-top: 20px;
}

.blueprint-card {
  cursor: pointer;
  text-align: center;
  transition: all 0.3s;
  height: 280px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.blueprint-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.2);
}

.blueprint-thumb {
  padding: 40px 20px;
  transition: transform 0.3s;
}

.blueprint-card:hover .blueprint-thumb {
  transform: scale(1.1);
}

.blueprint-info {
  padding: 20px 0;
  border-top: 1px solid #eee;
}

.name {
  font-weight: bold;
  font-size: 20px;
  margin-bottom: 10px;
  color: #303133;
}

.version {
  color: #909399;
  font-size: 16px;
}
</style>

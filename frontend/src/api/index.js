import axios from 'axios'
import * as mockData from '../mock'

// 检测是否为 GitHub Pages 环境（没有后端）
const isGitHubPages = import.meta.env.MODE === 'production' || !import.meta.env.VITE_API_BASE_URL

// 创建 axios 实例 - 支持环境变量
const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

const request = axios.create({
  baseURL,
  timeout: 10000
})

// ==================== 模具 API ====================
export const moldApi = {
  // 获取模具列表
  async getList() {
    if (isGitHubPages) return mockData.mockMolds
    return request.get('/molds')
  },

  // 获取模具详情
  async getById(id) {
    if (isGitHubPages) return mockData.mockMolds.find(m => m.id === parseInt(id))
    return request.get(`/molds/${id}`)
  },

  // 获取统计数据
  async getStatistics() {
    if (isGitHubPages) return mockData.mockStatistics
    return request.get('/molds/statistics')
  },

  // 创建模具
  create(data) {
    return request.post('/molds', data)
  },

  // 更新模具
  update(id, data) {
    return request.put(`/molds/${id}`, data)
  },

  // 删除模具
  delete(id) {
    return request.delete(`/molds/${id}`)
  },

  // 开始生产
  start(id) {
    return request.post(`/molds/${id}/start`)
  },

  // 完成生产
  complete(id) {
    return request.post(`/molds/${id}/complete`)
  },

  // 更新进度
  updateProgress(id, progress) {
    return request.put(`/molds/${id}/progress`, null, { params: { progress } })
  }
}

// ==================== 工序 API ====================
export const processApi = {
  // 获取模具的工序列表
  async getByMoldId(moldId) {
    if (isGitHubPages) return mockData.mockProcesses[moldId] || []
    return request.get(`/processes/mold/${moldId}`)
  },

  // 获取模具指定模块的工序列表
  async getByMoldIdAndModule(moldId, moduleType) {
    const processes = isGitHubPages
      ? (mockData.mockProcesses[moldId] || [])
      : await request.get(`/processes/mold/${moldId}`)
    return processes.filter(p => p.moduleType === moduleType)
  },

  // 获取工序详情
  getById(id) {
    return request.get(`/processes/${id}`)
  },

  // 创建工序
  create(data) {
    return request.post('/processes', data)
  },

  // 更新工序
  update(id, data) {
    return request.put(`/processes/${id}`, data)
  },

  // 删除工序
  delete(id) {
    return request.delete(`/processes/${id}`)
  },

  // 开始工序
  start(id) {
    return request.post(`/processes/${id}/start`)
  },

  // 完成工序
  complete(id) {
    return request.post(`/processes/${id}/complete`)
  }
}

// ==================== 图纸 API ====================
export const blueprintApi = {
  // 获取模具的图纸列表
  async getByMoldId(moldId) {
    if (isGitHubPages) return mockData.mockBlueprints[moldId] || []
    return request.get(`/blueprints/mold/${moldId}`)
  },

  // 获取图纸详情
  getById(id) {
    return request.get(`/blueprints/${id}`)
  },

  // 创建图纸
  create(data) {
    return request.post('/blueprints', data)
  },

  // 更新图纸
  update(id, data) {
    return request.put(`/blueprints/${id}`, data)
  },

  // 删除图纸
  delete(id) {
    return request.delete(`/blueprints/${id}`)
  }
}

// ==================== 物料 API ====================
export const materialApi = {
  // 获取物料列表
  async getList() {
    if (isGitHubPages) return mockData.mockMaterials
    return request.get('/materials')
  },

  // 获取物料详情
  getById(id) {
    return request.get(`/materials/${id}`)
  },

  // 获取统计数据
  async getStatistics() {
    if (isGitHubPages) return mockData.mockMaterialStatistics
    return request.get('/materials/statistics')
  },

  // 搜索物料
  search(keyword) {
    return request.get('/materials/search', { params: { keyword } })
  },

  // 添加物料
  create(data) {
    return request.post('/materials', data)
  },

  // 更新物料
  update(id, data) {
    return request.put(`/materials/${id}`, data)
  },

  // 删除物料
  delete(id) {
    return request.delete(`/materials/${id}`)
  },

  // 物料入库
  inStock(id, data) {
    return request.post(`/materials/${id}/in`, data)
  },

  // 物料出库
  outStock(id, data) {
    return request.post(`/materials/${id}/out`, data)
  }
}

export default request

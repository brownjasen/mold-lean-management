// 模拟数据
export const mockMolds = [
  {
    id: 1,
    moldCode: 'M2024-001',
    orderTime: 1705339200000,
    startProductionTime: 1705425600000,
    progress: 85,
    completionTime: null,
    status: 'processing'
  },
  {
    id: 2,
    moldCode: 'M2024-002',
    orderTime: 1705252800000,
    startProductionTime: 1705339200000,
    progress: 100,
    completionTime: 1705684800000,
    status: 'completed'
  },
  {
    id: 3,
    moldCode: 'M2024-003',
    orderTime: 1705598400000,
    startProductionTime: null,
    progress: 0,
    completionTime: null,
    status: 'pending'
  },
  {
    id: 4,
    moldCode: 'M2024-004',
    orderTime: 1705512000000,
    startProductionTime: 1705598400000,
    progress: 45,
    completionTime: null,
    status: 'processing'
  },
  {
    id: 5,
    moldCode: 'M2024-005',
    orderTime: 1705425600000,
    startProductionTime: 1705512000000,
    progress: 100,
    completionTime: 1705857600000,
    status: 'completed'
  }
]

export const mockStatistics = {
  totalMolds: 5,
  processingMolds: 2,
  completedMolds: 2,
  pendingMolds: 1
}

export const mockProcesses = {
  '1': [
    { id: 1, moldId: 1, name: '模架加工', moduleType: 'frame', operator: '张三', status: 'completed', startTime: 1705425600000, endTime: 1705454400000 },
    { id: 2, moldId: 1, name: '三大件制作', moduleType: 'mainParts', operator: '李四', status: 'completed', startTime: 1705454400000, endTime: 1705483200000 },
    { id: 3, moldId: 1, name: '辅料准备', moduleType: 'accessories', operator: '王五', status: 'inProgress', startTime: 1705483200000, endTime: null },
    { id: 4, moldId: 1, name: '组装', moduleType: 'assembly', operator: '赵六', status: 'notStarted', startTime: null, endTime: null },
    { id: 5, moldId: 1, name: '试模', moduleType: 'testing', operator: '孙七', status: 'notStarted', startTime: null, endTime: null }
  ]
}

export const mockBlueprints = {
  '1': [
    { id: 1, moldId: 1, name: '模具总装图', version: 'V1.2', createdAt: 1705339200000 },
    { id: 2, moldId: 1, name: '模架设计图', version: 'V1.0', createdAt: 1705339200000 },
    { id: 3, moldId: 1, name: '三大件图纸', version: 'V1.1', createdAt: 1705339200000 },
    { id: 4, moldId: 1, name: '零件清单', version: 'V1.0', createdAt: 1705339200000 }
  ]
}

export const mockMaterials = [
  { id: 1, code: 'MAT-001', name: 'P20模具钢', specification: '200x300x50mm', unit: '块', currentStock: 15, safetyStock: 10 },
  { id: 2, code: 'MAT-002', name: '718H模具钢', specification: '250x400x60mm', unit: '块', currentStock: 8, safetyStock: 10 },
  { id: 3, code: 'MAT-003', name: 'S136H模具钢', specification: '300x500x80mm', unit: '块', currentStock: 20, safetyStock: 5 },
  { id: 4, code: 'MAT-004', name: '导柱', specification: 'Φ30x200mm', unit: '根', currentStock: 50, safetyStock: 20 },
  { id: 5, code: 'MAT-005', name: '导套', specification: 'Φ30x50mm', unit: '个', currentStock: 100, safetyStock: 30 },
  { id: 6, code: 'MAT-006', name: '弹簧', specification: 'Φ50x80mm', unit: '个', currentStock: 5, safetyStock: 20 }
]

export const mockMaterialStatistics = {
  totalMaterials: 6,
  lowStockCount: 2,
  normalStockCount: 4
}

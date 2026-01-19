-- ============================================
-- 模具精益管理系统 - 完整初始化脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS mold_lean_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mold_lean_management;

-- ============================================
-- 删除已存在的表（如果需要重新初始化）
-- ============================================
DROP TABLE IF EXISTS blueprint;
DROP TABLE IF EXISTS `process`;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS mold;

-- ============================================
-- 创建模具表
-- ============================================
CREATE TABLE IF NOT EXISTS mold (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    mold_code VARCHAR(50) NOT NULL UNIQUE COMMENT '模具编号',
    priority INT NOT NULL DEFAULT 1 COMMENT '优先级',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING-待开始, PROCESSING-生产中, COMPLETED-已完成',
    progress INT NOT NULL DEFAULT 0 COMMENT '进度(0-100)',
    order_time DATETIME NOT NULL COMMENT '下单时间',
    start_production_time DATETIME COMMENT '开始生产时间',
    completion_time DATETIME COMMENT '完成时间',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_order_time (order_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模具表';

-- ============================================
-- 创建工序表
-- ============================================
CREATE TABLE IF NOT EXISTS `process` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    mold_id BIGINT NOT NULL COMMENT '模具ID',
    name VARCHAR(50) NOT NULL COMMENT '工序名称',
    module_type VARCHAR(30) NOT NULL COMMENT '模块类型：FRAME-模架, MAIN_PARTS-三大件, ACCESSORIES-辅料, ASSEMBLY-组装, TESTING-试模, REPAIR-返修',
    operator VARCHAR(50) COMMENT '负责人',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING-未开始, PROCESSING-进行中, COMPLETED-已完成',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    FOREIGN KEY (mold_id) REFERENCES mold(id) ON DELETE CASCADE,
    INDEX idx_mold_id (mold_id),
    INDEX idx_module_type (module_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工序表';

-- ============================================
-- 创建图纸表
-- ============================================
CREATE TABLE IF NOT EXISTS blueprint (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    mold_id BIGINT NOT NULL COMMENT '模具ID',
    name VARCHAR(100) NOT NULL COMMENT '图纸名称',
    version VARCHAR(20) NOT NULL COMMENT '版本号',
    url VARCHAR(500) COMMENT '文件路径',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    FOREIGN KEY (mold_id) REFERENCES mold(id) ON DELETE CASCADE,
    INDEX idx_mold_id (mold_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图纸表';

-- ============================================
-- 创建物料表
-- ============================================
CREATE TABLE IF NOT EXISTS material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '物料编号',
    name VARCHAR(100) NOT NULL COMMENT '物料名称',
    specification VARCHAR(200) COMMENT '规格',
    unit VARCHAR(20) COMMENT '单位',
    current_stock INT NOT NULL DEFAULT 0 COMMENT '当前库存',
    safety_stock INT NOT NULL DEFAULT 0 COMMENT '安全库存',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    INDEX idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料表';

-- ============================================
-- 插入模具数据
-- ============================================
INSERT INTO mold (id, mold_code, priority, status, progress, order_time, start_production_time, completion_time, created_at, updated_at) VALUES
(1, 'MD-2023-001', 1, 'PROCESSING', 60, '2023-11-12 10:00:00', '2023-11-14 09:00:00', NULL, '2023-11-12 10:00:00', '2023-11-19 10:00:00'),
(2, 'MD-2023-002', 2, 'PENDING', 0, '2023-11-16 14:00:00', NULL, NULL, '2023-11-16 14:00:00', '2023-11-16 14:00:00'),
(3, 'MD-2023-003', 3, 'COMPLETED', 100, '2023-10-20 08:00:00', '2023-10-25 09:00:00', '2023-11-17 16:00:00', '2023-10-20 08:00:00', '2023-11-17 16:00:00'),
(4, 'MD-2023-004', 4, 'PROCESSING', 35, '2023-11-09 11:00:00', '2023-11-11 10:00:00', NULL, '2023-11-09 11:00:00', '2023-11-19 10:00:00'),
(5, 'MD-2023-005', 5, 'PROCESSING', 88, '2023-11-04 15:00:00', '2023-11-07 09:00:00', NULL, '2023-11-04 15:00:00', '2023-11-19 10:00:00');

-- ============================================
-- 插入工序数据
-- ============================================
INSERT INTO `process` (id, mold_id, name, module_type, operator, status, start_time, end_time, created_at, updated_at) VALUES
-- 模具1的工序
(1, 1, '下料', 'FRAME', '张三', 'COMPLETED', '2023-11-14 09:00:00', '2023-11-15 17:00:00', '2023-11-14 09:00:00', '2023-11-15 17:00:00'),
(2, 1, '粗加工', 'FRAME', '李四', 'PROCESSING', '2023-11-16 08:00:00', NULL, '2023-11-16 08:00:00', '2023-11-19 10:00:00'),
(3, 1, '精加工', 'FRAME', '王五', 'PENDING', NULL, NULL, '2023-11-16 08:00:00', '2023-11-16 08:00:00'),
(4, 1, '热处理', 'MAIN_PARTS', '赵六', 'PENDING', NULL, NULL, '2023-11-16 08:00:00', '2023-11-16 08:00:00'),
(5, 1, '电火花加工', 'MAIN_PARTS', '孙七', 'PENDING', NULL, NULL, '2023-11-16 08:00:00', '2023-11-16 08:00:00'),
(6, 1, '线切割', 'MAIN_PARTS', '周八', 'PENDING', NULL, NULL, '2023-11-16 08:00:00', '2023-11-16 08:00:00'),
(7, 1, '零件装配', 'ASSEMBLY', '吴九', 'PENDING', NULL, NULL, '2023-11-16 08:00:00', '2023-11-16 08:00:00'),
(8, 1, '总装调试', 'ASSEMBLY', '郑十', 'PENDING', NULL, NULL, '2023-11-16 08:00:00', '2023-11-16 08:00:00'),
(9, 1, '试模', 'TESTING', NULL, 'PENDING', NULL, NULL, '2023-11-16 08:00:00', '2023-11-16 08:00:00'),
(10, 1, '返修', 'REPAIR', NULL, 'PENDING', NULL, NULL, '2023-11-16 08:00:00', '2023-11-16 08:00:00'),
-- 模具3的工序（已完成）
(11, 3, '下料', 'FRAME', '张三', 'COMPLETED', '2023-10-25 09:00:00', '2023-10-26 17:00:00', '2023-10-25 09:00:00', '2023-10-26 17:00:00'),
(12, 3, '粗加工', 'FRAME', '李四', 'COMPLETED', '2023-10-27 08:00:00', '2023-10-28 17:00:00', '2023-10-27 08:00:00', '2023-10-28 17:00:00'),
(13, 3, '精加工', 'FRAME', '王五', 'COMPLETED', '2023-10-29 08:00:00', '2023-10-31 17:00:00', '2023-10-29 08:00:00', '2023-10-31 17:00:00'),
(14, 3, '热处理', 'MAIN_PARTS', '赵六', 'COMPLETED', '2023-11-01 08:00:00', '2023-11-02 17:00:00', '2023-11-01 08:00:00', '2023-11-02 17:00:00'),
(15, 3, '零件装配', 'ASSEMBLY', '吴九', 'COMPLETED', '2023-11-03 08:00:00', '2023-11-05 17:00:00', '2023-11-03 08:00:00', '2023-11-05 17:00:00'),
(16, 3, '试模', 'TESTING', '郑十', 'COMPLETED', '2023-11-06 08:00:00', '2023-11-07 17:00:00', '2023-11-06 08:00:00', '2023-11-07 17:00:00');

-- ============================================
-- 插入图纸数据
-- ============================================
INSERT INTO blueprint (id, mold_id, name, version, url, created_at, updated_at) VALUES
(1, 1, '总装图', 'v1.0', '/uploads/blueprints/mold001-assembly.pdf', '2023-11-12 10:00:00', '2023-11-12 10:00:00'),
(2, 1, '模架图', 'v1.0', '/uploads/blueprints/mold001-frame.pdf', '2023-11-12 10:00:00', '2023-11-12 10:00:00'),
(3, 1, '零件图A', 'v1.2', '/uploads/blueprints/mold001-partA.pdf', '2023-11-13 14:00:00', '2023-11-15 10:00:00'),
(4, 1, '零件图B', 'v1.0', '/uploads/blueprints/mold001-partB.pdf', '2023-11-13 14:00:00', '2023-11-13 14:00:00'),
(5, 2, '总装图', 'v1.0', '/uploads/blueprints/mold002-assembly.pdf', '2023-11-16 14:00:00', '2023-11-16 14:00:00'),
(6, 3, '总装图', 'v1.2', '/uploads/blueprints/mold003-assembly.pdf', '2023-10-20 08:00:00', '2023-11-10 09:00:00'),
(7, 3, '模架图', 'v1.1', '/uploads/blueprints/mold003-frame.pdf', '2023-10-20 08:00:00', '2023-11-08 14:00:00');

-- ============================================
-- 插入物料数据
-- ============================================
INSERT INTO material (id, code, name, specification, unit, current_stock, safety_stock, created_at, updated_at) VALUES
(1, 'M001', '钢板', '10mm*1000mm*2000mm', '张', 50, 20, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(2, 'M002', '螺栓', 'M8*50', '个', 15, 50, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(3, 'M003', '弹簧', 'Φ20*100', '个', 200, 100, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(4, 'M004', '轴承', '6205', '个', 80, 30, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(5, 'M005', '铜套', 'Φ30*50', '个', 120, 60, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(6, 'M006', '导柱', 'Φ25*300', '根', 35, 40, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(7, 'M007', '导套', 'Φ25', '个', 90, 50, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(8, 'M008', '顶针', 'Φ10*200', '根', 60, 40, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(9, 'M009', '回位销', 'Φ12*50', '个', 25, 30, '2023-11-01 10:00:00', '2023-11-19 10:00:00'),
(10, 'M010', '润滑油', '桶', 5, 10, '2023-11-01 10:00:00', '2023-11-19 10:00:00');

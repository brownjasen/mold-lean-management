# 模具精益管理系统

一个用于模具生产管理的全栈 Web 应用系统，实现模具生产过程的精益化管理。

## 项目概述

本系统旨在解决模具制造过程中的生产进度跟踪、工序管理、物料管理等核心问题，通过信息化手段提升生产效率和管理水平。

### 核心功能

- **生产总览** - 实时监控所有模具的生产状态和进度
- **模具管理** - 模具全生命周期管理，支持创建、编辑、删除等操作
- **工序管理** - 六大工序模块（模架、三大件、辅料、组装、试模、返修）的进度跟踪
- **图纸管理** - 设计图纸的版本管理和查看
- **物料管理** - 物料库存管理、入库出库操作、库存预警

---

## 技术栈

### 前端 (frontend/)

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 3.3.4 | 渐进式 JavaScript 框架 |
| Vite | 4.4.5 | 新一代前端构建工具 |
| Vue Router | 4.2.4 | 官方路由管理器 |
| Pinia | 2.1.6 | Vue 官方状态管理库 |
| Element Plus | 2.3.8 | Vue 3 组件库 |
| Axios | 1.4.0 | HTTP 客户端 |
| Day.js | 1.11.9 | 轻量级日期处理库 |

### 后端 (backend/)

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 编程语言 |
| Spring Boot | 3.2.0 | 应用框架 |
| Spring Data JPA | - | ORM 持久层框架 |
| MySQL | 8.0+ | 关系型数据库 |
| Maven | - | 项目管理工具 |
| Lombok | - | 简化 Java 代码 |

---

## 项目结构

```
mold-lean-management/
├── frontend/                    # 前端项目
│   ├── src/
│   │   ├── api/                # API 请求封装
│   │   │   └── index.js        # 统一的 API 接口定义
│   │   ├── components/         # 公共组件
│   │   │   ├── BlueprintList.vue       # 图纸列表组件
│   │   │   ├── MoldProgress.vue        # 进度条组件
│   │   │   ├── ProcessList.vue         # 工序表格组件
│   │   │   └── ProcessStatusTag.vue    # 状态标签组件
│   │   ├── views/              # 页面组件
│   │   │   ├── Dashboard.vue          # 生产总览页
│   │   │   ├── MaterialManagement.vue # 物料管理页
│   │   │   └── MoldDetail.vue         # 模具详情页
│   │   ├── router/             # 路由配置
│   │   │   └── index.js
│   │   ├── assets/             # 静态资源
│   │   ├── App.vue             # 根组件
│   │   └── main.js             # 入口文件
│   ├── public/                 # 公共资源
│   ├── index.html              # HTML 模板
│   ├── package.json            # 项目配置
│   └── vite.config.js          # Vite 配置
│
└── backend/                     # 后端项目
    ├── src/main/java/com/mold/
    │   ├── MoldManagementApplication.java    # 启动类
    │   ├── controller/           # 控制器层
    │   │   ├── MoldController.java
    │   │   ├── ProcessController.java
    │   │   ├── BlueprintController.java
    │   │   └── MaterialController.java
    │   ├── service/              # 业务逻辑层
    │   │   ├── MoldService.java
    │   │   ├── ProcessService.java
    │   │   ├── BlueprintService.java
    │   │   └── MaterialService.java
    │   ├── repository/           # 数据访问层
    │   │   ├── MoldRepository.java
    │   │   ├── ProcessRepository.java
    │   │   ├── BlueprintRepository.java
    │   │   └── MaterialRepository.java
    │   ├── entity/               # 实体类
    │   │   ├── Mold.java
    │   │   ├── Process.java
    │   │   ├── Blueprint.java
    │   │   └── Material.java
    │   ├── dto/                  # 数据传输对象
    │   │   ├── ApiResponse.java
    │   │   ├── MoldStatistics.java
    │   │   ├── MaterialStatistics.java
    │   │   └── ...
    │   ├── config/               # 配置类
    │   │   └── CorsConfig.java
    │   └── exception/            # 异常处理
    │       └── GlobalExceptionHandler.java
    ├── src/main/resources/
    │   ├── application.yml      # 应用配置
    │   ├── data.sql             # 初始化数据
    │   └── init.sql             # 建表脚本
    └── pom.xml                  # Maven 配置
```

---

## 环境要求

### 前端环境
- Node.js >= 16.0.0
- npm >= 8.0.0

### 后端环境
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

---

## 快速开始

### 1. 数据库配置

创建数据库并执行初始化脚本：

```sql
CREATE DATABASE mold_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mold_management;

-- 执行 backend/src/main/resources/init.sql 建表
-- 执行 backend/src/main/resources/data.sql 初始化数据
```

修改 `backend/src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mold_management
    username: root
    password: your_password
```

### 2. 启动后端服务

```bash
cd backend
mvn spring-boot:run
```

后端服务启动后访问：http://localhost:8080/api

### 3. 启动前端服务

```bash
cd frontend
npm install
npm run dev
```

前端服务启动后访问：http://localhost:5173

---

## API 接口文档

### 基础信息

- **Base URL**: `http://localhost:8080/api`
- **响应格式**: JSON
- **统一响应结构**:
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 模具接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/molds` | 获取模具列表 |
| GET | `/molds/{id}` | 获取模具详情 |
| GET | `/molds/statistics` | 获取统计数据 |
| POST | `/molds` | 创建模具 |
| PUT | `/molds/{id}` | 更新模具 |
| DELETE | `/molds/{id}` | 删除模具 |
| POST | `/molds/{id}/start` | 开始生产 |
| POST | `/molds/{id}/complete` | 完成生产 |
| PUT | `/molds/{id}/progress` | 更新进度 |

### 工序接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/processes/mold/{moldId}` | 获取模具的工序列表 |
| GET | `/processes/mold/{moldId}/module/{type}` | 获取指定模块的工序 |
| GET | `/processes/{id}` | 获取工序详情 |
| POST | `/processes` | 创建工序 |
| PUT | `/processes/{id}` | 更新工序 |
| DELETE | `/processes/{id}` | 删除工序 |
| POST | `/processes/{id}/start` | 开始工序 |
| POST | `/processes/{id}/complete` | 完成工序 |

### 图纸接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/blueprints/mold/{moldId}` | 获取模具的图纸列表 |
| GET | `/blueprints/{id}` | 获取图纸详情 |
| POST | `/blueprints` | 创建图纸 |
| PUT | `/blueprints/{id}` | 更新图纸 |
| DELETE | `/blueprints/{id}` | 删除图纸 |

### 物料接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/materials` | 获取物料列表 |
| GET | `/materials/{id}` | 获取物料详情 |
| GET | `/materials/statistics` | 获取统计数据 |
| GET | `/materials/search` | 搜索物料 |
| POST | `/materials` | 添加物料 |
| PUT | `/materials/{id}` | 更新物料 |
| DELETE | `/materials/{id}` | 删除物料 |
| POST | `/materials/{id}/in` | 物料入库 |
| POST | `/materials/{id}/out` | 物料出库 |

---

## 开发规范

### 代码规范

#### 前端
- 组件命名采用 PascalCase
- 文件命名采用 PascalCase (组件) 或 kebab-case (工具)
- 使用 Composition API 编写组件
- 样式采用 scoped 避免污染

#### 后端
- 类名采用 PascalCase
- 方法名和变量名采用 camelCase
- 常量名采用 UPPER_SNAKE_CASE
- 遵循 RESTful API 设计规范

### Git 提交规范

```
<type>(<scope>): <subject>

<body>

<footer>
```

**type 类型**:
- `feat`: 新功能
- `fix`: 修复 bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 重构代码
- `test`: 测试相关
- `chore`: 构建/工具变动

**示例**:
```
feat(frontend): 添加物料管理页面

- 实现物料列表展示
- 添加入库/出库功能
- 实现库存预警逻辑

Closes #123
```

---

## 构建部署

### 前端构建

```bash
cd frontend
npm run build
```

构建产物位于 `frontend/dist/` 目录，可部署到 Nginx 或其他静态服务器。

### 后端打包

```bash
cd backend
mvn clean package
```

打包后的 JAR 文件位于 `backend/target/` 目录。

运行 JAR 文件：
```bash
java -jar target/mold-lean-management-1.0.0.jar
```

---

## 常见问题

### 1. 跨域问题

后端已配置 CORS 允许跨域访问，如需修改请编辑 `CorsConfig.java`。

### 2. 数据库连接失败

检查 MySQL 服务是否启动，用户名密码是否正确，数据库是否已创建。

### 3. 前端 API 请求失败

确认后端服务已启动，检查 `frontend/src/api/index.js` 中的 baseURL 配置。

---

## 许可证

[MIT License](LICENSE)

---

## 联系方式

如有问题或建议，请提交 Issue。

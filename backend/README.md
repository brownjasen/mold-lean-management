# 模具精益管理系统 - 后端

基于 Spring Boot + MySQL 的模具精益管理系统后端服务。

## 技术栈

- **Spring Boot** 3.2.0
- **Spring Data JPA** - 数据持久化
- **MySQL** - 数据库
- **Lombok** - 简化代码
- **Maven** - 项目构建

## 项目结构

```
src/main/java/com/mold/
├── config/           # 配置类
│   └── CorsConfig.java
├── controller/       # 控制器层
│   ├── MoldController.java
│   ├── ProcessController.java
│   ├── BlueprintController.java
│   └── MaterialController.java
├── dto/              # 数据传输对象
│   ├── ApiResponse.java
│   ├── MoldStatistics.java
│   ├── MaterialStatistics.java
│   ├── MaterialTransactionRequest.java
│   └── ProcessOperationRequest.java
├── entity/           # 实体类
│   ├── Mold.java
│   ├── Process.java
│   ├── Blueprint.java
│   └── Material.java
├── exception/        # 异常处理
│   └── GlobalExceptionHandler.java
├── repository/       # 数据访问层
│   ├── MoldRepository.java
│   ├── ProcessRepository.java
│   ├── BlueprintRepository.java
│   └── MaterialRepository.java
├── service/          # 服务层
│   ├── MoldService.java
│   ├── ProcessService.java
│   ├── BlueprintService.java
│   └── MaterialService.java
└── MoldManagementApplication.java
```

## 数据库初始化

1. 确保 MySQL 已安装并运行

2. 创建数据库并导入初始数据：
```bash
mysql -u root -p < src/main/resources/data.sql
```

或者手动执行：
```sql
CREATE DATABASE IF NOT EXISTS mold_lean_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 修改数据库配置（`src/main/resources/application.yml`）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mold_lean_management
    username: root
    password: your_password  # 修改为你的密码
```

## 运行项目

### 方式一：使用 Maven
```bash
mvn spring-boot:run
```

### 方式二：使用 IDE
1. 在 IDEA 中打开项目
2. 等待 Maven 依赖下载完成
3. 运行 `MoldManagementApplication.java`

### 方式三：打包运行
```bash
mvn clean package
java -jar target/mold-lean-management-1.0.0.jar
```

启动成功后，访问：`http://localhost:8080/api`

## API 接口

### 模具管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/molds` | 获取模具列表 |
| GET | `/api/molds/{id}` | 获取模具详情 |
| GET | `/api/molds/statistics` | 获取统计数据 |
| POST | `/api/molds` | 创建模具 |
| PUT | `/api/molds/{id}` | 更新模具 |
| DELETE | `/api/molds/{id}` | 删除模具 |
| POST | `/api/molds/{id}/start` | 开始生产 |
| POST | `/api/molds/{id}/complete` | 完成生产 |
| PUT | `/api/molds/{id}/progress` | 更新进度 |

### 工序管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/processes/mold/{moldId}` | 获取模具的工序列表 |
| GET | `/api/processes/{id}` | 获取工序详情 |
| POST | `/api/processes` | 创建工序 |
| PUT | `/api/processes/{id}` | 更新工序 |
| DELETE | `/api/processes/{id}` | 删除工序 |
| POST | `/api/processes/{id}/start` | 开始工序 |
| POST | `/api/processes/{id}/complete` | 完成工序 |

### 图纸管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/blueprints/mold/{moldId}` | 获取模具的图纸列表 |
| GET | `/api/blueprints/{id}` | 获取图纸详情 |
| POST | `/api/blueprints` | 创建图纸 |
| PUT | `/api/blueprints/{id}` | 更新图纸 |
| DELETE | `/api/blueprints/{id}` | 删除图纸 |

### 物料管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/materials` | 获取物料列表 |
| GET | `/api/materials/{id}` | 获取物料详情 |
| GET | `/api/materials/statistics` | 获取统计数据 |
| GET | `/api/materials/search` | 搜索物料 |
| POST | `/api/materials` | 添加物料 |
| PUT | `/api/materials/{id}` | 更新物料 |
| DELETE | `/api/materials/{id}` | 删除物料 |
| POST | `/api/materials/{id}/in` | 物料入库 |
| POST | `/api/materials/{id}/out` | 物料出库 |

## 数据模型

### Mold (模具)
- `id` - 主键
- `moldCode` - 模具编号（唯一）
- `priority` - 优先级
- `status` - 状态 (PENDING/PROCESSING/COMPLETED)
- `progress` - 进度 (0-100)
- `orderTime` - 下单时间
- `startProductionTime` - 开始生产时间
- `completionTime` - 完成时间

### Process (工序)
- `id` - 主键
- `moldId` - 模具ID
- `name` - 工序名称
- `moduleType` - 模块类型 (FRAME/MAIN_PARTS/ACCESSORIES/ASSEMBLY/TESTING/REPAIR)
- `operator` - 负责人
- `status` - 状态 (PENDING/PROCESSING/COMPLETED)
- `startTime` - 开始时间
- `endTime` - 结束时间

### Blueprint (图纸)
- `id` - 主键
- `moldId` - 模具ID
- `name` - 图纸名称
- `version` - 版本号
- `url` - 文件路径

### Material (物料)
- `id` - 主键
- `code` - 物料编号（唯一）
- `name` - 物料名称
- `specification` - 规格
- `unit` - 单位
- `currentStock` - 当前库存
- `safetyStock` - 安全库存

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

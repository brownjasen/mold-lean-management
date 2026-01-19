# Azure 部署指南

本指南说明如何将模具精益管理系统部署到 Azure（使用学生认证免费额度）。

---

## 部署架构

```
                    ┌─────────────────────────────────┐
                    │      Azure DNS (自定义域名)      │
                    └──────────────┬──────────────────┘
                                   │
                    ┌──────────────┴──────────────────┐
                    │                                 │
         ┌──────────▼──────────┐        ┌───────────▼──────────┐
         │ Static Web Apps     │        │  App Service          │
         │ (前端 Vue3)         │◄──────►│  (后端 Spring Boot)  │
         │ 免费: 100GB/月      │        │  B1 基本层 (~$25/月)   │
         └─────────────────────┘        └───────────┬──────────┘
                                                   │
                                                   │
                                    ┌──────────────▼─────────────┐
                                    │ Azure Database for MySQL   │
                                    │ 灵活服务器 - 无计算层        │
                                    │ (~$10-20/月)               │
                                    └────────────────────────────┘
```

---

## 第一步：创建 MySQL 数据库

### 1.1 在 Azure Portal 创建 MySQL 服务器

1. 登录 [Azure Portal](https://portal.azure.com)
2. 搜索 "Azure Database for MySQL Flexible Server"
3. 点击 **创建**

**配置：**
| 配置项 | 值 |
|--------|-----|
| 资源组 | `mold-management-rg` (新建) |
| 服务器名称 | `mold-mysql-xxx` (全局唯一) |
| 区域 | East Asia (香港) 或 Southeast Asia (新加坡) |
| MySQL 版本 | 8.0 |
| 计算层 | **无计算** (节省费用) |
| 存储大小 | 32 GB |
| 管理员用户名 | `moldadmin` |
| 密码 | 设置强密码 |

**网络配置：**
- 勾选 "允许从任何 Azure 服务访问此服务器"
- 勾选 "允许公共访问"

### 1.2 创建数据库并初始化

1. 在门户中进入 MySQL 服务器 → **数据库**
2. 点击 **创建**，数据库名：`mold_management`
3. 连接到数据库（使用 Azure Cloud Shell 或本地 MySQL Workbench）
4. 执行初始化脚本：
   - `backend/src/main/resources/init.sql`
   - `backend/src/main/resources/data.sql`

**连接字符串格式：**
```
jdbc:mysql://mold-mysql-xxx.mysql.database.azure.com:3306/mold_management?ssl-mode=required
```

---

## 第二步：部署后端到 App Service

### 2.1 打包应用

```bash
cd backend
mvn clean package -DskipTests
```

生成文件：`target/mold-lean-management-1.0.0.jar`

### 2.2 创建 App Service

1. 在 Azure Portal 搜索 "App Service"
2. 点击 **创建**

**配置：**
| 配置项 | 值 |
|--------|-----|
| 资源组 | `mold-management-rg` |
| 应用名称 | `mold-backend-api` (全局唯一) |
| 发布 | **代码** |
| 运行时堆栈 | Java 17 |
| Java Web 服务器 | **内置 Tomcat** (或嵌入服务器) |
| 操作系统 | Linux |
| 区域 | 与数据库相同 |
| 定价计划 | **基本 B1** (或开发/免费 F1) |

### 2.3 配置环境变量

进入 App Service → **环境变量**，添加以下配置：

| 名称 | 值 |
|------|-----|
| `AZURE_MYSQL_URL` | `jdbc:mysql://mold-mysql-xxx.mysql.database.azure.com:3306/mold_management?useSSL=true&requireSSL=true&serverTimezone=Asia/Shanghai` |
| `AZURE_MYSQL_USERNAME` | `moldadmin@mold-mysql-xxx` |
| `AZURE_MYSQL_PASSWORD` | 你的密码 |

### 2.4 部署 JAR 文件

**方式一：通过 Azure CLI**
```bash
az webapp deploy --resource-group mold-management-rg \
  --name mold-backend-api \
  --src-path target/mold-lean-management-1.0.0.jar \
  --type jar
```

**方式二：通过 FTP / VS Code**
1. 在 App Service 中获取 FTP 凭据
2. 上传 JAR 文件到 `/site/wwwroot/`
3. 重启服务

**方式三：从 GitHub 自动部署**
1. 在 App Service → **部署中心**
2. 连接 GitHub 仓库
3. 选择 `backend` 文件夹
4. 配置构建命令：`mvn clean package -DskipTests`

### 2.5 配置启动命令

进入 App Service → **配置** → **常规设置**：

| 设置 | 值 |
|------|-----|
| 启动命令 | `java -jar target/mold-lean-management-1.0.0.jar --spring.profiles.active=azure` |

### 2.6 配置 CORS（如果需要）

在 `CorsConfig.java` 中已配置，但生产环境建议指定具体域名：
```java
.addCorsConfiguration("corsConfiguration", config
    .setAllowedOrigins(Arrays.asList("https://your-frontend-app.azurestaticapps.net"))
```

---

## 第三步：部署前端到 Static Web Apps

### 3.1 创建 Static Web Apps

1. 在 Azure Portal 搜索 "Static Web Apps"
2. 点击 **创建**

**配置：**
| 配置项 | 值 |
|--------|-----|
| 资源组 | `mold-management-rg` |
| 应用名称 | `mold-frontend` (全局唯一) |
| 区域 | East Asia |
| SKU | **免费** |
| 部署源 | GitHub |

### 3.2 关联 GitHub 仓库

1. 点击 "用 GitHub 登录"
2. 选择你的仓库
3. 配置构建设置：

| 设置 | 值 |
|------|-----|
| 应用位置 | `/` |
| API 位置 | 留空 |
| 构建输出位置 | `dist` |
| 构建命令 | `cd frontend && npm install && npm run build` |

### 3.3 配置环境变量

在 Static Web Apps → **环境变量** 添加：

```
VITE_API_BASE_URL = https://mold-backend-api.azurewebsites.net/api
```

### 3.4 部署

推送代码到 GitHub，Azure 会自动部署。
首次部署约需 2-3 分钟。

---

## 第四步：配置自定义域名（可选）

### 4.1 购买域名
- 在 Azure 或其他域名注册商购买域名（如 `moldapp.com`）

### 4.2 配置 DNS
```
CNAME 前端: moldapp.com → your-frontend-app.azurestaticapps.net
A    后端: api.moldapp.com → 后端 IP
```

---

## 成本估算（学生账户）

| 服务 | 规格 | 月费用 |
|------|------|--------|
| Static Web Apps | 免费层 | $0 |
| App Service | B1 基本层 | ~$25-30 |
| MySQL Flexible | 无计算层 + 32GB | ~$10-20 |
| 带宽 | 包含 100GB/月 | $0 |
| **合计** | | **~$35-50/月** |

节省技巧：
- 开发测试使用 App Service **免费 F1 层**（功能有限）
- MySQL 使用**无计算层**（按存储付费）
- 关闭不用的服务

---

## 验证部署

### 检查后端
```bash
curl https://mold-backend-api.azurewebsites.net/api/molds
```

### 检查前端
访问：`https://your-frontend-app.azurestaticapps.net`

---

## 故障排查

### 后端启动失败
1. 查看 App Service 日志：**日志流**
2. 检查环境变量是否正确配置
3. 确认数据库连接

### 前端无法访问 API
1. 检查 `VITE_API_BASE_URL` 是否正确
2. 确认 CORS 配置
3. 使用浏览器开发工具查看请求

### 数据库连接失败
1. 检查 Azure 防火墙规则
2. 确认用户名格式：`username@servername`
3. 验证 SSL 设置

---

## 下一步

- [ ] 配置 CI/CD 自动部署
- [ ] 设置 Application Insights 监控
- [ ] 配置备份和恢复
- [ ] 设置 Azure DevOps 管道

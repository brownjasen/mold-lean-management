# GitHub 部署指南

本文档说明如何将项目代码托管到 GitHub，并使用 GitHub Pages 免费部署前端。

---

## 部署方案说明

```
┌─────────────────────────────────────────────────────────┐
│                    GitHub 仓库                          │
│  (代码托管 + Issue + PR)                                │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
        ▼                         ▼
┌───────────────┐         ┌──────────────┐
│ GitHub Pages  │         │   后端需要    │
│  (前端免费)   │◄──────►│  其他部署     │
│              │         │  (见下方)     │
│ xxx.github.io│         └──────────────┘
└───────────────┘
```

**关键点：**
- ✅ **前端** → GitHub Pages（免费）
- ❌ **后端** → 需要 Render/Railway/Fly.io 等免费云服务
- ❌ **数据库** → 需要 Supabase/PlanetScale 等免费云数据库

---

## 第一步：创建 GitHub 仓库

### 1.1 在 GitHub 创建新仓库

1. 访问 https://github.com/new
2. 填写仓库信息：
   - **Repository name**: `mold-lean-management`（或任意名称）
   - **Description**: 模具精益管理系统
   - **Visibility**: Public（免费）或 Private
   - **不要**勾选 "Add a README file"
   - **不要**勾选 ".gitignore"

3. 点击 **Create repository**

### 1.2 关联本地仓库

```bash
cd E:\vue

# 添加远程仓库（替换 YOUR_USERNAME）
git remote add origin https://github.com/YOUR_USERNAME/mold-lean-management.git

# 推送到 GitHub
git branch -M main
git push -u origin main
```

---

## 第二步：配置 GitHub Pages（前端部署）

### 2.1 启用 GitHub Pages

1. 进入 GitHub 仓库
2. 点击 **Settings** → **Pages**
3. **Source** 选择：`GitHub Actions`

### 2.2 配置环境变量

在仓库中设置后端 API 地址：

1. **Settings** → **Secrets and variables** → **Actions**
2. 点击 **New repository variable**
3. 添加变量：

```
Name: API_BASE_URL
Value: https://your-backend-service.com/api
```

### 2.3 触发部署

推送代码后会自动触发部署：

```bash
# 任意修改后推送
git add .
git commit -m "Update deployment"
git push
```

### 2.4 访问前端

部署完成后（约 1-2 分钟），访问：

```
https://YOUR_USERNAME.github.io/mold-lean-management/
```

---

## 第三步：后端部署（免费云服务）

GitHub Pages 只能托管静态文件，后端需要其他服务。

### 选项一：Render（推荐）

| 服务 | 免费额度 |
|------|----------|
| Web Service | 750 小时/月（自动休眠） |
| PostgreSQL | 1GB 数据库 |

**步骤：**
1. 访问 https://render.com
2. 连接 GitHub 账户
3. **New** → **Web Service**
4. 选择 `backend` 文件夹
5. 配置：
   - **Runtime**: Java 17
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/mold-lean-management-1.0.0.jar`
6. 添加环境变量（数据库连接）

### 选项二：Railway

| 服务 | 免费额度 |
|------|----------|
| 服务 | $5/月 免费额度 |
| MySQL | 包含在内 |

**步骤：**
1. 访问 https://railway.app
2. **New Project** → **Deploy from GitHub repo**
3. 选择仓库后自动检测 Java 项目
4. 添加 MySQL 插件
5. 配置环境变量

### 选项三：Fly.io

| 服务 | 免费额度 |
|------|----------|
| 应用 | 3 个小型 VM |
| 存储 | 1GB |

**步骤：**
```bash
# 安装 Fly CLI
curl -L https://fly.io/install.sh | sh

# 登录并部署
fly launch
fly deploy
```

---

## 第四步：数据库部署（免费云数据库）

### 选项一：Supabase（推荐）

- ✅ 免费 500MB
- ✅ PostgreSQL 兼容
- ✅ 实时功能
- 访问：https://supabase.com

### 选项二：PlanetScale

- ✅ 免费 5GB
- ✅ MySQL 兼容
- ✅ 分支功能
- 访问：https://planetscale.com

### 选项三：Render PostgreSQL

- ✅ 免费 90 天
- ✅ 随 Render Web Service 附带
- 访问：https://render.com

---

## 免费部署架构

```
┌──────────────────────────────────────────────────────────┐
│                      GitHub                               │
│  ┌───────────────────────────────────────────────────┐  │
│  │              代码仓库                               │  │
│  └───────────────────────────────────────────────────┘  │
└────────────────────┬──────────────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
        ▼                         ▼
┌─────────────────┐      ┌─────────────────┐
│ GitHub Pages    │      │   Render /      │
│  (前端免费)     │◄────►│  Railway (后端) │
│                 │      │   (免费层)      │
└─────────────────┘      └────────┬────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │  Supabase /     │
                         │  PlanetScale    │
                         │  (数据库免费)   │
                         └─────────────────┘
```

---

## 完整免费部署方案

| 组件 | 服务 | 费用 | 限制 |
|------|------|------|------|
| 前端 | GitHub Pages | 免费 | 仅静态文件 |
| 后端 | Render / Railway | 免费 | 休眠/流量限制 |
| 数据库 | Supabase / PlanetScale | 免费 | 500MB-5GB |
| 代码仓库 | GitHub | 免费 | 公开仓库 |

**总计：$0/月**

---

## 域名配置（可选）

### 自定义域名到 GitHub Pages

1. **Settings** → **Pages** → **Custom domain**
2. 输入你的域名（如 `moldapp.com`）
3. 在域名提供商添加 CNAME 记录：
   ```
   moldapp.com → YOUR_USERNAME.github.io
   ```

---

## 常见问题

### Q: GitHub Pages 部署失败？
**A:** 检查 `.github/workflows/deploy.yml` 文件是否存在，查看 Actions 页面的错误日志。

### Q: 前端页面空白？
**A:** 检查：
1. 浏览器控制台错误
2. API 地址是否正确配置
3. 后端服务是否正常运行

### Q: 后端自动休眠怎么办？
**A:** 免费服务会自动休眠，首次请求需等待 30-60 秒唤醒。可使用服务监控工具定期 ping 保持活跃。

---

## 下一步

- [ ] 推送代码到 GitHub
- [ ] 启用 GitHub Pages
- [ ] 在 Render/Railway 部署后端
- [ ] 在 Supabase 创建数据库
- [ ] 更新前端 API 地址

# Railway 部署指南

## 🚀 快速部署（5分钟）

### 1. 登录 Railway

访问 https://railway.app，点击 **Login** → **Sign in with GitHub**

---

### 2. 创建新项目

1. 点击 **New Project** → **Deploy from GitHub repo**
2. 选择 **brownjasen/mold-lean-management** 仓库
3. 点击 **Deploy**

---

### 3. 添加 MySQL 数据库

部署完成后（或部署中）：

1. 在项目页面点击 **New** → **Database**
2. 选择 **MySQL** → **Add MySQL**
3. Railway 会自动创建数据库

---

### 4. 配置环境变量

在后端服务中添加环境变量：

1. 点击后端服务 **mold-lean-management**
2. 进入 **Variables** 标签
3. 添加以下变量：

| 变量名 | 值 |
|--------|-----|
| `SPRING_DATASOURCE_URL` | 点击 MySQL 旁边的 **Connect** 按钮自动获取 |
| `SPRING_DATASOURCE_USERNAME` | 自动获取 |
| `SPRING_DATASOURCE_PASSWORD` | 自动获取 |

**或者** 使用 Railway 的 **Ref** 功能：
```
SPRING_DATASOURCE_URL = ${MYSQLDATABASE_URL}
SPRING_DATASOURCE_USERNAME = ${MYSQLDATABASE_USERNAME}
SPRING_DATASOURCE_PASSWORD = ${MYSQLDATABASE_PASSWORD}
```

---

### 5. 重新部署

配置完环境变量后：

1. 点击 **Deploy** 按钮
2. 等待 3-5 分钟部署完成

---

### 6. 获取后端地址

部署成功后：

1. 点击后端服务
2. 在顶部找到 **域名**（如 `mold-lean-management.up.railway.app`）
3. 复制这个地址

---

### 7. 更新前端 API 地址

回到 GitHub 仓库，修改前端配置：

1. 访问 https://github.com/brownjasen/mold-lean-management/settings/secrets/actions
2. 添加新的变量：

```
Name: API_BASE_URL
Value: https://你的后端地址.up.railway.app/api
```

3. 触发新的部署（在 Actions 页面点击 **Re-run**）

---

## 📋 检查清单

- [ ] Railway 登录并授权 GitHub
- [ ] 从 GitHub 部署项目
- [ ] 添加 MySQL 数据库
- [ ] 配置数据库环境变量
- [ ] 重新部署后端
- [ ] 获取后端地址
- [ ] 更新前端 API 地址

---

## 🎯 完成后

访问：https://brownjasen.github.io/mold-lean-management/

你应该能看到完整的模具管理系统界面和数据！

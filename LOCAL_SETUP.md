# 本地开发运行指南

本文档详细说明如何在本地运行模具精益管理系统。

---

## 环境准备

### 必需软件

| 软件 | 版本要求 | 下载地址 |
|------|----------|----------|
| JDK | 17+ | https://adoptium.net/ |
| MySQL | 8.0+ | https://dev.mysql.com/downloads/mysql/ |
| Node.js | 16+ | https://nodejs.org/ |
| Maven | 3.6+ | https://maven.apache.org/download.cgi |

### 可选工具

| 工具 | 用途 |
|------|------|
| MySQL Workbench | 数据库可视化管理 |
| Navicat | 数据库可视化管理 |
| VS Code | 前端开发 |

---

## 第一步：数据库配置

### 1.1 启动 MySQL 服务

**Windows:**
```bash
net start mysql80
```

**macOS/Linux:**
```bash
sudo systemctl start mysql
# 或
sudo service mysql start
```

### 1.2 创建数据库

使用命令行或 MySQL Workbench 执行：

```sql
CREATE DATABASE mold_lean_management
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

### 1.3 执行初始化脚本

```bash
# 方式一：命令行
mysql -u root -p mold_lean_management < backend/src/main/resources/init.sql
mysql -u root -p mold_lean_management < backend/src/main/resources/data.sql
mysql -u root -p mold_lean_management < backend/src/main/resources/insert_material.sql
mysql -u root -p mold_lean_management < backend/src/main/resources/insert_mold_4_5.sql

# 方式二：MySQL Workbench
# 打开 SQL 文件，复制粘贴执行
```

### 1.4 验证数据库

```sql
USE mold_lean_management;
SHOW TABLES;
-- 应该看到：mold, process, blueprint, material 等表
```

---

## 第二步：启动后端服务

### 2.1 检查数据库配置

查看 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mold_lean_management
    username: root      # 你的 MySQL 用户名
    password: root      # 你的 MySQL 密码
```

如果用户名或密码不同，请修改此文件。

### 2.2 启动后端

**方式一：使用 Maven（推荐）**

```bash
cd backend
mvn spring-boot:run
```

**方式二：使用 IDE**

1. 用 IntelliJ IDEA 打开 `backend` 目录
2. 找到 `MoldManagementApplication.java`
3. 右键 → Run 'MoldManagementApplication'

### 2.3 验证后端启动

看到以下输出表示启动成功：

```
=================================
模具精益管理系统后端启动成功！
访问地址: http://localhost:8080/api
=================================
```

**测试 API：**
```bash
curl http://localhost:8080/api/molds
```

或浏览器访问：http://localhost:8080/api/molds

---

## 第三步：启动前端服务

### 3.1 安装依赖

```bash
cd frontend
npm install
```

如果安装缓慢，可使用国内镜像：
```bash
npm install --registry=https://registry.npmmirror.com
```

### 3.2 启动开发服务器

```bash
npm run dev
```

### 3.4 访问前端

浏览器打开：http://localhost:5173

---

## 完整启动流程（一键启动）

### Windows (批处理文件)

创建 `start.bat`：

```batch
@echo off
echo ========================================
echo    模具精益管理系统 - 本地启动脚本
echo ========================================

echo.
echo [1/3] 检查 MySQL 服务...
net start | findstr /i "mysql" >nul
if errorlevel 1 (
    echo MySQL 未启动，正在启动...
    net start mysql80
) else (
    echo MySQL 已运行
)

echo.
echo [2/3] 启动后端服务...
cd backend
start "Mold Backend" cmd /k "mvn spring-boot:run"
cd ..

echo 等待后端启动...
timeout /t 15 /nobreak

echo.
echo [3/3] 启动前端服务...
cd frontend
start "Mold Frontend" cmd /k "npm run dev"
cd ..

echo.
echo ========================================
echo   启动完成！
echo   前端: http://localhost:5173
echo   后端: http://localhost:8080/api
echo ========================================
pause
```

### macOS/Linux (Shell 脚本)

创建 `start.sh`：

```bash
#!/bin/bash

echo "========================================"
echo "   模具精益管理系统 - 本地启动脚本"
echo "========================================"

echo ""
echo "[1/3] 检查 MySQL 服务..."
if pgrep -x "mysqld" > /dev/null; then
    echo "MySQL 已运行"
else
    echo "启动 MySQL..."
    sudo systemctl start mysql
fi

echo ""
echo "[2/3] 启动后端服务..."
cd backend
osascript -e 'tell app "Terminal" to do script "cd '"$(pwd)"' && mvn spring-boot:run"' 2>/dev/null || \
gnome-terminal -- bash -c "mvn spring-boot:run; exec bash" 2>/dev/null || \
xterm -e "mvn spring-boot:run" &

echo "等待后端启动..."
sleep 15

echo ""
echo "[3/3] 启动前端服务..."
cd ../frontend
osascript -e 'tell app "Terminal" to do script "cd '"$(pwd)"' && npm run dev"' 2>/dev/null || \
gnome-terminal -- bash -c "npm run dev; exec bash" 2>/dev/null || \
xterm -e "npm run dev" &

echo ""
echo "========================================"
echo "  启动完成！"
echo "  前端: http://localhost:5173"
echo "  后端: http://localhost:8080/api"
echo "========================================"
```

---

## 常见问题

### 后端启动失败

**问题：** `Communications link failure`

**解决：**
1. 确认 MySQL 服务已启动
2. 检查用户名密码是否正确
3. 确认数据库名称 `mold_lean_management` 存在

---

**问题：** `Unknown database 'mold_lean_management'`

**解决：**
```sql
-- 重新创建数据库
CREATE DATABASE mold_lean_management;
-- 并执行所有初始化脚本
```

---

### 前端启动失败

**问题：** `Cannot find module 'xxx'`

**解决：**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

---

**问题：** 前端页面空白

**解决：**
1. 按 F12 打开浏览器控制台查看错误
2. 确认后端已启动 (访问 http://localhost:8080/api/molds)
3. 检查 API 地址配置

---

### 端口冲突

**8080 端口被占用：**
```bash
# Windows 查找占用进程
netstat -ano | findstr :8080
taskkill /PID <进程ID> /F

# macOS/Linux
lsof -i :8080
kill -9 <进程ID>
```

**5173 端口被占用：**
```bash
# 前端会自动使用下一个可用端口
# 注意查看终端输出的实际地址
```

---

## 开发调试

### 前端调试

- VS Code 安装插件：`Vue - Official`
- 浏览器按 F12 打开开发者工具
- Vue DevTools 扩展

### 后端调试

- IntelliJ IDEA 内置调试器
- 在代码行号处打断点
- Debug 模式启动 `MoldManagementApplication`

---

## 停止服务

**手动停止：**
- 关闭后端/终端窗口
- 或按 `Ctrl + C`

**停止 MySQL (Windows)：**
```bash
net stop mysql80
```

**停止 MySQL (macOS/Linux)：**
```bash
sudo systemctl stop mysql
```

---

## 下一步

运行成功后，你可以：
- 查看 [README.md](README.md) 了解项目结构
- 查看 [API 文档](#api-接口文档) 开发新功能
- 修改代码并实时预览（前端热更新）

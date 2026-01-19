<script setup>
</script>

<template>
  <div id="app">
    <el-container>
      <el-header class="app-header">
        <div class="header-content">
          <div class="logo-section">
            <div class="logo-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <h1>模具精益管理系统</h1>
          </div>
          <el-menu
            mode="horizontal"
            :default-active="$route.path"
            router
            class="nav-menu">
            <el-menu-item index="/">
              生产总览
            </el-menu-item>
            <el-menu-item index="/material">
              物料管理
            </el-menu-item>
          </el-menu>
        </div>
      </el-header>
      <el-main class="app-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
  background: #f5f7fa;
  width: 100%;
  overflow-x: hidden;
}

.el-container {
  height: 100%;
  flex-direction: column;
  width: 100% !important;
  max-width: 100% !important;
}

.app-header {
  background: #ffffff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0;
  height: 64px !important;
  flex-shrink: 0;
  width: 100% !important;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 32px;
  width: 100%;
  max-width: 1600px;
  margin: 0 auto;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  color: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-icon svg {
  width: 100%;
  height: 100%;
}

.logo-section h1 {
  color: #1f2937;
  font-size: 20px;
  font-weight: 600;
  letter-spacing: -0.02em;
}

.nav-menu {
  background: transparent;
  border: none;
  flex: 1;
  margin-left: 60px;
}

.nav-menu .el-menu-item {
  color: #6b7280;
  font-weight: 500;
  font-size: 15px;
  border-bottom: 2px solid transparent;
  margin: 0 8px;
  padding: 0 20px;
  height: 64px;
  line-height: 64px;
  transition: all 0.2s;
}

.nav-menu .el-menu-item:hover {
  color: #409eff;
  background: transparent;
}

.nav-menu .el-menu-item.is-active {
  color: #409eff;
  background: transparent;
  border-bottom-color: #409eff;
}

.app-main {
  background-color: #f5f7fa;
  padding: 24px;
  overflow-y: auto;
  flex: 1;
  width: 100% !important;
  max-width: 100% !important;
}
</style>

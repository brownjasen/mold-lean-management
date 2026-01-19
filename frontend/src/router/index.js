// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'

const routes = [
    {
        path: '/',
        name: 'dashboard',
        component: Dashboard
    },
    {
        path: '/mold/:id',
        name: 'moldDetail',
        component: () => import('../views/MoldDetail.vue')
    },
    {
        path: '/material',
        name: 'materialManagement',
        component: () => import('../views/MaterialManagement.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
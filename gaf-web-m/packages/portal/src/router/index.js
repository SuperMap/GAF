import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

export const routes = [
  {
    key: 'home',
    title: '主页',
    path: '/',
    name: 'home',
    component: () => import('../pages/Home.vue')
  },
  {
    key: 'login',
    title: '登录',
    path: '/login',
    name: 'login',
    component: () => import('../pages/Login.vue')
  },
  {
    key: 'customzation',
    title: '门户定制',
    path: '/customzation',
    name: 'customzation',
    component: () => import('../pages/Customzation.vue')
  },
  {
    key: 'apps-log1',
    title: 'log 主页',
    path: '/log/'
  },
  {
    key: 'apps-log2',
    title: 'log 列表页',
    path: '/log/table'
  },
  {
    key: 'apps-config1',
    title: 'config 主页',
    path: '/config/'
  },
  {
    key: 'apps-config2',
    title: 'config 列表页',
    path: '/config/table'
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router

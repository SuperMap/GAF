import Home from '../pages/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import(/* webpackChunkName: "about" */ '../pages/Login.vue')
  },
  {
    path: '/table',
    name: 'Table',
    component: () => import(/* webpackChunkName: "about" */ '../pages/Table.vue')
  },
  {
    path: '/SysDics',
    name: 'Dics',
    component: () => import(/* webpackChunkName: "about" */ '../pages/dics/index.vue')
  },
  {
    path: '/SysResourceDatasource',
    name: 'SysResourceDatasource',
    component: () => import(/* webpackChunkName: "about" */ '../pages/SysResourceDatasource/index.vue')
  },
  {
    path: '/StorageManagement',
    name: 'StorageManagement',
    component: () => import(/* webpackChunkName: "about" */ '../pages/StorageManagement/index.vue')
  }
]

export default routes

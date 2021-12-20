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
    path: '/WebgisButton',
    name: 'WebgisButton',
    component: () => import('../pages/WebgisButton/index.vue')
  },
  {
    path: '/WebgisCatalogLayer',
    name: 'WebgisCatalogLayer',
    component: () => import('../pages/WebgisCatalogLayer/index.vue')
  },
  {
    path: '/WebgisResourceCatalog',
    name: 'WebgisResourceCatalog',
    component: () => import('../pages/WebgisResourceCatalog/index.vue')
  },
  {
    path: '/WebgisService',
    name: 'WebgisService',
    component: () => import('../pages/WebgisService/index.vue')
  },
  {
    path: '/WebgisToolbar',
    name: 'WebgisToolbar',
    component: () => import('../pages/WebgisToolbar/index.vue')
  },
  {
    path: '/WebgisToolbarButton',
    name: 'WebgisToolbarButton',
    component: () => import('../pages/WebgisToolbarButton/index.vue')
  }
  
]

export default routes

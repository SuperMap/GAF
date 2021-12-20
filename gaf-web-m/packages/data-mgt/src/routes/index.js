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
    path: '/dataWorkspace',
    name: 'DataWorkspace',
    component: () => import(/* webpackChunkName: "about" */ '../pages/DataWorkspace/index.vue')
  }
]

export default routes

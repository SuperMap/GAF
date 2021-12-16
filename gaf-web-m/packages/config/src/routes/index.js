import Home from '../pages/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/table',
    name: 'Table',
    component: () => import(/* webpackChunkName: "about" */ '../pages/Table.vue')
  }
]

export default routes

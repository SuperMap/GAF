import Vue from 'vue'
import App from './App.vue'
import router from './router'
import startQiankun from './micro'
import store from './store'
import axios from './axios'
import GafUI from '@gaf/ui'
import antd from 'ant-design-vue'
import './assets/theme/theme.less'

Vue.use(GafUI)
Vue.use(antd)

Vue.prototype.$axios = axios
Vue.config.productionTip = false

startQiankun()

new Vue({
  store,
  router,
  render: h => h(App)
}).$mount('#app')

import Vue from 'vue'
import App from './App.vue'
import routes from './routes'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import VueRouter from 'vue-router'
import './public-path'
import store from './store'
import axios from './axios'

Vue.prototype.$axios = axios
Vue.config.productionTip = false

Vue.use(Antd)
Vue.use(VueRouter)

let instance = null
let router = null

function render (props = {}) {
  // 在 render 中创建 VueRouter，可以保证在卸载微应用时，移除 location 事件监听，防止事件污染
  router = new VueRouter({
    base: window.__POWERED_BY_QIANKUN__ ? '/log/' : '/apps-log/',
    mode: 'history',
    routes
  })
  const { container } = props
  instance = new Vue({
    store,
    router,
    render: (h) => h(App)
  }).$mount(container ? container.querySelector('#app') : '#app')
}

// 独立运行时，直接挂载应用
if (!window.__POWERED_BY_QIANKUN__) {
  render()
}

/**
 * bootstrap 只会在微应用初始化的时候调用一次，下次微应用重新进入时会直接调用 mount 钩子，不会再重复触发 bootstrap。
 * 通常我们可以在这里做一些全局变量的初始化，比如不会在 unmount 阶段被销毁的应用级别的缓存等。
 */
export async function bootstrap () {}

/**
 * 应用每次进入都会调用mount方法，通常我们在这里触发应用的渲染方法
 */
export async function mount (props) {
  render(props)
}

/**
 * 应用每次卸载时会调用的方法，通常在这里我们会卸载微应用的应用实例
 */
export async function unmount () {
  instance.$destroy()
  instance = null
  router = null
}

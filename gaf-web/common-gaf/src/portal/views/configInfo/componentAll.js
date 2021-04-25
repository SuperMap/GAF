/**
 * Created by huanl on 2019/11/5
 */
import Vue from 'vue'

const extendCtrls = []
const requireCtrls = require.context('./controls', false, /\.vue$/)
const requireCtrlsExtend = require.context('./extendControls', false, /\.vue$/)

requireCtrls.keys().forEach((fileName) => {
  const config = requireCtrls(fileName)
  const component = config.default || config
  Vue.component(component.name, component)
})

requireCtrlsExtend.keys().forEach((fileName) => {
  const config = requireCtrlsExtend(fileName)
  const component = config.default || config
  extendCtrls.push({
    name: component.name,
    icon: component.icon || 'ellipsis',
    title: component.title || component.name,
  })
  Vue.component(component.name, component)
})

export default extendCtrls

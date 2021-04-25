/**
 * Created by huanl on 2020/11/12
 */
import Vue from 'vue'

const requireCtrls = require.context('./controls', false, /\.vue$/)

requireCtrls.keys().forEach((fileName) => {
  const config = requireCtrls(fileName)
  const component = config.default || config
  Vue.component(component.name, component)
})

/**
 * Created by huanl on 2019/7/15
 */
import Vue from 'vue'
import AntUI from 'ant-design-vue/lib'
import VueiClient from '@supermap/vue-iclient3d-webgl'
// import MonacoWebpackPlugin from 'monaco-editor-webpack-plugin'
import monacoeditor from '../components/Monaco/MonacoEditor'
export default () => {
  Vue.use(AntUI)
  Vue.use(VueiClient)
  // Vue.use(MonacoWebpackPlugin)
  Vue.use(monacoeditor)
}

/**
 * Created by huanl on 2019/7/23
 */
import Vue from 'vue'
import echarts from 'echarts'
import { axios } from 'gaf-ui'

import gafUI from './gaf-ui'
import antUI from './ant-ui'
gafUI()
antUI()

Vue.prototype.$echarts = echarts

export default axios

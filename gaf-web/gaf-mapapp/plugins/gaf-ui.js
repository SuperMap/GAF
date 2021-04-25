/**
 * Created by huanl on 2019/7/15
 */
import Vue from 'vue'
import GafUI from 'gaf-ui'
import GafMapUi from 'gaf-map-ui'
import SlideVerify from 'vue-monoplasty-slide-verify'
Vue.prototype.$mapActions = GafMapUi.mapActions

export default () => {
  Vue.use(GafUI)
  Vue.use(SlideVerify)
  Vue.use(GafMapUi)
}

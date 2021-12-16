import Vue from 'vue'
import Vuex from 'vuex'
import { state, getters, mutations, actions } from '@gaf/ui'
Vue.use(Vuex)

const store = new Vuex.Store({
  state,
  getters,
  mutations,
  actions
})

export default store

// index state
import { mutations as initMutations } from 'gaf-ui'
import { state as initState } from 'gaf-ui'
export { getters, actions } from 'gaf-ui'

const defaultState = initState()
export const state = () => ({
  ...defaultState,
  config: {
    ...defaultState.config,
    profileUrl: '#/users/profile',
    logoutUrl: '/logout',
    helpUrl: 'http://doc.gaf.net.cn',
  },
})
export const mutations = {
  ...initMutations,
  setUserInfo(state, userInfo) {
    state.userInfo = userInfo
  },
  setMenuInfo(state, menuInfo) {
    state.menuInfo = menuInfo
  },
  setToolsContent(state, { commonTools, businessTools }) {
    state.commonTools = commonTools
    state.businessTools = businessTools
  },
  setViewerLoaded(state, loaded) {
    state.loaded = loaded
  },
  setlegendList(state, legendList) {
    state.legendList = legendList
  },
}

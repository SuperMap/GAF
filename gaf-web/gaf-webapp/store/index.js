// index state
import { state as initState } from 'gaf-ui'
export { getters, mutations, actions } from 'gaf-ui'

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

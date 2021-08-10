import { ConfigServerApi } from '../api/configServer'
export default ({ $axios }, inject) => {
  inject('configServerApi', ConfigServerApi($axios))
}

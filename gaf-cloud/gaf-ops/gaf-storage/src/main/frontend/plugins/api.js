import api from '~/api'

export default ({ $axios }, inject) => {
  inject('api', api($axios))
}

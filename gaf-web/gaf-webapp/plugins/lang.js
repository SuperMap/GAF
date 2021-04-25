import lang from '~/lang'

export default ({ store }, inject) => {
  inject('lang', lang(store.state.config.lang || {}))
}

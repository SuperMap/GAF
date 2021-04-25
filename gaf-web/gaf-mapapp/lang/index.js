/**
 * 此处注入组件需要的国际化资源，可以通过state.config.lang属性控制不同语言
 * 要求此处return对象的key值命名尽量保持简洁。
 * 具体可参考gaf-web-datacenter组件的lang目录index.js文件
 * 使用方式：在每一个vue组件实例中通过
 * this.$api[key]。此处的key是return对象的key
 */
export default (lang) => {
  return {}
}

/**
 * 此处注入所有跟服务端需要交互的api接口，与服务端rest资源层接口保持一致
 * 要求此处return对象的key值命名尽量保持简洁。
 * 具体可参考gaf-web-datacenter组件的api目录index.js文件
 * 使用方式：在每一个vue组件实例中通过
 * this.$api[key]。此处的key是return对象的key
 */

export default ($axios) => {
  return {}
}

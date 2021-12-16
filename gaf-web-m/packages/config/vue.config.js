const path = require('path')
const isDev = process.env.NODE_ENV !== 'production'
const { proxy } = require('../../project.config.json')

module.exports = {
  lintOnSave: false,
  publicPath: isDev ? '/' : '/apps-config/',
  assetsDir: 'static',
  devServer: {
    port: 10202,
    disableHostCheck: true,
    headers: {
      'Access-Control-Allow-Origin': '*'
    },
    proxy
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src')
      }
    },
    output: {
      // 微应用的包名，这里与主应用中注册的微应用名称一致
      library: 'apps-config',
      // 将你的 library 暴露为所有的模块定义下都可运行的方式
      libraryTarget: 'umd',
      // 按需加载相关，设置为 webpackJsonp_pkg-name 即可
      jsonpFunction: 'webpackJsonp_config'
    }
  }
}

const { proxy } = require('../../project.config.json')
module.exports = {
  lintOnSave: false,
  publicPath: '/',
  assetsDir: 'static',
  devServer: {
    port: 10200,
    proxy
  },
  css: {
    loaderOptions: {
      less: {
        lessOptions: {
          javascriptEnabled: true
        }
      }
    }
  }
}

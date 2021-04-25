module.exports = {
  pages: {
    entry: 'src/index.js'
  },
  css: {
    extract: true
  },
  configureWebpack: {
    output: {
      libraryExport: 'default'
    },
    externals: { // invalid?
      antd: {
        root: 'antd',
        commonjs: 'antd',
        commonjs2: 'antd',
        amd: 'antd'
      }
    }
  }
}

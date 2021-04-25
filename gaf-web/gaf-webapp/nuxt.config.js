const pkg = require('./package')
const { proxy } = require('./project.config.json')
const isDev = process.env.NODE_ENV === 'development'
const prefix = isDev ? '' : '.'

export default {
  ssr: false,
  telemetry: false,
  target: 'static',
  server: {
    port: 9000,
    host: '0.0.0.0',
  },
  head: {
    title: pkg.title,
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: 'gaf' },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
    script: [
      {
        src: prefix + '/libs/polyfill.min.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/config/menus.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/config/dirs.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/config/panel.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/config/tools.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/config/theme.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/webGL/Cesium/Cesium.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/webGL/js/Convert.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/webGL/js/tooltip.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/config/data-service.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/config/cameraCoordinate.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
      {
        src: prefix + '/classic/SuperMap.Include.js',
        type: 'text/javascript',
        charset: 'utf-8',
      },
    ],
  },
  css: ['~/assets/theme.less', '~/assets/theme.css'],
  plugins: [
    '@/plugins/proxy',
    '@/plugins/index',
    '@/plugins/api',
    '@/plugins/lang',
    '@/plugins/configserver-api',
  ],
  components: true,
  buildModules: ['@nuxtjs/eslint-module'],
  modules: ['@nuxtjs/axios'],
  axios: {
    proxy: true,
  },
  proxy,
  build: {
    publicPath: '/_static/',

    loaders: {
      less: {
        javascriptEnabled: true,
        sourceMap: process.env.NODE_ENV === 'development',
      },
    },

    /*
     ** You can extend webpack config here
     */
    extend(config, ctx) {
      // const targets = ['.jsx?$']
      // targets.forEach(target => resolveNodeModules(config, target))

      // Run ESLint on save
      if (ctx.isDev && ctx.isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/,
        })
        config.devtool = 'source-map'
      }
      if (!ctx.isDev) {
        config.output.publicPath = './_static/'
      }
    },
  },
  router: {
    mode: 'hash',
  },
}

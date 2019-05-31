import pkg from './package'

export default {
  mode: 'spa',

  /*
   ** Headers of the page
   */
  head: {
    title: 'svn-commit-notify',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: 'Subversion commit notification.' }
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }]
  },

  /*
   ** Customize the progress-bar color
   */
  loading: { color: '#fff' },

  router: {
    mode: 'hash'
  },

  plugins: [
    '@plugins/axios.js',
    '@plugins/element-ui.js'
  ],

  modules: [
    '@nuxtjs/axios'
  ],

  axios: {
    proxy: true
  },

  proxy: {
    '/api': 'http://localhost:9000'
  },

  /*
   ** Build configuration
   */
  build: {
    analyze: false,
    transpile: [/^element-ui/],

    /*
    ** You can extend webpack config here
    */
    extend(config, ctx) {
      if (ctx.isDev) {
        config.devtool = 'eval-source-map'
      }
      // Run ESLint on save
      if (ctx.isDev && ctx.isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    }
  }
}

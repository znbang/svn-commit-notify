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
  /*
   ** Global CSS
   */
  css: [
    'element-ui/lib/theme-chalk/index.css'
  ],
  /*
   ** Plugins to load before mounting the App
   */
  plugins: [
    '@plugins/axios',
    '@plugins/element-ui'
  ],
  /*
   ** Nuxt.js dev-modules
   */
  buildModules: [
    // Doc: https://github.com/nuxt-community/eslint-module
    '@nuxtjs/eslint-module'
  ],
  /*
   ** Nuxt.js modules
   */
  modules: [
    '@nuxtjs/axios'
  ],

  router: {
    mode: 'hash'
  },

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
    }
  }
}

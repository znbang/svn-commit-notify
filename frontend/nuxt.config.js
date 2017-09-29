module.exports = {
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
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },
  /*
  ** Customize the progress bar color
  */
  loading: { color: '#3B8070' },
  /*
  ** Build configuration
  */
  build: {
    /*
    ** Run ESLint on save
    */
    extend (config, ctx) {
      if (ctx.dev && ctx.isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    },
    vendor: [
      'axios'
    ]
  },
  mode: 'spa',
  router: {
    mode: 'hash'
  },
  plugins: [
    '~plugins/axios.js',
    '~plugins/element-ui.js'
  ],
  modules: [
    '@nuxtjs/proxy'
  ],
  proxy: {
     '/api': 'http://127.0.0.1:9000/'
  }
}

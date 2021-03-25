module.exports = {
  root: true,
  env: {
    browser: true,
    node: true
  },
  parserOptions: {
    parser: 'babel-eslint'
  },
  extends: [
    // '@nuxtjs',
    'plugin:vue/essential',
    // 'plugin:vue/recommended',
    'eslint:recommended',
  ],
  // add your custom rules here
  rules: {}
}

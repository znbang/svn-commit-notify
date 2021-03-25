import axios from 'axios'

function showError (ctx, message) {
  ctx.app.$message.closeAll()
  ctx.app.$message({
    message,
    type: 'error',
    duration: 0,
    showClose: true
  })
}

function hideError (ctx, config) {
  ctx.app.$message.closeAll()
  return config
}

function handleError (ctx, error) {
  const { response } = error
  // 統一錯誤內容: { statusCode: 0, message: '' }
  if (response) {
    if (response.data.errors || response.data.message) {
      error = response.data
    } else {
      error.message = response.data
    }
    error.statusCode = response.status
  } else {
    // 網路錯誤沒有response
    error.statusCode = 500
    error.message = '無法與伺服器連線。'
  }
  if (!error.errors) {
    error.errors = {}
  }
  if (error.statusCode >= 500) {
    showError(ctx, error.message)
  }
  return Promise.reject(error)
}

export default (ctx, inject) => {
  inject('axios', axios)
  axios.defaults.baseURL = '/api'
  axios.defaults.headers.Accept = 'application/json'
  axios.interceptors.request.use(config => hideError(ctx, config))
  axios.interceptors.response.use(undefined, error => handleError(ctx, error))
}

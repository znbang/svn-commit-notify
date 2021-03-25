import Vue from 'vue'
import ElementUI from 'element-ui'

Vue.use(ElementUI)

export default (ctx, inject) => {
  inject('message', ElementUI.Message)
}

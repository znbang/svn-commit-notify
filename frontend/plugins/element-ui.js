import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'

Vue.use(ElementUI)

export default (ctx, inject) => {
  inject('message', ElementUI.Message)
}

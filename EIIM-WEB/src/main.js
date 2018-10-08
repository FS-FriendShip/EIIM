import Vue from 'vue'
import App from './App'
import router from './router'
import store from './vuex'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import global_ from './common/global'
import VueContextMenu from 'vue-contextmenu'
import 'vue-contextmenu/style/css/font-awesome.min.css'
import './assets/css/iconfont.css'

Vue.config.productionTip = false

Vue.prototype.GLOBAL = global_

Vue.use(VueContextMenu)
Vue.use(ElementUI)

router.beforeEach((to, from, next) => {
  console.log(to)
  if (to.matched.some(m => m.meta.auth)) {
    if (to.name === 'Login') {
      next()
    } else {
      if (localStorage.getItem('account-key')) {
        next()
      } else {
        next({name: 'Login'})
      }
    }
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store: store,
  components: { App },
  template: '<App/>'
})

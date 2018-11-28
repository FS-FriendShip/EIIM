// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './vuex'
import global_ from './common/global'
import MintUI from 'mint-ui'
import 'mint-ui/lib/style.css'
import './assets/iconfont/iconfont.css'
import {dateFormat} from './common/utils'

Vue.use(MintUI)

Vue.prototype.GLOBAL = global_

Vue.filter('formatDate', function (value, scene) {
  if (value) {
    let date = new Date(value)
    let currentDate = new Date()
    if (scene === 'session') {
      if (dateFormat(currentDate, 'yy/MM/dd') === dateFormat(date, 'yy/MM/dd')) {
        return dateFormat(date, 'hh:mm')
      } else {
        return dateFormat(date, 'yy/MM/dd')
      }
    } else {
      return dateFormat(date, 'yyyy年MM月dd日 hh:mm')
    }
  }
})

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})

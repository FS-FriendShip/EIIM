// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './vuex'
import filters from './filters' // 将全部过滤器放在 filters/index.js 中便于管理
import global_ from './common/global'
import {Field, Button, Cell} from 'mint-ui'
import 'mint-ui/lib/style.css'

Vue.component(Field.name, Field)
Vue.component(Button.name, Button)
Vue.component(Cell.name, Cell)

Vue.prototype.GLOBAL = global_

filters(Vue)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})

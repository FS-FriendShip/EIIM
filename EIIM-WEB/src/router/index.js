import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/views/Login'
import Main from '@/views/Main'
import AdminMain from '@/views/admin/Main'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '',
      name: 'Login',
      component: HelloWorld
    },

    {
      path: '/Main',
      name: 'Main',
      component: Main
    },

    {
      path: '/admin',
      name: 'Admin',
      component: AdminMain
    }
  ]
})

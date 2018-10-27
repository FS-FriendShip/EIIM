import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/Login'
import ChatMain from '@/views/chat/Main'
import AdminMain from '@/views/admin/Main'
import ContactMain from '@/views/contact/Main'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '',
      name: 'Login',
      meta: {auth: true, keepAlive: false},
      component: Login
    },

    {
      path: '/Chat',
      name: 'Chat',
      meta: {auth: true, keepAlive: true},
      component: ChatMain
    },

    {
      path: '/Contact',
      name: 'Contact',
      meta: {auth: true, keepAlive: true},
      component: ContactMain
    },

    {
      path: '/Admin',
      name: 'Admin',
      meta: {auth: true, keepAlive: true},
      component: AdminMain
    }
  ]
})

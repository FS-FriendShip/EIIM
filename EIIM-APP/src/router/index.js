import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: resolve => require(['@/pages/Login.vue'], resolve)
    },
    {
      path: '/Main',
      name: 'Main',
      component: resolve => require(['@/pages/Main.vue'], resolve)
    },
    {
      path: '/show',
      name: 'Login',
      component: resolve => require(['@/pages/Login.vue'], resolve)
    },
    {
      path: '/chatroom/:roomId',
      name: 'chatroom',
      component: resolve => require(['@/pages/ChatRoom.vue'], resolve)
    },

    {
      path: '/contacts',
      name: 'contacts',
      component: resolve => require(['@/pages/Contact.vue'], resolve)
    },

    {
      path: '/contact/:id',
      name: 'contact-detail',
      component: resolve => require(['@/pages/ContactDetail.vue'], resolve)
    },

    {
      path: '/chatRoomInfo/:roomId',
      name: 'chatRoomInfo',
      component: resolve => require(['@/pages/ChatRoomInfo.vue'], resolve)
    },

    {
      path: '/groupChatRoomInfo/:roomId',
      name: 'groupChatRoomInfo',
      component: resolve => require(['@/pages/GroupChatRoomInfo.vue'], resolve)
    },

    {
      path: '/showPersonal',
      name: 'showPersonal',
      component: resolve => require(['@/pages/Personal.vue'], resolve)
    }
  ]
})

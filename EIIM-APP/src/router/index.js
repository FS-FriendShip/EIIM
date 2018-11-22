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
      components: {
        'default': resolve => require(['@/pages/Main.vue'], resolve),
        'subPage': resolve => require(['@/pages/ChatRoom.vue'], resolve)
      }
    },

    {
      path: '/contacts',
      name: 'contacts',
      components: {
        'default': resolve => require(['@/pages/Main.vue'], resolve),
        'subPage': resolve => require(['@/pages/Contact.vue'], resolve)
      }
    },

    {
      path: '/contact/:id',
      name: 'contact-detail',
      components: {
        'default': resolve => require(['@/pages/Main.vue'], resolve),
        'subPage': resolve => require(['@/pages/ContactDetail.vue'], resolve)
      }
    },

    {
      path: '/chatRoomInfo/:roomId',
      name: 'chatRoomInfo',
      components: {
        'default': resolve => require(['@/pages/Main.vue'], resolve),
        'subPage': resolve => require(['@/pages/ChatRoomInfo.vue'], resolve)
      }
    },

    {
      path: '/groupChatRoomInfo/:roomId',
      name: 'groupChatRoomInfo',
      components: {
        'default': resolve => require(['@/pages/Main.vue'], resolve),
        'subPage': resolve => require(['@/pages/GroupChatRoomInfo.vue'], resolve)
      }
    },

    {
      path: '/showPersonal',
      name: 'showPersonal',
      component: resolve => require(['@/pages/Personal.vue'], resolve)
    }
  ]
})

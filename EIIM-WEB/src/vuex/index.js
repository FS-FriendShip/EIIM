import Vue from 'vue'
import Vuex from 'vuex'
import account from './account'
import app from './app'
import chatroom from './chatroom'
import contact from './contact'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    account: account,
    app: app,
    chatroom: chatroom,
    contact: contact
  }
})

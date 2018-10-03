import api from '../../api/restApi.js'
import * as types from './types'
import state from './state'

export default {
  /**
   * 获取通讯录信息
   * @param commit
   * @returns {PromiseLike<boolean> | Promise<boolean> | *}
   */
  api_get_contacts: ({commit}) => {
    return api.getContacts().then(res => {
      commit(types.UPDATE_CONTACTS, res.data)
    })
  },

  /**
   * 获取单条通讯录信息
   * @param commit
   * @param id
   * @returns {*}
   */
  api_get_contact: ({commit}, id) => {
    console.log(state.contacts)
    return state.contacts.filter(contact => contact.id === id)
  }
}

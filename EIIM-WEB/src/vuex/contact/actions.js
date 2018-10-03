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
    return state.contacts.filter(contact => contact.id === id)
  },

  /**
   * 获取组织架构信息
   * @param commit
   */
  api_init_orgs: ({commit}) => {
    return api.getOrgs().then(res => {
      commit(types.INIT_ORGS, res.data)
    })
  },

  /**
   * 选择聊天室
   * @param commit
   * @param roomId
   */
  api_select_org: ({commit}, orgId) => {
    commit(types.SELECT_ORG, orgId)
  },

  /**
   * 新建机构
   * @param commit
   * @param org
   */
  api_save_org: ({commit}, org) => {
    if (org.id) {
      return api.saveOrg(org).then(res => {
        commit(types.UPDATE_ORG, res.data)
      })
    } else {
      return api.saveOrg(org).then(res => {
        commit(types.CREATE_ORG, res.data)
      })
    }
  },

  /**
   *
   * @param commit
   */
  api_delete_org: ({commit}, orgId) => {
    return api.delOrg(orgId).then(res => {
      // commit(types.UPDATE_ORG, res.data)
    })
  },

  /**
   *
   * @param commit
   * @param user
   * @returns {Promise<T>}
   */
  api_save_user: ({commit}, user) => {
    if (user.id) {
      return api.saveUser(user).then(res => {
        commit(types.UPDATE_USER, res.data)
      })
    } else {
      return api.saveUser(user).then(res => {
        commit(types.CREATE_USER, res.data)
      })
    }
  }
}
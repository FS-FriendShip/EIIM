export default {
  contactsInitialList: state => {
    let initialList = []
    let allContacts = state.contacts
    let max = allContacts.length
    for (var i = 0; i < max; i++) {
      if (initialList.indexOf(allContacts[i].initial) === -1) {
        initialList.push(allContacts[i].initial)
      }
    }
    return initialList.sort()
  },

  // 将联系人根据首字母进行分类
  api_get_contacts: (state, getters) => {
    let contactsList = state.contacts

    return contactsList.filter(item => item.account)
  },

  /**
   * 获取用户信息
   * @param state
   * @param id
   * @returns {{}}
   */
  contactInfo: (state, getters) => {
    return function (id) {
      return state.contacts.filter(item => item.id === id)
    }
  },

  /**
   *
   * @param state
   * @param getters
   */
  api_get_orgs: (state, getters) => {
    return state.orgs
  },

  /**
   *
   * @param state
   * @param getters
   */
  api_get_org: (state, getters) => {
    let orgId = state.selectedOrgId
    let org

    if (!orgId) {
      org = state.orgs[0]
    } else {
      org = state.orgs.filter(org => org.id === orgId)[0]
    }

    if (!org.employees) {
      org.employees = []
    }

    return org
  }
}

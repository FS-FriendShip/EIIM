export default {
  // 将联系人根据首字母进行分类
  api_get_contacts: (state, getters) => {
    let contactsList = state.contacts
    contactsList.forEach(item => {
      item.checked = false

      item.active = (item.id === state.selectedContactId)

      if (item.account && !item.account.avatar.startsWith('http')) {
        item.account.avatar = process.env.FILE_SERVER_ENV + item.account.avatar
      }
      return item
    })
    return contactsList.filter(item => item.account)
  },

  /**
   * 获取用户信息
   * @param state
   * @param id
   * @returns {{}}
   */
  api_get_contact: (state, getters) => {
    let currentId = state.selectedContactId

    let contacts = state.contacts.filter(contact => contact.id === currentId)
    if (!contacts || contacts.length === 0) {
      return {}
    }

    return contacts[0]
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

    if (state.orgs.length > 0) {
      if (!orgId) {
        org = state.orgs[0]
      } else {
        org = state.orgs.filter(org => org.id === orgId)[0]
      }

      if (org && !org.employees) {
        org.employees = []
      }
    }

    return org
  }
}

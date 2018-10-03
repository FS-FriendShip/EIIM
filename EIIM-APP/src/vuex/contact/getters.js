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
  contactsList: (state, getters) => {
    let contactsList = state.contacts
    return contactsList
  },

  /**
   * 获取用户信息
   * @param state
   * @param id
   * @returns {{}}
   */
  contactInfo: (state, getters) => {
    return function (id) {
      console.log(state.contacts)
      return state.contacts.filter(item => item.id === id)
    }
  }
}

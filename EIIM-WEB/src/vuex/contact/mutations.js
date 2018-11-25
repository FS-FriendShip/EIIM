import * as types from './types.js'

export default {
  [types.UPDATE_CONTACTS] (state, contacts) {
    if (contacts) {
      contacts.forEach((contact, index) => {
        contact.active = (index === 0)
      })
    }
    state.contacts = contacts
  },

  [types.SELECT_CONTACT] (state, id) {
    state.selectedContactId = id
  },

  [types.UPDATE_USER_ACCOUNT] (state, account) {
    let orgId = state.selectedOrgId
    let org = state.orgs.find(org => org.id === orgId)
    if (org.employees) {
      let employees = org.employees
      let personId = account.person.id

      let person = employees.find(employee => employee.id === personId)
      if (person) {
        person.account = account
      }
    }
  },

  /**
   * 更新用户信息
   * @param state
   * @param user
   */
  [types.UPDATE_USER] (state, user) {
    let orgId = user.org.id
    let org = state.orgs.find(org => org.id === orgId)
    if (org) {
      if (!org.employees) org.employees = []

      let employee = org.employees.find(item => item.id === user.id)
      if (employee) {
        employee.fullName = user.fullName
        employee.title = user.title
        employee.phone = user.phone
        employee.email = user.email
        employee.mobile = user.mobile
      } else {
        org.employees.splice(0, 0, user)
      }
    }
  },

  [types.CREATE_USER] (state, user) {
    let orgId = user.org.id
    let org = state.orgs.find(org => org.id === orgId)
    if (!org.employees) {
      org.employees = []
    }

    org.employees.push(user)
  },

  [types.DELETE_USER] (state, personId) {
    let orgId = state.selectedOrgId
    let org = state.orgs.find(org => org.id === orgId)
    if (org.employees) {
      let employees = org.employees

      let index = employees.findIndex(item => item.id === personId)
      if (index !== -1) {
        org.employees.splice(index, 1)
      }
    }
  },

  [types.INIT_ORGS] (state, orgs) {
    if (!orgs) {
      orgs = []
    }

    orgs.forEach((org, index) => {
      org.active = (index === 0)
    })

    state.orgs = orgs
  },

  [types.SELECT_ORG] (state, orgId) {
    state.selectedOrgId = orgId
  },

  [types.CREATE_ORG] (state, org) {
    state.selectedOrgId = org.id

    state.orgs.push(org)
  },

  [types.UPDATE_ORG] (state, org) {
    state.selectedOrgId = org.id
    org.active = true

    state.orgs.forEach(item => {
      if (item.id === org.id) {
        item.active = true
      } else {
        item.active = false
      }
    })

    let index = state.orgs.findIndex(item => item.id === org.id)
    state.orgs.splice(index, 1, org)
  }
}

import * as types from './types.js'

export default {
  [types.UPDATE_CONTACTS] (state, contacts) {
    state.contacts = contacts
  },

  [types.UPDATE_USER] (state, user) {
    let orgId = user.orgId
    let org = state.orgs.find(org => org.id === orgId)
    let index = org.employees.findIndex(item => item.id === user.id)
    org.splice(index, 1, user)
  },

  [types.CREATE_USER] (state, user) {
    let orgId = user.org.id
    let org = state.orgs.find(org => org.id === orgId)
    if (!org.employees) {
      org.employees = []
    }

    org.employees.push(user)
  },

  [types.INIT_ORGS] (state, orgs) {
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
    let orgId = org.id
    let index = state.orgs.findIndex(item => item.id === orgId)
    state.orgs.splice(index, 1, org)
  }
}

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

  [types.UPDATE_USER] (state, user) {
    let orgId = user.orgId
    let org = state.orgs.find(org => org.id === orgId)
    let index = org.employees.findIndex(item => item.id === user.id)
    org.employees.splice(index, 1, user)
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

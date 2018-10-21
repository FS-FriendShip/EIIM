'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  WEBSOCKET_ENV: '"60.173.195.18:60993"',
  API_SERVER_ENV: '""',
  FILE_SERVER_ENV: '"http://60.173.195.18:4080/rest/v1/download/"'
})

'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  WEBSOCKET_ENV: '"localhost:9997"',
  API_SERVER_ENV: '"http://localhost:9999"',
  FILE_SERVER_ENV: '"http://localhost:9999/rest/v1/download/"'
})

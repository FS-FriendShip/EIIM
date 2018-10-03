import axios from 'axios'

// 创建axios实例 application/x-www-data-urlencoded  application/json
axios.defaults.timeout = 5000
// axios.defaults.baseURL = 'http://localhost:9999/rest'
axios.defaults.baseURL = '/rest'

let ACCOUNT = (function () {
  let account = localStorage.getItem('account-key') ? JSON.parse(localStorage.getItem('account-key')) : {}
  if (account) {
    return account.account
  }
  return undefined
})()

// http request 拦截器
axios.interceptors.request.use(
  config => {
    // const token = getCookie('名称');注意使用的时候需要引入cookie方法，推荐js-cookie
    config.data = JSON.stringify(config.data)
    config.headers = {
      'Content-Type': 'application/json'
    }
    if (ACCOUNT) {
      config.headers.token = ACCOUNT.token
      config.headers.deviceId = ACCOUNT.deviceKey + '.pc'
    }
    console.log(config)
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// http response 拦截器
axios.interceptors.response.use(
  response => {
    console.log(response)
    if (response.data.errCode === 2) {
      this.$route.push({
        path: '/',
        querry: {redirect: this.$route.currentRoute.fullPath}// 从哪个页面跳转
      })
    }
    return response
  },
  error => {
    return Promise.reject(error)
  }
)

/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */

export function get (url, params = {}) {
  if (ACCOUNT) {
    if (url.indexOf('?') <= 0) {
      url = url + '?accountCode=' + ACCOUNT.accountCode
    }
  }
  return new Promise((resolve, reject) => {
    axios.get(url, {
      params: params
    })
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}

/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function post (url, data = {}) {
  if (ACCOUNT) {
    if (url.indexOf('?') <= 0) {
      url = url + '?accountCode=' + ACCOUNT.accountCode
    }
  }
  return new Promise((resolve, reject) => {
    axios.post(url, data)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
  })
}

/**
 * 封装patch请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function patch (url, data = {}) {
  if (ACCOUNT) {
    if (url.indexOf('?') <= 0) {
      url = url + '?accountCode=' + ACCOUNT.accountCode
    }
  }
  return new Promise((resolve, reject) => {
    axios.patch(url, data)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
  })
}

/**
 * 封装put请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function put (url, data = {}) {
  if (ACCOUNT) {
    if (url.indexOf('?') <= 0) {
      url = url + '?accountCode=' + ACCOUNT.accountCode
    }
  }
  return new Promise((resolve, reject) => {
    axios.put(url, data)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
  })
}

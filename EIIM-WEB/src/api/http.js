import axios from 'axios'

// 创建axios实例 application/x-www-data-urlencoded  application/json
axios.defaults.timeout = 5000
// axios.defaults.baseURL = 'http://121.40.51.91:8088/rest'
axios.defaults.baseURL = '/rest'

var getAccount = function () {
  let account = localStorage.getItem('account-key') ? JSON.parse(localStorage.getItem('account-key')) : {}
  if (account) {
    return account.account
  }
  return undefined
}

// http request 拦截器
axios.interceptors.request.use(
  config => {
    console.log(config.url)
    // const token = getCookie('名称');注意使用的时候需要引入cookie方法，推荐js-cookie
    if (config.url.includes('v1/upload')) {
      config.headers = {
        'Content-Type': 'multipart/form-data'
      }
    } else {
      config.data = JSON.stringify(config.data)
      config.headers = {
        'Content-Type': 'application/json'
      }
    }

    if (!config.url.includes('login')) {
      try {
        let account = getAccount()
        config.headers.token = account.token
        config.headers.deviceId = account.deviceKey + '.pc'
      } catch (err) {
        console.log(err)
      }
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// http response 拦截器
axios.interceptors.response.use(
  response => {
    console.log(response.data)
    let errorCode = response.data.errorCode
    if (errorCode === 2) {
      this.$route.push({
        path: '/',
        querry: {redirect: this.$route.currentRoute.fullPath}// 从哪个页面跳转
      })
    } else if (errorCode !== 0) {
      alert(response.data.errorMessage)
      throw new Error(response.data.errorMessage)
    }
    return response
  },
  error => {
    if (error && error.response) {
      switch (error.response.status) {
        case 400:
          error.message = '请求错误'
          break

        case 401:
          error.message = '未授权，请登录'
          break

        case 403:
          error.message = '拒绝访问'
          break

        case 404:
          error.message = `请求地址出错: ${error.response.config.url}`
          break

        case 408:
          error.message = '请求超时'
          break

        case 500:
          error.message = '服务器内部错误'
          break

        case 501:
          error.message = '服务未实现'
          break

        case 502:
          error.message = '网关错误'
          break

        case 503:
          error.message = '服务不可用'
          break

        case 504:
          error.message = '网关超时'
          break

        case 505:
          error.message = 'HTTP版本不受支持'
          break

        default:
      }
    }

    // 执行自定义错误回调
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
  let account = getAccount()

  if (account) {
    if (url.indexOf('?') <= 0) {
      url = url + '?accountCode=' + account.account.code
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
  let account = getAccount()
  if (account) {
    if (url.indexOf('?') <= 0) {
      url = url + '?accountCode=' + account.account.code
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
 * 封装修改请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function update (url, data = {}) {
  let account = getAccount()
  if (account) {
    if (url.indexOf('?') <= 0) {
      url = url + '?accountCode=' + account.account.code
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

/**
 * 封装删除请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function remove (url, data = {}) {
  let account = getAccount()
  if (account) {
    if (url.indexOf('?') <= 0) {
      url = url + '?accountCode=' + account.account.code
    }
  }
  return new Promise((resolve, reject) => {
    axios.delete(url, data)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
  })
}

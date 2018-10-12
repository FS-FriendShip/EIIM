<template>
  <div>
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
  * {
    box-sizing: border-box;
  }
  *:before, *:after {
    box-sizing: inherit;
  }
  body, html {
    height: 100%;
    width: 100%;
    margin: 0;
    padding: 0;
    overflow: hidden;
  }

  body {
    color: #4d4d4d;
    font: 14px/1.4em 'Helvetica Neue', Helvetica, 'Microsoft Yahei', Arial, sans-serif;
    background: #f5f5f5 url('./assets/bg.jpg') no-repeat center;
    background-size: cover;
    font-smoothing: antialiased;
  }

  li {
    list-style:none;
  }

  .avatar-small {
    width: 30px;
    height: 30px;
  }

  .avatar-medium {
    width: 40px;
    height: 40px;
  }

  .avatar-large {
    width: 50px;
    height: 50px;
  }
</style>

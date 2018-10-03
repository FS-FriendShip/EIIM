<template>
  <div class="card">
    <header>
      <img class="avatar" width="40" height="40" :src="currentUser.avatar">
      <p class="name">{{currentUser.nickname}}</p>
    </header>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Profile',
  computed: {
    ...mapGetters({currentUser: 'account/getCurrentUser'})
  }
}
</script>

<style scoped lang="less">
  .card {
    padding: 12px;
    border-bottom: solid 1px #24272C;

    footer {
      margin-top: 10px;
    }

    .avatar, .name {
      vertical-align: middle;
    }
    .avatar {
      border-radius: 2px;
    }
    .name {
      display: inline-block;
      margin: 0 0 0 15px;
      font-size: 16px;
    }
  }
</style>

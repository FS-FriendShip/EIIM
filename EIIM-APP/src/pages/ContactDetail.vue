<template>
  <div id="contact-detail">
    <header id="wx-header">
      <div class="center">
        <router-link to="-1" tag="div" class="iconfont icon-return-arrow">
          <span>{{this.appConfig.appName}}</span>
        </router-link>
        <span>{{pageName}}</span>
      </div>
    </header>

    <section>
      <div class="contact-section">
        <mt-cell :title="contactInfo.name">
          <!--<img slot="icon" src="../assets/100x100.png" width="24" height="24">-->
        </mt-cell>
      </div>

      <div class="contact-section">
        <mt-cell title="手机" :value="contactInfo.mobile">
          <!--<img slot="icon" src="../assets/100x100.png" width="24" height="24">-->
        </mt-cell>

        <mt-cell title="座机" :value="contactInfo.mobile">
          <!--<img slot="icon" src="../assets/100x100.png" width="24" height="24">-->
        </mt-cell>

        <mt-cell title="邮箱" :value="contactInfo.email">
          <!--<img slot="icon" src="../assets/100x100.png" width="24" height="24">-->
        </mt-cell>

        <mt-cell title="部门">
          <!--<img slot="icon" src="../assets/100x100.png" width="24" height="24">-->
        </mt-cell>
      </div>

      <div class="contact-section">
        <div style="padding:0px 10px">
          <mt-button type="primary" size="large" @click="createChatRoom">发消息</mt-button>
        </div>

        <div style="padding:0px 10px">
          <mt-button type="primary" size="large" @click="createChatRoom">语音通话</mt-button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  data () {
    return {
      id: this.$route.params.id,
      pageName: '个人信息',
      contactInfo: {}
    }
  },

  created () {
    this.contactInfo = this.getContactInfo(this.$route.params.id)[0]
  },

  computed: {
    ...mapGetters({getContactInfo: 'contact/contactInfo'})
  },

  methods: {
    createChatRoom () {
      var chatroom = this.$store.dispatch('chatroom/api_new_chatroom', this.contactInfo)

      if (chatroom) {
        this.$router.push({name: 'chatroom', params: {roomId: chatroom.id}})
      }
    }
  }

}
</script>

<style>
  @import '../assets/css/contact.css';
</style>

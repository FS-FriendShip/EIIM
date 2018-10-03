<template>
  <div id="contact">
    <header id="wx-header">
      <div class="center">
        <router-link to="/Main" tag="div" class="iconfont icon-return-arrow">
          <span>{{this.appConfig.appName}}</span>
        </router-link>
        <span>{{pageName}}</span>
      </div>
    </header>

    <section class="contact-section">
      <div class="section-label">
        <span>外部联系人</span>
      </div>
      <div>
        <router-link to="">
          <mt-cell title="外部联系人">
            <!--<img slot="icon" src="../assets/100x100.png" width="24" height="24">-->
          </mt-cell>
        </router-link>
      </div>
    </section>

    <section>
      <div class="section-label">
        <span>企业通讯录</span>
      </div>
      <div>
        <router-link v-for="item in contactsList" :key="item.id" :to="{name: 'contact-detail', params: {id: item.id}}">
          <mt-cell :title="item.name">
            <!--<img slot="icon" src="../assets/100x100.png" width="24" height="24">-->
          </mt-cell>
        </router-link>
      </div>
    </section>
    <!--检索-->
    <!--<div class="initial-bar"><span @click="toPs(i)" v-for="i in contactsInitialList" :key="i.wxid">{{i}}</span></div>-->
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  data () {
    return {
      pageName: '通讯录',
      page: 'contacts'
    }
  },

  created () {
    this.$store.dispatch('contact/api_get_contacts')
  },

  computed: {
    ...mapGetters({contactsInitialList: 'contact/contactsInitialList', contactsList: 'contact/contactsList'})
  }
}
</script>

<style>
  @import '../assets/css/contact.css';
</style>

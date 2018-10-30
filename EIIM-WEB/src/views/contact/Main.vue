<template>
  <el-container id = "Contact"  v-bind:style="{height:adminHeight}">
    <el-aside class="contact-list">
      <el-container>
        <el-header style="padding:10px">
          <el-row>
            <el-input
              placeholder="检索"
              class="search"
              @keyup.native="searchSession"
              prefix-icon="el-icon-search"
              v-model="key">
            </el-input>
          </el-row>
        </el-header>

        <el-main>
          <el-row type="flex" align="middle" class="contact-item" v-bind:class="contact.active ? 'active':''" :gutter="50" v-for="contact in contacts"  @click.native="selectContact(contact)" :key="contact.id">
            <el-col :span="4"><img class="avatar-medium" :src="contact.account.avatar"></el-col>
            <el-col :span="20" :push="1">
              <span class="title">{{contact.fullName}}</span>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-aside>

    <el-main class="contact-info">
      <el-row class="contact-item header" type="flex" align="middle">
        <el-col :span="3" :offset="4" style="font-size:24px; font-weight:bolder">{{contact.fullName}}</el-col>
        <el-col :span="12"></el-col>
        <el-col :span="1"><img class="avatar-large" :src="contact.account?contact.account.avatar:''"></el-col>
      </el-row>

      <el-row class="contact-item">
        <el-col :span="3" :offset="4">职务</el-col>
        <el-col :span="17">{{contact.title}}</el-col>
      </el-row>

      <el-row class="contact-item">
        <el-col :span="3" :offset="4">固定电话</el-col>
        <el-col :span="17">{{contact.phone}}</el-col>
      </el-row>

      <el-row class="contact-item">
        <el-col :span="3" :offset="4">移动电话</el-col>
        <el-col :span="17">{{contact.mobile}}</el-col>
      </el-row>

      <el-row class="contact-item">
        <el-col :span="3" :offset="4">邮箱</el-col>
        <el-col :span="17">{{contact.email}}</el-col>
      </el-row>

      <el-row style="margin-top:50px">
        <el-col :span="24" :offset="12">
          <el-button  type="primary" @click="send(contact)">发消息</el-button>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Contact',

  data () {
    return {
      selectedContactId: '',
      key: '',
      adminHeight: (window.innerHeight) + 'px',

      user: {
        id: null,
        orgId: null,
        fullName: null,
        title: null,
        sex: 'MALE',
        mobile: null,
        phone: null,
        email: null
      }
    }
  },

  computed: {
    ...mapGetters({contacts: 'contact/api_get_contacts', contact: 'contact/api_get_contact'})
  },

  methods: {
    selectContact: function (contact) {
      this.selectedContactId = contact.id
      this.$store.dispatch('contact/api_get_contact', contact.id)
    },

    send: function (contact) {
      let _this = this
      let session = {}
      let names = contact.account.nickName
      let accountCodes = [this.GLOBAL.getAccount().code]
      accountCodes.push(contact.account.code)

      session.name = names
      session.accountCodes = accountCodes

      this.$store.dispatch('chatroom/api_new_chatroom', session).then((data) => {
        if (data.existed) {
          _this.$router.push({name: 'Chat', params: {chatroomId: data.room.id}})
        } else {
          _this.dialogVisible = false
          // //发送消息
          let message = '我是' + _this.GLOBAL.getAccount().nickname
          let roomId = data.id
          this.$store.dispatch('chatroom/api_send_text_message', {
            sessionId: data.id,
            message: message
          }).then(
            _this.$router.push({name: 'Chat', params: {chatroomId: roomId}})
          )
        }
      })
    }
  },

  created  () {
    this.$store.dispatch('contact/api_get_contacts').then(() => {

    })
  }
}
</script>

<style scoped  lang="less">
  #Contact {
    width: 100%;
    height: 100%;
    padding: 0;

    text-align: left;
    background: #fff;
  }

  #Contact .contact-list {
    width:300px!important;
    background: #eee;

    .contact-item{
      padding: 5px 0px;

      &.active {
        background-color: #fff;
      }
    }
  }

  #Contact .contact-info {
    .contact-item {
      padding:15px 0px;
    }

    .contact-item.header {
      height:100px;
      border-bottom:1px solid #eee;
      margin-bottom:10px;
    }
  }
</style>

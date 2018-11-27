<template>
  <div id="contact"  class="dialogue">
    <mt-header  class="app-header" fixed title="通讯录">
      <router-link to="/Main" slot="left">
        <mt-button icon="back"></mt-button>
      </router-link>
    </mt-header>

    <section v-bind:style="{height:messageHeight}" class="message dialogue-section clearfix">
      <ul>
        <li class="contact-item" v-for="item in userList" :key="item.id">
          <div><img class="avatar-large" :src="item.account.avatar"></div>
          <div style="margin-left:20px">{{item.account.nickname}}</div>
          <div class="item-check">
            <span>
               <input type="checkbox" class="Checkbox">
               <label></label>
            </span>
            <!--<el-checkbox v-model="item.checked" :name="item.id" width="10%" :key="item.id" @change="selectUser(item)"></el-checkbox>-->
          </div>
        </li>
      </ul>
    </section>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  data () {
    return {
      page: 'contacts',
      messageHeight: (window.innerHeight - 45) + 'px'
    }
  },

  created () {
    this.$store.dispatch('contact/api_get_contacts')
  },

  computed: {
    ...mapGetters({userList: 'contact/api_get_contacts'})
  }
}
</script>

<style scoped>
  .contact-item {
    display: flex;
    display: -webkit-flex;
    flex-direction: row;
    flex-wrap: wrap;
    padding:5px 10px;
    border-bottom: 1px solid silver;
    align-items:center
  }

  .contact-item .item-check{
    margin-left: auto;
  }

  span {
    position: relative;
  }

  .Checkbox {
    position: absolute;
    visibility: hidden;
  }

  .Checkbox+label {
    position:absolute;
    width: 16px;
    height: 16px;
    border: 1px solid #A6A6A6;
    border-radius: 50%;
    background-color:#DEDEDE;
  }

  .Checkbox:checked+label:after {
    content: "";
    position: absolute;
    left: 2px;
    top: 2px;
    width: 9px;
    height: 4px;
    border: 2px solid #424242;
    border-top-color: transparent;
    border-right-color: transparent;
    transform: rotate(-45deg);
    -ms-transform: rotate(-60deg);
    -moz-transform: rotate(-60deg);
    -webkit-transform: rotate(-60deg);
  }
</style>

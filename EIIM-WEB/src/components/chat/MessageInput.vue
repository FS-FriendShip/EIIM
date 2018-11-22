<template>
  <div id="editbox" class="box" contenteditable="true" v-html="innerText" v-bind:style="{background:bgColor}" @keyup="changeTxt($event)" @focus="focus('editbox')" @blur="blur('editbox')">

  </div>
</template>

<script>
export default {
  name: 'MessageInput',
  data: function () {
    return {
      innerText: this.content.txt,
      bgColor: '#eee'
    }
  },
  props: {
    content: {
      type: Object,
      default: () => {
        return {txt: ''}
      }
    }
  },
  watch: {
    content: {
      handler (newValue, oldValue) {
        if (!this.lock) {
          this.innerText = this.content.txt
        }
      },
      deep: true
    }
  },

  methods: {
    changeTxt: function (e) {
      this.content.txt = this.$el.innerHTML
      if ((e.keyCode === 13 || e.keyCode === 91) && this.$el.innerHTML.length) {
        this.$emit('send')
        this.$el.innerHTML = ''
      }
    },

    focus: function () {
      this.lock = true
      this.bgColor = '#fff'
    },

    blur: function () {
      this.lock = false
      this.bgColor = '#eee'
    }
  }
}
</script>

<style scoped>

</style>

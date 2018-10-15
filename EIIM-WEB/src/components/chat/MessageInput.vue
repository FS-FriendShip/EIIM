<template>
  <div id="editbox" class="box" contenteditable="true" v-html="innerText" @keyup="changeTxt($event)" @focus="lock=true" @blur="lock=false">

  </div>
</template>

<script>
export default {
  name: 'MessageInput',
  data: function () {
    return {
      innerText: this.content.txt
    }
  },
  props: {
    content: {
      type: Object,
      default:  {txt: ''}
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
      if (e.ctrlKey && (e.keyCode === 13 || e.keyCode === 91) && this.$el.innerHTML.length) {
        this.$emit('send')
        this.$el.innerHTML = ''
      }
    }
  }
}
</script>

<style scoped>

</style>

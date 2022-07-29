<template>
  <div>
    <el-button type="primary" @click="aaa">测试按钮</el-button>
    <div>{{ val }}</div>
  </div>

</template>

<script>
export default {
  name: "test",
  data() {
    return {
      val: ''
    }
  },
  created() {
    this.test();
    this.test2();
  },
  methods: {

    aaa() {
      this.test();
      this.test2();
    },

    async test() {
      console.log('1')
      await this.$axios.get('api/test').then(res => {
        if (res.errcode === 0) {
          console.log('请求1')
        }
      }).catch(error => {
        alert(error)
        console.log(error)
      })
      console.log('请求1结束')
    },

    async test2() {
      console.log('2')
      await this.$axios.get('api/get').then(res => {
        if (res.errcode === 0) {
          console.log('请求2')
        }
      }).catch(error => {
        alert(error)
        console.log(error)
      })
      console.log('请求2结束')
    },
  }
}
</script>

<style scoped>

</style>

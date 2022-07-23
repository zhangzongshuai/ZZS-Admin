<template>
<div>
  <el-button @click="init">初始化运费单价</el-button>
</div>
</template>

<script>
export default {
  name: "initFreight",
  data(){
    return{

    }
  },
  methods:{
    init(){
      this.$axios.get("/api/ymdz/initFreight").then(function (res) {
        if (res.errcode === 0){
          alert("初始成功")
        }
        else{
          alert('初始失败')
          console.log(res.errmsg);
        }
      });
    }
  }
}
</script>

<style scoped>

</style>
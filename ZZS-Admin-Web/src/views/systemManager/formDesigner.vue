<template>
  <div>
    <div>
      <fc-designer ref="designer" height="800px">
        <template slot="handle">
          <el-button round type="primary" size="small" @click="saveForm">保 存</el-button>
        </template>
      </fc-designer>
    </div>

  </div>
</template>

<script>
export default {
  name: "formDesigner",
  data(){
    return{

    }
  },
  methods:{
    saveForm(){
      //FcDesigner 生成的`JSON`
      const FcDesignerRule = this.$refs.designer.getRule();
      //FcDesigner 生成的`options`
      const FcDesignerOptions = this.$refs.designer.getOption();

      console.log(FcDesignerRule)
      console.log(FcDesignerOptions)
    }
  }
}
</script>

<style scoped>

</style>

<template>
  <el-select v-model="selectOil"
             :filterable="filterable"
             :clearable="clearable"
             placeholder="请选择油品"
             :popper-class="className"
             :size="size"
             value-key="oilCode"
             @change="selectChange">
    <el-option
        v-for="(item,index) in oilOptions"
        :key="index"
        :label="item.name"
        :value="item">
    </el-option>
  </el-select>
</template>

<script>
export default {
  name: "OilSelect",
  props: {
    currentSelect: String,
    className:{
      type:String,
      default:null
    },
    size:{
      type:String,
      default: 'small',
    },
    filterable:{
      type:Boolean,
      default:true
    },
    clearable:{
      type:Boolean,
      default:true
    },
    index:{
      type:Number,
      default :0
    }
  },
  data(){
    return{
      oilOptions:[],
      selectOil:""
    }
  },
  created() {
    this.selectStation = this.currentSelect;
    this.getOilOptions();
  },
  methods:{
    //获取油品

    getOilOptions() {
      let params = {
        pageNum:1,
        pageSize:5000
      }
      let _this = this;
      this.$axios.get(this.$api.getOilList,{params}).then(function (res) {
        if (res.errcode === 0) {
          _this.oilOptions = res.datas;
        }
        else{
          _this.$message.error(res.errmsg);
        }
      });
    },
    selectChange(val){
      this.$emit("select",{value:val.oilCode,index:this.index,label:val.name});
    },
    getSelectId(){
      return this.selectOil.oilCode;
    },
    getSelectName(){
      return this.selectOil.name;
    }
  },
  watch:{
    // selectStation:function (oldVal,newVal){
    //   console.log("lll")
    //   if (newVal != oldVal){
    //     this.currentSelect = this.selectStation;
    //   }
    // }
  }


}
</script>

<style scoped>

</style>

<template>
  <el-select v-model="selectStation"
             :filterable="filterable"
             :clearable="clearable"
             placeholder="请选择到站"
             :popper-class="className"
             :size="size"
              @change="selectChange">
    <el-option
        v-for="(item,index) in stationOptions"
        :key="index"
        :label="item.station"
        :value="item.station">
    </el-option>
  </el-select>
</template>

<script>
export default {
  name: "StationSelect",
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
      stationOptions:[],
      selectStation:""
    }
  },
  created() {
    this.selectStation = this.currentSelect;
    this.getStationList();
  },
  methods:{
    //获取到站
    getStationList(){
      console.log("ok");
      let _this = this;
      _this.$axios.get(_this.$api.getStationList).then(function (res){
        if (res.errcode === 0){
          _this.stationOptions = res.datas;
        }

      });
    },
    selectChange(val){
      this.$emit("select",{value:val,index:this.index});
    }
  },
  watch:{
  }


}
</script>

<style scoped>

</style>
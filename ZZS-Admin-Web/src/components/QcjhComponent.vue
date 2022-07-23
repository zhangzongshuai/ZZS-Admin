<template>
  <div>

    <el-form label-position="right" label-width="110px" size="small">
      <el-form-item label="油品名称">
        <el-select v-model="qcjhForm.oilName" value-key="id" placeholder="请选择油品" style="width: 300px">
          <el-option
              v-for="item in oilOptions"
              :key="item.id"
              :label="item.name"
              :value="item.name">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="计划发车日期">
        <el-date-picker
            style="width: 300px"
            v-model="qcjhForm.planDate"
            type="date"
            placeholder="选择发车日期"
            :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
    </el-form>


    <el-form :key="index" v-for="(domain, index) in qcjhForm.detail" :inline="true" label-width="110px"
             size="small">
      <el-form-item :label="'到站'+ (index+1)">
        <el-select v-model="domain.endStation"
                   value-key="id"
                   filterable
                   clearable
                   placeholder="请选择到站"
                   size="small"
                   @change="stationChange(index)">
          <el-option
              v-for="(item) in stationOptions"
              :key="item.id"
              :label="item.station"
              :value="item.station">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="车数" label-width="60px">
        <el-input style="width: 80px" v-model="domain.carNum" @blur="carNumChange(domain.carNum,index)"></el-input>
      </el-form-item>
      <el-form-item label="运费" label-width="60px">
        <el-input v-model="domain.freight" readonly></el-input>
      </el-form-item>
      <el-form-item label-width="10px">
        <el-tooltip effect="dark" content="删除到站" placement="bottom" v-if="qcjhForm.detail.length != 1">
          <el-button type="danger" icon="el-icon-delete" circle @click.prevent="removeStation(index)"></el-button>
        </el-tooltip>
        <el-tooltip effect="dark" content="新增到站" placement="bottom" v-if="qcjhForm.detail.length == index +1">
          <el-button type="primary" icon="el-icon-plus" circle @click.prevent="addStation"></el-button>
        </el-tooltip>
      </el-form-item>

    </el-form>
    <el-form label-width="110px" size="small" inline>
      <el-form-item label="运费总计">
        <div style="width: 100px;padding-left: 20px;font-weight: bold">{{ totalFreight }}</div>
      </el-form-item>
      <el-form-item label="总车数">
        <div style="width: 150px;padding-left: 20px;font-weight: bold">{{ totalCarNum }}</div>
      </el-form-item>

    </el-form>
    <el-form label-width="110px" size="small" v-if="!qcjhForm.id">
      <el-form-item>
        <el-button type="primary" @click="onEstimate">确定</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>


  </div>
</template>

<script>

//import {accMul} from "@/common/mathUtil";

export default {
  name: "QcjhComponent",
  props: {
    qcjhData: {
      detail: [],

    },
  },
  data() {
    return {
      currOilType: null,
      oilOptions: [],
      qcjhForm: {},
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '明天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '后天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 2);
            picker.$emit('pick', date);
          }
        }]
      },
      stationOptions: [],
      totalFreight: 0,
      totalCarNum: 0
    }
  },
  created() {
    this.getPlan();

    this.getStationList();
    this.getOilOptions();

    //this.qcjhForm = this.qcjhData;

// let _this = this;
//     setTimeout(function () {
//       _this.getStationList();
//       _this.getOilOptions();
//     }, 1000)


  },
  mounted() {

  },
  methods: {
    getPlan() {
      let _this = this;
      if (this.qcjhData.id) {
        var params = {id: this.qcjhData.id}
        _this.$axios.get(_this.$api.getPlan, {params}).then(function (res) {
          if (res.errcode === 0) {
            _this.qcjhForm = res.data;
            _this.calcFreight();
          } else {
            _this.$message.error(res.errmsg);
            console.error(res.detail_msg);
          }
        });
      } else {
        _this.qcjhForm = _this.qcjhData;
      }
    },
    getStationList() {
      let _this = this;
      _this.$axios.get(_this.$api.getStationList).then(function (res) {
        if (res.errcode === 0) {
          _this.stationOptions = res.datas;
          if (_this.qcjhForm.id) {
            for (let i = 0; i < _this.qcjhForm.detail.length; i++) {
              _this.qcjhForm.detail[i].endStation = _this.stationOptions.filter(function (val) {
                if (val.station == _this.qcjhForm.detail[i].endStation) {
                  return val
                }
              })[0];
            }
          }

        }

      });
    },
    // oilChange(val){
    //   if (val.typeCode.indexOf('gasoline') > -1){
    //       this.currOilType = 'gasoline';//汽油
    //   }
    //   else if (val.typeCode.indexOf('diesel') > -1){
    //     this.currOilType = 'diesel';//柴油
    //   }
    // },


    //新增到站
    addStation() {
      this.qcjhData.detail.push({
        endStation: '',
        carNum: '',
        freight: null
      });
    },
    //删除到站
    removeStation(index) {
      if (index !== -1) {
        this.qcjhForm.detail.splice(index, 1)
      }
    },

    //获取油品
    getOilOptions() {
      let _this = this;
      let params = {
        pageNum:1,
        pageSize:5000
      }
      this.$axios.get(this.$api.getOilList,{params}).then(function (res) {
        if (res.errcode === 0) {
          _this.oilOptions = res.datas;
          if (_this.qcjhForm.id) {
            _this.qcjhForm.oil = _this.oilOptions.filter(function (val) {
              if (val.name == _this.qcjhForm.oilName) {
                return val
              }
            })[0];
          }
        }
      });
    },

    //到站发生变化时计算运费
    stationChange(index) {
      let _this = this;
      if (!this.qcjhForm.oilName) {
        this.$message.warning("请先选择油品")
        return;
      }

      let domain = this.qcjhForm.detail[index];
      if (domain.endStation && domain.carNum) {
        var params = domain;
        var oliName = encodeURIComponent(_this.qcjhForm.oilName)
        _this.$axios.post(_this.$api.calcFreight + "?oilName=" + oliName, params).then(function (res) {
          if (res.errcode === 0) {
            _this.qcjhForm.detail[index].freight = res.data.freight;
            _this.calcFreight();
          } else {
            _this.$message.error('计算失败')
            console.error(res.errmsg);
          }
        });
      } else {
        domain.freight = '';
      }
      //this.calcFreight();


      //
      // if (domain.endStation && domain.carNum) {
      //   let oilPrice = 0;
      //   if (this.qcjhForm.oil.typeCode.indexOf('gasoline') > -1) {
      //     oilPrice = domain.endStation.gasolinePrice;
      //   } else if (this.qcjhForm.oil.typeCode.indexOf('diesel') > -1) {
      //     oilPrice = domain.endStation.dieselPrice;
      //   }
      //   domain.freight = accMul(oilPrice, domain.carNum);
      // } else {
      //   domain.freight = '';
      // }
      // this.calcFreight();
    },

    //车数发生变化时计算运费
    carNumChange(val, index) {
      let _this = this;
      let domain = this.qcjhForm.detail[index];
      if (domain.endStation && val) {
        var params = domain;
        var oliName = encodeURIComponent(_this.qcjhForm.oilName)
        _this.$axios.post(_this.$api.calcFreight + "?oilName=" + oliName, params).then(function (res) {
          if (res.errcode === 0) {
            _this.qcjhForm.detail[index].freight = res.data.freight;
            _this.calcFreight();
          } else {
            _this.$message.error('计算失败')
            console.error(res.errmsg);
          }
        });
      } else {
        domain.freight = '';
      }
     // this.qcjhForm.detail = domain;




      // let detail = this.qcjhForm.detail;
      // if (detail[index].endStation && val) {
      //
      //   let oilPrice = 0;
      //   if (this.qcjhForm.oil.typeCode.indexOf('gasoline') > -1) {
      //     oilPrice = detail[index].endStation.gasolinePrice;
      //   } else if (this.qcjhForm.oil.typeCode.indexOf('diesel') > -1) {
      //     oilPrice = detail[index].endStation.dieselPrice;
      //   }
      //   detail[index].freight = accMul(oilPrice, val);
      // } else {
      //   detail[index].freight = '';
      // }
      // this.qcjhForm.detail = detail;
      // this.calcFreight();
      // if (domain.endStation && domain.carNum) {
      //   let oilPrice = 0;
      //   if (this.qcjhForm.oil.typeCode.indexOf('gasoline') > -1){
      //     oilPrice = domain.endStation.gasolinePrice;
      //   }
      //   else if (this.qcjhForm.oil.typeCode.indexOf('diesel') > -1){
      //     oilPrice = domain.endStation.dieselPrice;
      //   }
      //   domain.freight = accMul(oilPrice,domain.carNum);
      // }
      // let detail = JSON.parse(JSON.stringify(this.qcjhForm.detail));
      // for (let i = 0; i < detail.length; i++) {
      //   let domain = detail[i];
      //   if (domain.endStation && domain.carNum){
      //     domain.freight = 100;
      //   }
      //   else{
      //     domain.freight = '';
      //   }
      // }
      // this.qcjhForm.detail = detail;
    },

    //计算总运费
    calcFreight() {
      // let carNum = this.qcjhForm.detail[index].carNum;
      // this.qcjhForm.detail[index].freight = accMul(oliPrice, carNum);
      // let oilPrice = 0;
      // if (this.qcjhForm.oil.typeCode.indexOf('gasoline') > -1){
      //   oilPrice = detail[index].endStation.gasolinePrice;
      // }
      // else if (this.qcjhForm.oil.typeCode.indexOf('diesel') > -1){
      //   oilPrice = detail[index].endStation.dieselPrice;
      // }
      let total = 0;
      let carNum = 0;
      for (let i = 0; i < this.qcjhForm.detail.length; i++) {
        let domain = this.qcjhForm.detail[i];
        if (domain.freight) {
          total += Number(domain.freight);
        }
        if (domain.carNum) {
          carNum += Number(domain.carNum);
        }
      }
      this.totalFreight = total;
      this.totalCarNum = carNum;
    },
    onEstimate() {
      this.$emit('onSubmit', this.qcjhForm)
    },
    onReset() {
      this.qcjhForm = {
        detail: [{endStation: null, carNum: "", freight: null},
          {endStation: null, carNum: "", freight: null},
          {endStation: null, carNum: "", freight: null},
          {endStation: null, carNum: "", freight: null},
          {endStation: null, carNum: "", freight: null}]
      }
      this.totalCarNum = 0;
      this.totalFreight = 0;
    }
  },
  components: {}
}
</script>

<style scoped>

</style>
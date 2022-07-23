<!--请车计划模块-->
<template>
  <div>
    <el-card class="box-card">
      <div class="mb20">
        <el-alert
            v-if="alertShow"
            title="提示:运费不足,请及时预付运费"
            type="error"
            effect="light"
            :closable="false">
        </el-alert>
        <br>
        <el-card shadow="never" :body-style="{padding: '20px',backgroundColor:'#D7D7D7'}">
          <span class="f16">预估剩余运费:</span>&nbsp;&nbsp;&nbsp;<span class="f32">{{ balance }}</span>&nbsp;元
          <!--        <el-row>-->
          <!--          <el-col :span="20">-->
          <!--            <span class="f16">预估剩余运费:</span>&nbsp;&nbsp;&nbsp;<span class="f32">{{ balance }}</span>&nbsp;元-->
          <!--          </el-col>-->
          <!--          <el-col :span="4">-->
          <!--          </el-col>-->
          <!--        </el-row>-->

        </el-card>

      </div>


      <div :style="{margin:'0 auto',height:dheight}" class="oa">
        <Qcjh-Component :qcjh-data="qcjhForm" ref="qcjh" @onSubmit="onEstimate"></Qcjh-Component>
      </div>

      <!--    <el-row>-->
      <!--      <el-col :offset="5">-->
      <!--        <Qcjh-Component :qcjh-data="qcjhForm" ref="qcjh" @onSubmit = "onEstimate"></Qcjh-Component>-->
      <!--        <el-form label-position="right" label-width="110px" size="small">-->
      <!--          <el-form-item label="油品名称">-->
      <!--            <el-select v-model="qcjhForm.oilName" placeholder="请选择油品" style="width: 90%">-->
      <!--              <el-option-->
      <!--                  v-for="item in oilOptions"-->
      <!--                  :key="item.value"-->
      <!--                  :label="item.label"-->
      <!--                  :value="item.value">-->
      <!--              </el-option>-->
      <!--            </el-select>-->
      <!--          </el-form-item>-->
      <!--          <el-form-item label="计划发车日期">-->
      <!--            <el-date-picker-->
      <!--                style="width: 90%"-->
      <!--                v-model="qcjhForm.time"-->
      <!--                type="date"-->
      <!--                placeholder="选择发车日期">-->
      <!--            </el-date-picker>-->
      <!--          </el-form-item>-->
      <!--        </el-form>-->
      <!--        <el-form :inline="true" label-width="110px" size="small">-->
      <!--          <el-form-item label="到站1">-->
      <!--            <el-select v-model="qcjhForm.endStation" filterable  placeholder="请选择到站">-->
      <!--              <el-option-->
      <!--                  v-for="(item,index) in stationOptions"-->
      <!--                  :key="index"-->
      <!--                  :label="item.station"-->
      <!--                  :value="item.station">-->
      <!--              </el-option>-->
      <!--            </el-select>-->
      <!--          </el-form-item>-->
      <!--          <el-form-item label="车数" label-width="60px">-->
      <!--            <el-input style="width: 80px" v-model="qcjhForm.carNum"></el-input>-->
      <!--          </el-form-item>-->
      <!--          <el-form-item label-width="5px" v-if="qcjhForm.domains.length == 0">-->
      <!--            <el-tooltip effect="dark" content="新增到站" placement="right">-->
      <!--              <el-button type="primary" icon="el-icon-plus" circle @click.prevent="addStation"></el-button>-->
      <!--            </el-tooltip>-->
      <!--          </el-form-item>-->
      <!--        </el-form>-->
      <!--        <el-form :key="domain.key" v-for="(domain, index) in qcjhForm.domains" :inline="true" label-width="110px" size="small">-->
      <!--          <el-form-item :label="'到站'+ (index+2)">-->
      <!--            <el-select v-model="domain.endStation" filterable  placeholder="请选择到站">-->
      <!--              <el-option-->
      <!--                  v-for="(item,index) in stationOptions"-->
      <!--                  :key="index"-->
      <!--                  :label="item.station"-->
      <!--                  :value="item.station">-->
      <!--              </el-option>-->
      <!--            </el-select>-->
      <!--          </el-form-item>-->
      <!--          <el-form-item label="车数" label-width="60px">-->
      <!--            <el-input style="width: 80px" v-model="domain.carNum"></el-input>-->
      <!--          </el-form-item>-->
      <!--          <el-form-item label-width="10px">-->
      <!--            <el-tooltip effect="dark" content="删除到站" placement="bottom">-->
      <!--              <el-button type="danger" icon="el-icon-delete" circle @click.prevent="removeStation(domain)"></el-button>-->
      <!--            </el-tooltip>-->
      <!--            <el-tooltip effect="dark" content="新增到站" placement="bottom" v-if="qcjhForm.domains.length == index +1">-->
      <!--              <el-button type="primary" icon="el-icon-plus" circle @click.prevent="addStation"></el-button>-->
      <!--            </el-tooltip>-->
      <!--          </el-form-item>-->
      <!--        </el-form>-->


      <!--        <el-form label-width="110px" size="small" inline>-->
      <!--          <el-form-item label="运费总计">-->
      <!--            <div style="width: 150px;padding-left: 20px">{{ totalFreight }}</div>-->
      <!--          </el-form-item>-->
      <!--          <el-form-item>-->
      <!--            <el-button type="primary" @click="onEstimate">确定</el-button>-->
      <!--          </el-form-item>-->
      <!--        </el-form>-->


      <!--      </el-col>-->

      <!--      <el-col :span="12">-->

      <!--        <div class="pl50">-->
      <!--          <el-row :gutter="20" class="m20">-->
      <!--            <el-col :span="8">油品名称:&nbsp;&nbsp;&nbsp;&nbsp;<b>{{ tableTitle.oilName }}</b></el-col>-->
      <!--            <el-col :span="12">计划发车时间:&nbsp;&nbsp;&nbsp;&nbsp;<b>{{ tableTitle.time }}</b></el-col>-->
      <!--          </el-row>-->

      <!--          <el-table-->
      <!--              :data="tableData"-->
      <!--              border-->
      <!--              :cell-style="{padding:'2px 0'}"-->
      <!--              show-summary-->
      <!--              style="width: 100%">-->
      <!--            <el-table-column-->
      <!--                prop="endStation"-->
      <!--                label="到站"-->
      <!--                width="180">-->
      <!--            </el-table-column>-->
      <!--            <el-table-column-->
      <!--                prop="carNum"-->
      <!--                label="车数"-->
      <!--                width="180">-->
      <!--            </el-table-column>-->
      <!--            <el-table-column-->
      <!--                prop="freight"-->
      <!--                label="预估运费">-->
      <!--            </el-table-column>-->
      <!--          </el-table>-->

      <!--          <div class="mt30" style="text-align: center">-->
      <!--            <el-button  type="primary" @click="onSubmit" size="small">保存</el-button>-->
      <!--          </div>-->


      <!--        </div>-->

      <!--      </el-col>-->
      <!--    </el-row>-->

    </el-card>
  </div>
</template>

<script>
import QcjhComponent from "@/components/QcjhComponent";
import {showHeight} from "@/common/tableHeight";

export default {
  name: "qcjh",
  data() {
    return {
      balance: 0.00,
      alertShow: false,
      oilOptions: [{
        value: '92#汽油',
        label: '92#汽油'
      },
        {
          value: '95#汽油',
          label: '95#汽油'
        }],
      stationOptions: [],
      qcjhForm: {
        id: null,
        oilName: '',
        planDate: '',
        // domains: [{endStation:'',carNum:"",freight:null},
        //   {endStation:'',carNum:"",freight:null},
        //   {endStation:'',carNum:"",freight:null},
        //   {endStation:'',carNum:"",freight:null},
        //   {endStation:'',carNum:"",freight:null}],
        detail: [{endStation: null, carNum: "", freight: null},
          {endStation: null, carNum: "", freight: null},
          {endStation: null, carNum: "", freight: null},
          {endStation: null, carNum: "", freight: null},
          {endStation: null, carNum: "", freight: null}],

      },
      tableTitle: {
        oilName: "",
        time: ""
      },
      tableData: [],
      totalFreight: 0,
      dheight: 0
    }
  },
  created() {
    this.dheight = showHeight(400) + 'px'
    window.onresize = () => {
      this.dheight = showHeight(400) + 'px'
    }
    console.log(this.dheight)
    this.getBalance();
  },
  methods: {

    //获取当前余额
    getBalance() {
      let _this = this;
      _this.$axios.get(_this.$api.getBalance).then(function (res) {
        if (res.errcode === 0) {
          _this.balance = res.data ? res.data.balance : 0;
          _this.alertShow = _this.balance <= 100000;
        } else {
          _this.$message.error(res.errmsg);
          console.log(res.detail_msg);
        }
      });
    },

    //新增到站
    addStation() {
      this.qcjhForm.domains.push({
        endStation: '',
        carNum: ''
      });
    },
    //删除到站
    removeStation(item) {
      var index = this.qcjhForm.domains.indexOf(item)
      if (index !== -1) {
        this.qcjhForm.domains.splice(index, 1)
      }
    },

    // //预估运费
    // onEstimate(val) {
    //   console.log(val);
    //   let valid = true;
    //   var qcjhForm = this.$refs.qcjh.qcjhForm;
    //
    //   if (qcjhForm.oilName &&  qcjhForm.planDate) {
    //     if (qcjhForm.domains.length > 0) {
    //       for (let i = 0; i < qcjhForm.domains.length; i++) {
    //         const domain = qcjhForm.domains[i];
    //         if (!(domain.endStation && domain.carNum)) {
    //           valid = false;
    //         }
    //       }
    //     }
    //   } else {
    //     valid = false;
    //   }
    //
    //
    //   if (!valid) {
    //     this.$message.error("所有表单均为必填项")
    //   } else {
    //     this.tableTitle.oilName = this.qcjhForm.oilName;
    //     this.tableTitle.time = formatDate(this.qcjhForm.planDate,"Y-m-d")
    //
    //     //计算运费的逻辑
    //
    //
    //     let loading = this.$loading({
    //       text: "正在计算中,请稍后...",
    //       target: document.getElementsByClassName("content-box")[0]
    //     });
    //     var params={
    //       oilName:qcjhForm.oilName,
    //       planDate:qcjhForm.planDate,
    //       detail:qcjhForm.domains
    //     }
    //     console.log(params);
    //     let _this = this;
    //     this.$axios.post(this.$api.calcFreight,params).then(function (res){
    //       if (res.errcode === 0){
    //         _this.tableData = res.data.detail;
    //         loading.close();
    //       }
    //       else{
    //         _this.$message.error(res.errmsg);
    //         console.error(res.detail_msg);
    //         loading.close();
    //       }
    //     });
    //
    //
    //   }
    //
    // },


    //预估运费
    onEstimate(val) {
      console.log(val);
      let valid = true;
      var qcjhForm = val;

      if (qcjhForm.oilName && qcjhForm.planDate) {
        if (qcjhForm.detail.length > 0) {
          for (let i = 0; i < qcjhForm.detail.length; i++) {
            const domain = qcjhForm.detail[i];
            if (!(domain.endStation && domain.carNum)) {
              valid = false;
            }
          }
        }
      } else {
        valid = false;
      }


      if (!valid) {
        this.$message.error("所有表单均为必填项")
      } else {


        //计算运费的逻辑


        let loading = this.$loading({
          text: "操作中,请稍后...",
          target: document.getElementsByClassName("content-box")[0]
        });

        let detail = qcjhForm.detail.map(function (item) {
          var temp = {
            id: null,
            endStation: item.endStation,
            carNum: item.carNum,
            freight: item.freight,
            planId: null
          }

          return temp;
        });
        var params = {
          oilName: qcjhForm.oilName,
          planDate: qcjhForm.planDate,
          detail: detail,
        }
        console.log(params);
        let _this = this;
        this.$axios.post(this.$api.addPlan, params).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success('保存成功!')
            //_this.tableData = res.data.detail;
            loading.close();
            _this.getBalance();
            _this.$router.push({path: "/ygyf"})
          } else {
            _this.$message.error(res.errmsg);
            console.error(res.detail_msg);
            loading.close();
          }
        });


      }

    },

    onSubmit() {

      //此处
      let _this = this;
      let loading = this.$loading({
        text: "操作中,请稍后...",
        target: document.getElementsByClassName("content-box")[0]
      });
      _this.tableData;
      var params = {}
      _this.axios.post(_this.$api.addPlan, params).then();


      setTimeout(function () {
        loading.close();
        _this.$router.push({path: "/ygyf"})
      }, 1500)

    }
  },
  components: {
    "Qcjh-Component": QcjhComponent
  }
}
</script>

<style scoped>
</style>

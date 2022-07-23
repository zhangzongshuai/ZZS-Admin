<template>
  <div>
    <div>
    <!-- 顶部结算信息-->
      <el-row :gutter="5">
        <el-col :span="8">
          <el-card shadow="always" :body-style="{height:'80px'}">
            <div slot="header" class="f20" style="color: #409EFF">计划与完成</div>
            <el-row>
              <el-col :span="12">汽油计划：<span
                  style="font-size: 20px;">{{ planAndFinished[0] === 0 ? '--' : planAndFinished[0] }}</span>&nbsp;吨
              </el-col>
              <el-col :span="12">柴油计划：<span
                  style="font-size: 20px;">{{ planAndFinished[1] === 0 ? '--' : planAndFinished[1] }}</span>&nbsp;吨
              </el-col>
            </el-row>
            <br>
            <el-row>
              <el-col :span="12">汽油结算：<span style="font-size: 20px;">{{ planAndFinished[2] }}</span>&nbsp;吨</el-col>
              <el-col :span="12">柴油结算：<span style="font-size: 20px;">{{ planAndFinished[3] }}</span>&nbsp;吨</el-col>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="always" :body-style="{height:'80px'}">
            <div slot="header" class="f20" style="color: #409EFF">从量与增量</div>
            <el-row>
              <el-col :span="12">汽油从量从价：<span style="font-size: 20px;">{{ gasolineClcj }}</span>&nbsp;吨</el-col>
              <el-col :span="12">柴油从量从价：<span style="font-size: 20px;">{{ dieselClcj }}</span>&nbsp;吨</el-col>
            </el-row>
            <br>
            <el-row>
              <el-col :span="12">汽油增量结算：<span style="font-size: 20px;">{{ gasolineBalance }}</span>&nbsp;吨</el-col>
              <el-col :span="12">柴油增量结算：<span style="font-size: 20px;">{{ dieselBalance }}</span>&nbsp;吨</el-col>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="always" :body-style="{height:'80px'}">
            <div slot="header" class="f20" style="color: #409EFF">结算信息</div>
            <el-row>
              <el-col :span="12">油品：<span style="font-size: 20px;">{{
                  previewData.name ? previewData.name : '--'
                }}</span></el-col>
              <el-col :span="12">到站：<span
                  style="font-size: 20px;">{{ previewData.station ? previewData.station : '--' }}</span></el-col>
            </el-row>
            <el-row>
              <el-col :span="12">车数：<span
                  style="font-size: 20px;">{{ previewData.cs ? previewData.cs : '--' }}</span>&nbsp;
              </el-col>
              <el-col :span="12">数量：<span style="font-size: 20px;">{{ previewData.jsl ? previewData.jsl : '--' }}</span>&nbsp;吨
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">价格：<span
                  style="font-size: 20px;">{{ previewData.price ? previewData.price : '--' }}</span>&nbsp;元/吨
              </el-col>
              <el-col :span="12">金额：<span style="font-size: 20px;">{{ previewData.sum ? previewData.sum : '--' }}</span>&nbsp;元
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-card class="box-card" style="margin-top: 5px">
      <div>
          <el-checkbox v-model="zcjs" border size="small"  @change="zcjsChange">正常结算</el-checkbox>
          <el-checkbox v-model="clcj" border size="small"  @change="clcjChange">从量从价</el-checkbox>
          <el-checkbox v-model="zljs" border size="small"  @change="zljsChange">增量结算</el-checkbox>
      </div>
      <el-form :inline="true" :model="form" size="small" class="mt20">
        <el-form-item label="油品类别">
          <el-select v-model="form.type" placeholder="选择油品类别" clearable>
            <el-option :key="item.id" v-for="item in oilTypes" :label="item.name" :value="item.code"></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="运输方式">-->
<!--          <el-select v-model="form.transportType" clearable placeholder="选择运输方式">-->
<!--            <el-option label="火车" value="1"></el-option>-->
<!--            <el-option label="管输" value="2"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item label="到站">
          <stationSelect @select="selectStation"></stationSelect>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="form.transportType" size="small" @change="search">
            <el-radio-button label="1">铁路</el-radio-button>
            <el-radio-button label="2">管输</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template v-if="form.transportType == 1">
        <el-table
            class="noCheckTable"
            ref="table"
            show-summary
            :summary-method="getSummaries"
            :data="tableData"
            border
            row-key="id"
            :height="tableHeight"
            :max-height="tableHeight"
            :cell-style="{padding:'5px 0'}"
            style="width: 100%"
            @select="selectJLD"
            @selection-change="handleSelectionChange">
          <el-table-column
              type="selection"
              width="80">
          </el-table-column>
          <el-table-column
              type="index"
              label="#"
              width="50">
          </el-table-column>
          <el-table-column
              prop="jldh"
              label="计量单号"
              width="180">
          </el-table-column>
          <el-table-column
              prop="materialname"
              label="油品名称"
              width="180">
          </el-table-column>
          <el-table-column
              prop="gh"
              label="罐号"
              width="100">
          </el-table-column>
          <el-table-column
              prop="stationname"
              label="到站"
              width="100">
          </el-table-column>
          <el-table-column
              prop="CCLDCSJ"
              label="发货时间"
              width="180">
          </el-table-column>
          <el-table-column
              prop="transporttype"
              label="运输方式"
              width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.transporttype==1">火车</span>
              <span v-if="scope.row.transporttype==2">管输</span>
            </template>
          </el-table-column>
          <el-table-column
              prop="cs"
              label="车数"
              width="100">
          </el-table-column>
          <el-table-column
              prop="JSL"
              label="数量(t)"
              width="180">
          </el-table-column>
          <el-table-column
              label="操作">
            <template slot-scope="scope">
              <el-button @click="split(scope.row)" type="text" size="small">拆分计量单</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div style="float: right;margin:10px 100px 10px 0;">
          <el-button type="primary" size="small" @click="onSettlement">铁路结算</el-button>
          <el-button type="primary" size="small" @click="onYujie">预结算</el-button>
        </div>
      </template>

      <template v-if="form.transportType == 2">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-table
                ref="table"
                show-summary
                :summary-method="getSummaries"
                :data="tableData"
                border
                row-key="id"
                :height="tableHeight"
                :max-height="tableHeight"
                :cell-style="{padding:'5px 0'}"
                style="width: 100%">
<!--              <el-table-column-->
<!--                  type="selection"-->
<!--                  width="50">-->
<!--              </el-table-column>-->
              <el-table-column
                  type="index"
                  label="#"
                  width="50">
              </el-table-column>
              <el-table-column
                  prop="jldh"
                  label="计量单号"
                  width="180">
              </el-table-column>
              <el-table-column
                  prop="materialname"
                  label="油品名称"
                  min-width="160">
              </el-table-column>
              <el-table-column
                  prop="gh"
                  label="罐号"
                  width="80">
              </el-table-column>
<!--              <el-table-column-->
<!--                  prop="stationname"-->
<!--                  label="到站"-->
<!--                  width="100">-->
<!--              </el-table-column>-->
              <el-table-column
                  prop="CCLDCSJ"
                  label="发货时间"
                  width="120">
              </el-table-column>
<!--              <el-table-column-->
<!--                  prop="transporttype"-->
<!--                  label="运输方式"-->
<!--                  width="100">-->
<!--                <template slot-scope="scope">-->
<!--                  <span v-if="scope.row.transporttype==1">火车</span>-->
<!--                  <span v-if="scope.row.transporttype==2">管输</span>-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--              <el-table-column-->
<!--                  prop="cs"-->
<!--                  label="车数"-->
<!--                  width="100">-->
<!--              </el-table-column>-->
              <el-table-column
                  fixed="right"
                  prop="JSL"
                  label="数量(t)"
                  min-width="120">
              </el-table-column>
<!--              <el-table-column-->
<!--                  label="操作">-->
<!--                <template slot-scope="scope">-->
<!--                  <el-button @click="split(scope.row)" type="text" size="small">拆分计量单</el-button>-->
<!--                </template>-->
<!--              </el-table-column>-->
            </el-table>
          </el-col>
          <el-col :span="8">
            <div style="border: 0.5px solid #f0f0f0;padding : 20px;">
              <el-form :model="pipeForm" label-width="120px" size="small" label-position="left">
                <el-form-item label="汽油管输未结">
                  <span>{{qywq}}</span>
                </el-form-item>
                <el-form-item label="柴油管输未结">
                  <span>{{cywq}}</span>
                </el-form-item>
                <el-form-item label="油品">
                  <oilSelect ref="pipeOil" @select="selectPipeOil"></oilSelect>
                </el-form-item>
                <!--              <el-form-item label="到站">-->
                <!--                <stationSelect @select="selectYujieStation"></stationSelect>-->
                <!--              </el-form-item>-->
                <!--              <el-form-item label="车数">-->
                <!--                <el-input v-model="yujieForm.cs" autocomplete="off"></el-input>-->
                <!--              </el-form-item>-->
                <el-form-item label="数量">
                  <el-input v-model="pipeForm.jsl" autocomplete="off" @blur="inputJsl"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="settlementPipe">管输结算</el-button>
                  <!--                <el-button>取消</el-button>-->
                </el-form-item>
              </el-form>
            </div>

          </el-col>
        </el-row>
      </template>

    </el-card>

    <el-dialog
        title="拆分计量单"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="splitDialog"
        width="40%">
      <el-form :model="splitForm" label-width="80px" size="small" inline>
        <el-form-item label="车数">
          <el-input v-model="splitForm.cs" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="splitForm.jsl" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="splitDialog = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitSplit" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        title="预结算"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="yujieDialog"
        width="30%">
      <el-form :model="yujieForm" label-width="80px" size="small">
        <el-form-item label="油品">
          <oilSelect @select="selectYujieOil"></oilSelect>
        </el-form-item>
        <el-form-item label="到站">
          <stationSelect @select="selectYujieStation"></stationSelect>
        </el-form-item>
        <el-form-item label="车数">
          <el-input v-model="yujieForm.cs" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="yujieForm.jsl" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="yujieDialog = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitYujie" size="small">确 定</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";
import OilSelect from "@/components/OilSelect";
import StationSelect from "@/components/StationSelect";
import {accAdd} from "@/common/mathUtil";

export default {
  name: "settlement",
  data() {
    return {
      planAndFinished: [0, 0, 0, 0],
      tableHeight: null,
      currentPlanArr: [],
      finishedArr: [],
      settlementType: ['1'],
      oilTypes:[],
      zcjs: true,
      clcj: false,
      zljs: false,
      yjs: false,
      form: {
        transportType:1,
      },
      tableData: [],
      checkedData: [],

      dialogShow: false,
      zlDialogShow: false,
      cjclTableData: [],
      gasolinePriceRadio: "",
      gasolinePrice: [],
      dieselPrice: [],
      zlInfluencePrice: -1000,
      zlTableData: [{
        gasoline: 10750000,
        diesel: 8380000,
        tatol: 19140000
      }],


      //增量结算未结清
      gasolineBalance: 0,
      dieselBalance: 0,

      //从量从价未结清
      gasolineClcj: 0,
      dieselClcj: 0,

      splitDialog: false,
      splitForm: {
        id: null,
        CS: null,
        JSJLD: null
      },
      //确认的从量从价差价
      submitPrice: {
        gasoline: null,
        diesel: null
      },
      yujieDialog: false,
      yujieForm: {},
      tjDate: '2020年9月20日',
      previewData: {},
      ysfs:'铁路',
      pipeForm:{},
      qywq:0.0,
      cywq:0.0
    }
  },
  created() {
    this.tableHeight = showHeight(500)
    window.onresize = () => {
      this.tableHeight = showHeight(500)
    }
    // this.tableHeight = showHeight(600) / 2
    // window.onresize = () => {
    //   this.tableHeight = showHeight(600) / 2
    // }
    this.getPlanAndFinished();
    this.getZLBalance();
    this.getClcjResidue();
    //this.getCurrentPlan();
    //this.getSettlementFinished();
    this.getGaugeTicket();
    this.getOilType();
    this.getPipeNotClear();
  },
  methods: {

    //计划与完成
    getPlanAndFinished() {
      this.$axios.get(this.$api.getPlanAndFinished).then(res => {
        if (res.errcode === 0) {
          this.planAndFinished = res.data;
        } else {
          this.$message.error(res.errmsg)
        }
      })
    },


    //配置计划
    getCurrentPlan() {
      let _this = this;
      _this.$axios.get(_this.$api.currentPlan).then(function (res) {
        if (res.errcode === 0) {
          _this.currentPlanArr = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    //已完成
    getSettlementFinished() {

      let _this = this;
      _this.$axios.get(_this.$api.getSettmentFinished).then(function (res) {
        if (res.errcode === 0) {
          _this.finishedArr = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },


//获取增量余额
    getZLBalance() {
      let _this = this;
      _this.$axios.get(_this.$api.getIncrementBalance).then(function (res) {
        if (res.errcode === 0) {
          _this.gasolineBalance = res.data.gasolineBalance;
          _this.dieselBalance = res.data.dieselBalance;
        } else {
          _this.$message.error(res.errmsg);
        }
      })
    },

    //从量从价未结清
    getClcjResidue() {
      let _this = this;
      this.$axios.get(this.$api.clcjResidue).then(function (res) {
        if (res.errcode === 0) {
          _this.dieselClcj = res.data.diesel;
          _this.gasolineClcj = res.data.gasoline;
        }
      });
    },


    // 获取油品类别
    getOilType() {
      let params = {
        level: 3
      }
      this.$axios.get(this.$api.getOilTypeList, {params}).then(res => {

        if (res.errcode == 0) {
          this.oilTypes = res.datas;
        } else {
          this.$message.error(res.errmsg);
        }
      });
    },


    // 未结算的计量单
    getGaugeTicket() {
      // this.checkedData = [];
      // this.previewData = {};
      let _this = this;
      let params = {
        type: this.form.type,
        transportType: this.form.transportType,
        endStation: this.form.endStation,
        sTime: this.form.time ? this.form.time[0] : null,
        eTime: this.form.time ? this.form.time[1] : null,
      }
      _this.$axios.get(_this.$api.getJSJLD, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.$nextTick(()=>{
            _this.$refs.table.doLayout();
          })
          // setTimeout(function () {
          //   console.log(1)
          //   if (_this.checkedData.length > 0) {
          //     _this.checkedData.forEach(row => {
          //       _this.$refs.table.toggleRowSelection(row, true);
          //     });
          //   }
          // }, 500);

        } else {
          _this.$message.error(res.errmsg);
        }
      });
    }
    ,

    // selectOil(val) {
    //   console.log(val)
    //   this.form.oilName = val.label;
    // },

    selectStation(val) {
      this.form.endStation = val.value;
    }
    ,
    selectJLD(selection, row) {
      if (selection.indexOf(row) >= 0) {

        if (this.checkedData.length > 0) {
          if (this.checkedData[0].materialname != row.materialname) {
            this.$message.error("请选择同一个油品");
            this.$refs.table.toggleRowSelection(row);
            return;
          }
          // else if (this.checkedData[0].stationname != row.stationname) {
          //   this.$message.error("同选择同一个到站");
          //   this.$refs.table.toggleRowSelection(row);
          //   return;
          // } else if (this.checkedData[0].transportType != row.transportType) {
          //   this.$message.error("同选择同一个运输方式");
          //   this.$refs.table.toggleRowSelection(row);
          //   return;
          // }
        }
        this.checkedData.push(row)
      } else {
        this.checkedData.splice(this.checkedData.indexOf(row), 1)
      }
      this.settlementPreview(this.checkedData);
    }
    ,
    handleSelectionChange() {
      //this.checkedData = val;
    }
    ,
    settlementTypeChange(val) {
      var index = this.settlementType.indexOf('1');
      var ind = this.settlementTypeCoby.indexOf('1');
      if (index !== -1 && ind !== -1) {
        this.settlementType = ["1"]
      } else if (index !== -1 && ind === -1) {
        this.settlementType = ["1"]
      } else {
        this.settlementType.splice(index, 1);
      }
      console.log(val)
    }
    ,
    checkClick(val) {
      console.log(val)
      if (val === '1') {
        this.settlementType = ["1"]
      } else {
        var index = this.settlementType.indexOf('1');
        if (index !== -1) {
          this.settlementType.splice(index, 1);
        }
      }
    }
    ,
    zcjsChange() {
      this.zcjs = true;
      if (this.clcj || this.zljs) {
        this.clcj = false;
        this.zljs = false;
      }
      this.settlementPreview(this.checkedData);
    }
    ,
    clcjChange() {
      if (!this.clcj && !this.zljs) {
        this.zcjs = true;
      } else if (this.zcjs) {
        this.zcjs = false;
      }
      this.settlementPreview(this.checkedData);
    }
    ,
    priceFormater(row) {
      if (!row.diffPrice) {
        return "按照旧价格结算"
      } else {
        return row.diffPrice;
      }
    }
    ,
    zljsChange() {
      if (!this.clcj && !this.zljs) {
        this.zcjs = true;
        return;
      }
      if (this.zcjs) {
        this.zcjs = false;
      }
      if (this.zljs) {
        //this.zlDialogShow = true;
      }
      this.settlementPreview(this.checkedData);
    }
    ,
    yjsChange() {

    }
    ,
    search() {
      this.tableData = [];
      this.checkedData = [];
      this.previewData={};
      this.getGaugeTicket();
    }
    ,
//拆分计量单
    split(row) {
      this.splitForm = {};
      if (row.isSplit === 0) {
        this.splitForm.parentid = row.id;
        this.splitForm.isSplit = row.isSplit;
      } else {
        this.splitForm.id = row.id;
        this.splitForm.parentid = row.parentid;
        this.splitForm.isSplit = row.isSplit;
      }

      if (this.clcj && !this.zljs) {
        if (row.materialname.indexOf("汽油") >= 0) {
          this.splitForm.jsl = this.gasolineClcj
        } else if (row.materialname.indexOf("柴油") >= 0) {
          this.splitForm.jsl = this.dieselClcj
        }

      } else if (this.clcj && !this.zljs) {
        if (row.materialname.indexOf("汽油") >= 0) {
          this.splitForm.jsl = this.gasolineBalance
        } else if (row.materialname.indexOf("柴油") >= 0) {
          this.splitForm.jsl = this.dieselBalance;
        }
      }

      this.splitDialog = true;
    }
    ,
//确定拆分计量单
    submitSplit() {
      let _this = this;
      let params = _this.splitForm;
      _this.$axios.post(_this.$api.splitJSJLD + '?type=' + _this.splitForm.isSplit, params).then(function (res) {
        if (res.errcode === 0) {
          _this.splitDialog = false;
          _this.getGaugeTicket();
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    }
    ,
    remove(id, index) {
      this.checkedData.splice(index, 1);
      var row = this.tableData.filter(function (item) {
        return item.id == id;
      });
      this.$refs.table.toggleRowSelection(row[0], false);
    }
    ,

//结算
    onSettlement() {
      let _this = this;
      if (this.checkedData.length == 0) {
        this.$message.error("请选择计量单")
      } else {
        let type = 1;
        let types = [];
        if (this.zcjs) {
          types.push(1);
          console.log('正常结算')
        }
        if (this.clcj) {
          types.push(2);
          console.log('从量从价')
        }
        if (this.zljs) {
          types.push(3)
          console.log('增量结算')
        }
        type = types.join(",");
        console.log(this.checkedData);
        let params = this.checkedData;
        this.$axios.post(this.$api.settlement + '?type=' + type, params).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success('结算成功');
            _this.checkedData = [];
            _this.getPlanAndFinished();
            _this.getZLBalance();
            _this.getClcjResidue();
            _this.getCurrentPlan();
            _this.getGaugeTicket();
            _this.previewData={};

          } else {
            _this.$message.error(res.errmsg);
          }
        });

      }

    }
    ,
//选择汽油从量从价价格
    chooseGasolinePrice(val) {
      console.log(val);

      this.submitPrice.gasoline = val
    }
    ,
//柴油从量从价价格
    chooseDieselPrice(val) {
      console.log(val);
      this.submitPrice.diesel = val
    }
    ,
//确定从量从价结算价格
    confirmPrice() {
      let _this = this;
      let params =
          {
            gasoline: {
              type: "gasoline",
              diffPrice: this.submitPrice.gasoline.diffPrice,
              amount: this.submitPrice.gasoline.amount,
            },
            diesel: {
              type: "diesel",
              diffPrice: this.submitPrice.diesel.diffPrice,
              amount: this.submitPrice.diesel.amount
            }
          }
      //记录选择从量从价价格与量
      _this.$axios.post(_this.$api.recordPrice, params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("操作成功")
          _this.dialogShow = false;
        } else {
          _this.$message.error(res.errmsg);
        }
      });

    }
    ,

    cancelPrice() {
      this.clcj = false;
      this.zcjs = true;
      this.dialogShow = false;
    }
    ,
    onYujie() {
      let _this = this;
      _this.yujieDialog = true;
    }
    ,
//选择预结算油品
    selectYujieOil(val) {
      console.log(val);
      this.yujieForm.oilName = val.label;
    }
    ,
//选择预结算到站
    selectYujieStation(val) {
      this.yujieForm.dz = val.value;
    }
    ,
//预计算
    submitYujie() {
      let _this = this;
      let type = 1;
      let types = [];
      if (this.zcjs) {
        types.push(1);
        console.log('正常结算')
      }
      if (this.clcj) {
        types.push(2);
        console.log('从量从价')
      }
      if (this.zljs) {
        types.push(3)
        console.log('增量结算')
      }
      type = types.join(",");
      _this.yujieForm.ysfs = 1;
      let params = _this.yujieForm;
      params.jsfs = type;
      _this.$axios.post(_this.$api.yujie + "?type="+type +"&yjs=1", params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("结算成功")
          _this.getGaugeTicket();
          _this.getZLBalance();
          _this.yujieDialog = false;
        } else {
          _this.$message.error(res.errmsg);
        }
      });


    },
    // 预览结算信息
    settlementPreview(data) {
      let that = this;
      if (data.length == 0) {
        that.previewData = {}
      } else {
        let type = 1;
        let types = [];
        if (this.zcjs) {
          types.push(1);
        }
        if (this.clcj) {
          types.push(2);
        }
        if (this.zljs) {
          types.push(3)
        }
        type = types.join(",");
        let params = data;

        that.$axios.post(that.$api.settlementPreview + '?type=' + type, params).then(function (res) {
          if (res.errcode === 0) {
            that.previewData = res.data;
          } else {
            //that.zcjsChange();
            that.$message.error(res.errmsg)
          }
        });
      }
    },
    selectPipeOil(val){
      this.checkedData = [];
      this.pipeForm.oilType = val.value;
      this.pipeForm.oilName = val.label;
      if(this.pipeForm.jsl){
        let obj = {
          JSL: this.pipeForm.jsl,
          cs: 1,
          jsl: this.pipeForm.jsl,
          materialname: val.label,
          stationname: "沧炼管输站",
          transporttype: 2,
          type: this.pipeForm.oilType,
        }
        this.checkedData.push(obj)
        this.settlementPreview(this.checkedData);
      }
      else{
        this.previewData = {}
      }
    },
    inputJsl(){
      this.checkedData = [];
      if(this.pipeForm.jsl && this.pipeForm.oilName){
        let obj = {
          JSL: this.pipeForm.jsl,
          cs: 1,
          jsl: this.pipeForm.jsl,
          materialname: this.pipeForm.oilName,
          stationname: "沧炼管输站",
          transporttype: 2,
          type: this.pipeForm.oilType,
        }
        this.checkedData.push(obj)
        this.settlementPreview(this.checkedData);
      }
      else{
        this.previewData = {}
      }
    },
    // 结算管输
    settlementPipe(){
      let _this = this;
      let type = 1;
      let types = [];
      if (this.zcjs) {
        types.push(1);
        console.log('正常结算')
      }
      if (this.clcj) {
        types.push(2);
        console.log('从量从价')
      }
      if (this.zljs) {
        types.push(3)
        console.log('增量结算')
      }
      type = types.join(",");
      this.pipeForm.oilName = this.$refs.pipeOil.getSelectName();
      this.pipeForm.oilType = this.$refs.pipeOil.getSelectId();
      this.pipeForm.ysfs = '2';
      this.pipeForm.cs = 1;
      this.pipeForm.dz = "沧炼管输站";
      let params = _this.pipeForm;
      params.type = type;
      _this.$axios.post(_this.$api.yujie + "?type="+type + "&yjs=3", params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("结算成功")
          _this.getGaugeTicket();
          _this.getZLBalance();
          _this.getPipeNotClear();
          _this.previewData = {};
          _this.pipeForm = {};
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    getPipeNotClear(){
      this.$axios.get(this.$api.getPipeNotClear).then(res=>{
        if (res.errcode === 0){
          this.qywq = res.data[0];
          this.cywq = res.data[1];
        }
      })
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        if ( column.property == 'cs' ||column.property == 'JSL'){
          const values = data.map(item => Number(item[column.property]));
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return accAdd(prev,curr);//prev + curr;
              } else {
                return prev;
              }
            }, 0);
            // sums[index] += ' 元';
          } else {
            sums[index] = 'N/A';
          }
        }
      });

      return sums;
    },
  },
  components: {
    "oilSelect":
    OilSelect,
    "stationSelect":
    StationSelect
  }

}
</script>

<style scoped>
li {
  margin-bottom: 5px;
}
</style>

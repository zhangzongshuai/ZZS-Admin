<template>
  <div>
    <div>
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

    <el-card class="box-card" style="margin-top: 10px">
      <div>
        <el-checkbox v-model="zcjs" border size="small"  @change="zcjsChange">正常结算</el-checkbox>
        <el-checkbox v-model="clcj" border size="small"  @change="clcjChange">从量从价</el-checkbox>
        <el-checkbox v-model="zljs" border size="small"  @change="zljsChange">增量结算</el-checkbox>
      </div>

      <el-tabs value="first">
        <el-tab-pane label="用户管理" name="first">

        </el-tab-pane>
        <el-tab-pane label="配置管理" name="second">


        </el-tab-pane>
      </el-tabs>

      <el-form :inline="true" :model="form" size="small" class="mt20">
        <el-form-item label="油品类别">
          <el-select v-model="form.type" placeholder="选择油品类别" clearable>
            <el-option :key="item.id" v-for="item in oilTypes" :label="item.name" :value="item.code"></el-option>
          </el-select>
          <!--          <oilSelect @select="selectOil"></oilSelect>-->
        </el-form-item>
        <el-form-item label="运输方式">
          <el-select v-model="form.transportType" clearable placeholder="选择运输方式">
            <el-option label="火车" value="1"></el-option>
            <el-option label="管输" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="到站">
          <!--        <el-select v-model="form.endStation" clearable placeholder="选择到站">-->
          <!--          <el-option label="保定" value="92"></el-option>-->
          <!--          <el-option label="二到沙河" value="95"></el-option>-->
          <!--        </el-select>-->
          <stationSelect @select="selectStation"></stationSelect>
        </el-form-item>
        <!--      <el-form-item label="发货时间">-->
        <!--        <el-date-picker-->
        <!--            v-model="form.time"-->
        <!--            type="daterange"-->
        <!--            value-format="yyyy-MM-dd"-->
        <!--            range-separator="至"-->
        <!--            start-placeholder="开始日期"-->
        <!--            end-placeholder="结束日期">-->
        <!--        </el-date-picker>-->
        <!--      </el-form-item>-->
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
      </el-form>

      <!--      <el-radio-group v-model="form.type">-->
      <!--        <el-radio label="正常结算"></el-radio>-->
      <!--        <el-radio label="从量从价"></el-radio>-->
      <!--        <el-radio label="增量结算"></el-radio>-->
      <!--      </el-radio-group>-->


      <el-table
          ref="table"
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
            width="50">
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

      <!--      <br>-->
      <!--      <el-table-->
      <!--          ref="checkTable"-->
      <!--          :data="checkedData"-->
      <!--          border-->
      <!--          :height="tableHeight"-->
      <!--          :cell-style="{padding:'5px 0'}"-->
      <!--          style="width: 100%">-->
      <!--        <el-table-column-->
      <!--            type="index"-->
      <!--            label="#"-->
      <!--            width="50">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column-->
      <!--            prop="jldh"-->
      <!--            label="计量单号"-->
      <!--            width="100">-->
      <!--        </el-table-column>-->

      <!--        <el-table-column-->
      <!--            prop="materialname"-->
      <!--            label="油品名称"-->
      <!--            width="180">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column-->
      <!--            prop="gh"-->
      <!--            label="罐号"-->
      <!--            width="100">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column-->
      <!--            prop="stationname"-->
      <!--            label="到站"-->
      <!--            width="100">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column-->
      <!--            prop="CCLDCSJ"-->
      <!--            label="发货时间"-->
      <!--            width="150">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column-->
      <!--            prop="transportType"-->
      <!--            label="运输方式"-->
      <!--            width="100">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column-->
      <!--            prop="cs"-->
      <!--            label="车数"-->
      <!--            width="100">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column-->
      <!--            prop="JSL"-->
      <!--            label="数量(t)">-->
      <!--        </el-table-column>-->
      <!--        &lt;!&ndash;      <el-table-column&ndash;&gt;-->
      <!--        &lt;!&ndash;          prop="price"&ndash;&gt;-->
      <!--        &lt;!&ndash;          label="结算单价"&ndash;&gt;-->
      <!--        &lt;!&ndash;          width="100">&ndash;&gt;-->
      <!--        &lt;!&ndash;      </el-table-column>&ndash;&gt;-->
      <!--        &lt;!&ndash;      <el-table-column&ndash;&gt;-->
      <!--        &lt;!&ndash;          prop="jlprice"&ndash;&gt;-->
      <!--        &lt;!&ndash;          label="增量影响单价"&ndash;&gt;-->
      <!--        &lt;!&ndash;          width="100">&ndash;&gt;-->
      <!--        &lt;!&ndash;      </el-table-column>&ndash;&gt;-->
      <!--        &lt;!&ndash;      <el-table-column&ndash;&gt;-->
      <!--        &lt;!&ndash;          prop="clprice"&ndash;&gt;-->
      <!--        &lt;!&ndash;          label="从量从价影响单价"&ndash;&gt;-->
      <!--        &lt;!&ndash;          width="100">&ndash;&gt;-->
      <!--        &lt;!&ndash;      </el-table-column>&ndash;&gt;-->
      <!--        <el-table-column-->

      <!--            label="操作">-->
      <!--          <template slot-scope="scope">-->
      <!--            <el-button @click="remove(scope.row.id,scope.$index)" type="text" size="small">移除</el-button>-->
      <!--          </template>-->
      <!--        </el-table-column>-->
      <!--      </el-table>-->
      <!--      <br>-->
      <!--      <el-row :gutter="20" type="flex" justify="space-between">-->
      <!--        <el-col :span="16">-->
      <!--          <span class="ml20">到站：{{ previewData.station }}</span>-->
      <!--          <span class="ml20">结算单价：{{ previewData.price }}</span>-->
      <!--          <span class="ml20">结算量：{{ previewData.jsl }}</span>-->
      <!--          <span class="ml20">结算金额：{{ previewData.sum }}</span>-->
      <!--        </el-col>-->
      <!--        <el-col :span="4">-->
      <!--          <el-button type="primary" size="small" @click="onSettlement">结算</el-button>-->
      <!--          <el-button type="primary" size="small" @click="onYujie">预结算</el-button>-->
      <!--        </el-col>-->
      <!--      </el-row>-->
      <div style="float: right;margin:10px 100px 10px 0;">
        <el-button type="primary" size="small" @click="onSettlement">结算</el-button>
        <el-button type="primary" size="small" @click="onYujie">预结算</el-button>
      </div>
    </el-card>
    <!--从量从价-->
    <el-dialog
        v-dialogDrag
        title="从量从价"
        :visible.sync="dialogShow"
        width="50%">

      <div>
        <span class="f20">{{ tjDate }}调价前从量从价计划对账表</span>

        <el-table
            :data="cjclTableData"
            border
            :cell-style="{padding:'5px 0'}"
            style="width: 100%">


          <el-table-column
              prop="typeName"
              label="油品"
              width="50">
          </el-table-column>

          <el-table-column
              prop="planAmount"
              label="本月应交(t)">
          </el-table-column>
          <el-table-column
              prop="lastMonthCql"
              label="上月累计超欠(t)">
          </el-table-column>
          <el-table-column
              prop="totalAmount"
              label="合计应交(t)">
          </el-table-column>
          <el-table-column
              prop="realAmount"
              label="实际结算(t)">
          </el-table-column>
          <el-table-column
              prop="diffAmount"
              label="结算超欠(t)">
          </el-table-column>

        </el-table>
        <!--        <div class="mt10">本月实际结算汽油50531.936吨,超结8031.936吨</div>-->
        <!--        <div class="mt10">实际结算柴油58877.643吨,超结4977.643吨</div>-->
        <!--        <div class="mt10 f12">点击选择合适价格执行从量从价</div>-->
        <br>
        <div>
          <el-table
              :data="gasolinePrice"
              max-height="180"
              border
              highlight-current-row
              :cell-style="{padding:'5px 0'}"
              @current-change="chooseGasolinePrice"
              style="width: 100%">
            <el-table-column
                label="汽油(点击行选择合适的倍数执行汽油从量从价)">
              <el-table-column
                  prop="no"
                  label="倍数"
                  width="180">
              </el-table-column>
              <el-table-column
                  prop="amount"
                  label="从量从价数量"
                  width="180">
              </el-table-column>
              <el-table-column
                  prop="diffPrice"
                  label="影响价格"
                  :formatter="priceFormater">
              </el-table-column>
              <!--              <el-table-column-->
              <!--                  prop="realPrice"-->
              <!--                  label="结算价格">-->
              <!--              </el-table-column>-->
            </el-table-column>
          </el-table>
          <!--          <el-radio-group v-model="gasolinePriceRadio">-->
          <!--            <el-radio :label="1">2倍:</el-radio>-->
          <!--            <el-radio :label="2">备选项</el-radio>-->
          <!--            <el-radio :label="3">备选项</el-radio>-->
          <!--          </el-radio-group>-->


          <div class="mt20"></div>
          <el-table
              :data="dieselPrice"
              max-height="180"
              border
              highlight-current-row
              @current-change="chooseDieselPrice"
              :cell-style="{padding:'5px 0'}"
              style="width: 100%">
            <el-table-column
                label="柴油(点击行选择合适的倍数执行柴油从量从价)">
              <el-table-column
                  prop="no"
                  label="倍数"
                  width="180">
              </el-table-column>
              <el-table-column
                  prop="amount"
                  label="从量从价数量"
                  width="180">
              </el-table-column>
              <el-table-column
                  prop="diffPrice"
                  label="影响价格"
                  :formatter="priceFormater">
              </el-table-column>
              <!--              <el-table-column-->
              <!--                  prop="realPrice"-->
              <!--                  label="结算价格">-->
              <!--              </el-table-column>-->
            </el-table-column>
          </el-table>

        </div>

      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelPrice" size="small">取 消</el-button>
        <el-button type="primary" @click="confirmPrice" size="small">确 定</el-button>
      </span>
    </el-dialog>

    <!--增量结算-->
    <el-dialog
        v-dialog-drag
        title="增量结算"
        :close-on-click-modal="false"
        :visible.sync="zlDialogShow"
        width="30%">
      <el-table
          :data="zlTableData"
          border
          style="width: 100%">
        <el-table-column
            prop="tatol"
            label="合计">
        </el-table-column>
        <el-table-column
            prop="gasoline"
            label="汽油">
        </el-table-column>
        <el-table-column
            prop="diesel"
            label="柴油">
        </el-table-column>
      </el-table>
      <br>
      <div>
        <el-form label-width="140px" size="small">
          <el-form-item label="汽油补贴单价:">
            <el-input v-model="zlInfluencePrice"></el-input>
          </el-form-item>
          <el-form-item label="柴油补贴单价:">
            <el-input v-model="zlInfluencePrice"></el-input>
          </el-form-item>
        </el-form>
      </div>

      <span slot="footer" class="dialog-footer">
    <el-button @click="zlDialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="zlDialogShow = false" size="small">确 定</el-button>
  </span>
    </el-dialog>


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
      form: {},
      tableData: [],
      checkedData: [],

      dialogShow: false,
      zlDialogShow: false,
      cjclTableData: [
        // {oilName: "合计", sum: 96400, currMonthPlan: 89400, lastMonth: -7000},
        // {oilName: "汽油", sum: 42500, currMonthPlan: 45000, lastMonth: 2500},
        // {oilName: "柴油", sum: 53900, currMonthPlan: 44400, lastMonth: -9500},
      ],
      gasolinePriceRadio: "",
      gasolinePrice: [
        //     {
        //   multipleNum: 1,
        //   weight: 8031.936,
        //   affectPrice: "-334",
        //   realPrice: 6088
        // },
        //   {
        //     multipleNum: 2,
        //     weight: 4015.968,
        //     affectPrice: "-668",
        //     realPrice: 5754
        //   },
      ],
      dieselPrice: [
        //     {
        //   multipleNum: 1,
        //   weight: 4977.643,
        //   affectPrice: "-300",
        //   realPrice: 4823
        // }
      ],
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
          setTimeout(function () {
            console.log(1)
            if (_this.checkedData.length > 0) {
              _this.checkedData.forEach(row => {
                _this.$refs.table.toggleRowSelection(row, true);
              });
            }
          }, 500);

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
      this.settlementPreview();
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
      this.settlementPreview();
    }
    ,
    clcjChange() {
      if (!this.clcj && !this.zljs) {
        this.zcjs = true;
      } else if (this.zcjs) {
        this.zcjs = false;
      }
      // if (this.clcj) {
      //
      //   let _this = this;
      //
      //   _this.$axios.get(_this.$api.isclcj).then(function (res) {
      //     if (res.errcode === 0) {
      //       if (res.data) {
      //
      //         //从量从价对账
      //         _this.$axios.get(_this.$api.clcjdz).then(function (res) {
      //           if (res.errcode === 0) {
      //             _this.cjclTableData = res.datas;
      //           } else {
      //             _this.$message.error(res.errmsg);
      //           }
      //         });
      //
      //
      //         //汽油从量从价信息
      //         _this.$axios.get(_this.$api.clcjPrice + "?type=1").then(function (res) {
      //           if (res.errcode === 0) {
      //             _this.gasolinePrice = res.datas;
      //           } else {
      //             _this.$message.error(res.errmsg);
      //           }
      //         });
      //
      //         //柴油从量从价信息
      //         _this.$axios.get(_this.$api.clcjPrice + "?type=2").then(function (res) {
      //           if (res.errcode === 0) {
      //             _this.dieselPrice = res.datas;
      //           } else {
      //             _this.$message.error(res.errmsg);
      //           }
      //         });
      //         _this.dialogShow = true;
      //       }
      //     } else {
      //       _this.$message.error(res.errmsg);
      //     }
      //   });
      //
      //
      // }
      this.settlementPreview();
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
      this.settlementPreview();
    }
    ,
    yjsChange() {

    }
    ,
    search() {
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

      let params = _this.yujieForm;
      params.jsfs = type;
      _this.$axios.post(_this.$api.yujie + "?type="+type, params).then(function (res) {
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
    settlementPreview() {
      let that = this;
      if (this.checkedData.length == 0) {
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
        let params = this.checkedData;

        that.$axios.post(that.$api.settlementPreview + '?type=' + type, params).then(function (res) {
          if (res.errcode === 0) {
            that.previewData = res.data;
          } else {
            //that.zcjsChange();
            that.$message.error(res.errmsg)
          }
        });
      }
    }
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

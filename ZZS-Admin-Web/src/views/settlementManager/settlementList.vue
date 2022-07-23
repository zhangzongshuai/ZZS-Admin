<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="serchParams" size="small">
        <el-form-item label="油品">
                  <el-select v-model="serchParams.type" placeholder="选择油品" clearable>
                    <el-option :key="item.id" v-for="item in oilTypes" :label="item.name" :value="item.code"></el-option>
                  </el-select>
<!--          <oilSelect @select="selectOil"></oilSelect>-->
        </el-form-item>
        <el-form-item label="到站">
          <!--        <el-select v-model="serchParams.oil" placeholder="选择到站">-->
          <!--          <el-option label="保定" value="92"></el-option>-->
          <!--          <el-option label="石家庄" value="95"></el-option>-->
          <!--        </el-select>-->
          <stationSelect @select="selectStation"></stationSelect>
        </el-form-item>
        <el-form-item label="运输方式">
          <el-select v-model="serchParams.ysfs" placeholder="选择运输方式" clearable>
            <el-option label="铁路" value="1"></el-option>
            <el-option label="管输" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="结算日期">
<!--          <el-date-picker-->
<!--              v-model="serchParams.date"-->
<!--              type="date"-->
<!--              value-format="yyyy-MM-dd"-->
<!--              placeholder="选择日期">-->
<!--          </el-date-picker>-->
          <el-date-picker
              v-model="serchParams.date"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-finished" @click="pipeClear">管输清算</el-button>
        </el-form-item>
      </el-form>
<!--       :summary-method="getSummaries"-->
      <el-table
          ref="tableData"
          :data="tableData"
          border
          :height="tableHeight"
          :cell-style="{padding:'5px 0'}"
          show-summary
          :summary-method="getSummaries"
          style="width: 100%">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <!--      <el-table-column-->
        <!--          prop="fhdh"-->
        <!--          label="发货单号"-->
        <!--          width="80">-->
        <!--      </el-table-column>-->
<!--        <el-table-column-->
<!--            prop="fhsj"-->
<!--            label="发货日期"-->
<!--            width="100">-->
<!--        </el-table-column>-->
        <!--      <el-table-column-->
        <!--          prop="zzsph"-->
        <!--          label="增值税票号"-->
        <!--          width="80">-->
        <!--      </el-table-column>-->
        <el-table-column
            prop="kpsj"
            label="开票日期"
            width="120">
        </el-table-column>
        <!--      <el-table-column-->
        <!--          prop="knmc"-->
        <!--          label="客户名称"-->
        <!--          width="120">-->
        <!--      </el-table-column>-->
        <!--      <el-table-column-->
        <!--          prop="sjshr"-->
        <!--          label="实际收货人"-->
        <!--          width="120">-->
        <!--      </el-table-column>-->
        <el-table-column
            prop="dz"
            label="到站"
            width="120">
        </el-table-column>
        <el-table-column
            prop="oilName"
            label="名称"
            width="180">
        </el-table-column>
        <el-table-column
            prop="ysfs"
            label="运输方式"
            width="80">
          <template slot-scope="scope">
            <span>{{ scope.row.ysfs === '1' ? '铁路' : '管输' }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="cs"
            label="车数"
            width="50">
        </el-table-column>
        <el-table-column
            prop="jsl"
            label="数量"
            width="120">
        </el-table-column>
        <el-table-column
            prop="bhsdj"
            label="不含税单价"
            width="120">
        </el-table-column>
        <el-table-column
            prop="ykje"
            label="油款金额"
            width="120">
        </el-table-column>
        <el-table-column
            prop="ghjxse"
            label="购货进项税额"
            width="120">
        </el-table-column>
        <el-table-column
            prop="pnhjjxse"
            label="票内合计进项税额"
            width="120">
        </el-table-column>
        <el-table-column
            prop="zzspzje"
            label="增值税票总金额"
            width="120">
        </el-table-column>
        <el-table-column
            fixed="right"
            prop="jsje"
            label="结算金额"
            width="120">
        </el-table-column>
        <el-table-column
            fixed="right"
            prop="ysfs"
            label="结算方式"
            width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.jsfs === 1">正常结算</span>
            <span v-if="scope.row.jsfs === 2">从量从价</span>
            <span v-if="scope.row.jsfs === 3">增量结算</span>
            <span v-if="scope.row.jsfs === 4">从量从价,增量结算</span>
          </template>
        </el-table-column>
        <el-table-column
            fixed="right"
            prop="yjs"
            label="是否预结"
            width="120">
          <template slot-scope="scope">
            <span v-if="!scope.row.yjs">否</span>
            <span v-if="scope.row.yjs === 1">预结算(未冲销)</span>
            <span v-if="scope.row.yjs === 2">预结算(已冲销)</span>
          </template>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="200">
          <template slot-scope="scope">
            <!--          <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>-->
            <el-button @click="cancelJLD(scope.row.id)" type="text" size="small">取消结算</el-button>
            <el-button v-if="scope.row.yjs === 1" @click="chooseJLD(scope.row)" type="text" size="small">冲销
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          class="mt10"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageOptions.pageNum"
          :page-sizes="[20, 50, 100,200,500]"
          :page-size="pageOptions.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageOptions.total">
      </el-pagination>
    </el-card>
    <el-dialog
        title="选择计量单"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="dialogShow"
        width="60%">

      <el-table
          ref="table"
          :data="dialogTableData"
          border
          row-key="id"
          :max-height="300"
          :cell-style="{padding:'5px 0'}"
          style="width: 100%"
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
            min-width="180">
        </el-table-column>
        <el-table-column
            prop="materialname"
            label="油品名称"
            min-width="180">
        </el-table-column>
<!--        <el-table-column-->
<!--            prop="gh"-->
<!--            label="罐号"-->
<!--            width="100">-->
<!--        </el-table-column>-->
        <el-table-column
            prop="stationname"
            label="到站"
            width="100">
        </el-table-column>
        <el-table-column
            prop="CCLDCSJ"
            label="发货时间"
            min-width="180">
        </el-table-column>
<!--        <el-table-column-->
<!--            prop="transporttype"-->
<!--            label="运输方式"-->
<!--            width="100">-->
<!--          <template slot-scope="scope">-->
<!--            <span v-if="scope.row.transporttype===1">火车</span>-->
<!--            <span v-if="scope.row.transporttype===2">管输</span>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column
            prop="cs"
            label="车数"
            width="80">
        </el-table-column>
        <el-table-column
            prop="JSL"
            label="数量(t)"
            width="150">
        </el-table-column>
<!--        <el-table-column-->
<!--            fixed="right"-->
<!--            label="操作">-->
<!--          <template slot-scope="scope">-->
<!--            <el-button @click="split(scope.row)" type="text" size="small">拆分计量单</el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogShow = false">取 消</el-button>
        <el-button type="primary" @click="submitChoose">确 定</el-button>
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
        title="管输清算"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="clearDialog"
        width="80%"
        >

      <div class="mb20">
        <el-row>
          <el-col :span="5">
            <el-select v-model="pipeClearOil" placeholder="选择油品" size="small" clearable>
              <el-option :key="item.id" v-for="item in oilTypes" :label="item.name" :value="item.code"></el-option>
            </el-select>
            <el-button style="margin-left: 10px" type="primary" @click="filterData" size="small">查询</el-button>
          </el-col>
          <el-col :span="5"  class="fb">已选结算单数量合计：{{ jsd_sum }}</el-col>
          <el-col :span="5"  class="fb">已选计量单数量合计：{{ jld_sum }}</el-col>
          <el-col :span="5"  class="fb">差值：{{ diff }}</el-col>
        </el-row>
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-table
              :data="jsdTableData"
              border
              height="400"
              :cell-style="{padding:'5px 0'}"
              style="width: 100%"
              @selection-change="selectJsd">
            <el-table-column
                type="selection"
                fixed="left">
            </el-table-column>
            <el-table-column
                type="index"
                label="#"
                width="50">
            </el-table-column>
            <el-table-column
                prop="fhsj"
                label="发货日期"
                width="100">
            </el-table-column>
            <el-table-column
                prop="kpsj"
                label="开票日期"
                width="120">
            </el-table-column>
            <el-table-column
                prop="oilName"
                label="名称"
                width="180">
            </el-table-column>
            <el-table-column
                prop="ysfs"
                label="运输方式"
                width="80">
              <template slot-scope="scope">
                <span>{{ scope.row.ysfs === '1' ? '铁路' : '管输' }}</span>
              </template>
            </el-table-column>
            <el-table-column
                prop="jsl"
                label="数量(t)"
                width="120"
                fixed="right">
            </el-table-column>
            <el-table-column
                prop="jsje"
                label="结算金额"
                width="120"
                fixed="right">
            </el-table-column>
<!--            <el-table-column-->
<!--                fixed="right"-->
<!--                prop="ysfs"-->
<!--                label="结算方式"-->
<!--                width="120">-->
<!--              <template slot-scope="scope">-->
<!--                <span v-if="scope.row.jsfs === 1">正常结算</span>-->
<!--                <span v-if="scope.row.jsfs === 2">从量从价</span>-->
<!--                <span v-if="scope.row.jsfs === 3">增量结算</span>-->
<!--                <span v-if="scope.row.jsfs === 4">从量从价,增量结算</span>-->
<!--              </template>-->
<!--            </el-table-column>-->
          </el-table>
        </el-col>
        <el-col :span="12">
          <el-table
              ref="jldTableData"
              :data="jldTableData"
              height="400"
              border
              :cell-style="{padding:'5px 0'}"
              style="width: 100%"
              @selection-change="selectJld"
              >
            <el-table-column
                type="selection">
            </el-table-column>
              <el-table-column
                  type="index"
                  label="#"
                  width="50">
              </el-table-column>
              <el-table-column
                  prop="jldh"
                  label="计量单号">
              </el-table-column>
              <el-table-column
                  prop="materialname"
                  label="物料名称">
              </el-table-column>
<!--              <el-table-column-->
<!--                  prop="gh"-->
<!--                  label="罐号">-->
<!--              </el-table-column>-->
              <el-table-column
                  prop="jsl"
                  label="数量(t)">
              </el-table-column>
              <el-table-column
                  prop="JLDSJ"
                  label="出厂时间">
              </el-table-column>
          </el-table>

        </el-col>
      </el-row>


      <span slot="footer" class="dialog-footer">
    <el-button @click="clearDialog = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitPipeClear" size="small">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";
//import OilSelect from "@/components/OilSelect";
import StationSelect from "@/components/StationSelect";
import {accAdd, accSub} from "@/common/mathUtil";

export default {
  name: "settlementList",
  data() {
    return {
      oilTypes:[],
      tableData: [],
      tableHeight: null,
      serchParams: {date:[]},
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },
      dialogTableData: [],
      dialogShow: false,
      chooseJld: [],
      row: null,
      splitForm: {},
      splitDialog: false,
      clearDialog:false,
      resJsdData : [],
      resJldData:[],
      // 结算单
      jsdTableData:[],
      // 计量单
      jldTableData:[],

      selectJsdData:[],
      selectJldData:[],
      jsd_sum:0,
      jld_sum:0,
      pipeClearOil:"",
    }
  },
  created() {
    this.tableHeight = showHeight(300)
    window.onresize = () => {
      this.tableHeight = showHeight(300)
    }
    this.getSettlementList();
    this.getOilType();
  },
  methods: {

    // 获取油品类别
    getOilType() {
      let params = {
        level: 3
      }
      this.$axios.get(this.$api.getOilTypeList, {params}).then(res => {

        if (res.errcode === 0) {
          this.oilTypes = res.datas;
        } else {
          this.$message.error(res.errmsg);
        }
      });
    },

    //结算清单
    getSettlementList() {
      let _this = this;
      var params = {
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize,
      }
      params.type = this.serchParams.type;
      params.station = this.serchParams.station;
      params.startTime = this.serchParams.date.length>0?this.serchParams.date[0]:null;
      params.endTime = this.serchParams.date.length>0?this.serchParams.date[1]:null;
      params.ysfs = this.serchParams.ysfs;
      _this.$axios.get(_this.$api.settlementList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
          _this.$nextTick(()=>{
            _this.$refs.tableData.doLayout();
          })
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getSettlementList();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getSettlementList();

    },
    selectOil(val) {
      this.serchParams.oilName = val.label;
    },
    selectStation(val) {
      this.serchParams.station = val.value;
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getSettlementList();
    },
    chooseJLD(row) {
      this.row = row;
      this.dialogShow = true;
      this.getGaugeTicket(row.oilType,row.dz)
    },
    getGaugeTicket(type,dz) {
      let _this = this;
      let params = {
        type: type,
        endStation:dz
      }
      _this.$axios.get(_this.$api.getJSJLD, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.dialogTableData = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    handleSelectionChange(val) {
      this.chooseJld = val;

    },
    submitChoose() {
      let _this = this;
      let chooseJsl = 0;
      let ids = [];
      this.chooseJld.forEach(function (item) {
        chooseJsl = accAdd(chooseJsl, item.jsl);
        ids.push(item.id);
      });
      // if (this.row.jsl != chooseJsl) {
      //   this.$message.error("选择的计量单的量不等于预结算的量");
      // }
      //else {
        let params = {
          settlementId: this.row.id,
          JldIds: ids.join(",")
        }
        _this.$axios.get(_this.$api.saveyujiejld, {params}).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("操作成功")
            _this.dialogShow = false;
            _this.search();

          } else {
            _this.$message.error(res.errmsg);
          }
        });
     // }

    },
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

      this.splitDialog = true;
    },
    //确定拆分计量单
    submitSplit() {
      let _this = this;
      let params = _this.splitForm;
      _this.$axios.post(_this.$api.splitJSJLD + '?type=' + _this.splitForm.isSplit, params).then(function (res) {
        if (res.errcode === 0) {
          _this.splitDialog = false;
          _this.getGaugeTicket(this.row.oilName,this.row.dz);
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },

    //取消结算
    cancelJLD(id) {
      let params = {
        id: id
      }
      let that = this;
      that.$axios.get(that.$api.cancleSettlement, {params}).then(function (res) {
        if (res.errcode === 0) {
          that.$message.success("取消成功");
          that.getSettlementList();
        } else {
          that.$message.error(res.errmsg);
        }
      });
    },

    pipeClear(){
      this.resJldData = [];
      this.resJsdData = [];
      this.jldTableData = [];
      this.jsdTableData = [];
      this.clearDialog = true;
      let _this = this;
      var params1 = {
        pageNum: 1,
        pageSize: 1000,
      }
      params1.type = this.serchParams.type;
      params1.ysfs = "2";
      params1.yjs = "3";
      _this.$axios.get(_this.$api.settlementList, {params:params1}).then(function (res) {
        if (res.errcode === 0) {
          _this.jsdTableData = res.datas;
          _this.resJsdData = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });

        var params2 = {
          pageNum: 1,
          pageSize: 1000,
          status: "0",
          transportType: 2
        }
        _this.$axios.get(_this.$api.allJsJld, {params:params2}).then(function (res) {
          if (res.errcode === 0) {
            _this.jldTableData = res.datas;
            _this.resJldData = res.datas;
          } else {
            _this.$message.error(res.errmsg);
          }
        });

    },

    selectJsd(val){
      this.selectJsdData = val.map(m=>m.id);
      this.jsd_sum = 0;
      for (let i = 0; i < val.length; i++) {
        let js = val[i];

        this.jsd_sum = accAdd(this.jsd_sum, js.jsl);
      }
    },
    selectJld(val){
      this.selectJldData = val.map(m=>m.id);
      this.jld_sum = 0;
      for (let i = 0; i < val.length; i++) {
        let jld = val[i];

        this.jld_sum = accAdd(this.jld_sum, jld.jsl);
      }
    },
    submitPipeClear(){
      if ( this.jsd_sum == 0 && this.jld_sum == 0){
        this.clearDialog = false;
        return ;
      }
      if (this.jsd_sum != this.jld_sum){
        this.$message.error('数据不相等,无法清算')
        return ;
      }
      let params = {
        jsds:this.selectJsdData.join(','),
        jlds:this.selectJldData.join(',')
      }
      this.$axios.get(this.$api.clearPipe,{params}).then(res=>{
        if (res.errcode === 0){
          this.$message.success("操作成功!")
          this.clearDialog = false;
        }
        else{
          this.$message.error("操作失败!")
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
        if ( column.property == 'cs' ||column.property == 'jsl' || column.property == 'jsje'){
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
    filterData(){
      if (this.pipeClearOil){
        this.jsdTableData = this.resJsdData.filter(f=>f.oilType==this.pipeClearOil);
        this.jldTableData = this.resJldData.filter(f=>f.type == this.pipeClearOil);
      }
      else{
        this.jsdTableData = this.resJsdData;
        this.jldTableData = this.resJldData;
      }

    }

  },
  components: {
    //"oilSelect": OilSelect,
    "stationSelect": StationSelect
  },
  computed: {
    diff: function () {
      var sub = accSub(this.jld_sum, this.jsd_sum);
      return sub;
    }
  }
}
</script>

<style scoped>

</style>

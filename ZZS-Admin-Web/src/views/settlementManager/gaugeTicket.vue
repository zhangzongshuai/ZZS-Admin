<template>
  <div>
    <el-card class="box-card">
      <!--    tab-position="left"-->
      <el-tabs>
        <el-tab-pane label="铁路出厂">
          <el-form :inline="true" :model="searchTrainParams" size="small">
            <el-form-item label="油品名称">
              <el-select v-model="searchTrainParams.type" placeholder="选择油品类别" clearable>
                <el-option :key="item.id" v-for="item in oilTypes" :label="item.name" :value="item.code"></el-option>
              </el-select>
            </el-form-item>
<!--            <el-form-item label="物料名称">-->
<!--              <oilSelect @select="selectOil"></oilSelect>-->
<!--            </el-form-item>-->
            <el-form-item label="到站">
              <station-select @select="selectStation"></station-select>
            </el-form-item>
            <el-form-item label="是否结算">
              <el-select v-model="searchTrainParams.status" placeholder="请选择是否结算" clearable>
                <el-option label="已结算" value="1"></el-option>
                <el-option label="未结算" value="0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="开始时间">
              <el-date-picker class="mr10" style="width:480px"
                              v-model="searchTrainParams.time"
                              value-format="yyyy-MM-dd"
                              type="daterange"
                              align="right"
                              unlink-panels
                              range-separator="至"
                              start-placeholder="开始日期"
                              end-placeholder="结束日期"
                              :picker-options="pickerOptions1">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="searchTrain">搜索</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-refresh" @click="syncTrain">同步</el-button>
            </el-form-item>
          </el-form>
          <el-table
              ref="trainTableData"
              :data="trainTableData"
              border
              :cell-style="{padding:'5px 0'}"
              :height="tableHeight"
              show-summary
              :summary-method="getSummaries"
              style="width: 100%">
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
            <el-table-column
                prop="gh"
                label="罐号">
            </el-table-column>
            <el-table-column
                prop="stationname"
                label="到站">
            </el-table-column>
            <el-table-column
                prop="cs"
                label="车数">
            </el-table-column>
            <el-table-column
                prop="jsl"
                label="数量(t)">
            </el-table-column>
            <el-table-column
                prop="JLDSJ"
                label="出厂时间">
            </el-table-column>

            <el-table-column
                label="结算状态">
              <template slot-scope="scope">
                <el-tag
                    effect="dark"
                    :type="scope.row.jsstatus === 0 ? 'danger' : 'success'"
                    disable-transitions>{{ scope.row.jsstatus === 0 ? '未结算' : '已结算' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
              class="mt10"
              background
              @size-change="trainSizeChange"
              @current-change="trainCurrentChange"
              :current-page="trainPageOptions.pageNum"
              :page-sizes="[20, 50, 100,200]"
              :page-size="trainPageOptions.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="trainPageOptions.total">>
          </el-pagination>

        </el-tab-pane>
        <el-tab-pane label="管输出厂">
          <el-form :inline="true" :model="searchPipelineParams" size="small">
            <el-form-item label="油品名称">
              <el-select v-model="searchPipelineParams.type" placeholder="选择油品类别" clearable>
                <el-option :key="item.id" v-for="item in oilTypes" :label="item.name" :value="item.code"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否结算">
              <el-select v-model="searchPipelineParams.status" placeholder="选择是否结算" clearable>
                <el-option label="已结算" value="1"></el-option>
                <el-option label="未结算" value="0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="开始时间">
              <el-date-picker class="mr10" style="width:480px"
                              v-model="searchPipelineParams.time"
                              value-format="yyyy-MM-dd"
                              type="daterange"
                              align="right"
                              unlink-panels
                              range-separator="至"
                              start-placeholder="开始日期"
                              end-placeholder="结束日期"
                              :picker-options="pickerOptions1">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="searchPipeline">搜索</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-refresh" @click="syncPipeline">同步</el-button>
            </el-form-item>
          </el-form>
          <el-table
              ref="pipelineTableData"
              :data="pipelineTableData"
              border
              :cell-style="{padding:'5px 0'}"
              :height="tableHeight"
              show-summary
              :summary-method="getSummaries"
              style="width: 100%">
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
            <el-table-column
                prop="gh"
                label="罐号">
            </el-table-column>
            <el-table-column
                prop="jsl"
                label="数量(t)">
            </el-table-column>
            <el-table-column
                prop="YWKSSJ"
                label="开始时间">
            </el-table-column>
            <el-table-column
                prop="YWLCSJ"
                label="结束时间">
            </el-table-column>
            <el-table-column
                label="结算状态">
              <template slot-scope="scope">
                <el-tag
                    effect="dark"
                    :type="scope.row.jsstatus === 0 ? 'danger' : 'success'"
                    disable-transitions>{{ scope.row.jsstatus === 0 ? '未结算' : '已结算' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
              class="mt10"
              background
              @size-change="pipelineSizeChange"
              @current-change="pipelineCurrentChange"
              :current-page="pipelinePageOptions.pageNum"
              :page-sizes="[20, 50, 100,200]"
              :page-size="pipelinePageOptions.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="pipelinePageOptions.total">>
          </el-pagination>

        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";
//import OilSelect from "@/components/OilSelect";
import StationSelect from "@/components/StationSelect";
import {accAdd} from "@/common/mathUtil";

export default {
  name: "gaugeTicket",
  data() {
    return {
      tableHeight: null,
      trainTableData: [],
      oilTypes:[],
      trainPageOptions: {
        total: 0,
        pageNum: 1,
        pageSize: 20,
        pages: 0
      },
      pipelineTableData: [],
      searchTrainParams: {
        oilName: "",
        time: ""
      },
      searchPipelineParams: {},
      pickerOptions1: {
        shortcuts: [
          {
            text: '今天',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '昨天',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              end.setTime(end.getTime() - 3600 * 1000 * 24);
              start.setTime(start.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '本月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setDate(1);
              end.setMonth(end.getMonth() + 1);
              end.setDate(0);
              picker.$emit('pick', [start, end]);
            }
          },
        ]
      },
      pipelinePageOptions: {
        total: 0,
        pageNum: 1,
        pageSize: 20,
        pages: 0
      },
    }

  },
  created() {
    this.tableHeight = showHeight(400)
    window.onresize = () => {
      this.tableHeight = showHeight(400)
    }
    this.getOilType();
    this.getTrainGaugeTicket();
    this.getPipelineGaugeTicket();
  },
  methods: {

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

    selectOil() {

    },
    selectStation(val) {
      this.searchTrainParams.station = val.value;
    },

    //油品
    getOilOptions() {
      let _this = this;
      this.$axios.get(this.$api.getOilList).then(function (res) {
        if (res.errcode === 0) {
          _this.oilOptions = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },

    //火车计量单
    getTrainGaugeTicket() {

      let _this = this;
      var params = {
        pageNum: _this.trainPageOptions.pageNum,
        pageSize: _this.trainPageOptions.pageSize,
        oilType: _this.searchTrainParams.type,
        station:_this.searchTrainParams.station,
        status:_this.searchTrainParams.status,
        transportType: 1,
        sTime: _this.searchTrainParams.time ? _this.searchTrainParams.time[0] : null,
        eTime: _this.searchTrainParams.time ? _this.searchTrainParams.time[1] : null
      }
      _this.$axios.get(_this.$api.allJsJld, {params}).then(function (res) {
        if (res.errcode === 0) {
          console.log(res.datas)
          _this.trainTableData = res.datas;
          _this.trainPageOptions.total = res.totalCount;
          _this.trainPageOptions.pages = res.pages;
          _this.$nextTick(()=>{
            _this.$refs.trainTableData.doLayout();
          })
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    trainSizeChange(val) {
      this.trainPageOptions.pageSize = val;
      this.getTrainGaugeTicket();
    },
    trainCurrentChange(val) {
      this.trainPageOptions.pageNum = val;
      this.getTrainGaugeTicket();
    },
    getPipelineGaugeTicket() {
      let _this = this;
      var params = {
        pageNum: _this.pipelinePageOptions.pageNum,
        pageSize: _this.pipelinePageOptions.pageSize,
        oilType: _this.searchPipelineParams.type,
        status: _this.searchPipelineParams.status,
        transportType: 2,
        sTime: _this.searchPipelineParams.time ? _this.searchPipelineParams.time[0] : null,
        eTime: _this.searchPipelineParams.time ? _this.searchPipelineParams.time[1] : null
      }
      _this.$axios.get(_this.$api.allJsJld, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.pipelineTableData = res.datas;
          _this.pipelinePageOptions.total = res.totalCount;
          _this.$nextTick(()=>{
            _this.$refs.pipelineTableData.doLayout();
          })
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    pipelineSizeChange(val) {
      this.pipelinePageOptions.pageSize = val;
      this.getPipelineGaugeTicket();
    },
    pipelineCurrentChange(val) {
      this.pipelinePageOptions.pageNum = val;
      this.getPipelineGaugeTicket();
    },
    searchTrain() {
      this.trainPageOptions.pageNum = 1;
      this.getTrainGaugeTicket();
    },
    searchPipeline() {
      this.trainPageOptions.pageNum = 1;
      this.getPipelineGaugeTicket();
    },
    syncTrain() {
      let _this = this;
      _this.$axios.get(_this.$api.syncTrain).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("同步成功");
          _this.getTrainGaugeTicket();

        } else {
          _this.$message.error(res.errmsg);
        }
      })
    },
    syncPipeline() {
      let _this = this;
      _this.$axios.get(_this.$api.syncPipeline).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("同步成功");
          _this.getPipelineGaugeTicket();
        } else {
          _this.$message.error(res.errmsg);
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
        if ( column.property == 'cs' ||column.property == 'jsl'){
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
    // "oilSelect":
    // OilSelect,
    "stationSelect":
    StationSelect
  }
}
</script>

<style scoped>

</style>

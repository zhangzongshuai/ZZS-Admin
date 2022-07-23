<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <!--      <el-form-item label="是否对账">-->

        <!--        <el-select v-model="searchParams.type" clearable placeholder="是否清账" class="mr10">-->
        <!--          <el-option key="1" label="已对账" value="1"></el-option>-->
        <!--          <el-option key="2" label="未对账" value="0"></el-option>-->
        <!--        </el-select>-->

        <!--      </el-form-item>-->
        <el-form-item label="到站">

          <station-select :current-select="searchParams.endStation" :class-name="'mr10'"
                          ref="station1"></station-select>

        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
              v-model="searchParams.date"
              type="monthrange"
              value-format="yyyy-MM"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="autoCheck" icon="el-icon-s-tools">自动对账</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="manualCheck" icon="el-icon-thumb">手动对账</el-button>
        </el-form-item>
      </el-form>
      <div class="mb20">
        <el-row>
          <el-col :span="5">借方小计:{{ jf_sum }}</el-col>
          <el-col :span="5">贷方小计:{{ df_sum }}</el-col>
          <el-col :span="5">差值:{{ diff }}</el-col>
        </el-row>
      </div>

      <el-row>
        <el-col :span="12">
          <el-table
              :data="jf_tableData"
              stripe
              border

              :height="tableHeight"
              :cell-style="{padding:'5px 0'}"
              style="width: 100%"
              @selection-change="jfSelectionChange">
            <el-table-column
                label="借方清单"

            >
              <el-table-column
                  type="index"
                  label="#"
              >

              </el-table-column>
              <el-table-column
                  type="selection"

                  :selectable="isDisabled">

              </el-table-column>
              <el-table-column
                  prop="pzCode"
                  label="凭证编号"
                  width="120">

              </el-table-column>
              <!--            <el-table-column-->
              <!--                prop="remark"-->
              <!--                label="文本"-->
              <!--                show-overflow-tooltip>-->
              <!--            </el-table-column>-->
              <el-table-column
                  prop="endStation"
                  label="到站(车数)"
                  width="130"
                  :formatter="stationFormater"
                  sortable>

              </el-table-column>
              <!--            <el-table-column-->
              <!--                prop="carNum"-->
              <!--                label="车数"-->
              <!--                width="80"-->
              <!--                sortable>-->

              <!--            </el-table-column>-->
              <el-table-column
                  prop="freight"
                  label="金额"
                  sortable
                  width="120">
              </el-table-column>
              <el-table-column
                  prop="pzTime"
                  label="凭证时间"
                  width="120"
                  sortable
                  :formatter="timeFormater">
              </el-table-column>
              <el-table-column
                  prop="type"
                  label="状态"
                 >
                <template slot-scope="scope">
                  <el-tag
                      effect="dark"
                      :type="scope.row.type === 0 ? 'danger' : 'success'"
                      disable-transitions>{{ scope.row.type === 0 ? '未对账' : '已对账' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="12">
          <el-table
              ref="table"
              :data="df_tableData"
              :height="tableHeight"
              stripe
              border

              :cell-style="{padding:'5px 0'}"
              style="width: 100%"
              @selection-change="dfSelectionChange">
            <el-table-column
                label="贷方清单"
            >
              <el-table-column
                  label="#"
                  type="index"
              >
              </el-table-column>
              <el-table-column
                  type="selection"
                  :selectable="isDisabled">

              </el-table-column>
              <el-table-column
                  prop="pzCode"
                  label="凭证编号"
                  width="120">

              </el-table-column>
              <el-table-column
                  prop="endStation"
                  label="到站(车数)"
                  width="130"
                  :formatter="stationFormater"
                  sortable>

              </el-table-column>
              <!--            <el-table-column-->
              <!--                prop="carNum"-->
              <!--                label="车数"-->
              <!--                width="80"-->
              <!--                sortable>-->

              <!--            </el-table-column>-->
              <!--            <el-table-column-->
              <!--                prop="remark"-->
              <!--                label="文本"-->
              <!--                show-overflow-tooltip>-->
              <!--            </el-table-column>-->
              <el-table-column
                  prop="freight"
                  label="金额"
                  sortable
                  width="120">
              </el-table-column>
              <el-table-column
                  prop="pzTime"
                  label="凭证时间"
                  width="120"
                  sortable
                  :formatter="timeFormater">
              </el-table-column>
              <el-table-column
                  prop="type"
                  label="状态"
                  >
                <template slot-scope="scope">
                  <el-tag
                      effect="dark"
                      :type="scope.row.type === 0 ? 'danger' : 'success'"
                      disable-transitions>{{ scope.row.type === 0 ? '未对账' : '已对账' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>

        </el-col>
      </el-row>

      <el-row class="mt20">
        <el-col :span="5" class="fb">借方合计：{{ jf_total }}</el-col>
        <el-col :span="5" class="fb">贷方合计：{{ df_total }}</el-col>
        <el-col :span="5" class="fb">差值：{{ diff_total }}</el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import {formatDate} from "@/common";
import StationSelect from "@/components/StationSelect";
import {accAdd} from "@/common/mathUtil"
import {showHeight} from "@/common/tableHeight";

export default {
  name: "dztz",
  data() {
    return {
      tableHeight: null,
      jf_tableData: [],
      df_tableData: [],
      searchParams: {
        type: '0',
        endStation: '',
        date: []
      },
      jf_sum: 0,
      df_sum: 0,

      jf_total: 0,
      df_total: 0,


      manualCheckData: {
        dfData: [],
        jfData: []
      },
      pickerOptions: {
        shortcuts: [{
          text: '本月',
          onClick(picker) {
            picker.$emit('pick', [new Date(), new Date()]);
          }
        },
          {
            text: '上月',
            onClick(picker) {
              var newDate = new Date();
              newDate.setMonth(newDate.getMonth() - 1);
              picker.$emit('pick', [newDate, newDate]);
            }
          },
          {
            text: '今年至今',
            onClick(picker) {
              const end = new Date();
              const start = new Date(new Date().getFullYear(), 0);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近六个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setMonth(start.getMonth() - 6);
              picker.$emit('pick', [start, end]);
            }
          }]
      },
    }
  },
  created() {
    this.tableHeight = showHeight(320)
    window.onresize = () => {
      this.tableHeight = showHeight(320)
    }
    this.getDfyfList();
    this.getYsyfList();
  },
  methods: {
    getDfyfList() {
      console.log('debug')
      var params = {
        station: this.$refs.station1 ? this.$refs.station1.selectStation : null,
        type: this.searchParams.type,
      }
      var sTime;
      var eTime;
      if (this.searchParams.date[0] && this.searchParams.date[1]) {
        sTime = this.searchParams.date[0];
        eTime = this.searchParams.date[1];
        sTime = formatDate(new Date(sTime).getTime(), 'Y-m-d');

        var temp = new Date(eTime);
        temp.setDate(1);
        temp.setMonth(temp.getMonth() + 1);
        eTime = formatDate(temp.getTime(), 'Y-m-d');
        params.sTime = sTime;
        params.eTime = eTime;
      }

      let _this = this;
      _this.$axios.get(_this.$api.getDfyfList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.jf_tableData = res.datas;
          _this.jf_total = 0;
          for (let i = 0; i < res.datas.length; i++) {
            let jf = res.datas[i];

            _this.jf_total = accAdd(_this.jf_total, jf.freight);
          }

        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    getYsyfList() {
      var params = {
        station: this.$refs.station1 ? this.$refs.station1.selectStation : null,
        type: this.searchParams.type,
      }
      var sTime;
      var eTime;
      if (this.searchParams.date[0] && this.searchParams.date[1]) {
        sTime = this.searchParams.date[0];
        eTime = this.searchParams.date[1];
        sTime = formatDate(new Date(sTime).getTime(), 'Y-m-d');

        var temp = new Date(eTime);
        temp.setDate(1);
        temp.setMonth(temp.getMonth() + 1);
        eTime = formatDate(temp.getTime(), 'Y-m-d');
        params.sTime = sTime;
        params.eTime = eTime;
      }
      let _this = this;
      _this.$axios.get(_this.$api.getYsyfList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.df_tableData = res.datas;

          _this.df_total = 0;
          for (let i = 0; i < res.datas.length; i++) {
            let jf = res.datas[i];

            _this.df_total = accAdd(_this.df_total, jf.freight);
          }
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },

    isDisabled(row) {
      return row.type == 0;
    },

    //自动对账
    autoCheck() {
      let loadingInstance = this.$loading({
        text: "处理中,请稍后",
        target: document.getElementsByClassName("content-box")[0]
      });
      let _this = this;
      this.$axios.get(_this.$api.checkYF).then(function (res) {
        if (res.errcode === 0) {
          loadingInstance.close();
          _this.$message.success("对账完成");
          _this.getDfyfList();
          _this.getYsyfList();
        } else {
          loadingInstance.close();
          _this.$message.error("对账失败");
        }
      });
    },
    //手动对账
    manualCheck() {
      if (this.diff != 0) {
        this.$message.error("选择的借方合计金额与贷方合计金额不一致,无法对账!")
        return;
      }

      var jfIds = this.manualCheckData.jfData.map(m => m.id);
      var dfIds = this.manualCheckData.dfData.map(m => m.id);
      var params = {
        jfIds: jfIds.join(','),
        dfIds: dfIds.join(',')
      }

      let _this = this;
      this.$axios.get(_this.$api.manualCheck, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success('对账成功!')
          _this.manualCheckData.dfData = [];
          _this.manualCheckData.jfData = [];
          _this.getDfyfList();
          _this.getYsyfList();
        } else {
          _this.$message.error(res.errmsg)
        }
      });
    },
    search() {
      this.getDfyfList();
      this.getYsyfList();
    },
    stationFormater(row) {
      if (!row.endStation) {
        return ''
      }
      return row.endStation + '（' + row.carNum + '车）';
    },
    timeFormater(row) {
      var t = new Date(row.pzTime);
      return formatDate(t, "Y-m-d")
    },
    jfSelectionChange(val) {
      this.manualCheckData.jfData = val;

      this.jf_sum = 0;
      for (let i = 0; i < this.manualCheckData.jfData.length; i++) {
        let jf = this.manualCheckData.jfData[i];

        this.jf_sum = accAdd(this.jf_sum, jf.freight);
      }
    },
    dfSelectionChange(val) {
      this.manualCheckData.dfData = val;

      this.df_sum = 0;
      for (let i = 0; i < this.manualCheckData.dfData.length; i++) {
        let df = this.manualCheckData.dfData[i];
        this.df_sum = accAdd(this.df_sum, df.freight);
      }
    },
    getSummaries(param) {
      const {columns, data} = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        } else if (index === 1 || index === 2 || index === 3 || index === 5 || index === 6) {
          sums[index] = '';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return accAdd(prev, curr);
            } else {
              return prev;
            }
          }, 0);
          sums[index];
        } else {
          sums[index] = 'N/A';
        }
      });

      return sums;
    }
  },
  components: {
    'StationSelect': StationSelect
  },
  computed: {
    diff: function () {
      var sub = accAdd(this.jf_sum, this.df_sum);
      return sub;
    },
    diff_total: function () {
      var sub = accAdd(this.jf_total, this.df_total);
      return sub;
    }

  }
}
</script>

<style scoped>

</style>

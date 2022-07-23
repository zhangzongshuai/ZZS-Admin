<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="serchParams" size="small">
        <el-form-item label="月份">
          <el-date-picker
              v-model="configPlanDate"
              type="month"
              value-format="yyyy-MM"
              :clearable="false"
              :picker-options="pickerOptions"
              @change="dateChange"
              placeholder="选择月">
          </el-date-picker>
        </el-form-item>
        <!--      <el-form-item>-->
        <!--        <el-button type="primary" icon="el-icon-s-tools" @click="changePlan" :disabled="disabled">调整计划</el-button>-->
        <!--      </el-form-item>-->
        <!--      <el-form-item>-->
        <!--        <el-button type="primary" icon="el-icon-edit" @click="modifyPlan" :disabled="disabled">编辑</el-button>-->
        <!--      </el-form-item>-->
        <!--      <el-form-item>-->
        <!--        <el-button type="primary" icon="el-icon-plus" @click="addStationPlan" :disabled="disabled">新增计划</el-button>-->
        <!--      </el-form-item>-->
        <!--      <el-form-item>-->
        <!--        <el-button type="primary" icon="el-icon-s-tools" @click="changeStationPlan">调整计划</el-button>-->
        <!--      </el-form-item>-->
      </el-form>

      <div style="text-align: center;font-weight: bold" class="f24 mt10 mb10">{{ configPlanDate }}配置计划</div>
      <el-table
          ref="table"
          :data="tableData"
          :span-method="objectSpanMethod"
          border
          :cell-style="{padding:'10px 0'}"
          :height="tableHeight"
          :row-style="rowStyle"
          style="width: 100%">
        <el-table-column
            prop="oilName"
            label="油品名称">
        </el-table-column>
        <el-table-column
            prop="planAmount"
            label="原计划">
        </el-table-column>
        <el-table-column
            prop="changeDate"
            label="调整时间">
        </el-table-column>
        <el-table-column
            prop="changeAmount"
            label="调整量">
        </el-table-column>
        <el-table-column
            prop="changedAmount"
            label="调整后">
        </el-table-column>
        <!--      <el-table-column-->
        <!--          label="操作">-->
        <!--        <template slot-scope="scope">-->
        <!--          <span v-if="scope.row.caozuo && scope.row.id != 'sum'">-->
        <!--            <el-button @click="modifyPlan(scope.row)" type="text" size="small">编辑</el-button>-->
        <!--            <el-button @click="changeConfigPlan(scope.row)" type="text" size="small">调整</el-button>-->
        <!--          </span>-->
        <!--          <span v-else>-->
        <!--            &#45;&#45;&#45;&#45;&#45;&#45;-->
        <!--          </span>-->

        <!--        </template>-->
        <!--      </el-table-column>-->
      </el-table>
    </el-card>
    <!--新增计划和编辑原计划 1.0-->
    <el-dialog
        v-dialogDrag
        :close-on-click-modal="false"
        :title="configTitle"
        :visible.sync="dialogShow"
        width="30%">
      <el-form label-width="100px" label-position="right" :model="planFrom" size="small">
        <el-form-item label="月份">
          <span>{{ configPlanDate }}</span>
        </el-form-item>
        <el-form-item :key="item.oilCode" :label="item.oilName" v-for="item in configPlanList">
          <el-input v-model="item.planAmount" placeholder="请输入配置计划量"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submit" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <!--调整计划和编辑调整计划 1.0-->
    <el-dialog
        v-dialogDrag
        :close-on-click-modal="false"
        title="调整计划"
        :visible.sync="changeDialog"
        width="30%">
      <el-form label-width="100px" label-position="right" :model="changeFrom" size="small">
        <el-form-item label="月份">
          <span>{{ configDate }}</span>
        </el-form-item>
        <!--        <el-form-item :key="item.oilCode" :label="item.oilName" v-for="item in configPlanList">-->
        <!--          <el-input v-model="item.changeAmount" placeholder="请输入调整量"></el-input>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item :label="changeFrom.oilName">-->
        <!--          <el-input v-model="changeFrom.changeAmount" placeholder="请输入调整量"></el-input>-->
        <!--        </el-form-item>-->
        <el-form-item :key="item.oilCode" :label="item.oilName" v-for="item in changePlanList">
          <el-input v-model="item.changeAmount" placeholder="请输入调整量"></el-input>
        </el-form-item>
        <el-form-item label="调整日期">
          <el-date-picker
              v-model="changeFrom.changeDate"
              type="date"
              placeholder="选择调整日期日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <span class="f12 zhushi">注:正数代表增加,负数代表减少</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="changeDialog = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitChange" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <!--编辑调整的计划 1.0-->
    <el-dialog
        v-dialogDrag
        :close-on-click-modal="false"
        title="编辑计划"
        :visible.sync="modifyChange"
        width="30%">
      <el-form label-width="100px" label-position="right" :model="modifyChangeFrom" size="small">
        <el-form-item label="月份">
          <span>{{ modifyChangeFrom.planDate }}</span>
        </el-form-item>
        <!--        <el-form-item :key="item.oilCode" :label="item.oilName" v-for="item in configPlanList">-->
        <!--          <el-input v-model="item.changeAmount" placeholder="请输入调整量"></el-input>-->
        <!--        </el-form-item>-->
        <el-form-item :label="modifyChangeFrom.oilName">
          <el-input v-model="modifyChangeFrom.changeAmount" placeholder="请输入调整量"></el-input>
        </el-form-item>
        <el-form-item label="调整日期">
          <el-date-picker
              v-model="modifyChangeFrom.changeDate"
              type="date"
              placeholder="选择调整日期日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <span class="f12 zhushi">注:正数代表增加,负数代表减少</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="modifyChange = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitModifyChange" size="small">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import {formatDate} from "@/common";
import {showHeight} from "@/common/tableHeight";

export default {
  name: "configPlanManager",
  data() {
    return {
      tableData: [],
      serchParams: {},
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      tableHeight: null,
      configPlanDate: null,
      combineRowSpan: [0, 0, 2, 0, 0, 0, 0, 0, 0],
      dialogShow: false,
      configTitle: "新增配置计划",
      planFrom: {
        type: 'add'
      },
      configPlanList: [],
      configDate: formatDate(new Date().getTime(), 'Y-m'),
      indexInfoList: [],
      spanArr: [],
      pos: 0,
      modifyDialog: false,
      changeDialog: false,
      modifyChange: false,
      changeFrom: {
        type: "change",
        //changeAmount: "",
        changeDate: new Date()
      },
      modifyFrom: {},
      modifyChangeFrom: {},
      dataList: [],
      disabled: false,
      changePlanList: [],
      modifyOriginal: false,//编辑原计划,


      //新增计划2.0
      createDialog: false,
      planDate: formatDate(new Date().getTime(), 'Y-m'),
      stationData: [],
      provinceData: [],
      transportData: [{id: '1', type: '火车'}, {id: '2', type: '管线'}],
      stationPlanData: [],
      currRow: {},
      cyCount: 0,
      qyCount: 0,

      //编辑计划2.0
      changeStationPlanDialog: false,
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.configPlanDate = formatDate(new Date().getTime(), 'Y-m');
    this.getConfigPlan();

    this.getOilType();

  },
  methods: {
    addConfigPlan() {
      this.addDialogShow = true;
      let _this = this;
      this.dataList.forEach(function (item) {
        let obj = {
          oilName: item.name,
          oilCode: item.code,
          planAmount: null
        }
        _this.configPlanList.push(obj);
      });
    },
    getConfigPlan() {
      let _this = this;
      let params = {
        date: _this.configPlanDate
      }
      _this.$axios.get(_this.$api.getConfigPlanList, {params}).then(function (res) {
        if (res.errcode === 0) {
          if (res.datas.length > 0) {
            _this.rowSpan(res.datas)
            _this.tableData = res.datas;
            _this.disabled = true;
          } else {
            _this.tableData = res.datas;
            _this.$confirm("本月未设置配置计划，是否现在设置", "提示", {
              confirmButtonText: '去设置',
              cancelButtonText: '暂不设置',
              type: 'warning'
            }).then(function () {

              _this.$router.push("/stationPlan")
              // _this.dialogShow = true;
              // _this.configTitle = "新增配置计划";
              // _this.configPlanList = [];
              // _this.dataList.forEach(function (item) {
              //   let obj = {
              //     oilName: item.name,
              //     oilCode: item.code,
              //     planAmount: null
              //   }
              //   _this.configPlanList.push(obj);
              //   _this.planFrom.type = 'add';
              // });

            }).catch(function () {
            })
          }
        } else {
          _this.$message.error(res.errmsg);
        }
      });

    },
    dateChange() {
      let date = this.configPlanDate;
      let now = formatDate(new Date().getTime(), 'Y-m');
      if (date != now) {
        this.disabled = true;
      } else {
        this.disabled = false;
      }
      this.getConfigPlan();
    },

    rowSpan(data) {
      this.spanArr = [];
      this.pos = 0;
      for (var i = 0; i < data.length; i++) {

        if (i === 0) {
          this.spanArr.push(1);
          this.pos = 0
        } else {
          // 判断当前元素与上一个元素是否相同
          if (data[i].oilCode === data[i - 1].oilCode) {
            this.spanArr[this.pos] += 1;
            this.spanArr.push(0);

          } else {
            this.spanArr.push(1);
            this.pos = i;
          }
        }
        data[this.pos].caozuo = true;
      }
    },
    // eslint-disable-next-line no-unused-vars
    objectSpanMethod({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 0 || columnIndex === 1) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    },
    rowStyle(obj) {
      if (obj.row.id == 'sum') {
        return {
          backgroundColor: "#cfd6d7",
          fontSize: "16px",
          fontWeight: '1000'
        }
      }

    },

    getOilType() {
      let _this = this;
      _this.dataList = [];
      this.$axios(this.$api.getOilTypeList + '?level=3').then(function (res) {
        if (res.errcode === 0) {
          _this.dataList = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });

    },

    submit() {
      let _this = this;
      let params = _this.configPlanList;
      if (_this.planFrom.type == 'add') {
        _this.$axios.post(_this.$api.addConfigPlan + '?date=' + _this.configDate
            , params).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("保存成功");
            _this.dialogShow = false;
            _this.getConfigPlan();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      } else if (_this.planFrom.type == 'modify') {
        _this.$axios.post(_this.$api.modifyPlan + '?date=' + _this.configDate
            , params).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("保存成功");
            _this.modifyDialog = false;
            _this.getConfigPlan();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      }

    },
    modifyPlan1(row) {
      if (row.changeAmount) {
        this.modifyChange = true;
        this.modifyChangeFrom = JSON.parse(JSON.stringify(row));
      } else {
        this.modifyDialog = true;
        this.modifyFrom = JSON.parse(JSON.stringify(row));
      }


    },
    changeConfigPlan(row) {
      this.changeDialog = true;
      this.changeFrom.id = row.id;
      this.changeFrom.planDate = row.planDate;
      this.changeFrom.oilCode = row.oilCode;
      this.changeFrom.oilName = row.oilName;
      this.changeFrom.changeAmount = '';
    },
    submitChange() {
      let _this = this;
      // let params = this.changeFrom;
      // _this.$axios.post(_this.$api.changeConfigPlan, params).then(function (res) {
      //   if (res.errcode === 0) {
      //     _this.$message.success("操作成功");
      //     _this.getConfigPlan();
      //     _this.changeDialog = false;
      //   } else {
      //     _this.$message.error(res.errmsg)
      //   }
      // });

      let params = this.changePlanList;
      params.forEach(f => {
        f.changeDate = _this.changeFrom.changeDate
      });

      if (_this.changeFrom.type == "change") {
        _this.$axios.post(_this.$api.changeConfigPlan + "?date=" + _this.configDate, params).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("操作成功")
            _this.changeDialog = false;
            _this.getConfigPlan();
          } else {
            _this.$message.error(res.errmsg);
          }
        })
      } else if (_this.changeFrom.type == "modify") {
        _this.$axios.post(_this.$api.modifyChange + "?date=" + _this.configDate, params).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("操作成功")
            _this.changeDialog = false;
            _this.getConfigPlan();
          } else {
            _this.$message.error(res.errmsg);
          }
        })
      }

    },
    submitModify() {
      let params = this.modifyFrom;
      let _this = this;
      _this.$axios.post(_this.$api.modifyPlan, params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("操作成功");
          _this.modifyDialog = false;
          _this.getConfigPlan();
        } else {
          _this.$message.error(res.errmsg);
        }
      });


    },
    submitModifyChange() {
      let params = this.modifyChangeFrom;
      let _this = this;
      _this.$axios.post(_this.$api.modifyPlan, params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("操作成功");
          _this.modifyChange = false;
          _this.getConfigPlan();
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    //调整计划
    changePlan() {
      let _this = this;
      _this.changeDialog = true;
      _this.changeFrom.planDate = formatDate(new Date().getTime(), 'Y-m')
      _this.modifyTitle = "调整配置计划";
      _this.changePlanList = [];
      _this.dataList.forEach(function (item) {
        let obj = {
          oilName: item.name,
          oilCode: item.code,
          changeAmount: null
        }
        _this.changePlanList.push(obj);
        _this.changeFrom.changeDate = formatDate(new Date().getTime(), 'Y-m-d');
      })
    },

    //编辑计划
    modifyPlan() {
      let _this = this;
      _this.$axios.get(_this.$api.lastPlan + '?date=' + _this.configDate).then(function (res) {
        if (res.errcode === 0) {
          let date = new Date();
          let firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
          let first = formatDate(firstDay, 'Y-m-d')
          for (let i = 0; i < res.datas.length; i++) {

            let dt = res.datas[i];
            //还未调过价格
            if (dt.changeDate == first) {

              _this.dialogShow = true;
              _this.configPlanList = res.datas;
              _this.planFrom.type = 'modify';

            } else {
              _this.changeDialog = true;
              _this.changePlanList = res.datas;
              _this.changeFrom.type = 'modify';
              for (let j = 0; j < res.datas.length; j++) {
                if (res.datas[i].changeDate) {
                  _this.changeFrom.changeDate = res.datas[i].changeDate;
                  break;
                }
              }
            }
          }

          //_this.modifyChangeFrom = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
      _this.configTitle = "编辑配置计划";

    },

  }
}
</script>

<style scoped>
.zhushi {
  color: #f02d2d;
}
</style>

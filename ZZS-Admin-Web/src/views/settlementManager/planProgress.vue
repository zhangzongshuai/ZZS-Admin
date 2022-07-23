<template>
  <div>
    <el-card class="box-card">
      <!--    <el-card class="box-card">-->
      <!--      <div slot="header" class="clearfix">-->
      <!--        <span>进度总览</span>-->
      <!--      </div>-->

      <!--    </el-card>-->
      <el-form :inline="true" size="small">
        <el-form-item label="月份">
          <el-date-picker
              :clearable="false"
              v-model="month"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择月份">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="getPlanProgress">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table
          :data="tableData"
          border
          style="width: 100%"
          :cell-style="{padding:'5px 0'}"
          :row-style="rowStyle">
        <el-table-column
            prop="name"
            label="油品"
            width="180">
          <template slot-scope="scope">
          <span v-if="scope.row.name!='汽油'&& scope.row.name !='柴油'">
                      <el-button @click="detailPlan(scope.row)" type="text"
                                 size="small">{{ scope.row.name }}</el-button>
          </span>
            <span v-else>
            {{ scope.row.name }}
          </span>

          </template>
        </el-table-column>
        <el-table-column
            prop="lastMonthCql"
            label="上月超欠"
            width="180">
        </el-table-column>
        <el-table-column
            prop="plan"
            label="月度计划"
            width="180">
        </el-table-column>
        <el-table-column
            prop="beforeAmount"
            label="前期完成量">
        </el-table-column>
        <el-table-column
            prop="dayAmount"
            label="当日完成量"
            width="180">
        </el-table-column>
        <el-table-column
            prop="totalAmount"
            label="累计完成量"
            width="180">
        </el-table-column>
        <el-table-column
            prop="cql"
            label="计划进度超欠"
            width="180">
        </el-table-column>
        <el-table-column
            prop="rate"
            label="完成率"
            width="180">
        </el-table-column>
      </el-table>
      <br>
      <!--    <template>-->
      <!--      <el-tabs tab-position="left">-->
      <!--        <el-tab-pane label="92#清洁" >-->
      <!--          <template>-->
      <!--            <el-table-->
      <!--                border-->
      <!--                :height="height2"-->
      <!--                :data="gasoline92Data"-->
      <!--                style="width: 100%">-->
      <!--              <el-table-column-->
      <!--                  prop="date"-->
      <!--                  label="日期"-->
      <!--                  width="180">-->
      <!--              </el-table-column>-->
      <!--              <el-table-column-->
      <!--                  prop="dayPlan"-->
      <!--                  label="日计划量"-->
      <!--                  width="180">-->
      <!--              </el-table-column>-->
      <!--              <el-table-column-->
      <!--                  prop="dayReal"-->
      <!--                  label="日实际出厂量">-->
      <!--              </el-table-column>-->
      <!--              <el-table-column-->
      <!--                  prop="dayDiff"-->
      <!--                  label="超欠量">-->
      <!--              </el-table-column>-->
      <!--            </el-table>-->
      <!--          </template>-->
      <!--        </el-tab-pane>-->
      <!--        <el-tab-pane label="95#清洁" >95#清洁</el-tab-pane>-->
      <!--        <el-tab-pane label="92#乙醇" >92#乙醇</el-tab-pane>-->
      <!--        <el-tab-pane label="95#乙醇">95#乙醇</el-tab-pane>-->
      <!--        <el-tab-pane label="0#柴油">0#柴油</el-tab-pane>-->
      <!--      </el-tabs>-->
      <!--    </template>-->
    </el-card>
  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";
import {formatDate} from "@/common";

export default {
  name: "planProgress",
  data() {
    return {
      tableData: [],
      gasoline92Data: [],
      month: formatDate(new Date().getTime(), 'Y-m'),
      height2: ''
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.height2 = showHeight(600)
    window.onresize = () => {
      this.height2 = showHeight(600)
    }
    this.getPlanProgress();
    //this.getFinishedRate();
    //this.getGasoline92Data();
  },
  methods: {
    getFinishedRate() {
      this.tableData = [
        {
          oilName: "汽油",
          monthPlan: "75141",
          beforeFinished: "9795.151",
          dayFinished: "2530.000",
          totalFinished: "12325.151",
          planDiff: "-2703",
          finishedRate: "0.16",
          highlight: true
        },
        {
          oilName: "92#清洁",
          monthPlan: "15190",
          beforeFinished: "2563.395",
          dayFinished: "2530",
          totalFinished: "5093.395",
          planDiff: "2055",
          finishedRate: "0.34",
          highlight: false
        },
        {
          oilName: "95#清洁",
          monthPlan: "3191",
          beforeFinished: "81.189",
          dayFinished: "",
          totalFinished: "81.189",
          planDiff: "-1408",
          finishedRate: "0.03",
          highlight: false
        },
        {
          oilName: "92#乙醇",
          monthPlan: "49760",
          beforeFinished: "4775.936",
          dayFinished: "",
          totalFinished: "4775.936",
          planDiff: "-18445",
          finishedRate: "0.10",
          highlight: false
        },
        {
          oilName: "95#乙醇",
          monthPlan: "7000",
          beforeFinished: "2374.631",
          dayFinished: "",
          totalFinished: "2374.631",
          planDiff: "-892",
          finishedRate: "0.34",
          highlight: false
        },
        {
          oilName: "柴油",
          monthPlan: "72000",
          beforeFinished: "9057.714",
          dayFinished: "2810",
          totalFinished: "11867.714",
          planDiff: "-2532",
          finishedRate: "0.16",
          highlight: true
        },
        {
          oilName: "0#柴油",
          monthPlan: "72000",
          beforeFinished: "9057.714",
          dayFinished: "2810",
          totalFinished: "11867.714",
          planDiff: "-2532",
          finishedRate: "0.16",
          highlight: false
        },
      ];
    },
    getPlanProgress() {

      let _this = this;
      var dateStr = this.month;
      let params = {
        monthDate: dateStr
      }
      _this.$axios.get(_this.$api.planProgress, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });

    },

    getGasoline92Data() {
      var days = new Date().getDate();
      for (let i = 0; i < days; i++) {
        var dateStr = formatDate(new Date().setDate(i + 1), 'Y-m-d')
        var dayReal = Math.floor(Math.random() * 200 + 400);
        var day = {date: dateStr, dayPlan: "490", dayReal: dayReal, dayDiff: parseFloat(dayReal) - 490}
        this.gasoline92Data.push(day)
      }
    },
    rowStyle(obj) {
      if (obj.row.name == '汽油' || obj.row.name == '柴油') {
        return {
          backgroundColor: "#cfd6d7",
          fontSize: "16px",
          fontWeight: '1000'
        }
      }

    }

  }
}
</script>

<style scoped>

</style>

<template>
<div>
  <el-card class="box-card">
  <el-form :inline="true" :model="searchParams" size="small">
    <el-form-item label="用户名">
      <el-input v-model="searchParams.userName" placeholder="用户名"></el-input>
    </el-form-item>
    <el-form-item>
      <el-date-picker
          v-model="searchDate"
          value-format="yyyy-MM-dd"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
      </el-date-picker>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
    </el-form-item>
  </el-form>
  <template>
    <el-table
        ref="table"
        :data="tableData"
        border
        :height="tableHeight"
        style="width: 100%">
      <el-table-column
          type="index"
          label="#"
          width="50">
      </el-table-column>
      <el-table-column
          prop="operator"
          label="姓名">
      </el-table-column>
      <el-table-column
          prop="operatorLoginName"
          label="登录名">
      </el-table-column>
      <el-table-column
          prop="clientIp"
          label="登录IP">
      </el-table-column>
      <el-table-column
          prop="inTime"
          label="登录时间">
      </el-table-column>
    </el-table>

    <el-pagination
        class="mt10"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageOptions.pageNum"
        :page-sizes="[20, 50, 100,200]"
        :page-size="pageOptions.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageOptions.total">
    </el-pagination>

  </template>
  </el-card>
</div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";

export default {
  name: "loginLog",
  data(){
    return{
      searchParams:{
        userName:""
      },
      searchDate:[],
      tableData:[],
      tableHeight:"",
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },

    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getLoginLog();
  },
  methods:{
    getLoginLog(){

      let _this = this;
      let params = {
        pageNum: _this.pageOptions.pageNum,
        pageSize: _this.pageOptions.pageSize,
      }
      if (_this.searchParams.userName) {
        params.userName = _this.searchParams.userName;
      }
      if (_this.searchDate) {
        params.startTime = _this.searchDate[0];
        params.endTime = _this.searchDate[1];
      }
      _this.$axios.get(_this.$api.getLoginLog,{params}).then(function (res) {
        if (res.errcode === 0){
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
        }
        else{
          _this.$message.error(res.errmsg);
        }
      });
    },
    search(){
      this.pageOptions.pageNum = 1;
      this.getLoginLog();
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getLoginLog();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getLoginLog();

    },
  }
}
</script>

<style scoped>

</style>

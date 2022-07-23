<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item label="月份">
          <el-date-picker
              v-model="searchParams.month"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择月">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table
          ref="table"
          :data="tableData"
          border
          :height="tableHeight"
          :cell-style="{padding:'10px 0'}"
          style="width: 100%">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <el-table-column
            prop="month"
            label="月份"
            width="180">
        </el-table-column>
        <el-table-column
            prop="oilName"
            label="油品"
            width="180">
        </el-table-column>
        <el-table-column
            prop="cql"
            label="实际超欠量">
        </el-table-column>
        <el-table-column
            prop="confirmCql"
            label="确认超欠量">
        </el-table-column>
<!--        <el-table-column-->
<!--            prop="confirmCql"-->
<!--            label="确认超欠量">-->
<!--        </el-table-column>-->
        <el-table-column
            fixed="right"
            label="操作">
          <template slot-scope="scope">
            <el-button v-if="!scope.row.confirmCql" @click="confirmCql(scope.row)" type="text" size="small">确认</el-button>
          </template>
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
    </el-card>

    <el-dialog
        v-if="dialogShow"
        v-dialogDrag
        :close-on-click-modal="false"
        title="确认超欠量"
        :visible.sync="dialogShow"
        width="30%">
      <el-form ref="userForm" :model="cqlForm" :rules="cqlRules" label-width="80px" size="small">
        <el-form-item label="登录名" prop="confirmCql">
          <el-input v-model="cqlForm.confirmCql" placeholder="请输入确认超欠量"></el-input>
        </el-form-item>
      </el-form>


      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submit()" size="small">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>
<script>

import {showHeight} from "@/common/tableHeight";

export default {
  name: "Cql",
  data() {
    return {
      tableData: [],
      tableHeight: null,
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },
      searchParams: {
        month: '',
      },
      dialogShow:false,
      cqlForm:{
        confirmCql:""
      },
      cqlRules:{
        confirmCql: {required: true, message: '请输入确认超欠量', trigger: 'blur'},
      }
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getCql();
  },
  methods: {
    getCql() {
      let that = this;
      let params = {
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize
      }
      if (this.searchParams.month) {
        params.month = this.searchParams.month
      }
      that.$axios.get(that.$api.getCql, {params}).then(function (res) {
        if (res.errcode === 0) {
          that.tableData = res.datas;
          that.pageOptions.total = res.totalCount
        } else {
          that.$message.error(res.errmsg);
        }
      });
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getCql();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getCql();

    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getCql();
    },
    confirmCql(row){
      this.dialogShow = true;
      this.cqlForm = JSON.parse(JSON.stringify(row)) ;
      this.cqlForm.confirmCql = Math.round(row.cql);
    },
    submit(){
      let params = this.cqlForm;
      this.$axios.post(this.$api.confirmCql,params).then(res=>{
        if (res.errcode === 0){
          this.$message.success("操作成功!");
          this.dialogShow = false;
          this.getCql();
        }
        else{
          this.$message.error(res.errmsg);
        }
      })
    }
  }
}
</script>

<style scoped>

</style>

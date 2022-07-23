<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item label="对账方式">
          <el-select v-model="searchParams.type" clearable placeholder="对账方式">
            <el-option key="1" label="自动对账" value="1"></el-option>
            <el-option key="2" label="手动对账" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="到站">
          <station-select :current-select="''" @select="selectStation"></station-select>
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
          :cell-style="{padding:'5px 0'}"
          style="width: 100%">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <el-table-column
            label="借方信息">
          <template slot-scope="scope">
            <span :key="de.id" v-for="de in scope.row.debtorList">{{ de }}<br></span>
          </template>
        </el-table-column>
        <el-table-column
            label="贷方信息">
          <template slot-scope="scope">
            <span :key="cr.id" v-for="cr in scope.row.creditorList">{{ cr }}<br></span>
          </template>
        </el-table-column>

        <el-table-column
            prop="type"
            label="对账方式"
            width="180">
          <template slot-scope="scope">
            <el-tag
                effect="dark"
                :type="scope.row.type === 1 ? '' : 'success'"
                disable-transitions>{{ scope.row.type === 1 ? '自动' : '手动' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="300">
          <template slot-scope="scope">
            <el-button @click="revocation(scope.row.id)" type="text" size="small">撤销对账</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          background

          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageOptions.pageNum"
          :page-sizes="[20, 50, 100,200]"
          :page-size="pageOptions.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageOptions.total">>
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";
import StationSelect from "@/components/StationSelect";

export default {
  name: "dzls",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      pageOptions: {
        total: 0,
        pageNum: 1,
        pageSize: 20,
        pages: 0
      },
      searchParams: {}
    }
  },
  created() {
    this.tableHeight = showHeight(270)
    window.onresize = () => {
      this.tableHeight = showHeight(270)
    }
    this.getDzHistory();
  },
  methods: {
    getDzHistory() {
      let _this = this;
      var params = this.searchParams;
      params.pageNum = _this.pageOptions.pageNum,
          params.pageSize = _this.pageOptions.pageSize

      _this.$axios.get(_this.$api.getDzHistory, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
          _this.pageOptions.pages = res.pages;
        } else {
          _this.$message.error(res.errmsg);
          console.error(res.detail_msg);
        }
      });
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getDzHistory();
    },
    handleCurrentChange(val) {
      if (val <= this.pageOptions.pages) {
        this.pageOptions.pageNum = val
        this.getDzHistory();
      }
    },
    //撤销对账
    revocation(id) {
      let _this = this;
      this.$confirm("确认撤销对账吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(
          function () {
            _this.$axios.delete(_this.$api.revocationDZ + "/" + id).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("操作成功");
                _this.getDzHistory();
              } else {
                _this.$message.error(res.errmsg);
                console.error(res.detail_msg);
              }
            });
          }
      ).catch(function () {
      });

    },
    selectStation(val) {
      this.searchParams.station = val.value;
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getDzHistory();
    }
  },
  components: {
    "StationSelect": StationSelect
  }
}
</script>

<style scoped>

</style>

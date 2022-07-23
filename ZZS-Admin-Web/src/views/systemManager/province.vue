<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item>
          <el-button @click="addProvince" icon="el-icon-plus" type="primary">新增省份</el-button>
        </el-form-item>
        <el-form-item label="车站名称">
          <el-input v-model="searchParams.name" placeholder="请输入省份名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="search" icon="el-icon-search" type="primary">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table
          ref="table"
          :data="tableData"
          border
          :height="tableHeight"
          :cell-style="{padding:'5px 0'}"
          style="width: 100%;height: 100%">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <el-table-column
            prop="name"
            label="省份">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button @click="modifyProvince(scope.row)" type="text" size="small">编辑</el-button>
            <el-button @click="deleteProvince(scope.row.id)" type="text" size="small">删除</el-button>
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
        :title="dialogTitle"
        :visible.sync="dialogShow"
        width="30%">

      <el-form ref="provinceForm" :model="provinceForm" :rules="rules" label-width="120px" size="small">

        <el-form-item label="省份名称" prop="name">
          <el-input v-model="provinceForm.name"></el-input>
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
  name: "province",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      searchParams: {
        name: ''
      },
      dialogTitle: "",
      dialogShow: false,
      provinceForm: {},
      copyProvince: {},
      rules: {name: {required: true, message: '请输入省份名称', trigger: 'blur'}},
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
    this.getProvinceList();
  },
  methods: {
    getProvinceList() {
      let params = {
        name: this.searchParams.name,
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize
      }
      this.$axios.get(this.$api.getProvinces, {params}).then(res => {
        if (res.errcode === 0) {
          this.tableData = res.datas;
          this.pageOptions.total = res.totalCount;
        } else {
          this.$message.error(res.errmsg);
        }
      })
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getProvinceList();
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getProvinceList();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getProvinceList();

    },
    addProvince() {
      this.dialogTitle = "添加省份";
      this.dialogShow = true;
      this.provinceForm = {};
    },
    submit() {
      let _this = this;
      var params = _this.provinceForm;
      this.$refs["provinceForm"].validate(function (valid) {
        if (valid) {
          if (params.id) {
            let ismodify = false;
            if (_this.copyProvince.name != params.name) {
              ismodify = true;
            }
            if (ismodify) {
              _this.$axios.post(_this.$api.savaProvince, params).then(function (res) {
                if (res.errcode === 0) {
                  _this.$message.success("保存成功");
                  _this.dialogShow = false;
                  _this.getProvinceList();
                } else {
                  _this.$message.error(res.errmsg);
                  _this.dialogShow = false;
                }
              });
            }
          } else {
            _this.$axios.post(_this.$api.savaProvince, params).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("保存成功");
                _this.dialogShow = false;
                _this.getProvinceList();
              } else {
                _this.$message.error(res.errmsg);
                _this.dialogShow = false;
              }
            });
          }

        }
      });
    },
    modifyProvince(row) {
      this.dialogTitle = "编辑省份";
      this.dialogShow = true;

      this.provinceForm = JSON.parse(JSON.stringify(row));
      this.copyProvince = JSON.parse(JSON.stringify(row));
    },
    deleteProvince(id) {
      let _this = this;
      _this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        _this.$axios.delete(_this.$api.deleteProvince + "/" + id).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("删除成功");
            _this.getProvinceList();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      }).catch(function () {
      });
    }
  }
}
</script>

<style scoped>

</style>

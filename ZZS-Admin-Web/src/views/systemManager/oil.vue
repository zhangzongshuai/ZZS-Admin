<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" size="small">
        <el-form-item>
          <el-button type="primary" icon="el-icon-plus" @click="addOil">新增油品</el-button>
        </el-form-item>
        <el-form-item label="油品名称">
          <el-input v-model="searchParams.name"></el-input>
        </el-form-item>
        <!--      <el-form-item label="油品类别">-->
        <!--        <el-cascader-->
        <!--            v-model="searchParams.typeId"-->
        <!--            :options="options"-->
        <!--            :props="cascaderConfig"-->
        <!--            clearable></el-cascader>-->
        <!--      </el-form-item>-->
        <el-form-item label="油品类别">
          <el-select v-model="searchParams.typeCode" clearable placeholder="请选择">
            <el-option
                v-for="item in options"
                :key="item.code"
                :label="item.name"
                :value="item.code">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
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
            prop="name"
            label="油品名称">
        </el-table-column>
        <el-table-column
            prop="oilCode"
            label="油品编号">
        </el-table-column>
        <el-table-column
            prop="typeName"
            label="油品类别">
        </el-table-column>
        <el-table-column
            prop="isEnabled"
            label="启用状态">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.isEnabled === 0 ? 'danger' : ''"
                disable-transitions>{{ scope.row.isEnabled === 0 ? '禁用' : '启用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="200">
          <template slot-scope="scope">
            <el-button @click="modify(scope.row.id)" type="text" size="small">编辑</el-button>
            <el-button @click="remove(scope.row.id)" type="text" size="small">删除</el-button>
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

      <el-form ref="oilForm" :model="oilForm" :rules="oilRules" label-width="80px" size="small">

        <el-form-item label="油品名称" prop="name">
          <el-input v-model="oilForm.name"></el-input>
        </el-form-item>
        <el-form-item label="油品编号" prop="oilCode">
          <el-input v-model="oilForm.oilCode"></el-input>
        </el-form-item>

        <el-form-item label="油品类别" prop="typeCode">
          <el-select v-model="oilForm.typeCode" clearable placeholder="请选择">
            <el-option
                v-for="item in options"
                :key="item.code"
                :label="item.name"
                :value="item.code">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="oilForm.isEnabled" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>


      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submit" size="small">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";

let userInfo = JSON.parse(localStorage.getItem('userInfo'));
export default {
  name: "oil",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },
      options: [],
      cascaderConfig: {},
      searchParams: {},
      dialogTitle: "",
      oilForm: {
        isEnabled: 1
      },
      oilCopy: {},
      oilRules: {
        name: {required: true, message: '请输入油品名称', trigger: 'blur'},
        oilCode: {required: true, message: '请输入油品编号', trigger: 'blur'},
        typeCode: {required: true, message: '请选择油品类别', trigger: 'blur'}
      },
      dialogShow: false,
      oilTypeOptions: [],
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.cascaderConfig = {
      checkStrictly: true,
      emitPath: false,
      value: 'id',
      label: 'name',
      children: 'children'
    }
    this.getOilTypeList();
    this.getOilList();
  },
  methods: {

    getOilType() {
      let _this = this;
      this.$axios.get(this.$api.getOilTypeList).then(function (res) {
        if (res.errcode === 0) {
          _this.oilTypeOptions = res.datas;
        } else {
          _this.$message.error(res.errmsg);
          console.log(res.detail_msg);
        }
      });
    },

    getOilList() {
      this.searchParams;
      var params = {
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize,
        name: this.searchParams.name,
        typeCode: this.searchParams.typeCode
      }

      let _this = this;
      this.$axios.get(this.$api.getOilList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },

    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getOilList();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getOilList();

    },
    getOilTypeTree() {
      let _this = this;
      this.$axios.get(this.$api.getOilTypeTree).then(function (res) {
        _this.options = res.datas;
      });
    },

    getOilTypeList() {
      let _this = this;
      this.$axios.get(this.$api.getOilTypeList + '?level=3').then(function (res) {
        _this.options = res.datas;
      });
    },
    modify(id) {
      this.dialogTitle = '编辑油品';
      this.dialogShow = true;
      let _this = this;
      let params = {id: id}
      _this.$axios.get(_this.$api.getOil, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.oilForm = res.data;
          _this.oilCopy = JSON.parse(JSON.stringify(res.data));
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    remove(id) {
      let _this = this;
      _this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        _this.$axios.delete(_this.$api.deleteOil + "/" + id).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("删除成功");
            _this.getOilList();
          } else {
            _this.$message.error(res.errmsg);
            console.log(res.detail_msg);
          }
        });
      }).catch(function () {
      });

    },
    addOil() {
      this.dialogTitle = '添加油品';
      this.dialogShow = true;
      this.oilForm = {}
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getOilList();
    },
    submit() {
      var _this = this;
      var params = _this.oilForm;
      this.$refs['oilForm'].validate((valid) => {
        if (valid) {
          if (_this.oilForm.id) {

            var isModify = false;
            if (params.name != _this.oilCopy.name) {
              isModify = true;
            }
            if (params.oilCode != _this.oilCopy.oilCode) {
              isModify = true;
            }
            if (params.typeCode != _this.oilCopy.typeCode) {
              isModify = true;
            }
            if (params.isEnabled != _this.oilCopy.isEnabled) {
              isModify = true;
            }
            if (!isModify) {
              _this.dialogShow = false;
              return;
            }

            params.modifier = userInfo.login_name;
            _this.$axios.put(_this.$api.modifyOil, params).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("修改成功");
                _this.dialogShow = false;
                _this.getOilList();
              } else {
                _this.$message.error(res.errmsg);
              }
            });
          } else {
            params.creator = userInfo.login_name;
            _this.$axios.post(_this.$api.addOil, params).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("添加成功");
                _this.dialogShow = false;
                _this.getOilList();
              } else {
                _this.$message.error(res.errmsg);
              }
            });
          }

        }
      });
    }
  }
}
</script>

<style scoped>

</style>

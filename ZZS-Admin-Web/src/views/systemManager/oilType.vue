<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" size="small">
        <el-form-item>
          <el-button type="primary" icon="el-icon-plus" @click="addOilType">新增类别</el-button>
        </el-form-item>
      </el-form>


      <el-table
          ref="table"
          :data="tableData"
          :height="tableHeight"
          default-expand-all
          :cell-style="{padding:'10px 0'}"
          style="width: 100%"
          row-key="id"
          border
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <el-table-column
            prop="name"
            label=类型名称>
        </el-table-column>
        <el-table-column
            prop="code"
            label=类型编号>
        </el-table-column>
        <el-table-column
            prop="description"
            label="类型说明">
        </el-table-column>
        <!--      <el-table-column-->
        <!--          label="操作"-->
        <!--          width="200">-->
        <!--        <template slot-scope="scope">-->
        <!--          <div v-if="scope.row.code != 'gasoline-95' && scope.row.code != 'gasoline-92' && scope.row.code != 'diesel-0'">-->
        <!--            <el-button @click="modifyType(scope.row)" type="text" size="small">编辑</el-button>-->
        <!--            <el-button @click="deleteType(scope.row.id)" type="text" size="small">删除</el-button>-->
        <!--          </div>-->

        <!--        </template>-->
        <!--      </el-table-column>-->
      </el-table>
    </el-card>
    <!--  添加或编辑类别-->
    <el-dialog
        v-if="dialogShow"
        v-dialogDrag
        :close-on-click-modal="false"
        :title="dialogTitle"
        :visible.sync="dialogShow"
        width="30%">

      <el-form ref="typeFrom" :model="typeForm" :rules="typeRules" label-width="80px" size="small">

        <el-form-item label="类别名称" prop="name">
          <el-input v-model="typeForm.name"></el-input>
        </el-form-item>

        <el-form-item label="父级类别">
          <el-cascader
              v-model="typeForm.parentIds"
              :options="options"
              :props="cascaderConfig"
              clearable></el-cascader>
        </el-form-item>
        <el-form-item label="类别编号" prop="code">
          <el-input v-model="typeForm.code"></el-input>
        </el-form-item>
        <el-form-item label="类型说明">
          <el-input type="textarea" v-model="typeForm.description"></el-input>
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
  name: "oilType",
  data() {
    return {
      tableHeight: null,
      tableData: [],

      dialogShow: false,
      dialogTitle: "",
      typeForm: {
        parentIds: []
      },
      typeRules: {
        name: {required: true, message: '请输入类别名称', trigger: 'blur'},
        code: {required: true, message: '请输入类别编号', trigger: 'blur'},
      },
      options: [],
      cascaderConfig: {},
      typeCopy: {}
    }
  },

  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.cascaderConfig = {
      checkStrictly: true,
      emitPath: true,
      value: 'id',
      label: 'name',
      children: 'children'
    }
    //  this.getOilTypeList();
    this.getOilTypeTree();
  },
  methods: {

    getOilTypeTree() {
      let _this = this;
      this.$axios.get(this.$api.getOilTypeTree).then(function (res) {
        _this.tableData = res.datas;
        _this.options = res.datas;
      });
    },

    getOilTypeList() {
      let _this = this;
      this.$axios.get(this.$api.getOilTypeList).then(function (res) {
        _this.tableData = res.datas;
      });
    },

    addOilType() {
      this.typeForm = {}
      this.dialogTitle = "添加油品类别"
      this.dialogShow = true;
    },

    modifyType(row) {
      this.typeCopy = JSON.parse(JSON.stringify(row));
      this.typeForm = JSON.parse(JSON.stringify(row))
      console.log(row)
      this.dialogTitle = "编辑油品类别",
          this.dialogShow = true;

    },

    deleteType(id) {
      let _this = this;
      this.$confirm("删除操作会删除当前类型与该类型下的所有子类型,确定删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

        _this.$axios.post(_this.$api.deleteOilType + '/' + id).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("删除成功!")
            _this.getOilTypeList();
          } else {
            _this.$message.error(res.errmsg);
          }
        })

      }).catch(function () {
      });
    },

    submit() {

      let _this = this;
      var params = this.typeForm;

      console.log(this.typeForm)
      if (!params.parentIds) {
        params.parentId = "0"
      } else {
        params.parentId = params.parentIds[params.parentIds.length - 1]
        if (params.parentIds.length > 1) {
          params.baseId = params.parentIds[0]
        }
      }
      this.$refs['typeFrom'].validate((valid) => {
        if (valid) {
          if (_this.typeForm.id) {
            let isModify = false;
            if (_this.typeCopy.name != params.name) {
              isModify = true;
            }
            if (_this.typeCopy.parentId != params.parentId) {
              isModify = true;
            }
            if (_this.typeCopy.description != params.description) {
              isModify = true;
            }
            if (isModify) {
              _this.$axios.post(_this.$api.modifyOilType, params).then(function (res) {
                if (res.errcode === 0) {
                  _this.$message.success("操作成功!")
                  _this.dialogShow = false;
                  //_this.getOilTypeList();
                  _this.getOilTypeTree();
                }
              })
            } else {
              _this.dialogShow = false;
            }

          } else {
            _this.$axios.post(_this.$api.addOilType, params).then(function (res) {

              if (res.errcode === 0) {
                _this.dialogShow = false;
                _this.$message.success("添加成功!")
                // _this.getOilTypeList();
                _this.getOilTypeTree();
              } else {
                _this.$message.error(res.errmsg);
              }

            });
          }
        } else {
          return false;
        }
      });
    }

  }
}
</script>

<style scoped>

</style>

<!--车站管理模块-->
<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item>
          <el-button @click="addStation" icon="el-icon-plus" type="primary">新增车站</el-button>
        </el-form-item>
        <el-form-item label="车站名称">
          <el-input v-model="searchParams.name" placeholder="请输入车站名称"></el-input>
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
            prop="station"
            label="车站">
        </el-table-column>
        <el-table-column
            prop="provinceName"
            label="省份">
        </el-table-column>
        <el-table-column
            prop="gasolinePrice"
            label="汽油运输单价(元/车)">
        </el-table-column>
        <el-table-column
            prop="dieselPrice"
            label="柴油运输单价(元/车)">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button @click="modifyStation(scope.row)" type="text" size="small">编辑</el-button>
            <el-button @click="deleteStation(scope.row.id)" type="text" size="small">删除</el-button>
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

      <el-form ref="stationForm" :model="stationForm" :rules="rules" label-width="120px" size="small">

        <el-form-item label="车站名称" prop="station">
          <el-input v-model="stationForm.station"></el-input>
        </el-form-item>
        <el-form-item label="省份" prop="provinceId">
          <el-select v-model="stationForm.provinceId" placeholder="请选择">
            <el-option
                v-for="item in provinceList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="汽油运输单价" prop="gasolinePrice">
          <el-input v-model="stationForm.gasolinePrice"></el-input>
        </el-form-item>
        <el-form-item label="柴油运输单价" prop="dieselPrice">
          <el-input v-model="stationForm.dieselPrice"></el-input>
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
  name: "station",
  data: function () {
    return {
      tableHeight: null,
      tableData: [],
      searchParams: {
        name: '',
        provinceId: ''
      },
      dialogTitle: "",
      dialogShow: false,
      stationForm: {},
      copyStation: {},
      rules: {
        station: {required: true, message: '请输入车站名称', trigger: 'blur'},
        provinceId: {required: true, message: '请选择省份', trigger: 'change'}
      },
      provinceList: [],
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
    this.getStationList();
  },
  methods: {

    getProvinceList() {
      let params = {
        pageNum: 1,
        pageSize: 10000
      }
      this.$axios.get(this.$api.getProvinces, {params}).then(res => {
        if (res.errcode === 0) {
          this.provinceList = res.datas;
        } else {
          this.$message.error(res.errmsg);
        }
      })
    },
    getStationList() {
      let _this = this;
      let params = {
        name: this.searchParams.name,
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize
      }
      _this.$axios.get(_this.$api.getStationPage, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getStationList();
    },

    deleteStation(id) {
      let _this = this;
      _this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        _this.$axios.delete(_this.$api.deleteStation + "/" + id).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("删除成功");
            _this.getStationList();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      }).catch(function () {
      });


    },

    addStation() {
      this.dialogTitle = "添加车站";
      this.dialogShow = true;
      this.stationForm = {};
    },

    submit() {
      let _this = this;
      var params = _this.stationForm;
      this.$refs["stationForm"].validate(function (valid) {
        if (valid) {
          if (params.id) {
            let ismodify = false;
            if (_this.copyStation.station != params.station) {
              ismodify = true;
            }
            if (_this.copyStation.provinceId != params.provinceId) {
              ismodify = true;
            }
            if (_this.copyStation.gasolinePrice != params.gasolinePrice) {
              ismodify = true;
            }
            if (_this.copyStation.dieselPrice != params.dieselPrice) {
              ismodify = true;
            }
            if (ismodify) {
              _this.$axios.post(_this.$api.modifyStation, params).then(function (res) {
                if (res.errcode === 0) {
                  _this.$message.success("保存成功");
                  _this.dialogShow = false;
                  _this.getStationList();
                } else {
                  _this.$message.error(res.errmsg);
                  _this.dialogShow = false;
                }
              });
            }
          } else {
            _this.$axios.post(_this.$api.addStation, params).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("保存成功");
                _this.dialogShow = false;
                _this.getStationList();
              } else {
                _this.$message.error(res.errmsg);
                _this.dialogShow = false;
              }
            });
          }

        }
      });
    },

    modifyStation(row) {
      this.dialogTitle = "编辑车站";
      this.dialogShow = true;

      this.stationForm = JSON.parse(JSON.stringify(row));
      this.copyStation = JSON.parse(JSON.stringify(row));
    },

    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getStationList();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getStationList();

    },


  }
}
</script>

<style scoped>

</style>

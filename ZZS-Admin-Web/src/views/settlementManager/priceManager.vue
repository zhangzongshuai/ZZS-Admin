<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <!--      <el-form-item label="油品">-->
        <!--        <el-select v-model="searchParams.oil" clearable placeholder="选择油品">-->
        <!--          <el-option v-for="item in oilOptions" :key="item.id" :label="item.name" :value="item.code"></el-option>-->
        <!--          &lt;!&ndash;          <el-option label="92#汽油" value="92"></el-option>&ndash;&gt;-->
        <!--          &lt;!&ndash;          <el-option label="95#汽油" value="95"></el-option>&ndash;&gt;-->
        <!--        </el-select>-->
        <!--      </el-form-item>-->
        <!--      <el-form-item label="日期">-->
        <!--        <el-date-picker-->
        <!--            v-model="searchParams.time"-->
        <!--            type="date"-->
        <!--            value-format="yyyy-MM-dd"-->
        <!--            placeholder="选择日期">-->
        <!--        </el-date-picker>-->
        <!--      </el-form-item>-->
        <!--      <el-form-item>-->
        <!--        <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>-->
        <!--      </el-form-item>-->
        <el-form-item>
          <el-button type="primary" icon="el-icon-edit" @click="changePrice">调价</el-button>
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
            label="油品"
            width="300">
        </el-table-column>
        <el-table-column
            prop="startTime"
            label="开始日期"
            width="200">
        </el-table-column>
        <el-table-column
            prop="endTime"
            label="结束日期"
            width="200">
        </el-table-column>
        <el-table-column
            prop="price"
            label="价格"
            width="200">
        </el-table-column>
        <el-table-column
            prop="changed"
            label="调整量"
            width="200">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button v-if="scope.row.canModify == 1" @click="modifyPrice(scope.row)" type="text" size="small">编辑
            </el-button>
            <el-button @click="priceHis(scope.row.type)" type="text" size="small">历史价格
            </el-button>
            <!--          <el-button v-if="!scope.row.endTime" @click="changePrice(scope.row   )" type="text" size="small">调价-->
            <!--          </el-button>-->
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
    <!--编辑-->
    <el-dialog
        v-dialog-drag
        :close-on-click-modal="false"
        :title="dialogTitle"
        :visible.sync="modifyDialogShow"
        width="30%">
      <el-form label-width="80px" label-position="right" :model="priceModel" size="small">
        <el-form-item label="油品">
          <span>{{ priceModel.name }}</span>
        </el-form-item>
        <el-form-item label="单价">
          <el-input v-model="priceModel.price" placeholder="单价"></el-input>
        </el-form-item>
        <el-form-item label="生效时间">
          <el-date-picker
              v-model="priceModel.startTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择时间">
          </el-date-picker>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="modifyDialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="saveModify" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <!--调价-->
    <el-dialog
        v-dialog-drag
        title="调整价格"
        :close-on-click-modal="false"
        :visible.sync="changeDialogShow"
        width="30%">
      <el-form label-width="80px" label-position="right" :model="changePriceFrom" size="small">
        <!--        <el-form-item label="92号汽油">-->
        <!--          <span>{{ priceModel.oilName }}</span>-->
        <!--        </el-form-item>-->
        <el-form-item :key="item.type" :label="item.name" v-for="item in priceList">
          <el-input v-model="item.price" placeholder="请输入价格"></el-input>
        </el-form-item>
        <el-form-item label="生效时间">
          <el-date-picker
              v-model="changePriceFrom.startTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择生效时间">
          </el-date-picker>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="changeDialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="saveChange" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        v-dialog-drag
        title="历史价格"
        :visible.sync="historyShow"
        width="30%">
      <el-table
          ref="table"
          :data="historyData"
          border
          :max-height="300"
          :height="300"
          :cell-style="{padding:'10px 0'}"
          style="width: 100%">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <el-table-column
            prop="name"
            label="油品">
        </el-table-column>
        <el-table-column
            prop="startTime"
            label="开始日期"
            width="100">
        </el-table-column>
        <el-table-column
            prop="endTime"
            label="结束日期"
            width="100">
        </el-table-column>
        <el-table-column
            prop="price"
            label="价格"
            width="100">
        </el-table-column>
      </el-table>
      <el-pagination
          class="mt10"
          background
          @size-change="hisSizeChange"
          @current-change="hisCurrentChange"
          :current-page="historyOptions.pageNum"
          :page-sizes="[20, 50, 100,200]"
          :page-size="historyOptions.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="historyOptions.total">
      </el-pagination>
    </el-dialog>


  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";

export default {
  name: "priceManager",
  data() {
    return {
      oilOptions: [],
      tableData: [],

      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },

      tableHeight: null,
      searchParams: {},
      modifyDialogShow: false,
      priceModel: {},
      dialogTitle: "编辑",
      changeDialogShow: false,
      changePriceFrom: {},
      priceList: [],
      historyData: [],
      historyShow: false,
      historyCode: '',
      historyOptions: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      }
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getOilType();
    this.getOilPriceData();
  },
  methods: {
    getOilType() {
      let _this = this;
      this.$axios(this.$api.getOilTypeList + '?level=2').then(function (res) {
        if (res.errcode === 0) {
          _this.oilOptions = res.datas;

          _this.oilOptions.forEach(function (item) {
            let obj = {
              name: item.name,
              type: item.code,
              price: null
            }
            _this.priceList.push(obj);
          });

        } else {
          _this.$message.error(res.errmsg);
        }
      });

    },
    getOilPriceData() {
      let _this = this;
      let params = {
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize,
        isHis: '1'
      }

      this.$axios.get(this.$api.getPriceList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getOilPriceData();
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getOilPriceData();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getOilPriceData();

    },
    modifyPrice(row) {
      this.modifyDialogShow = true;
      this.priceModel = row;
      this.dialogTitle = "编辑"
    },
    saveModify() {
      let _this = this;
      let params = JSON.parse(JSON.stringify(_this.priceModel));
      _this.$axios.put(_this.$api.modifyPrice, params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("操作成功");
          _this.modifyDialogShow = false;
          _this.getOilPriceData();
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    changePrice() {
      this.changeDialogShow = true;
    },
    saveChange() {
      console.log('111')
      let _this = this;
      let params = [];
      this.priceList.forEach(function (item) {
        let par = {
          type: item.type,
          price: item.price,
          startTime: _this.changePriceFrom.startTime
        }
        params.push(par);
      });
      _this.$axios.post(_this.$api.changeOilPrice, params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("操作成功");
          _this.changeDialogShow = false;
          _this.getOilPriceData();
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    priceHis(code) {

      this.historyShow = true;
      this.historyCode = code;
      this.getHistoryPrice();
    },
    getHistoryPrice() {
      let _this = this;
      let params = {
        pageNum: this.historyOptions.pageNum,
        pageSize: this.historyOptions.pageSize,
        type: this.historyCode,
        isHis: '2'
      }

      this.$axios.get(this.$api.getPriceList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.historyData = res.datas;
          _this.historyOptions.total = res.totalCount;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    hisSizeChange(val) {
      this.historyOptions.pageNum = 1;
      this.historyOptions.pageSize = val;
      this.getHistoryPrice();
    },
    hisCurrentChange(val) {

      this.historyOptions.pageNum = val
      this.getHistoryPrice();

    },
  }
}
</script>

<style scoped>

</style>

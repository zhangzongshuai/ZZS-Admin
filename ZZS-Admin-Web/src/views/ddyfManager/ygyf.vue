<!--预估运费模块-->

<template>
  <div>
    <el-card class="box-card">
      <el-alert
          v-if="alertShow"
          title="提示:运费不足,请及时预付运费"
          type="error"
          effect="light"
          :closable="false">
      </el-alert>
      <br>
      <el-card shadow="never" :body-style="{padding: '20px',backgroundColor:'#D7D7D7'}">

        <el-row>
          <el-col :span="20">
            <span class="f16">预估剩余运费:</span>&nbsp;&nbsp;&nbsp;<span class="f32">{{ balance }}</span>&nbsp;元
          </el-col>
          <!--        <el-col :span="4">-->
          <!--          <el-button-->
          <!--              type="text"-->
          <!--              size="small">-->
          <!--            预付-->
          <!--          </el-button>-->
          <!--        </el-col>-->
        </el-row>


      </el-card>
      <el-form :inline="true" :model="serchParams" class="mt20">
        <el-form-item label="油品名称">
          <!--        <el-input v-model="serchParams.oilName" placeholder="油品名称" size="small"></el-input>-->
          <el-select v-model="serchParams.oilName" clearable value-key="id" placeholder="请选择油品" size="small">
            <el-option
                v-for="item in oilOptions"
                :key="item.id"
                :label="item.name"
                :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="search">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table
          ref="table"
          :data="tableData"
          border
          :height="tableHeight"
          :cell-style="{padding:'5px 0'}"
          style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-table
                :data="props.row.detail"
                border
                :cell-style="{padding:'5px 0'}"
                style="width: 100%">
              <el-table-column
                  type="index"
                  label="#"
                  width="50">

              </el-table-column>
              <el-table-column
                  prop="endStation"
                  label="到站">
              </el-table-column>
              <el-table-column
                  prop="carNum"
                  label="车数">
              </el-table-column>
              <el-table-column
                  prop="freight"
                  label="运费">
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column type="index" label="#" width="50">
        </el-table-column>
        <el-table-column prop="oilName" label="油品名称">
        </el-table-column>
        <el-table-column prop="carNum" label="车数">
        </el-table-column>
        <el-table-column prop="createDate" label="生成时间">
        </el-table-column>
        <el-table-column prop="planDate" label="预计发车时间">
        </el-table-column>
        <el-table-column prop="totalFreight" label="运费">

        </el-table-column>
        <el-table-column
            label="操作"
            width="200">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="deleteRow(scope.row.id)"
                type="text"
                size="small">
              删除
            </el-button>
            <el-button
                @click.native.prevent="modifyRow(scope.row.id)"
                type="text"
                size="small">
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!--    编辑预估运费信息-->

    <el-dialog
        v-if="dialogShow"
        title="编辑"
        :visible.sync="dialogShow"
        width="55%">
      <Qcjh-Component :qcjh-data="qcjhForm"></Qcjh-Component>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="dialogShow = false" size="small">确 定</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
import QcjhComponent from "@/components/QcjhComponent";
import {showHeight} from "@/common/tableHeight";

export default {
  name: "ygys",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      serchParams: {},
      oilOptions: [],
      dialogShow: false,
      qcjhForm: {
        domains: [],

      },
      balance: 0.00,
      alertShow: false,
    }
  },
  created() {
    this.tableHeight = showHeight(400)
    window.onresize = () => {
      this.tableHeight = showHeight(400)
    }
    this.getBalance();
    this.getPlanList();
    this.getOilOptions();
  },
  mounted: function () {

  },

  methods: {
    //获取当前余额
    getBalance() {
      let _this = this;
      _this.$axios.get(_this.$api.getBalance).then(function (res) {
        if (res.errcode === 0) {
          _this.balance = res.data ? res.data.balance : 0;
          _this.alertShow = _this.balance <= 100000;
        } else {
          _this.$message.error(res.errmsg);
          console.log(res.detail_msg);
        }
      });
    },

    //获取油品
    getOilOptions() {
      let _this = this;
      this.$axios.get(this.$api.getOilList).then(function (res) {
        if (res.errcode === 0) {
          _this.oilOptions = res.datas;
        }
      });
    },

    getPlanList() {
      let _this = this;
      var params = _this.serchParams;
      params.pageNum = 1;
      params.pageSize = 10;
      _this.$axios.get(_this.$api.getPlanList, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;

        } else {
          _this.$message.error(res.errmsg);
          console.error(res.detail_msg);
        }
      });

    },

    deleteRow(id) {
      let _this = this;
      this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

        _this.$message.success("删除成功!")

      }).catch(function () {
      })


      console.log(id);
    },
    modifyRow(id) {

      let _this = this;
      _this.qcjhForm.id = id;

      this.dialogShow = true;

      // this.qcjhForm = {
      //   id:row.id,
      //   oilName:row.oilName,
      //   planDate:row.planDate,
      //   domains:[]
      // }
      // row.detail.forEach(function (item){
      //   _this.qcjhForm.domains.push({
      //     endStation:item.endStation,
      //     carNum:item.carNum
      //   });
      // });
      //
      // console.log(row);
    },
    search() {
      this.getPlanList();
    }
  },
  components: {
    "Qcjh-Component": QcjhComponent
  }
}
</script>

<style scoped>

</style>

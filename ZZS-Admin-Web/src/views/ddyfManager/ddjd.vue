<!--代垫校对-->
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
      <el-row :gutter="10">
        <el-col :span="16">
          <el-card shadow="never" :body-style="{padding: '20px',backgroundColor:'#D7D7D7'}">
            <span class="f16">预估剩余运费:</span>&nbsp;&nbsp;&nbsp;<span class="f32">{{ balance }}</span>&nbsp;元
            <!--          <el-row>-->
            <!--            <el-col :span="20">-->
            <!--              <span class="f16">预估剩余运费:</span>&nbsp;&nbsp;&nbsp;<span class="f32">{{ balance }}</span>&nbsp;元-->
            <!--            </el-col>-->
            <!--            <el-col :span="4">-->
            <!--            </el-col>-->
            <!--          </el-row>-->
          </el-card>
        </el-col>
        <!--      <el-col :span="4">-->
        <!--        <el-card shadow="never" :body-style="{padding: '0px',height:'70px'}" style="cursor:pointer;">-->
        <!--          <div @click="addFreight">-->
        <!--            <div style="float: left;text-align: center;line-height: 70px;background: #00a854;width: 70px;height: 70px">-->
        <!--              <i class="el-icon-plus plusIcon f32" style="font-weight: bold"></i>-->
        <!--            </div>-->
        <!--            <div style="text-align: center;font-weight: bold;line-height: 70px;background: #D7D7D7" class="f20" >预付运费</div>-->
        <!--          </div>-->
        <!--        </el-card>-->
        <!--      </el-col>-->
        <!--      <el-col :span="4">-->
        <!--        <el-card shadow="never" :body-style="{padding: '0px',height:'70px'}" style="cursor:pointer;">-->
        <!--          <div @click="checkFreight">-->
        <!--            <div style="float: left;text-align: center;line-height: 70px;background: #00a854;width: 70px;height: 70px">-->
        <!--              <i class="el-icon-check plusIcon f32" style="font-weight: bold"></i>-->
        <!--            </div>-->
        <!--            <div style="text-align: center;font-weight: bold;line-height: 70px;background: #D7D7D7" class="f20" >校对运费</div>-->
        <!--          </div>-->
        <!--        </el-card>-->
        <!--      </el-col>-->
        <el-col :span="4">
          <el-card shadow="always" :body-style="{padding: '10px',backgroundColor:'#D7D7D7'}" style="cursor:pointer">
            <div @click="addFreight">
              <div style=" text-align: center;margin-bottom: 10px">
                <i class="el-icon-plus plusIcon f32" style="font-weight: bold"></i>
              </div>
              <div style="text-align: center;font-weight: bold" class="plusIcon">预付运费</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card shadow="always" :body-style="{padding: '10px',backgroundColor:'#D7D7D7'}" style="cursor:pointer">
            <div @click="checkFreight">
              <div style=" text-align: center;margin-bottom: 10px">
                <i class="el-icon-check plusIcon f32" style="font-weight: bold"></i>
              </div>
              <div style="text-align: center;font-weight: bold" class="plusIcon">校对运费</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!--    <el-card shadow="never" :body-style="{padding: '20px',backgroundColor:'#D7D7D7'}">-->

      <!--      <el-row>-->
      <!--        <el-col :span="20">-->
      <!--          <span class="f16">预估剩余运费:</span>&nbsp;&nbsp;&nbsp;<span class="f32">{{ balance }}</span>&nbsp;元-->
      <!--        </el-col>-->
      <!--        <el-col :span="4">-->
      <!--        </el-col>-->
      <!--      </el-row>-->
      <!--    </el-card>-->
      <!--    <el-form :inline="true" size="small">-->
      <!--      <el-form-item>-->
      <!--        <el-button type="primary" icon="el-icon-plus">预付运费</el-button>-->
      <!--      </el-form-item>-->
      <!--      <el-form-item>-->
      <!--        <el-button type="primary" icon="el-icon-check">运费校对</el-button>-->
      <!--      </el-form-item>-->
      <!--    </el-form>-->
      <el-table
          :data="tableData"
          border

          class="mt20"
          :height="tableHeight"
          :cell-style="{padding:'10px 0'}"
          style="width: 100%;height: 100%">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="日期">
        </el-table-column>

        <el-table-column
            prop="deposit"
            label="存入">
        </el-table-column>
        <el-table-column
            prop="expend"
            label="支出">
        </el-table-column>
        <el-table-column
            prop="revise"
            label="校对">
        </el-table-column>
        <el-table-column
            prop="balance"
            label="余额">
        </el-table-column>
        <!--      <el-table-column-->
        <!--          label="操作"-->
        <!--          width="200">-->
        <!--        <template slot-scope="scope">-->
        <!--          <el-button @click="modify(scope.row.id)" type="text" size="small">编辑</el-button>-->
        <!--          <el-button @click="remove(scope.row.id)" type="text" size="small">删除</el-button>-->
        <!--        </template>-->
        <!--      </el-table-column>-->
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
          :total="pageOptions.total">>
      </el-pagination>
    </el-card>
    <el-dialog
        v-if="addDialog"
        title="预付运费"
        :visible.sync="addDialog"
        width="30%">
      <el-form :model="form" label-width="80px" size="small">
        <el-form-item label="运费金额" prop="freight">
          <el-input v-model="form.freight" placeholder="请输入预付运费金额"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialog = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submit" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        v-if="checkDialog"
        title="校对运费"
        :visible.sync="checkDialog"
        width="30%">
      <el-form :model="checkForm" label-width="80px" size="small">
        <el-form-item label="运费金额" prop="freight">
          <el-input v-model="checkForm.freight" placeholder="请输入校对运费金额"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="checkDialog = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitCheck" size="small">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";

export default {
  name: "ddjd",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0
      },
      balance: 0.00,
      alertShow: false,
      addDialog: false,
      checkDialog: false,
      form: {
        freight: 2000000.00
      },
      checkForm: {
        freight: null
      }
    }
  },
  created() {
    this.tableHeight = showHeight(340)
    window.onresize = () => {
      this.tableHeight = showHeight(340)
    }
    this.getBalance();
    this.getFreigthHistory();
    // this.tableData.push({
    //   createDate: "2020-11-21",
    //   deposit: "",
    //   expend: "-53918.43",
    //   balance: 10000.32,
    // })
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

    getFreigthHistory() {
      let _this = this;
      var params = {
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize
      }
      _this.$axios.get(_this.$api.getFreightHistory, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
        } else {
          _this.$message.error(res.errmsg);
          console.error(res.detail_msg);
        }
      });
    },
    handleSizeChange(val) {
      this.pageOptions.pageSize = val;
      this.getFreigthHistory();
    },
    handleCurrentChange(val) {
      this.pageOptions.pageNum = val;
      this.getFreigthHistory();
    },
    addFreight() {
      this.addDialog = true;
    },
    submit() {
      if (this.form.freight) {
        var params = {
          freight: this.form.freight
        }
        let _this = this;
        this.$axios.get(this.$api.advanceFreight, {params})
            .then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("操作成功")
                _this.addDialog = false;
                _this.getFreigthHistory();
              } else {
                _this.$message.error(res.errmsg);
                console.error(res.detail_msg);
              }
            });
      } else {
        this.$message.error('请输入运费金额')
      }
    },
    checkFreight() {
      this.checkDialog = true;
    },
    submitCheck() {
      if (this.checkForm.freight) {
        var params = {
          freight: this.checkForm.freight
        }
        let _this = this;
        this.$axios.get(this.$api.checkFreightHistory, {params})
            .then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("操作成功")
                _this.addDialog = false;
                _this.getFreigthHistory();
              } else {
                _this.$message.error(res.errmsg);
                console.error(res.detail_msg);
              }
            });
      } else {
        this.$message.error('请输入运费金额')
      }
    }
  }
}
</script>

<style scoped>
.plusIcon {
  color: #3B91FF;
}
</style>

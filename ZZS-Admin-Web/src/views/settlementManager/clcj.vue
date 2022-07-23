<template>
  <div>
    <el-card class="box-card">
      <template>
        <el-table
            :data="tableData"
            :height="tableHeight"
            border
            style="width: 100%">
          <el-table-column
              type="index"
              label="#"
              width="50">
          </el-table-column>
          <el-table-column
              prop="changePriceDate"
              label="调价日期"
              width="120">
          </el-table-column>
          <el-table-column
              prop="typeName"
              label="油品"
              width="120">
          </el-table-column>

          <el-table-column label="从量从价对账信息">
            <el-table-column
                prop="lastMonthCql"
                label="上月累计超欠量">
            </el-table-column>
            <el-table-column
                prop="planAmount"
                label="本月应交计划量">
            </el-table-column>
            <el-table-column
                prop="totalAmount"
                label="合计">
            </el-table-column>
          </el-table-column>
          <el-table-column label="从量从价结算信息">
            <el-table-column
                prop="realAmount"
                label="本月实际结算量">
            </el-table-column>
            <el-table-column
                prop="diffPrice"
                label="价格调整量">
            </el-table-column>
            <el-table-column
                prop="diffAmount"
                label="超欠结算量">
            </el-table-column>
            <el-table-column
                prop="clcjPrice"
                label="从价">
              <template slot-scope="scope">
              <span style="color: #f02d2d" v-if="scope.row.clcjPrice == null">
                旧价格结算
              </span>
                <span v-if="scope.row.clcjPrice != null">
                {{ scope.row.clcjPrice > 0 ? '+' + scope.row.clcjPrice.toString() : scope.row.clcjPrice.toString() }}
              </span>
              </template>
            </el-table-column>
            <el-table-column
                prop="clcjAmount"
                label="从量">
            </el-table-column>
            <el-table-column
                prop="residue"
                label="从量从价剩余量">
            </el-table-column>
            <el-table-column
                prop="isFinished"
                label="是否完成">
              <template slot-scope="scope">
                <el-tag
                    effect="dark"
                    :type="scope.row.isFinished === 0 ? 'danger' : 'success'"
                    disable-transitions>{{ scope.row.isFinished === 0 ? '未完成' : '已完成' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column
              label="操作">
            <template slot-scope="scope">
              <el-button v-if="scope.row.clcjAmount == scope.row.residue" type="text" size="small" @click="changePrice(scope.row)">调整从价</el-button>
            </template>
          </el-table-column>

        </el-table>
      </template>
    </el-card>

    <el-dialog
        v-if="dialogShow"
        title="调整从价"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="dialogShow"
        width="30%">

      <el-form ref="form" :model="form" label-width="150px" size="small">
        <el-form-item :label= "form.label" prop="price">
          <el-input v-model="form.price" placeholder="请输入从价调整价"></el-input>
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
  name: "clcj",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },
      dialogShow:false,
      form:{
        label:'',
        price:''
      }
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getCjclPage();
  },
  methods: {
    getCjclPage() {
      let _this = this;
      let params = {
        pageNum: 1,
        pageSize: 50,
      }
      _this.$axios.get(this.$api.getCjcl, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },

    changePrice(row){
      this.dialogShow = true;
      this.form.label = row.typeName + "从价调整量";
      this.form.id = row.id;
    },

    submit(){
      this.$axios.get(this.$api.changeClcjPrice + '?id='+ this.form.id+'&price=' + this.form.price).then(res=>{
        if (res.errcode === 0){
          this.$message.success("操作成功!");
          this.dialogShow = false;
          this.getCjclPage();
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

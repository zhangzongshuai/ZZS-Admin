<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" size="small">
        <el-form-item>
          <el-button type="primary" icon="el-icon-plus" size="small" @click="addIncrement">新增增量计划</el-button>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="finishedType" placeholder="请选择" clearable>
            <el-option label="已完成" value="1"></el-option>
            <el-option label="未完成" value="0"></el-option>
            <el-option label="执行中" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="search">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table
          :data="tableData"
          :height="tableHeight"
          border
          :cell-style="{padding:'5px 0'}"
          style="width: 100%">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="日期"
            width="180">
        </el-table-column>
        <el-table-column
            prop="name"
            label="油品">
        </el-table-column>
        <el-table-column
            prop="zlje"
            label="增量金额(不含税)">
        </el-table-column>
        <el-table-column
            prop="zlhsje"
            label="增量金额(含税)">
        </el-table-column>
        <el-table-column
            prop="zlbtdj"
            label="增量补贴单价">
        </el-table-column>
        <el-table-column
            prop="zljsl"
            label="增量补贴量">
        </el-table-column>
        <el-table-column
            prop="zlsyjsl"
            label="剩余补贴剩余结算量">
        </el-table-column>
        <el-table-column
            label="状态">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.isFinished">已完成</el-tag>
            <el-tag type="info" v-if="scope.row.isFinished && scope.row.stratDate">执行中</el-tag>
            <el-tag type="danger" v-if="!scope.row.isFinished">未完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                v-if="scope.row.zlsyjsl== scope.row.zljsl"
                @click.native.prevent="changeIncrementPrice(scope.row)"
                type="text"
                size="small">
              人工调价
            </el-button>
            <el-button
                v-if="!scope.row.isFinished"
                @click.native.prevent="deleteIncrement(scope.row.id)"
                type="text"
                size="small">
              删除
            </el-button>
            <el-button
                v-if="!scope.row.isFinished"
                @click.native.prevent="modifyIncrement(scope.row.id)"
                type="text"
                size="small">
              编辑
            </el-button>
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

      <el-form ref="userForm" :model="incrementForm" :rules="incrementRules" label-width="120px" size="small">
        <el-form-item label="汽油增量金额" prop="gasoline">
          <el-input v-model="incrementForm.gasoline"></el-input>
        </el-form-item>
        <el-form-item label="柴油增量金额" prop="diesel">
          <el-input v-model="incrementForm.diesel"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submit()" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        v-if="modifyDialogShow"
        title="编辑"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="modifyDialogShow"
        width="30%">

      <el-form ref="userForm" :model="modifyForm" label-width="120px" size="small">
        <el-form-item :label="modifyForm.type=='gasoline'?'汽油增量金额':'柴油增量金额'" prop="zlje">
          <el-input v-model="modifyForm.zlje"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="modifyDialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitModify()" size="small">确 定</el-button>
  </span>
    </el-dialog>


    <el-dialog
        v-if="changeDialogShow"
        title="调整增量补贴单价"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="changeDialogShow"
        width="30%">

      <el-form ref="changeForm" :model="changeForm" label-width="180px" size="small">
        <el-form-item :label="changeForm.label" prop="zlje">
          <el-input v-model="changeForm.price" placeholder="请输入增量结算价格补贴量"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="changeDialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitChange()" size="small">确 定</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";

export default {
  name: "increment",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      finishedType: null,
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },
      dialogShow: false,
      dialogTitle: '新增增量计划',
      incrementForm: {},
      incrementRules: {
        gasoline: {required: true, message: '请输入汽油金额', trigger: 'blur'},
        diesel: {required: true, message: '请输入柴油金额', trigger: 'blur'}
      },
      modifyDialogShow: false,
      modifyForm: {
        id: "",
        type: "",
        zlje: ""
      },
      changeDialogShow: false,
      changeForm: {
        id: '',
        label: '',
        price: ''
      }
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getIncrement();
  },
  methods: {

    getIncrement() {
      let _this = this;
      var params = {
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize,
      }
      if (this.finishedType) {
        params.finishedType = this.finishedType;
      }
      _this.$axios.get(_this.$api.getIncrements, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    addIncrement() {
      this.dialogShow = true;
      this.dialogTitle = '新增增量计划'
    },
    submit() {
      let _this = this;
      let params = this.incrementForm;
      if (params.id) {
        console.log('')
      } else {
        let params = [{
          type: "gasoline",
          zlje: this.incrementForm.gasoline
        }, {
          type: "diesel",
          zlje: this.incrementForm.diesel
        }];

        _this.$axios.post(_this.$api.addIncrement, params).then(function (res) {
          if (res.errcode === 0) {
            _this.dialogShow = false;
            _this.getIncrement();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      }
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getIncrement();
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getIncrement();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getIncrement();

    },
    deleteIncrement(id) {
      let _this = this;
      _this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        _this.$axios.delete(_this.$api.deleteIncrement + '/' + id).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success('删除成功')
            _this.getIncrement();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      }).catch(function () {

      });

    },
    modifyIncrement(id) {
      let _this = this;
      _this.modifyDialogShow = true;
      let params = {id: id}
      _this.$axios.get(_this.$api.getIncrement, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.modifyForm = res.data;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    submitModify() {
      let _this = this;
      let params = this.modifyForm;
      _this.$axios.put(_this.$api.modifyIncrement, params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success('操作成功');
          _this.modifyDialogShow = false;
          _this.getIncrement();
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },

    changeIncrementPrice(row){
      this.changeDialogShow = true;
      this.changeForm.id = row.id;
      this.changeForm.label = row.name + '从量从价价格调整量';
    },
    submitChange(){
      let params = {
        id:this.changeForm.id,
        price: this.changeForm.price
      }
      this.$axios.get(this.$api.changeIncrementPrice,{params}).then(res=>{
        if (res.errcode === 0){
          this.$message.success("操作成功");
          this.changeDialogShow = false;
          this.getIncrement();
        }else{
          this.$message.error(res.errmsg);
        }
      })
    }
  }
}
</script>

<style scoped>

</style>

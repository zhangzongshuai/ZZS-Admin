<template>
  <div>
    <el-card class="box-card">
    <el-form :inline="true" :model="searchParams" size="small">
      <el-form-item>
        <el-button type="primary" icon="el-icon-plus" @click="addProduct">新增产品</el-button>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="searchParams.name" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
      </el-form-item>
    </el-form>
    <template>
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
            label="名称"
            width="180">
        </el-table-column>
        <el-table-column
            prop="price"
            label="当前价格"
            width="180">
        </el-table-column>
        <el-table-column
            prop="startTime"
            label="当前价格开始时间"
            width="180">
        </el-table-column>
        <el-table-column
            label="启用状态"
            width="100">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.isEnabled === 0 ? 'danger' : ''"
                disable-transitions>{{ scope.row.isEnabled === 0 ? '禁用' : '启用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作">
          <template slot-scope="scope">
            <el-button @click="modifyProduct(scope.row.id)" type="text" size="small">编辑</el-button>
            <el-button @click="deleteProduct(scope.row.id)" type="text" size="small">删除</el-button>
            <el-button @click="changeProductPrice(scope.row)" type="text" size="small">调价</el-button>
            <el-button @click="price(scope.row.id)" type="text" size="small">历史价格
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
    </template>
    </el-card>
    <!--  添加或编辑-->
    <el-dialog
        v-if="dialogShow"
        v-dialogDrag
        :close-on-click-modal="false"
        :title="dialogTitle"
        :visible.sync="dialogShow"
        width="30%">

      <el-form ref="productForm" :model="productForm" :rules="productRules" label-width="120px" size="small">
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="productForm.name"></el-input>
        </el-form-item>

        <el-form-item label="产品单价" prop="price">
          <el-input v-model="productForm.price"></el-input>
        </el-form-item>

        <el-form-item label="单价生效日期" prop="startTime">
          <el-date-picker
              v-model="productForm.startTime"
              type="date"
              :clearable="false"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="是否启用">
          <el-switch v-model="productForm.isEnabled" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>


      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submit()" size="small">确 定</el-button>
  </span>

    </el-dialog>

    <el-dialog title="调整价格" :visible.sync="changePriceShow" width="30%">
      <el-form ref="productForm" :model="priceForm" :rules="priceRules" label-width="120px" size="small">
        <el-form-item label="产品名称" prop="name">
          <span>{{ productName }}</span>
        </el-form-item>

        <el-form-item label="产品单价" prop="price">
          <el-input v-model="priceForm.price"></el-input>
        </el-form-item>

        <el-form-item label="单价生效日期" prop="startTime">
          <el-date-picker
              v-model="priceForm.startTime"
              type="date"
              :clearable="false"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="changePriceShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitChange()" size="small">确 定</el-button>
  </span>
    </el-dialog>


    <el-dialog title="历史价格" :visible.sync="dialogPriceShow" width="30%">
      <el-table :data="priceData" max-height="200">
        <el-table-column property="startTime" label="开始时间" width="150"></el-table-column>
        <el-table-column property="endTime" label="结束时间" width="200"></el-table-column>
        <el-table-column property="price" label="价格"></el-table-column>
      </el-table>
      <el-pagination
          class="mt10"
          background
          @size-change="priceSizeChange"
          @current-change="priceCurrentChange"
          :current-page="pricePage.pageNum"
          :page-sizes="[5, 10, 20,50,100,200]"
          :page-size="pricePage.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pricePage.total">
      </el-pagination>
    </el-dialog>
  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";

let userInfo = JSON.parse(localStorage.getItem('userInfo'));

export default {
  name: "product",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },
      dialogTitle: "添加产品",
      dialogShow: false,
      searchParams: {
        name: ''
      },
      productForm: {
        id: "",
        name: "",
        price: "",
        startTime: new Date(),
        isEnabled: 1,
      },
      productRules: {
        name: {required: true, message: '请输入产品名称', trigger: 'blur'},
        price: {required: true, message: '请输入产品价格', trigger: 'blur'},
      },
      dialogPriceShow: false,
      priceData: [],
      productId: "",
      pricePage: {
        pageNum: 1,
        pageSize: 5,
        total: 0,
      },
      changePriceShow: false,
      priceForm: {
        id: "",
        price: "",
        startTime: ""
      },
      productName: "",
      priceRules: {
        price: {required: true, message: '请输入产品价格', trigger: 'blur'},
      }
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getProduct();
  },
  methods: {
    getProduct() {
      let that = this;
      var params = {
        pageNum: that.pageOptions.pageNum,
        pageSize: that.pageOptions.pageSize,
      }
      if (that.searchParams.name) {
        params.name = that.searchParams.name;
      }
      that.$axios.get(that.$api.getProductPage, {params}).then(function (res) {
        if (res.errcode === 0) {
          that.tableData = res.datas;
          that.pageOptions.total = res.totalCount;
        } else {
          that.$message.error(res.errmsg);
        }
      });
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getProduct();
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getProduct();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getProduct();

    },
    addProduct() {
      this.productForm = {
        id: "",
        name: "",
        price: "",
        startTime: new Date(),
        isEnabled: 1,
      };
      this.dialogShow = true;
    },
    submit() {

      let that = this;
      let params = that.productForm;

      if (!params.id) {
        params.creator = userInfo.login_name;
        that.$axios.post(that.$api.addProduct, params).then(function (res) {
          if (res.errcode === 0) {
            that.$message.success("添加成功")
            that.dialogShow = false;
            that.pageOptions.pageNum = 1;
            that.getProduct();
          } else {
            that.$message.error(res.errmsg);
          }
        });
      } else {
        params.modifier = userInfo.login_name;
        that.$axios.put(that.$api.modifyProduct, params).then(function (res) {
          if (res.errcode === 0) {
            that.$message.success("修改成功")
            that.dialogShow = false;
            that.pageOptions.pageNum = 1;
            that.getProduct();
          } else {
            that.$message.error(res.errmsg);
          }
        });
      }


    },
    modifyProduct(id) {
      let that = this;
      that.dialogShow = true;
      that.dialogTitle = "编辑产品"
      let params = {id: id}
      that.$axios.get(that.$api.getProduct, {params}).then(function (res) {
        if (res.errcode === 0) {
          that.productForm = res.data;
        } else {
          that.$message.error(res.errmsg);
        }
      })
    },
    deleteProduct(id) {
      let that = this;
      that.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        let params = {
          id: id
        }
        that.$axios.delete(that.$api.deleteProduct, {params}).then(function (res) {
          if (res.errcode === 0) {
            that.$message.success("删除成功");
            that.getProduct();
          } else {
            that.$message.error(res.errmsg);
          }
        });
      })
    },

    price(id) {

      let that = this;
      that.productId = id;

      that.getPriceHis();
      that.dialogPriceShow = true;
    },
    getPriceHis() {
      let that = this;
      let params = {
        productId: that.productId,
        pageNum: that.pricePage.pageNum,
        pageSize: that.pricePage.pageSize
      }
      that.$axios.get(that.$api.getPriceHis, {params}).then(function (res) {
        if (res.errcode === 0) {
          that.priceData = res.datas;
          that.pricePage.total = res.totalCount;
        } else {
          that.$message.error(res.errmsg);
        }
      });
    },
    changeProductPrice(row) {
      this.priceForm.id = "";
      this.productName = row.name;
      this.priceForm.productId = row.id;
      this.priceForm.price = row.price;
      this.priceForm.startTime = row.startTime;
      this.changePriceShow = true;
    },
    submitChange() {
      let that = this;
      let params = that.priceForm;
      that.$axios.post(that.$api.changeProductPrice + '?id=' + params.productId, params).then(function (res) {
        if (res.errcode === 0) {
          that.$message.success("调价成功")
          that.changePriceShow = false;
          that.getProduct();
        } else {
          that.$message.error(res.errmsg);
        }
      });
    },
    priceSizeChange(val) {
      this.pricePage.pageNum = 1;
      this.pricePage.pageSize = val;
      this.getPriceHis();
    },
    priceCurrentChange(val) {

      this.pricePage.pageNum = val
      this.getPriceHis();

    },


  }
}
</script>

<style scoped>

</style>

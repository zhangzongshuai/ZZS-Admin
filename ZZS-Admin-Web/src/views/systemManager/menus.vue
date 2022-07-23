<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item>
          <el-button type="primary" icon="el-icon-plus" @click="addMenu">新增菜单</el-button>
        </el-form-item>
        <el-form-item label="菜单名称">
          <el-input v-model="searchParams.name" placeholder="请输入菜单名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table
          :data="tableData"
          border
          :cell-style="{padding:'5px 0'}"
          :height="tableHeight"
          style="width: 100%">
        <el-table-column
            type="index"
            label="#"
            width="50">
        </el-table-column>
        <el-table-column
            prop="name"
            label="菜单名称"
            width="180">
        </el-table-column>
        <el-table-column
            prop="url"
            label="URL地址"
            width="180">
        </el-table-column>
        <el-table-column
            prop="orderId"
            label="排序号"
            width="100">
        </el-table-column>
        <el-table-column
            prop="isEnabled"
            label="状态"
            width="100">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.isEnabled === 0 ? 'danger' : ''"
                disable-transitions>{{ scope.row.isEnabled === 0 ? '禁用' : '启用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button @click="modifyMenu(scope.row.id)" type="text" size="small">编辑</el-button>
            <el-button @click="deleteMenu(scope.row.id)" type="text" size="small">删除</el-button>
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

      <el-form ref="userForm" :model="menuForm" :rules="menuRules" label-width="80px" size="small">

        <el-form-item label="菜单名" prop="name">
          <el-input v-model="menuForm.name"></el-input>
        </el-form-item>
        <el-form-item label="是否菜单">
          <el-switch v-model="menuForm.isMenu" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item label="父级菜单" prop="parentId" v-if="menuForm.isMenu">
          <treeselect
              v-model="menuForm.parentId"
              :multiple="false"
              :options="treeOptions"
              @select="selectTree"
              placeholder="请选择父级菜单"/>
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="menuForm.url"></el-input>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="menuForm.isEnabled" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item label="排序号" prop="orderId">
          <el-input v-model="menuForm.orderId"></el-input>
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
import {buildMenuTree} from "@/common/menuTree"

// import the component
import Treeselect from '@riophae/vue-treeselect'
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

let userInfo = JSON.parse(localStorage.getItem('userInfo'));
export default {
  name: "menus",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      searchParams: {
        name: ""
      },
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },
      dialogShow: false,
      menuForm: {
        id: null,
        name: '',
        url: '',
        isEnabled: 1,
        isAction: 1,
        isMenu: 1,
        orderId: null
      },
      copyMenu: {},
      menuRules: {
        name: {required: true, message: '请输入菜单名称', trigger: 'blur'},
        url: {required: true, message: '请输入url', trigger: 'blur'}
      },
      treeOptions: [],
      dialogTitle: '添加菜单'
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getMenuList();
  },
  methods: {

    getMenuList() {
      let _this = this;
      var params = {
        pageNum: _this.pageOptions.pageNum,
        pageSize: _this.pageOptions.pageSize,
      }
      if (_this.searchParams.name) {
        params.name = _this.searchParams.name;
      }
      _this.$axios.get(_this.$api.getMenus, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    getMenuTreeData(id) {

      let _this = this;
      var params = {
        pageNum: 1,
        pageSize: 5000,
      }
      _this.$axios.get(_this.$api.getMenus, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.treeOptions = buildMenuTree(res.datas, id);
        } else {
          _this.$message.error(res.errmsg);
        }
      });


    },
    selectTree(node, instanceId) {
      console.log(node, instanceId)
      if (node.id == this.menuForm.id) {
        this.$message.error('不可选择自己')
      }
    },

    addMenu() {
      this.dialogShow = true;
      this.menuForm = {
        name: '',
        url: '',
        isEnabled: 1,
        isAction: 1,
        isMenu: 1,
        orderId: null
      },
          this.getMenuTreeData();
    },
    submit() {
      let _this = this;
      var params = this.menuForm;
      if (this.menuForm.id) {
        var save = false;
        if (params.name != _this.copyMenu.name) {
          save = true
        }
        if (params.parentId != _this.copyMenu.parentId) {
          save = true
        }
        if (params.isMenu != _this.copyMenu.isMenu) {
          save = true
        }
        if (params.isAction != _this.copyMenu.isAction) {
          save = true
        }
        if (params.url != _this.copyMenu.url) {
          save = true
        }
        if (params.orderId != _this.copyMenu.orderId) {
          save = true
        }
        if (params.isEnabled != _this.copyMenu.isEnabled) {
          save = true
        }
        if (!save) {
          _this.dialogShow = false;
          return;
        }
        _this.$axios.put(_this.$api.modifyMenu + "?loginName=" + userInfo.login_name, params).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("修改成功!")
            _this.dialogShow = false;
            _this.getMenuList();
          } else {
            _this.$message.error(res.errmsg);
          }
        })
      } else {
        _this.$axios.post(_this.$api.addMenu + "?loginName=" + userInfo.login_name, params).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("添加成功!")
            _this.dialogShow = false;
            _this.getMenuList();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      }
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getMenuList();
    },
    modifyMenu(id) {
      let _this = this;
      _this.getMenuTreeData(id);

      _this.dialogTitle = "编辑菜单";
      let params = {id: id}
      _this.$axios.get(_this.$api.getMenu, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.menuForm = res.data;
          if (_this.menuForm.parentId == "0") {
            _this.menuForm.parentId = null;
          }
          _this.copyMenu = JSON.parse(JSON.stringify(_this.menuForm));
        } else {
          _this.$message.error(res.errmsg);
        }
      });
      _this.dialogShow = true;
    },
    deleteMenu(id) {
      if (!id) {
        return;
      }
      let _this = this;

      _this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        _this.$axios.delete(_this.$api.deleteMenu + '/' + id).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("删除成功!")
            _this.getMenuList();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      }).catch(function () {
      })


    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getMenuList();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getMenuList();

    },
  },
  components: {Treeselect},
}
</script>

<style scoped>

</style>

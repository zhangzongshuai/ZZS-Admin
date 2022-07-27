<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item>
          <el-button type="primary" icon="el-icon-plus" @click="addMenu" v-permissions="'menu:add'">新增菜单</el-button>
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
          row-key="id"
          default-expand-all
          :cell-style="{padding:'5px 0'}"
          :height="tableHeight"
          style="width: 100%">
        <el-table-column
            type="index"
            label="#"
            align="center"
            width="50">
        </el-table-column>
        <el-table-column
            prop="name"
            label="菜单名称"
            min-width="180">
        </el-table-column>
        <el-table-column
            prop="type"
            label="菜单类型"
            min-width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.type == 'menu'">菜单</span>
            <span v-if="scope.row.type == 'btn'">按钮</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="iconCode"
            label="图标"
            header-align="center"
            align="center"
            width="100">
          <template slot-scope="scope">
            <i :class="scope.row.iconCode"></i>
          </template>
        </el-table-column>
        <el-table-column
            prop="url"
            label="菜单路径"
            min-width="180">
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
            <el-button @click="modifyMenu(scope.row.id)" type="text" size="small" v-permissions="'menu:edit'">编辑
            </el-button>
            <el-button @click="deleteMenu(scope.row.id)" type="text" size="small" v-permissions="'menu:delete'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--      <el-pagination-->
      <!--          class="mt10"-->
      <!--          background-->
      <!--          @size-change="handleSizeChange"-->
      <!--          @current-change="handleCurrentChange"-->
      <!--          :current-page="pageOptions.pageNum"-->
      <!--          :page-sizes="[20, 50, 100,200]"-->
      <!--          :page-size="pageOptions.pageSize"-->
      <!--          layout="total, sizes, prev, pager, next, jumper"-->
      <!--          :total="pageOptions.total">-->
      <!--      </el-pagination>-->
    </el-card>

    <el-dialog
        v-dialogDrag
        :close-on-click-modal="false"
        :title="dialogTitle"
        :visible.sync="dialogShow"
        width="30%">

      <el-form ref="userForm" :model="menuForm" :rules="menuRules" label-width="100px" size="small">
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="menuForm.type" @change="menuTypeChange">
            <el-radio label="menu">菜单</el-radio>
            <el-radio label="btn">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="name">
          <span slot="label">
            {{ menuForm.type == 'menu' ? '菜单名称' : '按钮名称' }}
          </span>
          <el-input v-model="menuForm.name" placeholder="请输入菜单名称"></el-input>
        </el-form-item>
        <el-form-item label="上级菜单" prop="parentId">
          <treeselect
              v-model="menuForm.parentId"
              :multiple="false"
              :options="treeOptions"
              @select="selectTree"
              placeholder="请选择父级菜单"/>
        </el-form-item>
        <el-form-item label="授权标识" prop="permissionCode" v-if="menuForm.type == 'btn'">
          <el-input v-model="menuForm.permissionCode" placeholder="请输入授权标识，例如user:add"></el-input>
        </el-form-item>
        <el-form-item label="菜单路径" prop="url" v-if="menuForm.type == 'menu'">
          <el-input v-model="menuForm.url" placeholder="请输入url"></el-input>
        </el-form-item>
        <el-form-item label="图标" prop="iconCode" v-if="menuForm.type == 'menu'">
          <el-input placeholder="请点击选择图标" v-model="menuForm.iconCode">
            <el-button slot="append" type="primary" icon="el-icon-s-tools" @click="chooseIcon"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="menuForm.isEnabled" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item label="排序号" prop="orderId" v-if="menuForm.type == 'menu'">
          <el-input v-model="menuForm.orderId"></el-input>
        </el-form-item>
      </el-form>


      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogShow = false" size="small">取 消</el-button>
        <el-button type="primary" @click="submit()" size="small">确 定</el-button>
      </span>
    </el-dialog>


    <el-dialog
        v-dialogDrag
        :close-on-click-modal="false"
        title="选择图标"
        :visible.sync="iconDialogShow"
        width="50%">

      <div class="menuContainer">
        <template v-for="(icon,index) in iconList">
          <i :key="index" v-if="icon.check" class="iconItem checkIcon" :class=icon.iconCode
             @click="clickIcon(icon)"></i>
          <i :key="index" v-if="!icon.check" class="iconItem" :class=icon.iconCode @click="clickIcon(icon)"></i>
        </template>

      </div>


      <span slot="footer" class="dialog-footer">
        <el-button @click="iconDialogShow = false" size="small">取 消</el-button>
        <el-button type="primary" @click="confirmIcon()" size="small">确 定</el-button>
      </span>
    </el-dialog>

  </div>


</template>

<script>
import {showHeight} from "@/common/tableHeight";
import {buildMenuTree} from "@/common/menuTree"
import Treeselect from '@riophae/vue-treeselect'
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
        type: 'menu',
        name: '',
        parentId: null,
        permissionCode: '',
        url: '',
        iconCode: '',
        isEnabled: 1,
        orderId: null
      },
      copyMenu: {},
      menuRules: {
        name: {required: true, message: '请输入菜单名称', trigger: 'blur'},
        url: {required: true, message: '请输入url', trigger: 'blur'}
      },
      treeOptions: [],
      dialogTitle: '添加菜单',
      iconDialogShow: false,
      iconList: [],
      selectIcon: null
    }
  },
  created() {
    this.tableHeight = showHeight(240)
    window.onresize = () => {
      this.tableHeight = showHeight(240)
    }
    this.getMenuList();
  },
  methods: {

    menuTypeChange(val) {
      this.menuForm = {
        id: null,
        type: val,
        name: '',
        parentId: null,
        url: '',
        permissionCode: '',
        iconCode: '',
        isEnabled: 1,
        orderId: null
      }
    },

    getMenuList() {
      let _this = this;
      var params = {
        pageNum: _this.pageOptions.pageNum,
        pageSize: _this.pageOptions.pageSize,
      }
      if (_this.searchParams.name) {
        params.name = _this.searchParams.name;
      }
      _this.$axios.get(_this.$api.getMenuTree, {params}).then(function (res) {
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
      _this.$axios.get(_this.$api.getMenus).then(function (res) {
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
        type: 'menu',
        name: '',
        parentId: null,
        url: '',
        permissionCode: '',
        iconCode: '',
        isEnabled: 1,
        orderId: null
      },
          this.getMenuTreeData();
    },
    submit() {
      let _this = this;
      var params = this.menuForm;
      if (this.menuForm.id) {
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
    chooseIcon() {
      this.iconDialogShow = true;
      this.getIcon();
    },
    getIcon() {
      this.iconList = []
      this.$axios.get(this.$api.getIconList).then(res => {
        if (res.errcode === 0) {
          res.datas.forEach(f => {
            f.check = false;
            this.iconList.push(f);
          })
          let currIcon = this.iconList.filter(f => f.iconCode === this.menuForm.iconCode);
          if (currIcon.length > 0) {
            currIcon[0].check = true;
          }
        }
      })

    },
    clickIcon(icon) {
      this.iconList.forEach(f => {
        f.check = false;
      })
      icon.check = true;
      this.selectIcon = icon.iconCode;
    },
    confirmIcon() {
      if (this.selectIcon) {
        this.menuForm.iconCode = this.selectIcon
      }
      this.iconDialogShow = false;

    }
  },
  components: {Treeselect},
}
</script>

<style scoped>
.menuContainer {
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
  width: 100%;
  height: 500px;
  overflow: scroll;
}

.container .iconItem {
  border: #dddddd solid 1px;
  margin: 5px;
  width: 50px;
  height: 50px;
  font-size: 45px;
  text-align: center;
  line-height: 50px;
  cursor: pointer;
}

.iconItem:hover {
  color: #2d8cf0;
  background-color: #dddddd;
}

.checkIcon {
  color: #2d8cf0;
  background-color: #dddddd;
}

</style>

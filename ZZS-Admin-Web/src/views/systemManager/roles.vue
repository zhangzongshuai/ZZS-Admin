<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item>
          <el-button type="primary" icon="el-icon-plus" @click="addUser" v-permissions="'role:add'">新增角色</el-button>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="searchParams.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table
          :data="tableData"
          :height="tableHeight"
          :cell-style="{padding:'5px 0'}"
          border
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
            prop="description"
            label="描述"
            min-width="180">
        </el-table-column>
        <el-table-column
            prop="isEnabled"
            label="状态">
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
            <el-button @click="modifyRole(scope.row.id)" type="text" size="small" v-permissions="'role:edit'">编辑</el-button>
            <el-button @click="deleteRole(scope.row.id)" type="text" size="small" v-permissions="'role:delete'">删除</el-button>
            <el-button @click="assignMenu(scope.row.id,scope.row.name)" type="text" size="small" v-permissions="'role:setRoles'">分配权限</el-button>
            <el-button @click="assignUser(scope.row.id,scope.row.name)" type="text" size="small" v-permissions="'role:setUsers'">分配用户</el-button>
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
    <!--  添加或编辑角色-->
    <el-dialog
        v-if="dialogShow"
        v-dialogDrag
        :close-on-click-modal="false"
        :title="dialogTitle"
        :visible.sync="dialogShow"
        width="30%">

      <el-form ref="roleForm" :model="roleForm" :rules="roleRules" label-width="80px" size="small">

        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="roleForm.description"></el-input>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="roleForm.isEnabled" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submit()" size="small">确 定</el-button>
  </span>
    </el-dialog>


    <!--  配置菜单权限-->
    <el-dialog
        v-if="assignMenuDailog"
        title="分配权限"
        :visible.sync="assignMenuDailog"
        width="40%">
      <el-row :gutter="20">
        <el-col :span="6">
          <div>角色名称:</div>
        </el-col>
        <el-col :span="18">
          <div class="fb">{{ assignRole.name }}</div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mt20">
        <el-col :span="6">
          <div>分配权限:</div>
        </el-col>
        <el-col :span="18">
          <div class="p10 h300 oa" style="border: #adb3bc 1px solid">
            <el-tree
                ref="menuTree"
                default-expand-all
                :check-strictly="true"
                :data="menuTreeData"
                :props="defaultProps"
                node-key="id"
                show-checkbox>
            </el-tree>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
    <el-button @click="assignMenuDailog = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitAssignMenu()" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <!--  配置用户-->
    <el-dialog
        v-if="assignUserDailog"
        title="分配用户"
        :visible.sync="assignUserDailog"
        width="40%">
      <el-row :gutter="20">
        <el-col :span="4">
          <div>角色名称:</div>
        </el-col>
        <el-col :span="6">
          <div class="fb">{{ assignRoleUser.name }}</div>
        </el-col>
        <el-col :span="8">
          <div>
            <el-input v-model="searchName" placeholder="请输入用户名" size="small"></el-input>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-button type="primary" size="small" @click="searchUser">搜索</el-button>
          </div>
        </el-col>
      </el-row>
      <el-row class="mt20">
        <el-col :span="24">
          <div>
            <el-table
                ref="userTable"
                :data="userData"
                border
                row-key="id"
                :cell-style="{padding:'5px 0'}"
                @selection-change="selectChange"
                style="width: 100%">
              <el-table-column
                  type="selection"
                  reserve-selection
                  width="55">
              </el-table-column>
              <el-table-column
                  type="index"
                  label="#"
                  width="50">
              </el-table-column>

              <el-table-column
                  prop="name"
                  label="姓名"
                  width="180">
              </el-table-column>
              <el-table-column
                  prop="loginName"
                  label="登录名">
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
      <el-pagination
          class="mt10"
          background
          @size-change="userSizeChange"
          @current-change="userCurrentChange"
          :current-page="userPage.pageNum"
          :page-sizes="[2,10, 20, 50,100]"
          :page-size="userPage.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="userPage.total">
      </el-pagination>
      <span slot="footer" class="dialog-footer">
    <el-button @click="assignUserDailog = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitAssignUser()" size="small">确 定</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
import {showHeight} from "@/common/tableHeight";

let userInfo = JSON.parse(localStorage.getItem('userInfo'));
export default {
  name: "roles",
  data() {
    return {
      tableHeight: null,
      tableData: [],
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },
      searchParams: {
        name: null
      },
      dialogShow: false,
      dialogTitle: '新增角色',
      roleForm: {
        name: '',
        description: '',
        isEnabled: 1
      },
      roleRules: {
        name: {required: true, message: '请输入用户名', trigger: 'blur'},
      },
      copyRole: {},

      assignMenuDailog: false,
      assignRole: {
        id: "",
        name: "",
      },
      menuTreeData: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      defaultCheckedMenus:[],
      checkedKeys: [],

      assignUserDailog: false,
      assignRoleUser: {},
      searchName: "",
      userData: [],
      userPage: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
      selectUser: []
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getRoleList();
  },
  methods: {

    getRoleList() {
      let _this = this;
      let params = {
        pageNum: this.pageOptions.pageNum,
        pageSize: this.pageOptions.pageSize,
      }
      if (this.searchParams.name) {
        params.name = this.searchParams.name;
      }
      _this.$axios.get(_this.$api.getRoles, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.total = res.totalCount;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getRoleList();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getRoleList();

    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getRoleList();
    },
    addUser() {
      this.dialogTitle = "添加角色";
      this.dialogShow = true;
      this.roleForm = {
        name: '',
        description: '',
        isEnabled: 1
      }
    },
    submit() {
      let _this = this;
      var params = this.roleForm;
      _this.$refs['roleForm'].validate(function (valid) {
        if (valid) {
          if (_this.roleForm.id) {
            var save = false;
            if (params.name != _this.copyRole.name
                || params.description != _this.copyRole.description
                || params.isEnabled != _this.copyRole.isEnabled) {
              save = true;
            }
            if (!save) {
              _this.dialogShow = false;
              return;
            }
            _this.$axios.put(_this.$api.modifyRole + "?loginName=" + userInfo.login_name, params).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("修改成功!")
                _this.dialogShow = false;
                _this.getRoleList();
              } else {
                _this.$message.error(res.errmsg);
              }
            })
          } else {
            _this.$axios.post(_this.$api.addRole + "?loginName=" + userInfo.login_name, params).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("添加成功!")
                _this.dialogShow = false;
                _this.getRoleList();
              } else {
                _this.$message.error(res.errmsg);
              }
            });
          }
        } else {
          return false;
        }
      });

    },
    modifyRole(id) {
      let _this = this;
      _this.dialogTitle = "编辑角色";
      let params = {id: id}
      _this.$axios.get(_this.$api.getRole, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.roleForm = res.data;
          _this.copyRole = JSON.parse(JSON.stringify(_this.roleForm));
        } else {
          _this.$message.error(res.errmsg);
        }
      });
      _this.dialogShow = true;
    },
    deleteRole(id) {
      if (!id) {
        return;
      }
      let _this = this;

      _this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        _this.$axios.delete(_this.$api.deleteRole + '/' + id).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("删除成功!")
            _this.getRoleList();
          } else {
            _this.$message.error(res.errmsg);
          }
        });
      }).catch(function () {
      })
    },
    assignMenu(id, name) {
      this.assignMenuDailog = true;
      this.assignRole = {id: id, name: name};
      this.menuTreeData = [];
      let _this = this;
      _this.$axios.get(_this.$api.getMenuTree).then(function (res) {
        if (res.errcode === 0) {
          _this.getCheckesMenus(id, res.datas);
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },

    getCheckesMenus(id, data) {
      let _this = this;
      _this.$axios.get(_this.$api.getRoleMenus + "?roleId=" + id).then(function (res) {
        if (res.errcode === 0) {
          let temp = res.datas.map(function (item) {
            return item.menuId;
          });
          _this.menuTreeData = data;
          setTimeout(function () {
            temp.forEach(value => {
              _this.$refs.menuTree.setChecked(value, true, false);
            });
          }, 100);
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },

    submitAssignMenu() {
      let checkMenu = this.$refs.menuTree.getCheckedNodes(false, true);
      let menuIds = checkMenu.map(function (item) {
        return item.id;
      });
      let _this = this;
      _this.$axios.post(_this.$api.configRoleMenu + "?roleId=" + _this.assignRole.id, menuIds).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("操作成功");
          _this.assignMenuDailog = false;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    menuTreeCheck() {
      console.log(this.$refs.menuTree.getCheckedNodes(false, true));
    },

    assignUser(id, name) {
      this.assignRoleUser.id = id;
      this.assignRoleUser.name = name;
      this.assignUserDailog = true;
      //this.getRoleUserIds();
      this.searchName = null,
          this.userData = [];
      this.userPage = {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      }
      this.getUsersList();

    },
    getUsersList() {
      let _this = this;
      let params = {
        pageNum: this.userPage.pageNum,
        pageSize: this.userPage.pageSize,
        name: this.searchName,
        roleId: this.assignRoleUser.id
      }
      _this.$axios.get(_this.$api.getRoleUsers, {params}).then(function (res) {
        if (res.errcode == 0) {
          _this.userData = res.datas;
          _this.userPage.total = res.totalCount;
          let temp = _this.userData.filter(function (item) {
            return item.isAssign === 1;
          })

          //用来处理数据未渲染的时候无法设置选中
          setTimeout(function () {
            temp.forEach(row => {
              _this.$refs.userTable.toggleRowSelection(row, true);
            });
          }, 0)


        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    // getRoleUserIds() {
    //   let _this = this;
    //
    //   _this.$axios.get(_this.$api.getRoleUsers + '?roleId=' + _this.assignRoleUser.id).then(function (res) {
    //     if (res.errcode === 0) {
    //       let temp = res.datas;
    //       _this.checkedUserKeys = temp.map(function (item) {
    //         return item.userId;
    //       });
    //     } else {
    //       _this.$message.error(res.errmsg);
    //     }
    //   });
    // },
    searchUser() {
      this.userPage.pageNum = 1;
      this.getUsersList();
    },
    userSizeChange(val) {
      this.userPage.pageNum = 1;
      this.userPage.pageSize = val;
      this.getUsersList();
    },
    userCurrentChange(val) {

      this.userPage.pageNum = val
      this.getUsersList();

    },
    selectChange(val) {
      this.selectUser = val;
    },
    submitAssignUser() {
      let menuIds = this.selectUser.map(function (item) {
        return item.id;
      });
      let _this = this;
      _this.$axios.post(_this.$api.configRoleUser + "?roleId=" + _this.assignRoleUser.id, menuIds).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("操作成功");
          _this.assignUserDailog = false;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    }
  }
}
</script>

<style scoped>

</style>

<!--用户管理模块-->
<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="searchParams" size="small">
        <el-form-item>
          <el-button type="primary" icon="el-icon-plus" @click="addUser">新增用户</el-button>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="searchParams.name" placeholder="姓名"></el-input>
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
              label="姓名"
              width="180">
          </el-table-column>
          <el-table-column
              prop="loginName"
              label="登录名"
              width="180">
          </el-table-column>
          <el-table-column
              prop="sex"
              label="性别"
              width="80">
            <template slot-scope="scope">
              {{ scope.row.sex === 0 ? '女' : '男' }}
            </template>
          </el-table-column>
          <el-table-column
              prop="phoneNum"
              label="手机"
              width="180">
          </el-table-column>
          <el-table-column
              prop="email"
              label="邮箱"
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
              <el-button @click="modifyUser(scope.row.id)" type="text" size="small">编辑</el-button>
              <el-button @click="deleteUser(scope.row.id)" type="text" size="small">删除</el-button>
              <el-button @click="resetPwd(scope.row.id)" type="text" size="small">重置密码</el-button>
              <el-button @click="assignRole({id:scope.row.id,name:scope.row.name})" type="text" size="small">分配角色
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
    <!--  添加或编辑用户-->
    <el-dialog
        v-if="dialogShow"
        v-dialogDrag
        :close-on-click-modal="false"
        :title="dialogTitle"
        :visible.sync="dialogShow"
        width="30%">
      <el-form ref="userForm" :model="userForm" :rules="userRules" label-width="80px" size="small">

        <el-form-item label="登录名" prop="loginName">
          <el-input v-model="userForm.loginName" :disabled="userForm.id?true:false"></el-input>
        </el-form-item>

        <el-form-item label="用户名" prop="name">
          <el-input v-model="userForm.name"></el-input>
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="userForm.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userForm.email"></el-input>
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="userForm.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="userForm.isEnabled" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>


      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submit()" size="small">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        v-if="roleDialogShow"
        title="分配角色"
        :visible.sync="roleDialogShow"
        width="30%">
      <el-form ref="userForm" :model="roleForm" label-width="100px" size="small">

        <el-form-item label="登录名:" prop="loginName">
          <span class="fb">{{ roleForm.name }}</span>
        </el-form-item>
        <el-form-item label="分配角色:">
          <el-checkbox-group v-model="roleForm.checkRoleIds">
            <!--            <el-checkbox :key="item.id" v-for="item in roleList" :label="item.name"-->
            <!--                          :checked="item.isAssign?true:false"></el-checkbox>-->
            <el-checkbox v-for="item in roleList" :label="item.id" :key="item.id">{{ item.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="roleDialogShow = false" size="small">取 消</el-button>
    <el-button type="primary" @click="submitRole()" size="small">确 定</el-button>
  </span>
    </el-dialog>


  </div>

</template>

<script>
import {showHeight} from "@/common/tableHeight";

let userInfo = JSON.parse(localStorage.getItem('userInfo'));
export default {
  name: "users",
  data() {
    return {

      tableHeight: null,
      searchParams: {
        name: ''
      },
      tableData: [],
      pageOptions: {
        pageNum: 1,
        pageSize: 20,
        total: 0,
      },

      dialogTitle: "添加用户",
      dialogShow: false,
      userForm: {
        name: '',
        loginName: '',
        sex: 1,
        isEnabled: 1,
        phoneNum: '',
        email: ''
      },
      userCopy: {},
      userRules: {
        name: {required: true, message: '请输入用户名', trigger: 'blur'},
        loginName: {required: true, message: '请输入登录名', trigger: 'blur'}
      },
      roleDialogShow: false,
      roleForm: {
        userId: '',
        name: '',
        checkRoleIds: ['4f5ecb1f17564cceaee532f0fa37fed6']
      },
      roleList: []
    }
  },
  created() {
    this.tableHeight = showHeight(280)
    window.onresize = () => {
      this.tableHeight = showHeight(280)
    }
    this.getUsers();
  },
  methods: {
    getUsers() {
      var _this = this;
      var params = {
        pageNum: _this.pageOptions.pageNum,
        pageSize: _this.pageOptions.pageSize,
      }
      if (_this.searchParams.name) {
        params.name = _this.searchParams.name;
      }

      this.$axios.get(this.$api.getUsers, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.tableData = res.datas;
          _this.pageOptions.total = res.totalCount;
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    deleteUser(id) {
      let _this = this;
      _this.$confirm("确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        _this.$axios.delete(_this.$api.deleteUser + '/' + id + '/' + userInfo.login_name).then(function (res) {
          if (res.errcode === 0) {
            _this.$message.success("删除成功");
            _this.getUser();
          } else {
            _this.$message.error(res.errmsg);
          }
        })
      }).catch(function () {
      });

    },
    resetPwd(id) {
      var params = {id: id, loginName: userInfo.login_name};
      let _this = this;
      this.$axios.get(this.$api.resetPwd, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("密码重置成功");
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    },
    search() {
      this.pageOptions.pageNum = 1;
      this.getUsers();
    },
    addUser() {
      this.dialogShow = true;
      this.dialogTitle = "添加用户";
      this.userForm = {
        name: '',
        loginName: '',
        sex: 1,
        isEnabled: 1,
        phoneNum: '',
        email: ''
      }
    },
    submit() {
      var _this = this;
      var params = _this.userForm;
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (_this.userForm.id) {

            var isModify = false;
            if (params.name != _this.userCopy.name) {
              isModify = true;
            }
            if (params.email != _this.userCopy.email) {
              isModify = true;
            }
            if (params.phoneNum != _this.userCopy.phoneNum) {
              isModify = true;
            }
            if (params.sex != _this.userCopy.sex) {
              isModify = true;
            }
            if (params.isEnabled != _this.userCopy.isEnabled) {
              isModify = true;
            }
            console.log(params)
            console.log(_this.userCopy)
            if (!isModify) {
              _this.dialogShow = false;
              return;
            }

            params.modifier = userInfo.login_name;
            _this.$axios.put(_this.$api.modifyUser, params).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("修改成功");
                _this.dialogShow = false;
                _this.getUsers();
              } else {
                _this.$message.error(res.errmsg);
              }
            });
          } else {
            params.creator = userInfo.login_name;
            _this.$axios.post(_this.$api.addUser, params).then(function (res) {
              if (res.errcode === 0) {
                _this.$message.success("添加成功");
                _this.dialogShow = false;
                _this.getUsers();
              } else {
                _this.$message.error(res.errmsg);
              }
            });
          }

        }
      });
    },
    modifyUser(id) {

      var _this = this;
      var params = {id: id};
      _this.$axios.get(_this.$api.getUser, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.userForm = res.data;
          _this.userCopy = JSON.parse(JSON.stringify(_this.userForm));
        }
      });
      this.dialogTitle = "编辑用户";
      this.dialogShow = true;
    },
    handleSizeChange(val) {
      this.pageOptions.pageNum = 1;
      this.pageOptions.pageSize = val;
      this.getUsers();
    },
    handleCurrentChange(val) {

      this.pageOptions.pageNum = val
      this.getUsers();

    },

    // eslint-disable-next-line no-unused-vars
    assignRole({id, name}) {
      this.roleList = [];
      this.roleForm.userId = id;
      this.roleForm.name = name;
      let _this = this;
      let params = {userId: id}
      _this.$axios.get(_this.$api.getUserRoles, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.roleList = res.datas;
          _this.roleForm.checkRoleIds = res.datas.filter(function (item) {
            return item.isAssign === 1;
          }).map(function (item) {
            return item.id;
          })
        } else {
          _this.$message.error(res.errmsg);
        }
      });
      this.roleDialogShow = true;
    },
    submitRole() {
      let _this = this;
      let params = _this.roleForm.checkRoleIds;
      _this.$axios.post(_this.$api.configUserRole + '/?userId=' + _this.roleForm.userId, params).then(function (res) {
        if (res.errcode === 0) {
          _this.$message.success("操作成功");
          _this.roleDialogShow = false;
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

<template>

  <div class="header">
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="collapseChage">
      <i class="el-icon-menu"></i>
<!--      <img  src="../../public/favicon.jpg" style="width: 50px;height: 50px" alt="logo">-->
    </div>
    <div class="logo">沧州成品油结算管理系统</div>
    <div class="header-right">
      <div class="header-user-con">
        <div class="btn-fullscreen" @click="handleFullScreen">
          <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
            <i class="el-icon-rank"></i>
          </el-tooltip>
        </div>

        <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link">
            <span style="font-size: 14px;color: #fff">
              {{ user_name }}
            </span>
                         <i class="el-icon-caret-bottom" style="color:#fff"></i>
                    </span>
          <el-dropdown-menu slot="dropdown">
            <!--            <div>-->
            <!--              <el-dropdown-item command="userInfo">个人信息</el-dropdown-item>-->
            <!--            </div>-->
            <div>
              <el-dropdown-item command="modifyPassword">修改密码</el-dropdown-item>
            </div>
            <el-dropdown-item divided>
              <span style="display:block;" @click="logout">退出</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </div>
    </div>

    <!--    修改密码-->
    <el-dialog title="修改密码" :visible.sync="showModal" width="30%" :close-on-click-modal="false"
               :close-on-press-escape="false">
      <el-form :model="modalForm" label-width="100px" :rules="modalRules" ref="modalForm">

        <el-form-item label="旧密码" prop="oldPassword">
          <el-input type="password" v-model="modalForm.oldPassword"></el-input>
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="modalForm.newPassword"></el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="modalForm.confirmPassword"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="closeDialog">取 消</el-button>
                <el-button type="primary" @click="submitModify()">确 定</el-button>
            </span>
    </el-dialog>
  </div>

</template>
<script>


export default {

  data() {
    var validatePass = (rule, value, callback) => {

      if (this.modalForm.confirm_password !== '') {
        this.$refs.modalForm.validateField('confirm_password');
      }
      callback();

    };
    var validatePass2 = (rule, value, callback) => {
      if (value !== '') {
        if (value !== this.modalForm.newPassword) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    return {
      user_name: '',
      user_id: '',
      collapse: false,
      fullscreen: false,

      showModal: false,
      modalForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      modalRules: {
        oldPassword: {required: true, message: '请输入旧密码', trigger: 'blur'},
        newPassword: [{required: true, message: '请输入新密码', trigger: 'blur'}, {validator: validatePass, trigger: 'blur'}],
        confirmPassword: [{required: true, message: '请确认密码', trigger: 'blur'}, {
          validator: validatePass2,
          trigger: 'blur'
        }],
      },
      loading: false,
    }
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem('userInfo'));
    this.user_name = userInfo.user_name;
    this.user_id = userInfo.user_id;
    // this.setTheme();
  },
  computed: {},
  watch: {
    // // 如果主题改变
    // theme() {
    //   this.setTheme();
    // }
  },
  methods: {
    // 侧边栏折叠
    collapseChage() {
      this.collapse = !this.collapse;
      this.$bus.$emit('collapse', this.collapse);
    },
    logout() {
      let _this = this;
      this.$axios.post(this.$api.logout + '?userId=' + this.user_id).then(function (res) {
        if (res.errcode === 0) {
          console.log("登出")
        }
      });

      setTimeout(function () {
        window.localStorage.removeItem("token");
        window.localStorage.removeItem("userInfo");
        _this.$router.push({name: 'login'});
      }, 1000)

    },
    // 全屏事件
    handleFullScreen() {
      let element = document.documentElement;
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen();
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen();
        }
      }
      this.fullscreen = !this.fullscreen;
    },

    // 用户名下拉菜单选择事件
    handleCommand(command) {
      if (command == 'modifyPassword') {
        this.modifyPassworld();
      }
    },
    modifyPassworld() {
      this.showModal = true;
    },
    closeDialog() {
      this.showModal = false;
      this.modalForm = {};
    },
    //提交更改
    submitModify() {
      let _this = this;
      this.$refs.modalForm.validate(valid => {
        if (valid) {
          let params = _this.modalForm;
          params.userId = _this.user_id;
          _this.$axios.put(this.$api.modifyPassworld, params).then(res => {
            if (res.errcode === 0) {
              _this.$message.success('密码修改成功,请重新登录');
              _this.closeDialog();//关闭弹出层
              _this.logout();//登出
              // setTimeout(function () {
              //   window.localStorage.clear();
              //   _this.$router.push({name: 'login'})
              // }, 1000);
            } else {
              _this.$message({
                showClose: true,
                message: res.errmsg,
                type: 'error'
              });
            }
          }).catch(error => {
            console.log(error)
          })
        }
      })
    },
  },
  mounted() {
    if (document.body.clientWidth < 1500) {
      this.collapseChage();
    }
  }
}
</script>
<style scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #fff;
}

.collapse-btn {
  float: left;
  width: 63px;
  text-align: center;
  height: 100%;
  cursor: pointer;
  line-height: 70px;
}

.header .logo {
  float: left;
  margin-left: 20px;
  line-height: 70px;
}

.header-right {
  float: right;
  padding-right: 50px;
}

.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
  font-size: 16px;
}

.user-name {
  margin: 0 20px 0 10px;
}

.user-avator {
  margin-left: 20px;
}

.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.user-logout {
  border: 1px solid #fff;
  border-radius: 18px;
  padding: 3px 18px;
  font-size: 14px;
}

.collapse-btn:hover {
  background: rgba(0, 0, 0, 0.15)
}

.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}
</style>

<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">后台管理系统</div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="ms-content">
        <el-form-item prop="loginName">
          <el-input v-model="ruleForm.loginName" placeholder="用户名" prefix-icon="el-icon-user">
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="密码" v-model="ruleForm.password" prefix-icon="el-icon-lock">
          </el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <el-input 
            placeholder="验证码" 
            style="width: 250px"
            prefix-icon="el-icon-picture-outline"
            v-model="ruleForm.captcha" 
            @keyup.enter.native="submitForm('ruleForm')">
          </el-input>
          <img id="captchaImage" src="/api/captcha" alt="验证码" title="点击刷新" @click="refreshCaptcha">
        </el-form-item>
        <el-form-item prop="rememberMe">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" :loading="loginLoading" @click="submitForm('ruleForm')">登录</el-button>
        </div>

      </el-form>
    </div>
    <div class="login-foot">
      <a href="https://beian.miit.gov.cn/" target="_blank" style="color: white">豫ICP备19022606号-1</a>
    </div>
  </div>
</template>

<script>

export default {
  name: "login",
  data: function () {
    return {
      ruleForm: {
        loginName: '',
        password: '',
        captcha: ''
      },
      rememberMe: true,
      rules: {
        loginName: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ],
        captcha: [
          {required: true, message: '请输入验证码', trigger: 'blur'}
        ]
      },
      loginLoading: false,
    }
  },
  created() {
    let login = window.localStorage.getItem('login')
    let rememberMe = window.localStorage.getItem('rememberMe')
    if (typeof (rememberMe) != "undefined") {
      if (rememberMe == "true") {
        this.rememberMe = true;
        if (typeof (login) != "undefined") {
          let rememberUser = JSON.parse(login);
          this.ruleForm.loginName = rememberUser.loginName
          this.ruleForm.password = rememberUser.password
        }
      } else {
        this.rememberMe = false;
      }
    } else {
      this.rememberMe = true;
    }
    // if (typeof (login) != "undefined"){
    //   this.ruleForm = JSON.parse(login);
    // }
    // 直接访问的登陆页要执行logout相关操作
    window.localStorage.removeItem('token')
  },
  methods: {
    refreshCaptcha() {
      var captchaImage = document.getElementById('captchaImage');
      captchaImage.src = '/api/captcha?t=' + new Date().getTime();
    },
    submitForm(formName) {
      let _this = this;
      _this.loginLoading = true;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post(this.$api.login, this.ruleForm).then(res => {
            if (res.errcode === 0) {
              if (res.data) {
                window.localStorage.setItem('token', res.data.token)
                var userInfo = {
                  user_id: res.data.userId,
                  user_name: res.data.userName,
                  login_name: res.data.loginName
                }
                window.localStorage.setItem("rememberMe", this.rememberMe)
                if (this.rememberMe) {
                  window.localStorage.setItem('login', JSON.stringify({loginName: this.ruleForm.loginName, password: this.ruleForm.password}));
                  window.localStorage.setItem("rememberMe", this.rememberMe)
                } else {
                  window.localStorage.removeItem('login')
                }
                window.localStorage.setItem('userInfo', JSON.stringify(userInfo));
                // let userPermissions = ['sys:modifyMenu']
                // window.sessionStorage.setItem('userPermissions', JSON.stringify(userPermissions));
                // 延迟一秒跳转，直接跳转有可能localstorage存储异常
                setTimeout(function () {
                  _this.$router.push('/index');
                }, 1000);
              }
            } else {
              _this.$message({
                showClose: true,
                type: 'error',
                message: res.errmsg
              });
              _this.loginLoading = false;
            }
          }).catch(function (error) {
            console.error(error)
            _this.loginLoading = false;
          });
        } else {
          return false;
        }
      });
    }
  }
}
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background: url(../assets/img/timg.jpg) no-repeat center center / cover;
}

.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #fff;
  border-bottom: 1px solid #ddd;
}

.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 400px;
  margin: -190px 0 0 -175px;
  border-radius: 5px;
  background: rgba(0, 0, 0, 0.3);
  overflow: hidden;
}

#captchaImage {
  float: right;
  border-radius: 3px;
}

.ms-content {
  padding: 30px 30px;
}

.login-btn {
  text-align: center;
}

.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}

.login-tips {
  font-size: 12px;
  line-height: 30px;
  color: #fff;
}
.login-foot{
  position: absolute;
  bottom: 0px;
  text-align: center;
  width: 100%;
  background: rgba(0, 0, 0,0.3);
  padding: 12px;
  font-size: 12px;
}
</style>


<template>
  <div class="wrapper" style="margin-top: 1px">
    <the-header></the-header>
    <the-sidebar></the-sidebar>
    <div class="content-box" :class="{'content-collapse':collapse}">
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item v-for="(item,index) of crumbList" :key="index" :to="{ path:item.path }">{{ item.title }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
<!--      <div class="content">-->
<!--        <div class="container">-->
<!--          <el-card class="box-card" :body-style="{padding:'20px',height:'100%'}">-->
<!--            <transition name="fade-transform" mode="out-in">-->
<!--              <keep-alive :include="keepAlive">-->
<!--                <router-view></router-view>-->
<!--              </keep-alive>-->
<!--            </transition>-->
<!--          </el-card>-->
<!--        </div>-->
<!--      </div>-->
      <div class="content">
        <div class="container">
          <transition name="fade-transform" mode="out-in">
            <keep-alive :include="keepAlive">
              <router-view></router-view>
            </keep-alive>
          </transition>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TheHeader from "@/components/TheHeader";
import TheSidebar from "@/components/TheSidebar";

export default {
  data() {
    return {
      collapse: false,
      crumbList: '',
      keepAlive: []
    }
  },
  components: {
    TheHeader, TheSidebar
  },
  created() {
    this.setCrumb(this.$route)
    this.$bus.$on('collapse', msg => {
      this.collapse = msg;
    })
  },
  watch: {
    // 监听路由的跳转
    '$route'(to, from) {
      this.setCrumb(to)
      console.log(from);
    }
  },
  computed: {},
  methods: {
    setCrumb(to) {
      let title = to.query.title || to.meta.title;
     // let title = to.params.title;
      console.log(to)
      // 如果是二级页面则需要面包屑叠加
      if (to.meta.type === 'sub' || to.params.crumb === 'sub') {
        // 存在一级面包屑
        if (this.crumbList.length > 0) {
          this.crumbList.push({title: title, path: to.path});
          // 将面包屑的值存起来
          window.sessionStorage.setItem('crumbList', JSON.stringify(this.crumbList))
        } else {
          // 不存在一级面包屑（即刷新页面进入的）则从缓存中拿
          this.crumbList = JSON.parse(window.sessionStorage.getItem('crumbList'))
        }
      } else {
        // 不需要叠加则直接覆盖之前的面包屑
        this.crumbList = [{title: title, path: to.path}]
      }
    }
  }
}
</script>

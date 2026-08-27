
<template>
  <div class="wrapper" style="margin-top: 1px">
    <the-header></the-header>
    <the-sidebar></the-sidebar>
    <div class="content-box" :class="{'content-collapse':collapse}">
      <!-- <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item v-for="(item,index) of crumbList" :key="index" :to="{ path:item.path }">{{ item.title }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div> -->
      <div class="crumbs">
        <!-- <el-tag
          v-for="tag in tags"
          :key="tag.name"
          closable
          hit
          style="margin-right:5px"
          :effect="active==tag.name?'dark':'plain'"
          @close="handleClose(tag)">
          {{tag.name}}
        </el-tag> -->
        <router-link
          tag="span"
          :class="tags.length == 0 || active == 'home'?'active':''"
          class="tags-view-item"
          :to="{ path: '/home', query: '', fullPath: '/home'}"
        >
          首页
        </router-link>
        <router-link
          v-for="(tag, index) in tags"
          tag="span"
          :class="active==tag.name?'active':''"
          class="tags-view-item"
          :key="tag.name"
          :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
        >
        {{ tag.title }}
        <span class="el-icon-close" @click.prevent.stop="handleClose(tag,index)" />
      </router-link>
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
            <keep-alive>
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
      keepAlive: [],
      active: '',
      tags: []
    }
  },
  components: {
    TheHeader, TheSidebar
  },
  created() {
    this.setCrumb(this.$route)
    this.setTag(this.$route)
    this.$bus.$on('collapse', msg => {
      this.collapse = msg;
    })
  },
  watch: {
    // 监听路由的跳转
    '$route'(to, from) {
      this.setCrumb(to)
      console.log(from);
      this.setTag(to)
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
    },
    // TODO 
    setTag(to) {
      this.active = to.name
      let _tags = window.sessionStorage.getItem('__tags__')
      if (to.name == "home") {
        return
      }
      if (_tags){
        this.tags = JSON.parse(window.sessionStorage.getItem('__tags__'))
      }

      // this.tags.push({ name: to.meta.title})
      let n = -1
      for (let i = 0; i < this.tags.length; i++) {
        if (this.tags[i].name === to.name) {
          n = i
          break
        }
      }
      if (n < 0){
        this.tags.push({name: to.name, title : to.meta.title, path:to.path, fullPath: to.fullPath})
        window.sessionStorage.setItem('__tags__', JSON.stringify(this.tags))
      }      
      
    },
    handleClose(tag, index) {

      if (index == 0){
        this.active == 'home'
        this.$router.push({path: '/home'})
      }
      else{
        if (this.active == tag.name) {
          this.active = this.tags[index -1].name
          this.$router.push({path: this.tags[index -1].name})
        }
      }
      this.tags.splice(index, 1);
      window.sessionStorage.setItem('__tags__', JSON.stringify(this.tags))
    },
  }
}
</script>
<style lang="scss" scoped>
.tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 26px;
      line-height: 26px;
      border: 1px solid #d8dce5;
      color: #495060;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 5px;
      margin-top: 4px;
      &:first-of-type {
        margin-left: 15px;
      }
      &:last-of-type {
        margin-right: 15px;
      }
      &.active {
        background-color: #039be5;
        color: #fff;
        border-color: #039be5;
        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 2px;
        }
      }
    }
</style>

<template>
  <div class="sidebar">
    <el-menu class="sidebar-el-menu"
             :default-active="activeIndex"
             :collapse="collapse"
             background-color="#324157"
             text-color="#F0F0F0"
             unique-opened
             @select="selectFn"
    >
      <template v-for="item in routeMap">
        <template v-if="item.children">
          <el-submenu :index="item.url" :key="item.url">
            <template slot="title">
              <!--              <i :class="['iconfont','mr5','el-icon-folder']"></i><span slot="title">{{ item.title }}</span>-->
              <i class="el-icon-folder"></i><span  slot="title">{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.children">
              <el-submenu v-if="subItem.children" :index="subItem.url" :key="subItem.url">
                <template slot="title">{{ subItem.title }}</template>
                <el-menu-item v-for="(threeItem,i) in subItem.children" :key="i" :index="threeItem.name">
                  {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.url" :key="subItem.url">
                <i class="el-icon-document"></i> <span  slot="title">{{ subItem.title }}</span>
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.url" :key="item.url">
            <i class="el-icon-folder"></i><span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
let userInfo = JSON.parse(localStorage.getItem('userInfo'));
export default {
  data() {
    return {
      collapse: false,
      routeMap: [],
      activeIndex: '',
    }
  },
  created() {
    this.activeIndex = this.$route.name
    // 通过 Event Bus 进行组件间通信，来折叠侧边栏
    this.$bus.$on('collapse', msg => {
      this.collapse = msg;
    })
    //let pageList = this.getAllRoutes(this.$router.options.routes);
    userInfo = JSON.parse(localStorage.getItem('userInfo'));
    this.getMenu();
    // let routeMap = {};
    // for (let item of pageList) {
    //   if (item.name) {
    //     routeMap[item.name] = {path: item.path, title: item.meta.title}
    //   }
    // }
    // this.routeMap = routeMap
  },
  computed: {},
  watch: {
    $route(newVal, oldVal) {
      if (newVal != oldVal) {
        let params = {
          operateModule: newVal.meta.title,
          operator: userInfo.user_name,
          operatorId: userInfo.id,
          operatorLoginName: userInfo.login_name,
          requestUrl: newVal.fullPath,
        };
        if (localStorage.getItem("token"))
          this.$axios.post(this.$api.addAccessLog, params).then(() => {
          });
      }
    },
  },
  methods: {
    getTitle(item) {
      if (item.subs && item.subs.length > 0) return item.title
      else {
        console.log(this.routeMap[item.name] && this.routeMap[item.name].title)
        return this.routeMap[item.name] && this.routeMap[item.name].title
      }

    },
    // 选中菜单的方法
    selectFn(index, indexPath) {
      console.log(indexPath)
      if (!indexPath) {
        return;
      }
      let title = "1234"
      this.$router.push({path: index, params: {title}})
      this.activeIndex = index;
      // 如果是点击的菜单，把keepAlive的缓存清掉，使得从菜单点进去的页面都是没有缓存的
      //this.$store.dispatch('clearKeepAlive')
    },
    // 递归查所有既有name又有path的路由
    getAllRoutes(list) {
      let allRoutes = []
      for (let item of list) {
        if (item.children) {
          allRoutes.push(...this.getAllRoutes(item.children))
        } else if (item.path && item.name) {
          allRoutes.push(item);
        }
      }
      return allRoutes;
    },

    getMenu() {
      let _this = this;
      var params = {id: userInfo.user_id}
      _this.$axios.get(_this.$api.getUserMenu, {params}).then(function (res) {
        if (res.errcode === 0) {
          _this.routeMap = res.datas;
          if (_this.routeMap.length == 0) {
            _this.$message.error("账号没有授权,请联系管理员...")
            setTimeout(function () {
              _this.$router.push({path: "/login"})
            }, 2000)

          }
        } else {
          _this.$message.error(res.errmsg);
        }
      });
    }

  }
}
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
  z-index: 1;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 220px;
}

.sidebar > ul {
  height: 100%;
}
</style>

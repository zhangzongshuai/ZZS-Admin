import Vue from 'vue'
import App from './App.vue'
import router from "@/router";
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/icon.css';
//import './assets/iconfont/iconfont.css'
//import 'element-ui/lib/theme-chalk/index.css'
import './assets/theme/index.css'
import bus from 'vue-bus'
import './axios/http'
import axios from 'axios';
import apiIndex from "@/api";
import {download} from "@/common/download"
import "./assets/js/directives.js"

import "nprogress/nprogress.css"
import * as echarts from "echarts";
import 'echarts-liquidfill';
Vue.config.productionTip = false


Vue.prototype.$axios = axios;
Vue.prototype.$api = apiIndex;
Vue.prototype.$download = download;
Vue.prototype.$echarts = echarts;
Vue.use(Element);
//
// Vue.use(Element,{
//   size:'small'
// });
Vue.use(bus)
new Vue({
  router,
  render: h => h(App),
}).$mount('#app')

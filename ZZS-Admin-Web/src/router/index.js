import Vue from 'vue'
import VueRouter from 'vue-router'
import NProgress from "nprogress"

const routerPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return routerPush.call(this, location).catch(error=> error)
}
Vue.use(VueRouter)

import Layout from '@/views/layout/layout'
const router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            name:'login',
            component: ()=>import('@/views/login'),
            meta: {requireAuth:false}
        },
        {
            path: '/index',
            name:'index',
            component: Layout,
            children:[
                {
                    path: '',
                    redirect:'/home'
                },
                {
                    path: '/home',
                    name:'home',
                    component: () => import('@/views/index/index'),
                    meta: {title: '首页'}
                },
                {
                    path: '/debtorList',
                    name: 'debtorList',
                    component: ()=>import('@/views/ymdzManager/debtorList'),
                    meta: {title: '借方清单'}
                },
                {
                    path: '/creditorList',
                    name: 'creditorList',
                    component: ()=>import('@/views/ymdzManager/creditorList'),
                    meta: {title: '贷方清单'}
                },
                {
                    path: '/autoCheck',
                    name: 'autoCheck',
                    component: ()=>import('@/views/ymdzManager/autoCheck'),
                    meta: {title: '自动对账'}
                },
                {
                    path: '/checkHistory',
                    name: 'checkHistory',
                    component: ()=>import('@/views/ymdzManager/checkHistory'),
                    meta: {title: '对账历史'}
                },
                {
                    path: '/priceManager',
                    name: 'priceManager',
                    component: ()=>import('@/views/settlementManager/priceManager'),
                    meta: {title: '价格管理'}
                },
                {
                    path: '/settlement',
                    name: 'settlement',
                    component: ()=>import('@/views/settlementManager/settlement'),
                    meta: {title: '自动结算'}
                },
                {
                    path: '/settlementList',
                    name: 'settlementList',
                    component: ()=>import('@/views/settlementManager/settlementList'),
                    meta: {title: '结算清单'}
                },
                {
                    path: '/configPlan',
                    name: 'configPlan',
                    component: ()=>import('@/views/settlementManager/configPlanManager'),
                    meta: {title: '配置计划'}
                },
                {
                    path: '/stationPlan',
                    name: 'stationPlan',
                    component: ()=>import('@/views/settlementManager/stationPlan'),
                    meta: {title: '运输计划'}
                },
                {
                    path: '/increment',
                    name: 'increment',
                    component: ()=>import('@/views/settlementManager/increment'),
                    meta: {title: '增量计划'}
                },
                {
                    path: '/gaugeTicket',
                    name: 'gaugeTicket',
                    component: ()=>import('@/views/settlementManager/gaugeTicket'),
                    meta: {title: '计量单管理'}
                },
                {
                    path: '/planProgress',
                    name: 'planProgress',
                    component: ()=>import('@/views/settlementManager/planProgress'),
                    meta: {title: '计划进度'}
                },
                {
                    path: '/users',
                    name: 'users',
                    component: ()=>import('@/views/systemManager/users'),
                    meta: {title: '用户管理'}
                },
                {
                    path: '/station',
                    name: 'station',
                    component: ()=>import('@/views/systemManager/station'),
                    meta: {title: '车站管理'}
                },
                {
                    path: '/transportPlan',
                    name: 'transportPlan',
                    component: ()=>import('@/views/ddyfManager/transportPlan'),
                    meta: {title: '运输计划'}
                },
                {
                    path: '/ygyf',
                    name: 'ygyf',
                    component: ()=>import('@/views/ddyfManager/ygyf'),
                    meta: {title: '代垫台账'}
                },
                {
                    path: '/ddjd',
                    name: 'ddjd',
                    component: ()=>import('@/views/ddyfManager/ddjd'),
                    meta: {title: '代垫校对'}
                },
                {
                    path: '/oilType',
                    name: 'oilType',
                    component: ()=>import('@/views/systemManager/oilType'),
                    meta: {title: '油品类别管理'}
                },
                {
                    path: '/oil',
                    name: 'oil',
                    component: ()=>import('@/views/systemManager/oil'),
                    meta: {title: '成品油管理'}
                },
                {
                    path: '/menus',
                    name: 'menus',
                    component: ()=>import('@/views/systemManager/menus'),
                    meta: {title: '菜单管理'}
                },
                {
                    path: '/roles',
                    name: 'roles',
                    component: ()=>import('@/views/systemManager/roles'),
                    meta: {title: '菜单管理'}
                },
                {
                    path: '/accessLog',
                    name: 'accessLog',
                    component: ()=>import('@/views/logManager/accessLog'),
                    meta: {title: '访问日志'}
                },
                {
                    path: '/loginLog',
                    name: 'loginLog',
                    component: ()=>import('@/views/logManager/loginLog'),
                    meta: {title: '登录日志'}
                },
                {
                    path: '/product',
                    name: 'product',
                    component: ()=>import('@/views/productManager/product'),
                    meta: {title: '自销品管理'}
                },
                {
                    path: '/productPrice',
                    name: 'productPrice',
                    component: ()=>import('@/views/productManager/productPrice'),
                    meta: {title: '自销品价格管理'}
                },
                {
                    path: '/cql',
                    name: 'cql',
                    component: ()=>import('@/views/settlementManager/cql'),
                    meta: {title: '月度超欠量'}
                },
                {
                    path: '/cjcl',
                    name: 'cjcl',
                    component: ()=>import('@/views/settlementManager/clcj'),
                    meta: {title: '月度超欠量'}
                },
                {
                    path: '/province',
                    name: 'province',
                    component: ()=>import('@/views/systemManager/province'),
                    meta: {title: '月度超欠量'}
                },
                {
                    path: '/clcj',
                    name: 'clcj',
                    component: ()=>import('@/views/settlementManager/clcj'),
                    meta: {title: '月度超欠量'}
                },
                {
                    path: '/initFreight',
                    name: 'initFreight',
                    component: ()=>import('@/views/ddyfManager/initFreight'),
                    meta: {title: ''}
                },
                {
                    path: '/test',
                    name: 'test',
                    component: ()=>import('@/views/settlementManager/test'),
                    meta: {title: ''}
                },
            ]
        },

        {
            path: '/403',
            component: ()=>import('@/views/403'),
            meta: {requireAuth:false}
        },
        {
            path: '/404',
            component: ()=>import('@/views/404'),
            meta: {requireAuth:false}
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
})


router.beforeEach((to, from, next) => {
    const role = localStorage.getItem("token");
    NProgress.start()
     if (!role && to.path !== "/login") {
        next("/login");
    } else if (to.meta.requireAuth) {
        role ? next() : next("/403");
    }else if(to.matched.length === 0){
        next("/404");
    }
    else {
        next();
    }
});
router.afterEach(() => {
    NProgress.done()
})

export default router;

import Vue from 'vue'
import VueRouter from 'vue-router'
import NProgress from "nprogress"

const routerPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return routerPush.call(this, location).catch(error => error)
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
            name: 'login',
            component: () => import('@/views/login'),
            meta: {requireAuth: false}
        },
        {
            path: '/index',
            name: 'index',
            component: Layout,
            children: [
                {
                    path: '',
                    redirect: '/home'
                },
                {
                    path: '/home',
                    name: 'home',
                    component: () => import('@/views/index/index'),
                    meta: {title: '首页'}
                },
                {
                    path: '/users',
                    name: 'users',
                    component: () => import('@/views/systemManager/users'),
                    meta: {title: '用户管理'}
                },
                {
                    path: '/menus',
                    name: 'menus',
                    component: () => import('@/views/systemManager/menus'),
                    meta: {title: '菜单管理'}
                },
                {
                    path: '/roles',
                    name: 'roles',
                    component: () => import('@/views/systemManager/roles'),
                    meta: {title: '角色管理'}
                },
                {
                    path: '/accessLog',
                    name: 'accessLog',
                    component: () => import('@/views/logManager/accessLog'),
                    meta: {title: '访问日志'}
                },
                {
                    path: '/loginLog',
                    name: 'loginLog',
                    component: () => import('@/views/logManager/loginLog'),
                    meta: {title: '登录日志'}
                }
            ]
        },
        {
            path: '/403',
            component: () => import('@/views/403'),
            meta: {requireAuth: false}
        },
        {
            path: '/404',
            component: () => import('@/views/404'),
            meta: {requireAuth: false}
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
    } else if (to.matched.length === 0) {
        next("/404");
    } else {
        next();
    }
});
router.afterEach(() => {
    NProgress.done()
})

export default router;

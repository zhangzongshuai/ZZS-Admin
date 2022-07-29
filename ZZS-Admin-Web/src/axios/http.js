import axios from 'axios'
import {Message} from 'element-ui';
import router from '../router'
import NProgress from "nprogress"


// 添加请求拦截器
axios.interceptors.request.use(config => {
    if (window.localStorage.token) {
        NProgress.start();
        if (config.url.indexOf('refreshToken') > -1) {
            console.log('请求刷新token')
            config.headers.Authorization = window.localStorage.refreshToken;
        } else {
            config.headers.Authorization = window.localStorage.token;
        }
        if (config.params) {
            config.params.t = new Date().getTime();
        } else {
            config.params = {
                t: new Date().getTime(),
            };
        }
    }
    return config;
}, error => {
    return Promise.reject(error)
})

const codeMessage = {
    200: '服务器成功返回请求的数据。',
    201: '新建或修改数据成功。',
    202: '一个请求已经进入后台排队（异步任务）。',
    204: '删除数据成功。',
    400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
    401: '用户没有权限（令牌、用户名、密码错误）。',
    403: '用户得到授权，但是访问是被禁止的。',
    404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
    406: '请求的格式不可得。',
    410: '请求的资源被永久删除，且不会再得到的。',
    422: '当创建一个对象时，发生一个验证错误。',
    500: '服务器发生错误，请检查服务器。',
    502: '网关错误。',
    503: '服务不可用，服务器暂时过载或维护。',
    504: '网关超时。'
}

// 添加响应拦截器
axios.interceptors.response.use(response => {
    NProgress.done();
    if (response.data.errcode === 409) {
        window.localStorage.setItem("token", response.data.data);
        // return Promise.resolve(response.config);
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                try {
                    resolve(axios(response.config));
                } catch (error) {
                    reject(error)
                }
            }, 1000)
        });
    } else {
        return Promise.resolve(response.data);
    }

}, error => {
    if (error.response) {
        NProgress.done();
        let code = error.response.status;
        switch (code) {
            case 401:
                Message.error({message: '登录信息失效，正在为您跳转登录页面！'});
                window.localStorage.removeItem("token");
                window.localStorage.removeItem("userInfo");
                setTimeout(function () {
                    router.push("/login");
                }, 3000)
                break;
            case 500:
                Message.error(error.response.statusText)
                break;
            default:
                Message.error(codeMessage[code])
        }
        return Promise.reject(error.response.data);// 返回接口返回的错误信息
    }

})


module.exports = {
    assetsDir: 'static',
    productionSourceMap: false,
    publicPath: '',
    devServer: {
        open: true,
        host: "localhost",
        //host: "192.168.100.109",
        port: 8899,
        proxy: {

            '/api':{
                target:'http://localhost:5000',
                // target:'http://www.zzszone.cn:5200',
                changeOrigin:true
            },
            '/hello':{
                target:'http://localhost:7000',
                changeOrigin:true,
                pathRewrite:{
                    '/hello':'hello'
                }
            },
            '/stream':{
                target:'http://haiyou.s7.tunnelfrp.com',
                changeOrigin:true
            },
            '/haiyou':{
                target:'http://haiyou.s7.tunnelfrp.com',
                changeOrigin:true,
                pathRewrite:{
                    '/haiyou':''
                }
            },
            '/chatglm':{
                target:'https://chatglm.cn',
                changeOrigin:true,
                pathRewrite:{
                    '/chatglm':'chatglm'
                }
            },

        }
    },

    lintOnSave: false,


    chainWebpack: config => {
        config.plugins.delete('prefetch')
    }
}

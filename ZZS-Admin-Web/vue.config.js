
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
                changeOrigin:true,
                pathRewrite:{
                    '/api':'api'
                }
            },
            '/hello':{
                target:'http://localhost:7000',
                changeOrigin:true,
                pathRewrite:{
                    '/hello':'hello'
                }
            }

        }
    },

    lintOnSave: false,


    chainWebpack: config => {
        config.plugins.delete('prefetch')
    }
}

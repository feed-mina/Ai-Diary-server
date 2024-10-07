const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    publicPath: process.env.NODE_ENV === 'production' ? '/demo' : '/',
    transpileDependencies: true,

    devServer: {
        port: 3000,
    }


})
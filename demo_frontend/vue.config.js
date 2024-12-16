const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    publicPath: process.env.NODE_ENV === 'production' ? '/demo' : '/',
    transpileDependencies: true,
    devServer: {
        port: 3000,
        proxy: {
            '/api': {
                target: 'http://localhost:8080', // 실제 API 서버의 주소
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '' // 요청 URL에서 /api 제거
                }
            }
        }
    }
})
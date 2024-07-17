const path = require('path')
const { merge } = require('webpack-merge')
const baseConfig = require('./webpack.base')
const ReactRefreshWebpackPlugin = require('@pmmmwh/react-refresh-webpack-plugin')
const FriendlyErrorsWebpackPlugin = require('friendly-errors-webpack-plugin')
const portfinder = require('portfinder')

const devWebpackConfig = merge(baseConfig, {
  mode: 'development', // 开发模式，打包更加快速，省了代码优化步骤
  devtool: 'eval-cheap-module-source-map', //源码调试模式
  devServer: {
    port: 4000,
    compress: false, // gzip压缩，开发环境不开启，提供热更新速度
    hot: true, // 开启儿更新，
    historyApiFallback: true, //解决history 路由404问题。
    static: {
      directory: path.join(__dirname, '../public'), // 托管静态资源public文件夹
    },
    open: true,
    proxy: {
      '/htcloud': {
        target: 'http://192.168.0.14:8040/',
        pathRewrite: { '^/htcloud': '/htcloud' },
        secure: false,
        changeOrigin: true,
      },
    },
  },
  plugins: [
    new ReactRefreshWebpackPlugin({
      overlay: false,
    }), // 热更新。不需要浏览器刷新
  ],
})

module.exports = new Promise((resolve, reject) => {
  portfinder.basePort = devWebpackConfig.devServer.port
  portfinder.getPort((err, port) => {
    if (err) {
      reject(err)
    } else {
      // publish the new Port, necessary for e2e tests
      process.env.PORT = port
      // add port to devServer config
      devWebpackConfig.devServer.port = port

      // Add FriendlyErrorsPlugin
      devWebpackConfig.plugins.push(
        new FriendlyErrorsWebpackPlugin({
          compilationSuccessInfo: {
            messages: [`新的端口: http://localhost:${port}`],
            notes: [],
          },
          clearConsole: true,
        }),
      )
      resolve(devWebpackConfig)
    }
  })
})

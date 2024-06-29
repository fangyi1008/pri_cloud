const prodConfig = require('./webpack.prod')
const { merge } = require('webpack-merge')
const SpeedMeasureWebpackPlugin = require('speed-measure-webpack-plugin')
const smp = new SpeedMeasureWebpackPlugin()
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer')

module.exports = merge(prodConfig, {
  plugins: [
    new BundleAnalyzerPlugin(), //配置分析打包结果插件
  ],
})

const { merge } = require('webpack-merge')
const path = require('path')
const globAll = require('glob-all')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const { PurgeCSSPlugin } = require('purgecss-webpack-plugin')
const CssMinimizerWebpackPlugin = require('css-minimizer-webpack-plugin')
const TerserPlugin = require('terser-webpack-plugin')
const CompressionPlugin = require('compression-webpack-plugin')
const baseConfig = require('./webpack.base')

module.exports = merge(baseConfig, {
  mode: 'production', // 生产模式，会开启tree-shaking和代码压缩，以及其他代码优化
  optimization: {
    minimize: true,
    minimizer: [
      new CssMinimizerWebpackPlugin(),
      new TerserPlugin({
        // 压缩js
        parallel: true, // 开启多线程压缩
        terserOptions: {
          compress: {
            pure_funcs: ['console.log'], // 删除console.log
          },
        },
      }),
    ],
    splitChunks: {
      cacheGroups: {
        proTable: {
          test: /[\\/]node_modules[\\/](@ant-design[\\/]pro-table)[\\/]/,
          name: 'vendor.proTable',
          chunks: 'all',
        },
        antd: {
          test: /[\\/]node_modules[\\/](antd)[\\/]/,
          name: 'vendor.antd',
          chunks: 'all',
        },
        moment: {
          test: /[\\/]node_modules[\\/](moment)[\\/]/,
          name: 'vendor.moment',
          chunks: 'all',
        },
        vendors: {
          // 提取node_modules代码
          test: /node_modules/, // 只匹配node_modules里面的模块
          name: 'vendors', // 提取文件命名为vendors,js后缀和chunkhash会自动加
          minChunks: 1, // 只要使用一次就提取出来
          chunks: 'initial', // 只提取初始化就能获取到的模块,不管异步的
          minSize: 0, // 提取代码体积大于0就提取出来
          priority: -10, // 提取优先级为1
        },
        commons: {
          // 提取页面公共代码
          name: 'commons', // 提取文件命名为commons
          minChunks: 2, // 只要使用两次就提取出来
          chunks: 'all', // 只提取初始化就能获取到的模块,不管异步的
          minSize: 0, // 提取代码体积大于0就提取出来
        },
      },
    },
  },
  plugins: [
    new CopyWebpackPlugin({
      patterns: [
        {
          from: path.resolve(__dirname, '../public'), //复制pblic文件夹
          to: path.resolve(__dirname, '../dist'), // 复制到dist文件
          filter: source => {
            return !source.includes('index.html') // 忽略index.html
          },
        },
      ],
    }),
    new MiniCssExtractPlugin({
      filename: 'static/css/[name].[contenthash:8].css',
    }),
    // 清理无用css
    // new PurgeCSSPlugin({
    //   // 检测src下所有tsx文件和public下index.html中使用的类名和id和标签名称
    //   // 只打包这些文件中用到的样式
    //   paths: globAll.sync([
    //     `${path.join(__dirname, '../src')}/**/*.tsx`,
    //     path.join(__dirname, '../public/index.html')
    //   ]),
    //   safelist: {
    //     standard: [/^ant-/], // 过滤以ant-开头的类名，哪怕没用到也不删除
    //   }
    // }),
    new CompressionPlugin({
      test: /.(js|css)$/, // 只生成css,js压缩文件
      filename: '[path][base].gz', // 文件命名
      algorithm: 'gzip', // 压缩格式,默认是gzip
      threshold: 10240, // 只有大小大于该值的资源会被处理。默认值是 10k
      minRatio: 0.8, // 压缩率,默认值是 0.8
    }),
  ],
})

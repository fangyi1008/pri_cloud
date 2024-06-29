const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const webpack = require('webpack')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const isDev = process.env.NODE_ENV == 'development'
console.log('isDev==>', isDev)

module.exports = {
  entry: path.join(__dirname, '../src/index.tsx'),
  output: {
    filename: 'static/js/[name].[chunkhash:8].js',
    path: path.join(__dirname, '../dist'),
    clean: true,
    publicPath: '/',
  },
  cache: {
    type: 'filesystem',
  },
  module: {
    rules: [
      {
        test: /.(ts|tsx)$/,
        use: ['thread-loader', 'babel-loader'],
      },
      {
        test: /.(css)$/,
        use: [isDev ? 'style-loader' : MiniCssExtractPlugin.loader, 'css-loader', 'postcss-loader'],
      },
      {
        test: /.less$/, //匹配所有的 less 文件
        // include: [path.resolve(__dirname, '../src')],
        use: [
          isDev ? 'style-loader' : MiniCssExtractPlugin.loader, // 开发环境使用style-looader,打包模式抽离css
          'css-loader',
          'postcss-loader',
          {
            loader: 'less-loader',
            options: {
              lessOptions: {
                javascriptEnabled: true,
              },
              additionalData: `@import "@/styles/var.less";`,
            },
          },
        ],
      },
      {
        test: /.(png|jpg|jpeg|gif|svg)$/,
        type: 'asset',
        parser: {
          dataUrlCondition: {
            maxSize: 10 * 1024, // 小于10k转base64
          },
        },
        generator: {
          filename: 'static/images/[name].[contenthash:8][ext]', // 文件输出目录和名字  hash  ext  query
        },
      },
      {
        test: /.(woff2?|eot|ttf|otf)$/, // 匹配字体图标文件
        type: 'asset', // type选择asset
        parser: {
          dataUrlCondition: {
            maxSize: 10 * 1024, // 小于10kb转base64位
          },
        },
        generator: {
          filename: 'static/fonts/[name].[contenthash:8][ext]', // 文件输出目录和命名
        },
      },
      {
        test: /.(mp4|webm|ogg|mp3|wav|flac|aac)$/, // 匹配媒体文件
        type: 'asset', // type选择asset
        parser: {
          dataUrlCondition: {
            maxSize: 10 * 1024, // 小于10kb转base64位
          },
        },
        generator: {
          filename: 'static/media/[name].[contenthash:8][ext]', // 文件输出目录和命名
        },
      },
    ],
  },
  resolve: {
    extensions: ['.js', '.tsx', '.ts'],
    modules: ['node_modules'], // 查找第三方模块只在本项目的 node_modules 中查找
    aliasFields: ['browser'],
    alias: {
      '@': path.join(__dirname, '../src'),
      '@src': path.join(__dirname, '../src'),
      '@assets': path.join(__dirname, '../src/assets'),
      '@img': path.join(__dirname, '../src/assets/imgs'),
      '@components': path.join(__dirname, '../src/components'),
      '@pages': path.join(__dirname, '../src/pages'),
      '@redux': path.join(__dirname, '../src/redux'),
      '@common': path.join(__dirname, '../src/common'),
      '@layouts': path.join(__dirname, '../src/layouts'),
    },
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, '../public/index.html'),
      inject: true,
    }),
    new webpack.DefinePlugin({
      'process.env.BASE_ENV': JSON.stringify(process.env.BASE_ENV),
      'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV),
    }),
  ],
}

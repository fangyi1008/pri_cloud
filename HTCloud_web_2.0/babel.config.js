const isDev = process.env.NODE_ENV === 'development'

module.exports = {
  presets: [
    ['@babel/preset-env'],
    ['@babel/preset-react', { runtime: 'automatic' }],
    '@babel/preset-typescript',
  ],
  plugins: [
    [
      '@babel/plugin-transform-runtime',
      {
        corejs: 3,
      },
    ],
    ['@babel/plugin-proposal-decorators', { legacy: true }],
    isDev && require('react-refresh/babel'),
  ].filter(Boolean),
}

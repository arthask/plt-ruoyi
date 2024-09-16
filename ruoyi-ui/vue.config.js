'use strict'
const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const CompressionPlugin = require('compression-webpack-plugin')
const {CKEditorTranslationsPlugin} = require('@ckeditor/ckeditor5-dev-translations');
const {styles} = require('@ckeditor/ckeditor5-dev-utils');
// 构建时间统计插件
const SpeedMeasurePlugin = require('speed-measure-webpack-plugin');

const NodePolyfillPlugin = require("node-polyfill-webpack-plugin")

const name = process.env.VUE_APP_TITLE || 'learning-tool' // 网页标题

const os = require('os');
// cpu核数
const threads = os.cpus().length;

const port = process.env.port || process.env.npm_config_port || 3030 // 端口

// vue.config.js 配置说明
//官方vue.config.js 参考文档 https://cli.vuejs.org/zh/config/#css-loaderoptions
// 这里只列一部分，具体配置参考文档
module.exports = {
  // The source of CKEditor&nbsp;5 is encapsulated in ES6 modules. By default, the code
  // from the node_modules directory is not transpiled, so you must explicitly tell
  // the CLI tools to transpile JavaScript files in all ckeditor5-* modules.
  transpileDependencies: [
    // /ckeditor5-[^/\\]+[/\\]src[/\\].+\.js$/,
  ],
  // 部署生产环境和开发环境下的URL。
  // 默认情况下，Vue CLI 会假设你的应用是被部署在一个域名的根路径上
  // 例如 https://www.ruoyi.vip/。如果应用被部署在一个子路径上，你就需要用这个选项指定这个子路径。例如，如果你的应用被部署在 https://www.ruoyi.vip/admin/，则设置 baseUrl 为 /admin/。
  publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
  // 在npm run build 或 yarn build 时 ，生成文件的目录名称（要和baseUrl的生产环境路径一致）（默认dist）
  outputDir: 'dist',
  // 用于放置生成的静态资源 (js、css、img、fonts) 的；（项目打包之后，静态资源会放在这个文件夹下）
  assetsDir: 'static',
  // 是否开启eslint保存检测，有效值：ture | false | 'error'
  lintOnSave: process.env.NODE_ENV === 'development',
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  // webpack-dev-server 相关配置
  devServer: {
    host: 'localhost',
    port: port,
    open: true,
    proxy: {
      // detail: https://cli.vuejs.org/config/#devserver-proxy
      [process.env.VUE_APP_BASE_API]: {
        target: `http://localhost:8080`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      }
    },
  },
  css: {
    loaderOptions: {
      sass: {
        sassOptions: {
          outputStyle: "expanded"
        },
      }
    }
  },
  configureWebpack: {
    name: name,
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src')
      }
    },
    plugins: [
      // http://doc.ruoyi.vip/ruoyi-vue/other/faq.html#使用gzip解压缩静态文件
      new CompressionPlugin(),
      // CKEditor&nbsp;5 needs its own plugin to be built using webpack.
      new CKEditorTranslationsPlugin({
        // See https://ckeditor.com/docs/ckeditor5/latest/features/ui-language.html
        language: 'en',

        // Append translations to the file matching the `app` name.
        translationsOutputFile: /app/
      }),
      new SpeedMeasurePlugin(),
      new NodePolyfillPlugin()
    ],
  },
  chainWebpack(config) {
    config.plugins.delete('preload') // TODO: need test
    config.plugins.delete('prefetch') // TODO: need test

    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/assets/icons'))
      .add(path.join(__dirname, 'node_modules', '@ckeditor'))
      .end()

    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/assets/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    config.module
      .rule('cke-svg')
      .test(/ckeditor5-[^/\\]+[/\\]theme[/\\]icons[/\\][^/\\]+\.svg$/)
      .use('raw-loader')
      .loader('raw-loader');

    config.module
      .rule('cke-css')
      .test(/ckeditor5-[^/\\]+[/\\].+\.css$/)
      .use('postcss-loader')
      .loader('postcss-loader')
      .tap(() => {
        return {
          postcssOptions: styles.getPostCssConfig({
            themeImporter: {
              themePath: require.resolve('@ckeditor/ckeditor5-theme-lark')
            },
            minify: true
          })
        };
      });

    config.when(process.env.NODE_ENV !== 'development', config => {

      config.optimization.splitChunks({
        chunks: 'all',
        cacheGroups: {
          libs: {
            name: 'chunk-libs',
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: 'initial' // only package third parties that are initially dependent
          },
          elementUI: {
            name: 'chunk-elementUI', // split elementUI into a single package
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/, // in order to adapt to cnpm
            priority: 20 // the weight needs to be larger than libs and app or it will be packaged into libs or app
          },
          commons: {
            name: 'chunk-commons',
            test: resolve('src/components'), // can customize your rules
            minChunks: 3, //  minimum common number
            priority: 5,
            reuseExistingChunk: true
          }
        }
      })

      config.optimization.runtimeChunk('single'),
        {
          from: path.resolve(__dirname, './public/robots.txt'), //防爬虫文件
          to: './' //到根目录下
        }
    })
  }
}

const { defineConfig } = require('@vue/cli-service');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const { GenerateSW } = require("workbox-webpack-plugin");

const isWcMode = process.env.VUE_CLI_SERVICE_MODE === 'wc';

module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: "../public",
  publicPath: "/assets/",
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:9000',
        changeOrigin: true,
      },
    },
  },
  pages: isWcMode
      ? {
        wc: {
          entry: 'src/main-wc.js',
          filename: 'wc.html',
        },
      }
      : {
        index: {
          entry: 'src/main.js',
          template: 'public/index.html',
          filename: 'index.html',
        },
      },
  configureWebpack: (config) => {
    if (isWcMode) {
      config.output.libraryTarget = 'umd';
      config.optimization.splitChunks = false;
      config.optimization.runtimeChunk = false;
    }
    config.plugins.push(new CleanWebpackPlugin());
    config.plugins.push(new GenerateSW({
      clientsClaim: true,
      skipWaiting: true,
      runtimeCaching: [
        {
          urlPattern: new RegExp('/api/state'),
          handler: 'NetworkFirst',
          options: {
            cacheName: 'gamestate-cache',
            expiration: {
              maxEntries: 10,
              maxAgeSeconds: 60 * 60 * 24,
            },
          },
        }
      ]
    }));
  },
  chainWebpack: (config) => {
    if (isWcMode) {
      config.plugins.delete('html');
      config.plugins.delete('preload');
      config.plugins.delete('prefetch');
      config.optimization.splitChunks(false);
      config.optimization.runtimeChunk(false);
    }
  },
});
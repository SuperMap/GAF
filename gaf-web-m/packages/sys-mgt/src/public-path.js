/**
 * src/public-path.js
 * 微应用框架下动态设置 webpack publicPath，防止资源加载出错
 */
/* eslint-disable */
if (window.__POWERED_BY_QIANKUN__) {
  // eslint-disable-next-line no-undef
  __webpack_public_path__ = window.__INJECTED_PUBLIC_PATH_BY_QIANKUN__
}

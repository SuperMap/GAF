;(function (root) {
  root.SMWEBGIS = root.SMWEBGIS || {}
  root.SMWEBGIS.theme = root.SMWEBGIS.theme || {}
  // root.SMWEBGIS.theme.default = {
  //   '--mask-color-one': ' rgba(0,0,0,.6)',
  //   '--mask-color-two': ' rgba(0,0,0,.3)',
  //   '--bg-color': 'rgba(25, 40, 58, 0.3)',
  //   '--hover-color': 'rgba(25, 40, 58, 0.6)',
  //   '--font-color': 'rgba(255,255,255,1)',
  //   '--active-bg-color': 'rgba(15,250,255,0.2)',
  //   '--active-color': 'rgba(15,250,255,1)'
  // }
  root.SMWEBGIS.theme.themePlans = [
    {
      name: '默认', // 主题名称
      colorShow: '--bg-color', // 主题代表的颜色条显示
      color: {
        '--mask-color-one': ' rgba(0,0,0,.6)', // 遮罩层第一种渐变色
        '--mask-color-two': ' rgba(0,0,0,.3)', // 遮罩层第2种渐变色
        '--bg-color': 'rgba(84, 92, 100, 0.6)', // 默认背景色（渐变色第一种颜色值） --现用于工具条、导航、弹出框、抽屉的基本背景色
        '--bg-color-two': 'rgba(84, 92, 100, 0.1)', // 渐变色第二种颜色值  --浅色背景 --待定
        '--hover-bg-color': 'rgba(84, 92, 100, 0.8)', // 鼠标移入时背景色，与--bg-color配合使用 --浮动时的背景色用于如抽屉颜色的加深和弹出选项颜色的加深
        '--hover-color': 'rgba(15,250,255, 0.9)', // 鼠标移入时前景色 --高亮色，用户浮动时字体颜色
        '--font-color': 'rgba(255,255,255,1)', // 默认字体色
        '--active-bg-color': 'rgba(15,250,255,0.3)', // 激活背景色 --浅色高亮的背景 用于logo，弹窗title和资源目录的底色
        '--active-color': 'rgba(15,250,255, 0.9)', // 激活色，选中色
      },
    },
    {
      name: '灰色',
      colorShow: '--bg-color',
      color: {
        '--mask-color-one': ' rgba(0,0,0,.6)', // 遮罩层第一种渐变色
        '--mask-color-two': ' rgba(0,0,0,.3)', // 遮罩层第2种渐变色
        '--bg-color': 'rgba(84, 92, 100, 0.8)', // 默认背景色（渐变色第一种颜色值）
        '--bg-color-two': 'rgba(84, 92, 100, 0.1)', // 渐变色第二种颜色值
        '--hover-bg-color': 'rgba(84, 92, 100,1)', // 鼠标移入时背景色，与--bg-color配合使用
        '--hover-color': 'rgba(255, 208, 75, 0.6)', // 鼠标移入时前景色
        '--font-color': 'rgba(255,255,255,1)', // 默认字体色
        '--active-bg-color': 'rgba(255, 208, 75,0.2)',
        '--active-color': 'rgba(255, 208, 75, 0.9)', // 激活色，选中色
      },
    },

    {
      name: '蓝色',
      colorShow: '--bg-color',
      color: {
        '--mask-color-one': ' rgba(0,0,0,.6)',
        '--mask-color-two': ' rgba(0,0,0,.3)',
        '--bg-color': 'rgba(52,152,219,0.8)',
        '--bg-color-two': 'rgba(52,152,219,0.1)',
        '--hover-bg-color': 'rgba(52,152,219,1)',
        '--hover-color': 'rgba(255, 208, 75, 0.6)',
        '--font-color': 'rgba(255,255,255,1)',
        '--active-bg-color': 'rgba(255,208,75,0.2)',
        '--active-color': 'rgba(255, 208, 75, 0.9)',
      },
    },
    {
      name: '白色',
      colorShow: '--bg-color',
      color: {
        '--mask-color-one': 'rgba(109,123,138,.6)',
        '--mask-color-two': 'rgba(109,123,138,.3)',
        '--bg-color': 'rgba(255, 255, 255, 0.8)',
        '--bg-color-two': 'rgba(52,152,219,0.1)',
        '--hover-bg-color': 'rgba(255, 255, 255, 1)',
        '--hover-color': 'rgba(255, 255, 255, 1)',
        '--font-color': 'rgba(0,0,0,1)',
        '--active-bg-color': 'rgba(255,208,75,0.2)',
        '--active-color': 'rgba(255,208,75,1)',
      },
    },
  ]
})(this)

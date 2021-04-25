<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>WebGIS</title>
    <link href="./iframe/antd.min.css" rel="stylesheet" />
    <link href="./webGL/Cesium/Widgets/widgets.css" rel="stylesheet" />
    <link href="./iframe/css/font_577982_beimm7aom3g.css" rel="stylesheet" />
    <link href="./iframe/css/geoFont/iconfont.css" rel="stylesheet" />
    <style>
      ${styleContent}
    </style>
    <link rel="stylesheet" href="./iframe/iview.css" />
    <script src="./iframe/vue.min.js"></script>
    <script src="./iframe/iview.js"></script>
    <script src="./webGL/Cesium/Cesium.js"></script>
    <script src="./iframe/antd.min.js"></script>
    <script src="./iframe/gafmapui.umd.min.js"></script>
    <script src="./iframe/vue-iclient3d-webgl.min.js"></script>
    <script src="./iframe/echarts.min.js"></script>
    <script src="./iframe/tooltip.js"></script>
    <script src="./iframe/axios.min.js"></script>
    <script src="./classic/SuperMap.Include.js"></script>
  </head>
  <body>
    <div id="app"></div>
    <script>
       new Vue({
          el: '#app',
          template: `${htmlContent}`,
          ${jsContent}
        })
    </script>
  </body>
</html>

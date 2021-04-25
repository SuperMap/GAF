<template>
  <div class="help-link">
    <div id="help" class="big-itle">产品手册</div>
    <div class="flex">
      <a
        target="_blank"
        class="divider"
        href="//doc.gaf.net.cn/content/GAF平台/快速入门/DevOps.html"
      >
        <span class="title">开发者手册</span>
        <a class="sub-title">开启GAF云平台大门</a>
      </a>
      <a
        target="_blank"
        class="divider"
        href="//doc.gaf.net.cn/content/GAF平台/产品简介/introduction.html"
      >
        <span class="title">快速开始</span>
        <a class="sub-title">GAF的基本操作方法</a>
      </a>
      <a target="_blank" class="divider" href="//doc.gaf.net.cn/apis">
        <span class="title">API&SDK</span>
        <a class="sub-title">OpenAPI&SDK下载</a>
      </a>
      <a
        target="_blank"
        class="divider"
        href="//doc.gaf.net.cn/content/GAF平台/常见问题/FAQ.html"
      >
        <span class="title">常见问题</span>
        <a class="sub-title">常见问题FAQ</a>
      </a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HelpLink',
}
</script>

<style scoped>
.help-link {
  background-color: #fafafa;
  border-top: 1px solid #efefef;
  margin-top: 45px;
  overflow: hidden;
}
.big-itle {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin: 30px 0 40px;
}
.flex {
  display: flex;
  justify-content: space-around;
  margin-bottom: 45px;
}
.divider {
  /*border-right: 1px solid #dddddd;*/
  /*padding-right: 80px;*/
  text-decoration: none;
  color: #000;
}
.title {
  margin-bottom: 10px;
  font-size: 15px;
  font-weight: bold;
  display: block;
}
.sub-title {
  font-size: 13px;
  margin-right: 200px;
  display: block;
}
</style>

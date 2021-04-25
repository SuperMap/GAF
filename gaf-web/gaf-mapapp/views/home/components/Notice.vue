<template>
  <ul class="gong-gao">
    <!-- <li>9月31日，GAF1.0版本beta版发布。</li> -->
    <!-- <li>6月15日，GAF1.0版本API接口文档。</li> -->
    <li>5月18日，GAF1.0大数据开发帮助文档。</li>
    <li>4月18日，GAF1.0开发规范文档发布。</li>
    <li>3月18日，GAF1.0版本第一次交流会通知。</li>
  </ul>
  <!--        <a-tabs v-model="activeName">-->
  <!--          <a-tab-pane label="公告" name="first"></a-tab-pane>-->
  <!--          <a-tab-pane label="升级" name="second">升级</a-tab-pane>-->
  <!--          <a-tab-pane label="安全" name="third">安全</a-tab-pane>-->
  <!--          <a-tab-pane label="备案" name="fourth">备案</a-tab-pane>-->
  <!--          <a-tab-pane label="其他" name="five">其他</a-tab-pane>-->
  <!--        </a-tabs>-->
</template>

<script>
export default {
  name: 'Notice',
}
</script>

<style scoped>
.gong-gao {
  list-style: none;
  /*animation: rotate 3s linear infinite;*/
  position: relative;
}
.gong-gao > li {
  margin-bottom: 10px;
}
</style>

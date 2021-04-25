<template>
  <a-steps :current="4" direction="vertical" progress-dot>
    <!-- <a-step title="2019年10月" icon="el-icon-info">
      <span slot="description">
        GAF 正式版发布
        <a href="/">查看详情</a>
      </span>
    </a-step>
    <a-step title="2019年8月" icon="el-icon-info">
      <span slot="description">
        GAF Alpha版发布
        <a href="/">查看详情</a>
      </span>
    </a-step> -->
    <a-step title="2019年2月">
      <span slot="description">
        GAF 商业计划决策
        <!-- <a href="/">查看详情</a> -->
      </span>
    </a-step>
    <a-step title="2019年1月">
      <span slot="description">
        GAF 立项决策
        <!-- <a href="/">查看详情</a> -->
      </span>
    </a-step>
  </a-steps>
</template>

<script>
export default {
  name: 'ProductLog',
}
</script>

<style scoped></style>

<template>
  <a
    :href="attr.link"
    :target="attr.embed ? '_self' : '_blank'"
    class="thumbnail"
  >
    <a-avatar :size="attr.size" :icon="attr.icon" class="img" shape="square" />
    <br />
    <span :style="{ width: attr.size + 'px' }" class="title">{{
      attr.title
    }}</span>
  </a>
</template>

<script>
export default {
  name: 'PlatQuickLink',
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
  computed: {
    attr() {
      return this.data.attr || {}
    },
  },
}
</script>

<style scoped lang="less">
.thumbnail {
  text-align: center;
  display: inline-block;
  .title {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: inline-block;
    margin-top: 10px;
    color: gray;
  }
  .img {
    background: transparent;
    color: gray;
    border: 1px solid #cccccc;
  }
}
</style>

<template>
  <a-row>
    <a-col :span="itemLayout.label" :style="itemLayout.textTitle">标题：</a-col>
    <a-col :span="itemLayout.content">
      <a-input v-model="item.title" placeholder="请输入标题" />
    </a-col>
  </a-row>
</template>

<script>
export default {
  name: 'PanelTitle',
  props: {
    item: {
      type: Object,
      required: false,
      default: () => null,
    },
    itemLayout: {
      type: Object,
      required: true,
    },
  },
}
</script>

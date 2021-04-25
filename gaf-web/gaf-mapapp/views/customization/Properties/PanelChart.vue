<template>
  <div>
    <!--    <a-row :style="itemLayout.row">-->
    <!--      <a-col :span="itemLayout.label" :style="itemLayout.textTitle">宽度：</a-col>-->
    <!--      <a-col :span="itemLayout.content">-->
    <!--        <a-input v-model="attr.width"/>-->
    <!--      </a-col>-->
    <!--    </a-row>-->
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >高度：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-input-number v-model="attr.height" :min="0" :step="1" :max="1000" />
        像素
      </a-col>
    </a-row>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >接口地址：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-input v-model="attr.url" />
      </a-col>
    </a-row>
  </div>
</template>

<script>
export default {
  name: 'PanelChart',
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
  computed: {
    attr() {
      return this.item.attr
    },
  },
}
</script>
<style scoped></style>

<template>
  <div>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >链接标题：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-input v-model="attr.title" />
      </a-col>
    </a-row>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >链接图标：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-button :icon="attr.icon" @click="visible = true" />
      </a-col>
    </a-row>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >图标大小：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-input-number v-model="attr.size" :min="28" :max="512" :step="28" />
      </a-col>
    </a-row>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >链接地址：</a-col
      >
      <a-col :span="itemLayout.content" :style="itemLayout.textArea">
        <a-textarea v-model="attr.link" placeholder="请输入链接地址" />
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >打开方式：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-radio-group v-model="attr.embed" style="height: 54px">
          <a-radio :value="true">门户打开</a-radio>
          <a-radio :value="false">跳转新页面</a-radio>
        </a-radio-group>
      </a-col>
    </a-row>
    <gaf-icon v-model="visible" @select="selectedHandle" />
  </div>
</template>

<script>
export default {
  name: 'PanelQuickLink',
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
  data() {
    return {
      visible: false,
    }
  },
  computed: {
    attr() {
      return this.item.attr
    },
  },
  methods: {
    selectedHandle(icon) {
      this.$set(this.item.attr, 'icon', icon)
      this.visible = false
    },
  },
}
</script>

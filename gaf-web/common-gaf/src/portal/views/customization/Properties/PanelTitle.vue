<template>
<div>
  <a-row>
    <a-col :span="itemLayout.label" :style="itemLayout.textTitle">标题：</a-col>
    <a-col :span="itemLayout.content">
      <a-input v-model="item.title" placeholder="请输入标题" />
    </a-col>
  </a-row>
  <a-row>
    <a-col span="是否展示更多" :style="itemLayout.textTitle">是否展示更多</a-col>
    <a-col>
       <a-switch v-model="item.isShowMore" @change="onChange" />
    </a-col>
  </a-row>
  <a-row v-if="item.isShowMore">
    <a-col span="跳转地址" :style="itemLayout.textTitle">跳转地址</a-col>
    <a-col>
       <a-input v-model="item.href" />
    </a-col>
  </a-row>
  <a-row v-if="item.name !== 'gridTable'">
    <a-col span="自定义宽度" :style="itemLayout.textTitle" >自定义宽度</a-col>
    <a-col>
       <a-input placeholder="例:500" v-model="item.width" />
    </a-col>
  </a-row>
  <a-row  v-if="item.name !== 'gridTable'">
    <a-col span="自定义高度" :style="itemLayout.textTitle">自定义高度</a-col>
    <a-col>
       <a-input  placeholder="例:800" v-model="item.height" />
    </a-col>
  </a-row>

</div>
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
  methods: {
    onChange(val) {
      this.item.isShowMore = val
    }
  }
}
</script>

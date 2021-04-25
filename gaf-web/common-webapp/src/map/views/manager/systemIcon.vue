<template>
  <div class="SystemIcon">
    <gaf-modal :visible="visible" title="设置图标" @cancel="cancel" @ok="ok">
      <a-tabs tab-position="left">
        <a-tab-pane key="1" tab="方向性图标">
          <a-button :size="size" icon="step-backward" @click="onSelectedIcon" />
          <a-button :size="size" icon="step-forward" @click="onSelectedIcon" />
          <a-button :size="size" icon="fast-backward" @click="onSelectedIcon" />
          <a-button :size="size" icon="fast-forward" @click="onSelectedIcon" />
          <a-button :size="size" icon="down" @click="onSelectedIcon" />
          <a-button :size="size" icon="up" @click="onSelectedIcon" />
          <a-button :size="size" icon="left" @click="onSelectedIcon" />
          <a-button :size="size" icon="right" @click="onSelectedIcon" />
          <a-button :size="size" icon="double-right" @click="onSelectedIcon" />
          <a-button :size="size" icon="double-left" @click="onSelectedIcon" />
          <a-button :size="size" icon="vertical-left" @click="onSelectedIcon" />
          <a-button
            :size="size"
            icon="vertical-right"
            @click="onSelectedIcon"
          />
          <a-button :size="size" icon="arrow-up" @click="onSelectedIcon" />
          <a-button :size="size" icon="arrow-down" @click="onSelectedIcon" />
          <a-button :size="size" icon="arrow-left" @click="onSelectedIcon" />
          <a-button :size="size" icon="arrow-right" @click="onSelectedIcon" />
          <a-button :size="size" icon="left-square" @click="onSelectedIcon" />
          <a-button :size="size" icon="right-square" @click="onSelectedIcon" />
          <a-button :size="size" icon="login" @click="onSelectedIcon" />
          <a-button :size="size" icon="logout" @click="onSelectedIcon" />
          <a-button :size="size" icon="loading" @click="onSelectedIcon" />
        </a-tab-pane>
        <a-tab-pane key="2" tab="指示性图标">
          <a-button :size="size" icon="question" @click="onSelectedIcon" />
          <a-button :size="size" icon="plus" @click="onSelectedIcon" />
          <a-button :size="size" icon="minus" @click="onSelectedIcon" />
          <a-button :size="size" icon="info" @click="onSelectedIcon" />
          <a-button :size="size" icon="close" @click="onSelectedIcon" />
          <a-button :size="size" icon="close-circle" @click="onSelectedIcon" />
          <a-button :size="size" icon="close-square" @click="onSelectedIcon" />
          <a-button :size="size" icon="check" @click="onSelectedIcon" />
          <a-button :size="size" icon="check-circle" @click="onSelectedIcon" />
          <a-button :size="size" icon="check-square" @click="onSelectedIcon" />
          <a-button :size="size" icon="warning" @click="onSelectedIcon" />
          <a-button :size="size" icon="stop" @click="onSelectedIcon" />
          <a-button :size="size" icon="loading" @click="onSelectedIcon" />
        </a-tab-pane>
        <a-tab-pane key="3" tab="编辑类图标">
          <a-button :size="size" icon="edit" @click="onSelectedIcon" />
          <a-button :size="size" icon="copy" @click="onSelectedIcon" />
          <a-button :size="size" icon="delete" @click="onSelectedIcon" />
          <a-button :size="size" icon="highlight" @click="onSelectedIcon" />
          <a-button :size="size" icon="bold" @click="onSelectedIcon" />
          <a-button :size="size" icon="italic" @click="onSelectedIcon" />
          <a-button :size="size" icon="redo" @click="onSelectedIcon" />
          <a-button :size="size" icon="undo" @click="onSelectedIcon" />
          <a-button :size="size" icon="zoom-in" @click="onSelectedIcon" />
          <a-button :size="size" icon="zoom-out" @click="onSelectedIcon" />
          <a-button :size="size" icon="drag" @click="onSelectedIcon" />
          <a-button :size="size" icon="ordered-list" @click="onSelectedIcon" />
          <a-button :size="size" icon="loading" @click="onSelectedIcon" />
        </a-tab-pane>
        <a-tab-pane key="4" tab="数据类图标">
          <a-button :size="size" icon="area-chart" @click="onSelectedIcon" />
          <a-button :size="size" icon="pie-chart" @click="onSelectedIcon" />
          <a-button :size="size" icon="bar-chart" @click="onSelectedIcon" />
          <a-button :size="size" icon="dot-chart" @click="onSelectedIcon" />
          <a-button :size="size" icon="line-chart" @click="onSelectedIcon" />
          <a-button :size="size" icon="radar-chart" @click="onSelectedIcon" />
          <a-button :size="size" icon="heat-map" @click="onSelectedIcon" />
          <a-button :size="size" icon="fall" @click="onSelectedIcon" />
          <a-button :size="size" icon="rise" @click="onSelectedIcon" />
          <a-button :size="size" icon="stock" @click="onSelectedIcon" />
          <a-button :size="size" icon="box-plot" @click="onSelectedIcon" />
          <a-button :size="size" icon="fund" @click="onSelectedIcon" />
          <a-button :size="size" icon="sliders" @click="onSelectedIcon" />
        </a-tab-pane>
        <a-tab-pane key="5" tab="通用图标">
          <a-button :size="size" icon="lock" @click="onSelectedIcon" />
          <a-button :size="size" icon="unlock" @click="onSelectedIcon" />
          <a-button :size="size" icon="book" @click="onSelectedIcon" />
          <a-button :size="size" icon="calendar" @click="onSelectedIcon" />
          <a-button :size="size" icon="cloud" @click="onSelectedIcon" />
          <a-button
            :size="size"
            icon="cloud-download"
            @click="onSelectedIcon"
          />
          <a-button :size="size" icon="desktop" @click="onSelectedIcon" />
          <a-button :size="size" icon="download" @click="onSelectedIcon" />
          <a-button :size="size" icon="smile" @click="onSelectedIcon" />
          <a-button :size="size" icon="appstore" @click="onSelectedIcon" />
          <a-button :size="size" icon="notification" @click="onSelectedIcon" />
          <a-button :size="size" icon="picture" @click="onSelectedIcon" />
          <a-button :size="size" icon="reload" @click="onSelectedIcon" />
          <a-button :size="size" icon="setting" @click="onSelectedIcon" />
          <a-button :size="size" icon="upload" @click="onSelectedIcon" />
          <a-button :size="size" icon="user" @click="onSelectedIcon" />
          <a-button :size="size" icon="home" @click="onSelectedIcon" />
          <a-button :size="size" icon="video-camera" @click="onSelectedIcon" />
          <a-button :size="size" icon="heart" @click="onSelectedIcon" />
          <a-button :size="size" icon="eye" @click="onSelectedIcon" />
          <a-button :size="size" icon="team" @click="onSelectedIcon" />
          <a-button :size="size" icon="phone" @click="onSelectedIcon" />
          <a-button :size="size" icon="printer" @click="onSelectedIcon" />
          <a-button :size="size" icon="man" @click="onSelectedIcon" />
          <a-button :size="size" icon="woman" @click="onSelectedIcon" />
          <a-button :size="size" icon="shop" @click="onSelectedIcon" />
          <a-button :size="size" icon="copyright" @click="onSelectedIcon" />
          <a-button :size="size" icon="cluster" @click="onSelectedIcon" />
          <a-button :size="size" icon="gateway" @click="onSelectedIcon" />
          <a-button :size="size" icon="cloud-server" @click="onSelectedIcon" />
          <a-button :size="size" icon="cloud-upload" @click="onSelectedIcon" />
          <a-button
            :size="size"
            icon="cloud-download"
            @click="onSelectedIcon"
          />
          <a-button :size="size" icon="share-alt" @click="onSelectedIcon" />
          <a-button :size="size" icon="apartment" @click="onSelectedIcon" />
          <a-button :size="size" icon="tags" @click="onSelectedIcon" />
          <a-button
            :size="size"
            icon="safety-certificate"
            @click="onSelectedIcon"
          />
          <a-button :size="size" icon="rocket" @click="onSelectedIcon" />
          <a-button :size="size" icon="safety" @click="onSelectedIcon" />
          <a-button :size="size" icon="read" @click="onSelectedIcon" />
          <a-button :size="size" icon="project" @click="onSelectedIcon" />
          <a-button :size="size" icon="block" @click="onSelectedIcon" />
          <a-button :size="size" icon="robot" @click="onSelectedIcon" />
          <a-button :size="size" icon="crown" @click="onSelectedIcon" />
          <a-button :size="size" icon="fire" @click="onSelectedIcon" />
          <a-button :size="size" icon="loading" @click="onSelectedIcon" />
        </a-tab-pane>
        <a-tab-pane key="6" tab="品牌和标识">
          <a-button :size="size" icon="android" @click="onSelectedIcon" />
          <a-button :size="size" icon="apple" @click="onSelectedIcon" />
          <a-button :size="size" icon="windows" @click="onSelectedIcon" />
          <a-button :size="size" icon="chrome" @click="onSelectedIcon" />
          <a-button :size="size" icon="weibo" @click="onSelectedIcon" />
          <a-button :size="size" icon="twitter" @click="onSelectedIcon" />
          <a-button :size="size" icon="wechat" @click="onSelectedIcon" />
          <a-button :size="size" icon="qq" @click="onSelectedIcon" />
          <a-button :size="size" icon="google" @click="onSelectedIcon" />
          <a-button :size="size" icon="alibaba" @click="onSelectedIcon" />
          <a-button :size="size" icon="ant-design" @click="onSelectedIcon" />
          <a-button :size="size" icon="taobao" @click="onSelectedIcon" />
          <a-button :size="size" icon="loading" @click="onSelectedIcon" />
        </a-tab-pane>
      </a-tabs>
    </gaf-modal>
  </div>
</template>

<script>
export default {
  name: 'SystemIcon',
  model: {
    prop: 'visible',
    event: 'close',
  },
  props: {
    visible: {
      type: Boolean,
    },
  },
  data() {
    return {
      size: 'large',
    }
  },
  beforeCreate() {},
  created() {},
  methods: {
    ok() {
      this.$emit('closeIconSetting')
    },
    cancel() {
      this.$emit('selectedIcon', '')
      this.$emit('closeIconSetting')
    },
    onSelectedIcon(e) {
      const pro = e.target.firstChild.classList[1]
      const icon = pro.slice(8, pro.length)
      this.$emit('selectedIcon', icon)
    },
  },
}
</script>

<style scoped></style>

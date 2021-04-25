<template>
  <div class="LegendContainer">
    <gaf-map-draggable
      v-if="draggableVisible"
      :visible="draggableVisible"
      :width="188"
      title="图例"
      @cancel="handleCloseButton"
    >
      <dl v-for="(item, index) in dataList" :key="index" class="legend-item">
        <dt>
          <span :style="{ background: item.rgb }"></span>
        </dt>
        <dd>{{ item.name }}</dd>
      </dl>
    </gaf-map-draggable>
    <div v-else class="legend-btn" @click="openLegendPanel">
      <i class="iconfont icon-tuli"></i>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Legend',
  props: {
    dataList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      draggableVisible: true,
    }
  },
  methods: {
    openLegendPanel() {
      this.draggableVisible = true
    },
    handleCloseButton() {
      this.$emit('draggableClose')
      this.draggableVisible = false
    },
  },
}
</script>

<style lang="less" scoped>
.LegendContainer {
  width: 100%;
  div::after {
    content: '';
    display: block;
    clear: both;
  }
  /deep/#draggable {
    background: rgba(38, 38, 38, 0.3);
    border: 1px solid rgba(255, 255, 255, 0.2);
    top: unset;
    bottom: 20px;
    left: 80px;
    .content:after {
      content: '';
      display: block;
      clear: both;
    }
  }
  .left-drawer-plan-visible {
    left: 480px !important;
  }
  .legend-btn {
    position: absolute;
    left: 110px;
    bottom: 20px;
    width: 42px;
    height: 42px;
    background: rgba(38, 38, 38, 0.3);
    border: 1px solid rgba(255, 255, 255, 0.2);
    border-radius: 6px;
    text-align: center;
    line-height: 40px;
    color: #fff;
    z-index: 9999;
    i.iconfont {
      font-size: 28px;
    }
  }
  .legend-btn .legend-btn:hover {
    border: 1px solid rgba(0, 250, 255, 0.3);
    color: #0ffaff;
    cursor: pointer;
  }
  dl.legend-item {
    float: left;
    width: 100%;
    height: 24px;
    font-size: 13px;
    line-height: 24px;
    color: #fff;
    padding: 0 15px;
    dt {
      float: left;
      width: 30px;
      height: 100%;
      span {
        display: block;
        width: 100%;
        height: 14px;
        margin: 5px 0;
        background: red;
        border-radius: 4px;
      }
    }
    dd {
      float: left;
      padding-left: 10px;
    }
  }
}
</style>

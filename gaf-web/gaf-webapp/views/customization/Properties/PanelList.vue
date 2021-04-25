<template>
  <div>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.rdoTitle"
        >数据控制：</a-col
      >
      <a-col :span="itemLayout.content" :style="itemLayout.rdo">
        <a-radio-group v-model="attr.mode">
          <a-radio value="define">自定义</a-radio>
          <a-radio value="rest">读取接口</a-radio>
        </a-radio-group>
      </a-col>
    </a-row>
    <a-row v-if="!isDefine">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >接口地址：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-input v-model="attr.url" />
      </a-col>
    </a-row>
    <a-row v-if="!isDefine" :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle" />
      <a-col :span="itemLayout.content">
        <a-button @click="getData">
          <a-icon type="download" /> 获取数据
        </a-button>
      </a-col>
    </a-row>
    <a-row v-if="isDefine" :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >列配置：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-button @click="addColumn">
          <a-icon type="plus" /> 添加数据
        </a-button>
      </a-col>
    </a-row>
    <div v-if="isDefine">
      <a-row v-for="(d, idx) in attr.data" :key="idx" :style="itemLayout.row">
        <a-col :span="24">
          <a-input
            :value="d"
            style="width: 90%"
            @change="(e) => updateData(e, idx)"
          />
          <a-icon class="button" type="minus-circle" @click="remove(idx)" />
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PanelList',
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
      current: {},
      title: '',
      index: -1,
    }
  },
  computed: {
    attr() {
      return this.item.attr
    },
    isDefine() {
      return this.attr.mode === 'define'
    },
  },
  watch: {
    'attr.mode'(val) {
      if (val === 'define') {
        this.$set(this.item.attr, 'url', '')
      }
    },
  },
  methods: {
    async getData() {
      const url = this.attr.url
      const data = await this.$axios.$get(url)
      if (data && data.length > 0) {
        this.$set(this.item.attr, 'data', data)
      }
    },
    remove(index) {
      this.item.attr.data.splice(index, 1)
    },
    addColumn() {
      this.item.attr.data.push('')
    },
    updateData(e, index) {
      this.$set(this.item.attr.data, index, e.target.value)
    },
  },
}
</script>
<style scoped>
.button {
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s;
}
.button:hover {
  color: #777;
}
.button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>

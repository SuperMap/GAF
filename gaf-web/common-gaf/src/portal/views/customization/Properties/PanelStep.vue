<template>
  <div>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.rdoTitle"
        >方向控制：</a-col
      >
      <a-col :span="itemLayout.content" :style="itemLayout.rdo">
        <a-radio-group v-model="attr.direction">
          <a-radio value="horizontal">水平</a-radio>
          <a-radio value="vertical">垂直</a-radio>
        </a-radio-group>
      </a-col>
    </a-row>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.rdoTitle"
        >节点控制：</a-col
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
    <a-row v-if="!isDefine">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle" />
      <a-col :span="itemLayout.content">
        <a-button @click="getData">
          <a-icon type="download" /> 获取数据
        </a-button>
      </a-col>
    </a-row>
    <a-row v-if="isDefine">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >节点信息：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-button @click="add"> <a-icon type="plus" /> 添加节点 </a-button>
      </a-col>
    </a-row>
    <a-list
      v-if="isDefine"
      :data-source="attr.nodes"
      item-layout="horizontal"
      size="small"
    >
      <a-list-item slot="renderItem" slot-scope="record, idx">
        <a v-if="isDefine" slot="actions" @click="edit(record, idx)">
          <a-icon type="edit" />
        </a>
        <a v-if="isDefine" slot="actions" @click="remove(idx)">
          <a-icon type="minus-circle" />
        </a>
        <a-list-item-meta>
          <a slot="title">{{ record.title }}</a>
        </a-list-item-meta>
      </a-list-item>
    </a-list>
    <gaf-modal
      v-model="visible"
      :title="title"
      :width="400"
      @ok="save('myModal')"
    >
      <node-form ref="myModal" :item="current" />
    </gaf-modal>
  </div>
</template>

<script>
import NodeForm from './PropertyForm/NodeForm'
export default {
  name: 'PanelStep',
  components: {
    NodeForm,
  },
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
      const response = await this.$axios.$get(url)
      if (response.content && response.content.length > 0) {
        this.$set(this.item.attr, 'nodes', response.content)
      }
    },
    remove(index) {
      this.item.attr.nodes.splice(index, 1)
    },
    add() {
      this.title = '添加节点'
      this.visible = true
      this.current = { icon: 'setting', title: '节点标题', desc: '节点描述' }
      this.index = -1
    },
    edit(item, index) {
      this.title = '编辑节点'
      this.visible = true
      this.current = { ...item }
      this.index = index
    },
    save(frmRef) {
      const form = this.$refs[frmRef].form
      form.validateFields((err, nodeInfo) => {
        if (err) {
          return
        }
        if (this.index === -1) {
          this.item.attr.nodes.push(nodeInfo)
        } else {
          this.item.attr.nodes.splice(this.index, 1, nodeInfo)
        }
        this.visible = false
      })
    },
  },
}
</script>
<style scoped>
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  font-size: 24px;
  color: #999;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>

<template>
  <div>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.chkTitle"
        >显示表头：</a-col
      >
      <a-col :span="itemLayout.content" :style="itemLayout.chk">
        <a-switch v-model="attr.showHeader" />
      </a-col>
    </a-row>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.chkTitle"
        >显示边框：</a-col
      >
      <a-col :span="itemLayout.content" :style="itemLayout.chk">
        <a-switch v-model="attr.showBorder" />
      </a-col>
    </a-row>
    <a-row :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.chkTitle"
        >显示分页：</a-col
      >
      <a-col :span="itemLayout.content" :style="itemLayout.chk">
        <a-switch v-model="attr.showPage" />
      </a-col>
    </a-row>
    <a-row v-if="attr.showPage" :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >起始页：</a-col
      >
      <a-col :span="itemLayout.content" :style="itemLayout.chk">
        <a-input-number
          v-model="attr.pageIndex"
          :min="1"
          :max="1000"
          :step="1"
        />
      </a-col>
    </a-row>
    <a-row v-if="attr.showPage" :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >每页大小：</a-col
      >
      <a-col :span="itemLayout.content" :style="itemLayout.chk">
        <a-input-number
          v-model="attr.pageSize"
          :min="1"
          :max="1000"
          :step="1"
        />
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >接口地址：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-input v-model="attr.url" />
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle" />
      <a-col :span="itemLayout.content">
        <a-button @click="getData">
          <a-icon type="plus" /> 配置表格列
        </a-button>
      </a-col>
    </a-row>
    <draggable
      v-bind="dragOptions"
      :list="attr.columns"
      @start="drag = true"
      @end="drag = false"
    >
      <transition-group
        :name="!drag ? 'flip-list' : null"
        tag="div"
        class="control-parent-list"
        type="transition"
      >
        <div
          v-for="(col, index) in attr.columns"
          :key="col.key"
          class="column-item"
        >
          <div class="chk">
            <a-checkbox
              :checked="col.visible"
              @change="(e) => handleChange(e, index)"
            />
          </div>
          <div class="name">{{ col.dataIndex }}</div>
          <div class="alias"><a-input v-model="col.title" /></div>
          <div class="drag"><a-icon type="setting" /></div>
        </div>
      </transition-group>
    </draggable>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
export default {
  name: 'PanelTable',
  components: {
    Draggable,
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
      drag: false,
      dragOptions: {
        animation: 150,
        group: { name: 'columns' },
        sort: true,
        handle: '.drag',
      },
    }
  },
  computed: {
    attr() {
      return this.item.attr
    },
  },
  methods: {
    async getData() {
      const { url, showPage } = this.attr
      if (!url) {
        this.$message.warn('请输入接口地址')
        return
      }
      let tempUrl = url
      if (tempUrl.endsWith('?')) {
        tempUrl = tempUrl.substr(0, tempUrl.length - 1)
      }
      if (showPage) {
        tempUrl += '?pageSize=1&pageIndex=1'
      }
      const response = await this.$axios.$get(tempUrl)
      if (response.content && response.content.length > 0) {
        const keys = Object.keys(response.content[0])
        const columns = keys.map((key) => ({
          key,
          title: key,
          dataIndex: key,
          visible: true,
        }))
        this.$set(this.item.attr, 'columns', columns)
      }
    },
    handleChange(e, index) {
      const column = {
        ...this.item.attr.columns[index],
        visible: e.target.checked,
      }
      this.$set(this.item.attr.columns, index, column)
    },
  },
}
</script>
<style scoped lang="less">
.column-item {
  display: flex;
  .chk {
    width: 40px;
  }
  .alias,
  .name {
    width: 80px;
  }
  .drag {
    cursor: move;
    line-height: 32px;
    margin-left: 15px;
  }
}
</style>

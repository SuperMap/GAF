<template>
  <a-row>
    <a-col :span="24">
      <sortable-control-item-header
        @change="toggleCollapsed"
        @delete="$emit('delete')"
      />
    </a-col>
    <a-col v-show="!collapsed" :span="24" class="grid">
      <a-row>
        <a-col
          v-for="(col, cIdx) in data.columns"
          :key="cIdx"
          :span="col.span"
          :class="getItemClass(col)"
          @click.native.stop="onInnerItemClickHandle(col, cIdx)"
        >
          <sortable-control-item-inner :item="col" />
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>

<script>
import SortableControlItemInner from './SortableControlListInner'
import SortableControlItemHeader from './SortableControlItemHeader'
export default {
  name: 'SortableGridControlItem',
  inject: ['updateProperties'],
  components: {
    SortableControlItemInner,
    SortableControlItemHeader,
  },
  props: {
    data: {
      type: Object,
      required: true,
    },
    selectInnerItem: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      collapsed: false,
    }
  },
  methods: {
    toggleCollapsed(collapsed) {
      this.collapsed = collapsed
    },
    getItemClass(item) {
      let active = false
      if (this.selectInnerItem && item) {
        active = item.id === this.selectInnerItem.id
      }
      return {
        'control-item': true,
        active,
      }
    },
    onInnerItemClickHandle(col, index) {
      const selectItem = this.data.columns[index]
      this.$emit('selectitem', selectItem)
      this.updateProperties(selectItem.list[0])
    },
  },
}
</script>

<style lang="less" scoped>
.control-item {
  border: solid 1px #efefef;
  margin-bottom: 6px;
  padding: 6px;
}
.active {
  border: dashed 2px #3498db;
}
</style>

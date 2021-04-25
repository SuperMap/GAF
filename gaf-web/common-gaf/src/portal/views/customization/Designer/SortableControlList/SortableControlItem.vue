<template>
  <div :style="setWidthHeight">
    <sortable-control-item-header
      :title="data.title"
      :isShowMore="data.isShowMore"
      :href="data.href"
      @change="toggleCollapsed"
      @delete="$emit('delete')"
    />
    <component :is="data.name" v-show="!collapsed" :data="data" />
  </div>
</template>

<script>
import SortableControlItemHeader from './SortableControlItemHeader'
export default {
  name: 'SortableControlItem',
  components: {
    SortableControlItemHeader,
  },
  watch: {
  'data': function(val) {
    this.$emit('handleSelectItems', val, true)
  },
  },
  props: {
    data: {
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
    setWidthHeight() {
      const item = this.data
      let style = null
      let width = null
      let height = null
      if(item && item.width && item.width !== '') {
         width = item.width
         style += `;width:${width}px`
      }
      if(item && item.height && item.height !== '') {
         height = item.height
         style += `;height:${height}px;overflow: auto;`
      }
      this.$emit('style',style)
      return style
    },
    toggleCollapsed(collapsed) {
      this.collapsed = collapsed
    },
  },
}
</script>

<style scoped></style>

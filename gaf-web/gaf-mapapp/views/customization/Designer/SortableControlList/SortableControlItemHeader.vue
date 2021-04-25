<template>
  <div class="header">
    <span class="title">{{ title }}</span>
    <sortable-control-item-action
      @change="(collapsed) => $emit('change', collapsed)"
      @delete="$emit('delete')"
    />
  </div>
</template>

<script>
import SortableControlItemAction from './SortableControlItemAction'
export default {
  name: 'SortableControlItemHeader',
  components: {
    SortableControlItemAction,
  },
  props: {
    title: {
      type: String,
      default: '',
      required: false,
    },
  },
}
</script>

<style scoped>
.header {
  position: relative;
  background-color: #efefef;
  padding: 0 15px;
  line-height: 36px;
  height: 36px;
  margin-bottom: 2px;
  overflow: hidden;
  cursor: move;
}
</style>

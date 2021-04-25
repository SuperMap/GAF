<template>
  <div>
    <sortable-control-item-header
      :title="data.title"
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
    toggleCollapsed(collapsed) {
      this.collapsed = collapsed
    },
  },
}
</script>

<style scoped></style>

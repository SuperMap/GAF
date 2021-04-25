<template>
  <draggable
    v-model="data.list"
    v-bind="dragOptions"
    sort="true"
    @start="drag = true"
    @end="drag = false"
    @add="onAddHandler"
  >
    <transition-group
      :name="!drag ? 'flip-list' : null"
      tag="div"
      class="control-parent-list"
      type="transition"
    >
      <template v-for="(item, idx) in data.list">
        <div
          :key="item.id"
          :class="getItemClass(item)"
          @click.stop="onItemClickHandle(idx)"
        >
          <template v-if="item.name === 'grid'">
            <sortable-grid-control-item
              :data="item"
              :select-inner-item="selectInnerItem"
              @delete="deleteItem(idx)"
              @selectitem="onGridItemSelect"
            />
          </template>
          <template v-else>
            <sortable-control-item :data="item" @delete="deleteItem(idx)" />
          </template>
        </div>
      </template>
    </transition-group>
  </draggable>
</template>

<script>
import draggable from 'vuedraggable'
import SortableControlItem from './SortableControlItem'
import SortableGridControlItem from './SortableGridControlItem'
export default {
  name: 'SortableControlList',
  inject: ['updateProperties'],
  components: {
    draggable,
    SortableControlItem,
    SortableGridControlItem,
  },
  props: {
    data: {
      type: Object,
      default: () => ({ list: [] }),
      required: false,
    },
  },
  data() {
    return {
      drag: false,
      dragOptions: {
        animation: 150,
        group: { name: 'controls', pull: true },
        disabled: false,
        ghostClass: 'ghost',
        sort: true,
      },
      selectItem: {},
      selectInnerItem: {},
      collapsed: false,
    }
  },
  methods: {
    getItemClass(item) {
      let active = false
      if (this.selectItem && item) {
        active = item.id === this.selectItem.id
      }
      return {
        'control-item': true,
        active,
      }
    },
    onItemClickHandle(index) {
      this.selectItem = this.data.list[index]
      this.selectInnerItem = {}
      this.updateProperties(this.selectItem)
    },
    onAddHandler(evt) {
      this.selectItem = this.data.list[evt.newIndex]
      this.updateProperties(this.selectItem)
    },
    deleteItem(index) {
      this.data.list.splice(index, 1)
    },
    onGridItemSelect(selectInnerItem) {
      this.selectInnerItem = selectInnerItem
      this.selectItem = {}
    },
  },
}
</script>
<style scoped lang="less">
.control-parent-list {
  padding: 0 10px 5px;
  position: relative;
  min-height: 500px;
}
.control-item {
  border: solid 1px #efefef;
  margin-bottom: 6px;
  padding: 6px;
}
.active {
  border: dashed 2px #3498db;
}
flip-list-move {
  transition: transform 0.5s;
}
.ghost {
  opacity: 0.8;
  background: #c8ebfb;
}
</style>

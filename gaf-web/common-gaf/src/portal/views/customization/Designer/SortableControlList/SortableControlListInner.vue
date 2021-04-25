<template>
  <a-row>
    <draggable
      v-model="item.list"
      v-bind="dragOptions"
      sort="true"
      @start="drag = true"
      @end="drag = false"
    >
      <transition-group
        :name="!drag ? 'flip-list' : null"
        tag="div"
        class="control-child-list"
        type="transition"
      >
        <template v-for="(l, idx) in item.list">
          <sortable-control-item
            :key="idx"
            :data="l"
            @delete="deleteItem(idx)"
          />
        </template>
      </transition-group>
    </draggable>
  </a-row>
</template>

<script>
import draggable from 'vuedraggable'
import SortableControlItem from './SortableControlItem'
export default {
  name: 'SortableControlListInner',
  components: {
    draggable,
    SortableControlItem,
  },
  props: {
    item: {
      type: Object,
      default: () => {},
      required: false,
    },
  },
  data() {
    return {
      drag: false,
      dragOptions: {
        animation: 150,
        group: {
          name: 'controls',
          pull: true,
          put(to) {
            return to.el.children.length < 1
          },
        },
        ghostClass: 'ghost',
        sort: false,
      },
    }
  },
  methods: {
    deleteItem(index) {
      this.item.list.splice(index, 1)
    },
  },
}
</script>
<style scoped lang="less">
.control-child-list {
  min-height: 100px;
  margin: 6px;
}
flip-list-move {
  transition: transform 0.5s;
}
.ghost {
  opacity: 0.8;
  background: #c8ebfb;
}
</style>

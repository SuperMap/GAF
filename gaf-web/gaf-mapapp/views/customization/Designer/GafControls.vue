<template>
  <a-collapse
    v-model="activeKey"
    :bordered="false"
    class="gaf-collapse"
    accordion
  >
    <a-collapse-panel v-for="l of list" :key="l.type" :header="l.title">
      <draggable
        v-model="l.controls"
        :group="{ name: 'controls', pull: 'clone', put: false }"
        :clone="cloneControl"
        :sort="false"
        class="draggable-left"
      >
        <transition-group tag="ul">
          <li v-for="c in l.controls" :key="c.name">
            <a-icon :type="c.icon" />
            <span class="title">{{ c.title }}</span>
          </li>
        </transition-group>
      </draggable>
    </a-collapse-panel>
  </a-collapse>
</template>

<script>
import Draggable from 'vuedraggable'
export default {
  name: 'GafControls',
  components: {
    Draggable,
  },
  props: {
    list: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      activeKey: this.list.length > 0 ? this.list[0].type : '',
    }
  },
  methods: {
    cloneControl(control) {
      const item = {
        id: Math.random(),
        ...JSON.parse(JSON.stringify(control)),
      }
      if (item.name === 'grid') {
        item.columns.forEach((c) => (c.id = Math.random()))
      }
      return item
    },
  },
}
</script>

<style scoped lang="less">
.draggable-left {
  & > ul {
    list-style: none;
    margin-bottom: 0;
    & > li {
      cursor: move;
      padding: 8px 12px;
      & > .title {
        padding-left: 15px;
      }
    }
    & > li:hover {
      background-color: #e8e8e8;
    }
  }
}
</style>

<template>
  <div>
    <a-row v-for="(col, index) in columns" :key="index" :style="itemLayout.row">
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle"
        >栅格列：</a-col
      >
      <a-col :span="itemLayout.content">
        <a-input-number
          v-model="col.span"
          :step="1"
          :min="0"
          :max="24"
          style="width: 60px"
        />
        <a-icon
          v-if="columns.length > 2"
          class="dynamic-delete-button"
          type="minus-circle-o"
          @click="() => remove(index)"
        />
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="itemLayout.label" :style="itemLayout.textTitle" />
      <a-col :span="itemLayout.content">
        <a-button icon="plus" @click="add">添加列</a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script>
export default {
  name: 'PanelColumn',
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
  computed: {
    columns() {
      return this.item.columns
    },
  },
  methods: {
    remove(index) {
      this.item.columns.splice(index, 1)
    },
    add() {
      const column = {
        ...this.item.columns.slice(-1)[0],
        span: 0,
        id: Math.random(),
      }
      this.item.columns.push(column)
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

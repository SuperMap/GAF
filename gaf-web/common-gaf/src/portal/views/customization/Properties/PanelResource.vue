<template>
  <div>
    <a-row>
      <a-col>
        平台概况路径
      </a-col>
      <a-col>
        <a-input v-model="options.grafanaUrl" />
      </a-col>
    </a-row>
  </div>
</template>

<script>
export default {
  name: 'PanelResource',
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
    options() {
      return this.item
    },
  },
  methods: {
    // change() {
      // console.log('a', this.item)
      // console.log('a', this.itemLayout)
    // }
    // remove(index) {
    //   this.item.trs.splice(index, 1)
    // },
    // add() {
    //   const trs = {
    //     ...this.item.trs.slice(-1)[0],
    //     span: 0,
    //     id: Math.random(),
    //   }
    //   this.item.trs.push(trs)
    // },
  },
}
</script>
<style scoped>
/* .dynamic-delete-button {
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
} */
</style>

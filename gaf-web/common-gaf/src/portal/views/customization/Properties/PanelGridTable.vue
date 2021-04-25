<template>
  <div>
    <a-row>
      <a-col>
        宽度
      </a-col>
      <a-col>
        <a-input v-model="options.width" />
      </a-col>
    </a-row>
    <a-row>
      <a-col>
        表格样式CSS
      </a-col>
      <a-col>
        <a-input placeholder="例:width:500px;height:400px;" v-model="options.customStyle" />
      </a-col>
    </a-row>
    <a-row>
      <a-col>
        属性
      </a-col>
        <a-row>
          <a-col :span="12">
            <a-checkbox v-model="options.bordered" @change="change">
              显示边框
            </a-checkbox>
          </a-col>
          <a-col :span="12">
            <a-checkbox v-model="options.bright">
              鼠标经过点亮
            </a-checkbox>
          </a-col>
            <a-col :span="12">
            <a-checkbox v-model="options.small">
              紧凑型
            </a-checkbox>
          </a-col>
        </a-row>
    </a-row>
    <a-row>
      <a-col>
        提示:请点击右键增加行列，或者合并单元格
      </a-col>
    </a-row>
  </div>
</template>

<script>
export default {
  name: 'PanelGridTable',
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
      return this.item.options
    },
  },
  methods: {
    change() {
      // console.log('a', this.item)
      // console.log('a', this.itemLayout)
    }
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

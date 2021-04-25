<template>
  <div class="header">
    <span class="title">{{ title }}</span>
    <a @click="openHref" v-if="isShowMore" class="title div-float" >更多</a>
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
    isShowMore: {
      type: Boolean,
      default: false,
      required: false,
    },
    href: {
      type: String,
      default: '',
      required: false,
    }
  },
  methods:{
    openHref() {
      // const toUrl = this.$router.resolve({ path: this.href, query: { id: "1" } })
      const toUrl = this.$router.resolve({ path: this.href })
      window.open(toUrl.href, '_blank')
    }
  }
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
.div-float {
  float: right;
  color: #1890ff;
  text-decoration: none;
  background-color: transparent;
  outline: none;
  cursor: pointer;
  transition: color 0.3s;
  margin-right: 35px;
}
</style>

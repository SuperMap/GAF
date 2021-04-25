<template>
  <div
    :class="'mini-bar ' + overCls"
    :style="style"
    @click="handleChange"
    @mouseover="handleMouseOver"
    @mouseleave="handleMouseLeave"
  >
    <a-icon :type="icon" class="icon" />
  </div>
</template>

<script>
export default {
  name: 'GafMinimum',
  props: {
    position: {
      type: String,
      validator(value) {
        return ['left', 'right'].includes(value)
      },
      default() {
        return 'left'
      },
    },
  },
  data() {
    return {
      overCls: '',
      collapsed: false,
    }
  },
  computed: {
    style() {
      if (this.position === 'left') {
        return 'right:0;'
      } else {
        return 'left:0;'
      }
    },
    icon() {
      if (this.position === 'left') {
        return this.collapsed ? 'double-right' : 'double-left'
      } else {
        return this.collapsed ? 'double-left' : 'double-right'
      }
    },
  },
  methods: {
    handleChange() {
      this.collapsed = !this.collapsed
      this.$emit('toggle', this.collapsed)
    },
    handleMouseOver() {
      this.overCls = 'active'
    },
    handleMouseLeave() {
      this.overCls = ''
    },
  },
}
</script>

<style scoped lang="less">
.mini-bar {
  height: 100%;
  width: 14px;
  position: absolute;
  z-index: 1;
  & > .icon {
    cursor: pointer;
    position: absolute;
    top: 50%;
    margin-top: -7px;
  }
}
.active {
  cursor: pointer;
  background-color: #efefef;
}
</style>

<template>
  <a-button-group class="actions" size="small">
    <a-button
      :icon="collapsed ? 'down' : 'up'"
      class="button"
      @click.stop="miniHandle"
    />
    <a-button class="button" icon="delete" @click.stop="deleteHandle" />
  </a-button-group>
</template>

<script>
export default {
  name: 'SortableControlItemAction',
  data() {
    return {
      collapsed: false,
    }
  },
  methods: {
    miniHandle() {
      this.collapsed = !this.collapsed
      this.$emit('change', this.collapsed)
    },
    deleteHandle() {
      this.$emit('delete')
    },
  },
}
</script>

<style scoped lang="less">
.actions {
  position: absolute;
  top: 0;
  right: 0;
  .button {
    border-width: 0;
    background-color: #efefef;
    border-color: #efefef;
    font-size: 16px;
  }
}
</style>

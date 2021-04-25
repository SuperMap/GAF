<template>
  <a-steps
    :direction="attr.direction"
    :current="nodes.length"
    :progress-dot="!!attr.url"
    :style="style"
    size="small"
  >
    <a-step
      v-for="(node, index) in nodes"
      :key="index"
      :title="node.title"
      :description="node.desc"
    >
      <a-icon v-if="!!node.icon" slot="icon" :type="node.icon" />
    </a-step>
  </a-steps>
</template>

<script>
export default {
  name: 'PlatStep',
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      dataSource: [],
    }
  },
  computed: {
    attr() {
      return this.data.attr || {}
    },
    nodes() {
      return this.attr.nodes || this.dataSource
    },
    style() {
      return `margin-top:${this.attr.direction === 'horizontal' ? '35' : '0'}px`
    },
  },
  async mounted() {
    if (this.attr.mode === 'rest' && this.attr.url) {
      const data = await this.$axios.$get(this.attr.url)
      this.dataSource = data
    }
  },
}
</script>

<style scoped></style>

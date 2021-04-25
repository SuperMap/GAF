<template>
  <a-list v-if="visible" :data-source="dataSource" size="small">
    <a-list-item slot="renderItem" slot-scope="item">{{ item }}</a-list-item>
  </a-list>
</template>

<script>
export default {
  name: 'PlatList',
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      list: [],
    }
  },
  computed: {
    attr() {
      return this.data.attr || {}
    },
    dataSource() {
      return this.attr.data || this.list
    },
    visible() {
      return this.dataSource.length > 0
    },
  },
  async mounted() {
    if (this.attr.mode === 'rest' && this.attr.url) {
      const data = await this.$axios.$get(this.attr.url)
      this.list = data
    }
  },
}
</script>

<style scoped></style>

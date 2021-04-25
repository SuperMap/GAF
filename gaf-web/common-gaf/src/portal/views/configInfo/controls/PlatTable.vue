<template>
  <a-table
    :show-header="attr.showHeader"
    :bordered="attr.showBorder"
    :columns="columns"
    :pagination="pagination"
    :data-source="dataSource"
    size="small"
    @change="handleChange"
  />
</template>

<script>
export default {
  name: 'PlatTable',
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      dataSource: [],
      total: 0,
      pagination: false,
    }
  },
  computed: {
    attr() {
      return this.data.attr || {}
    },
    columns() {
      return this.attr.columns.filter((c) => c.visible) || []
    },
  },
  watch: {
    'attr.url': {
      handler(val) {
        if (val) {
          const { pageSize, pageIndex } = this.attr
          this.handleChange({ current: pageIndex, pageSize })
        }
      },
      immediate: true,
    },
    'attr.pageSize'(val) {
      const { url, pageIndex } = this.attr
      if (url) {
        this.handleChange({ current: pageIndex, pageSize: val })
      }
    },
    'attr.pageIndex'(val) {
      const { pageSize, url } = this.attr
      if (url) {
        this.handleChange({ current: val, pageSize })
      }
    },
    'attr.showPage'(val) {
      const { pageSize, pageIndex, url } = this.attr
      if (url && val) {
        this.handleChange({ current: pageIndex, pageSize })
      }
    },
  },
  methods: {
    async handleChange(pagination) {
      const pageIndex = pagination.current
      const { showPage, pageSize, url } = this.attr
      let tempUrl = url
      if (showPage) {
        tempUrl += `?pageIndex=${pageIndex}&pageSize=${pageSize}`
      }
      const data = await this.$axios.$get(tempUrl)
      if (data.data.pageList) {
        data.data.pageList.forEach((c, index) => {
          c.key = index
        })
        this.dataSource = data.data.pageList
        if (showPage) {
          this.pagination = {
            total: data.data.total,
            pageSize: pagination.pageSize,
            current: pagination.current,
          }
        } else {
          this.pagination = false
        }
      }
    },
  },
}
</script>

<style scoped></style>

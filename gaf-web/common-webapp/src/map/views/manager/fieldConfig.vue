<template>
  <div>
    <a-table
      :loading="false"
      :columns="columns"
      :data-source="dataSource"
      :pagination="pagination"
      :row-key="setTableKey"
      :row-selection="{
        selectedRowKeys: selectedRowKeys,
        onChange: onSelectChange,
      }"
      bordered
      size="small"
      @change="handleTableChange"
    />
  </div>
</template>

<script>
export default {
  model: {
    prop: 'serviceInfo',
    event: 'change',
  },
  props: {
    serviceInfo: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      columns: [
        { title: '字段名称', dataIndex: 'name', key: 'name' },
        { title: '字段别名', dataIndex: 'caption', key: 'caption' },
        { title: '字段类型', dataIndex: 'type', key: 'type' },
      ],
      pagination: {
        pageSize: 10,
        current: 1,
        total: 10,
        hideOnSinglePage: false,
        showSizeChanger: true,
        pageSizeOptions: ['5', '10', '30'],
      },
      selectedRowKeys: [],
      selectedRows: [],
      selectInfo: {},
      dataSource: [],
      pageSelectRowKeys: [],
    }
  },
  watch: {
    serviceInfo(val) {
      this.selectInfo = val
    },
  },
  created() {
    this.loadSelectService()
  },
  methods: {
    async loadSelectService() {
      const res = await this.$axios.$get(
        `/manager/map/service/fields/${this.selectInfo.dsName}/${this.selectInfo.dtName}`
      )
      if (res.length > 0) {
        this.pagination.total = res.length
        for (let i = 0; i < res.length; i++) {
          const mapLayerJsonUrl =
            'http://localhost:8090/iserver/services/data-SiChuanShengLeShanShiShiZhongQu-511102-/rest' +
            `/data/datasources/四川省乐山市市中区(511102)/datasets/ZD/fields/${res[i]}.jsonp`
          // this.selectInfo.url +
          // `/data/datasources/${this.selectInfo.dsName}/datasets/${this.selectInfo.dtName}/fields/${res[i]}.jsonp`
          const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
          const fieldInfo = {}
          fieldInfo.name = res[i]
          fieldInfo.caption = mapLayerJson.fieldInfo.caption
          fieldInfo.type = mapLayerJson.fieldInfo.type
          this.dataSource[i] = fieldInfo
        }
      }
      this.dataSource = [...this.dataSource]
    },
    handleTableChange(pagination) {
      const pager = { ...this.pagination }
      pager.current = pagination.current
      pager.pageSize = pagination.pageSize
      this.pagination = pager
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.$emit('selectedRows', selectedRows)
    },
    setTableKey(record) {
      return record.name
    },
  },
}
</script>

<style scoped></style>

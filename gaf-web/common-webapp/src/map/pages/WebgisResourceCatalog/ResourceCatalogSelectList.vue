<template>
  <div>
    <div style="margin-bottom: 10px">
      <a-input-search
        v-model="searchValue"
        :loading="loading"
        placeholder="请输入目录名称搜索"
        allow-clear
        size="default"
        style="width: 320px"
        @search="onSearch"
      >
        <!-- <button slot="enterButton" class="model-search"> 搜索 </button> -->
      </a-input-search>
    </div>
    <div>
      <gaf-table-with-page
        :show-x-h="false"
        :pagination="pagination"
        :row-selection="{
          type: 'radio',
          columnTitle: '选择',
          fixed: true,
          onChange: onChange,
          selectedRowKeys: selectedRowKeys,
        }"
        :row-key="(r, i) => r.catalogId"
        :data-source="rootCatalogList"
        :loading="loading"
        :columns="columns"
        bordered
        size="small"
        align="center"
        @change="tableChange"
      >
        <template slot="bizType" slot-scope="text">
          {{ bizMap.get(text) ? bizMap.get(text) : text }}
        </template>
      </gaf-table-with-page>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    // 选中的gis服务id
    selectedCatalogId: {
      type: String,
      default: '',
    },
    bizMap: {
      type: Map,
      default: () => new Map(),
    },
  },
  data() {
    return {
      rootCatalogList: [],
      searchValue: '',
      searchedColumn: 'name',
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      sorter: {
        order: null,
        field: null,
      },
      // 列表是否加载中
      loading: true,
      // 选中的行key
      selectedRowKeys: [],
      columns: [
        {
          title: '资源目录名称',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '目录类型',
          dataIndex: 'bizTypeCode',
          key: 'biz_type_code',
          scopedSlots: { customRender: 'bizType' },
        },
        {
          title: '目录描述',
          dataIndex: 'description',
          key: 'description',
        },
      ],
    }
  },
  computed: {},
  created() {
    this.getList()
    if (this.selectedCatalogId && this.selectedCatalogId !== '') {
      this.selectedRowKeys = [this.selectedCatalogId]
    }
  },
  methods: {
    // 当搜索时
    onSearch() {
      this.getList()
    },
    // 当选择目录改变时
    onChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.$emit('onSelectedChange', selectedRowKeys, selectedRows)
    },
    async getList() {
      this.loading = true
      let url = `/sys-mgt/sys-catalogs/resource-root-catalogs?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      if (this.searchValue.trim() && this.searchedColumn) {
        url =
          url +
          '&searchFieldName=' +
          this.searchedColumn +
          '&searchFieldValue=' +
          this.searchValue.trim()
      }
      if (this.sorter.order && this.sorter.field) {
        url =
          url +
          '&orderFieldName=' +
          this.sorter.field +
          '&orderMethod=' +
          this.sorter.order
      }
      const res = await this.$axios.$get(url)
      this.loading = false
      if (res.isSuccessed) {
        this.pagination.current = res.data.pageIndex
        this.pagination.pageSize = res.data.pageSize
        this.pagination.total = res.data.total
        this.rootCatalogList = res.data.content
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo, filters, sorter) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      if (sorter) {
        this.sorter.order = sorter.order === 'descend' ? 'DESC' : 'ASC'
        this.sorter.field = sorter.columnKey
      }
      this.getList()
    },
  },
}
</script>

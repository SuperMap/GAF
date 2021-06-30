<template>
  <div>
    <gaf-table-layout>
    <template #actions>
    </template>
    <template #filter>
      <a-input-search
        v-model="searchValue"
        allow-clear
        placeholder="请输入字段名称"
        size="large"
        style="width: 320px;margin-bottom: 10px;"
        @search="onSearch"
      >
        <!-- <button slot="enterButton" class="btn-search"> 搜索 </button> -->
      </a-input-search>
    </template>
    <template #default>
      <gaf-table-with-page
        :show-x-h="false"
        :pagination="pagination"
        :row-selection="{
          type: 'checkbox',
          fixed: true,
          onChange: onChange,
          selectedRowKeys: selectedRowKeys,
          getCheckboxProps: (record) => ({
            props: {
              disabled: record.fieldName === 'SmID',
              name: record.fieldName,
            },
          }),
        }"
        :row-key="(r, i) => r.fieldName"
        :data-source="
          dataconfigFieldList ? dataconfigFieldList : configFieldList
        "
        :loading="loading"
        :columns="columns"
        bordered
        size="small"
        align="center"
        @change="tableChange"
      >
        <template slot="fieldAliasRender" slot-scope="text, record">
          <a-input
            v-model="record.fieldAlias"
            style="border: none; padding: 0; height: auto"
          >
            {{ record.fieldAlias }}
          </a-input>
        </template>
      </gaf-table-with-page>
    </template>
    </gaf-table-layout>
    <!-- <div style="display: flex; justify-content: flex-end">
      <button
        style="color: white; margin-right: 10px"
        class="btn-ok"
        @click="onOk"
      >
        保存
      </button>
      <button class="btn-cancel" @click="onCancel">取消</button>
    </div> -->
  </div>
</template>
<script>
export default {
  props: {
    // 选中的gis服务id
    selectedServiceId: {
      type: String,
      default: '',
    },
    openconfigField: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      dataconfigFieldList: [],
      configFieldList: [],
      allConfigFieldList: [],
      searchValue: '',
      searchedColumn: 'fieldName',
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
          title: '字段',
          dataIndex: 'fieldName',
          key: 'field_name',
          width: '20%',
        },
        {
          title: '别名',
          dataIndex: 'fieldAlias',
          key: 'field_alias',
          width: '58%',
          scopedSlots: { customRender: 'fieldAliasRender' },
        },
        {
          title: '字段类型',
          dataIndex: 'fieldTypeCode',
          key: 'field_type_code',
          width: '20%',
        },
      ],
    }
  },
  computed: {},
  created() {
    this.getList()
  },
  methods: {
    async onOk() {
      this.openconfigField = false
      this.$emit('onOk', this.openconfigField)
      const url = `/map/webgis-data-service-fields/configuration/${this.selectedServiceId}`
      const res = await this.$axios.$put(url, this.configFieldList)
      if (res.isSuccessed) {
        this.$message.success(`${res.message}`)
      } else {
        this.$message.error(`修改失败,原因:${res.message}`)
      }
    },
    onCancel() {
      this.openconfigField = false
      this.$emit('onCancel', this.openconfigField)
    },
    // 当搜索时
    onSearch(searchValue) {
      if (searchValue === '') {
        this.dataconfigFieldList = this.allConfigFieldList
      } else {
        this.dataconfigFieldList = this.allConfigFieldList.filter(
          (item) => item.fieldName.includes(searchValue) === true
        )
      }
    },
    // 当选择目录改变时
    onChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      if (selectedRows !== null) {
        this.configFieldList = selectedRows
      }
    },
    async getList() {
      this.loading = true
      // let url = `/sys-mgt/sys-catalogs/resource-root-catalogs?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      const url = `/map/webgis-data-service-fields/data-service/${this.selectedServiceId}`
      const res = await this.$axios.$get(url)
      this.loading = false
      if (res.isSuccessed) {
        // this.pagination.current = res.data.pageIndex
        // this.pagination.pageSize = res.data.pageSize
        // this.pagination.total = res.data.total
        this.allConfigFieldList = res.data.fields
        this.dataconfigFieldList = this.allConfigFieldList
        res.data.fieldNames.forEach((item) => this.selectedRowKeys.push(item))
        res.data.fields.forEach((item) => {
          if (res.data.fieldNames.includes(item.fieldName)) {
            this.configFieldList.push(item)
          }
        })
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

<style scoped>
/* .ant-table-tbody > tr > td {
  white-space: break-spaces;
  padding: 0px;
} */
</style>

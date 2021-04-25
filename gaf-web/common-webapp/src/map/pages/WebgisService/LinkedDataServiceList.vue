<template>
  <div>
    <button class="btn-fun red" @click="batchDel">
      <span><a-icon type="delete" />
      批量删除</span>
    </button>
    <gaf-table-with-page
      :show-x-h="false"
      :row-selection="{
        selectedRowKeys: selectedRowKeys,
        onChange: rowChange,
        onSelect: rowSelect,
        onSelectAll: rowSelectAll,
      }"
      :pagination="pagination"
      :row-key="(r, i) => r.gisServiceAssocId"
      :data-source="webgisServiceList"
      :columns="columns"
      :loading="loading"
      bordered
      size="small"
      :locale="{
        filterConfirm: '确定',
        filterReset: '重置',
        emptyText: '暂时没有关联的服务',
      }"
      @change="tableChange"
    >
      <template slot="address" slot-scope="text">
        <div class="url">
          <a-tooltip placement="topLeft">
            <template slot="title">
              {{ text }}
            </template>
            <a :href="text" target="_blank">{{ text }}</a>
          </a-tooltip>
        </div>
      </template>
      <template slot="timeRender" slot-scope="text">
        {{ timeFormat(text) }}
      </template>
      <template slot="type" slot-scope="text">
        {{ webgisServiceTypes.get(text) ? webgisServiceTypes.get(text) : text }}
      </template>
      <template slot="operation" slot-scope="text, record">
        <a-popconfirm
          title="删除后无法恢复，确认是否继续?"
          ok-text="确认"
          cancel-text="取消"
          @confirm="() => handleDelete(record)"
        >
          <a href="javascript:;" class="btn-del"><a-icon type="delete" /> 删除</a>
        </a-popconfirm>
      </template>
    </gaf-table-with-page>
  </div>
</template>
<script>
// @ts-nocheck
export default {
  props: {
    webgisServiceId: {
      type: String,
      default: null,
    },
    webgisServiceTypes: {
      type: Map,
      default: () => new Map(),
    },
  },
  data() {
    return {
      webgisServiceList: [],
      selectedRowKeys: [],
      // 分页参数
      pagination: {
        pageSize: 5,
        current: 1,
        total: 0,
        pageSizeOptions: ['5', '10', '20', '30'],
      },
      loading: false,
      columns: [
        {
          title: '服务名称',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '服务地址',
          dataIndex: 'address',
          key: 'address',
          scopedSlots: { customRender: 'address' },
          width: '550px',
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' },
        },
      ],
    }
  },
  computed: {},
  created() {
    this.getLinkedServiceList()
  },
  methods: {
    async batchDel() {
      const url = '/map/webgis-service-associations/'
      const selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys })
        if (rst.data.isSuccessed) {
          this.$message.success('删除成功')
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`)
        }
        this.$nextTick(() => {
          this.getLinkedServiceList()
        })
      } else {
        this.$message.warn('请选择您要删除的内容')
      }
    },
    rowChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      this.getLinkedServiceList()
    },
    // 删除关联关系
    async handleDelete(record) {
      const url = `/map/webgis-service-associations/${record.gisServiceAssocId}`
      const res = await this.$axios.$delete(url)
      if (res.isSuccessed) {
        this.$message.success(`删除成功`)
        this.getLinkedServiceList()
      } else {
        this.$message.error(`删除失败,原因:${res.message}`)
      }
    },
    // 获取已关联的服务列表
    async getLinkedServiceList() {
      const url = `/map/webgis-service-associations/webgis-service/${this.webgisServiceId}?pageNum=${this.pagination.current}&pageSize=${this.pagination.pageSize}&typeCode=RESTDATA`
      this.loading = true
      const res = await this.$axios.$get(url)
      if (res.successed) {
        this.pagination.current = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize
        this.pagination.total = res.data.totalCount
        this.webgisServiceList = res.data.pageList
      } else {
        this.$message.error(`获取已关联的服务列表失败，原因:${res.message}`)
      }
      this.loading = false
    },
    timeFormat(str) {
      if (!str || str === '') {
        return ''
      }
      const dt = new Date(str)
      const year = dt.getFullYear()
      let month = dt.getMonth() + 1
      let date = dt.getDate()
      let hour = dt.getHours()
      let minute = dt.getMinutes()
      let second = dt.getSeconds()

      month = month < 10 ? '0' + month : month
      date = date < 10 ? '0' + date : date
      hour = hour < 10 ? '0' + hour : hour
      minute = minute < 10 ? '0' + minute : minute
      second = second < 10 ? '0' + second : second

      return `${year}/${month}/${date} ${hour}:${minute}:${second}`
    },
  },
}
</script>

<style scoped>
.url {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 550px;
}

.url a {
  color: rgb(34, 34, 34);
  text-decoration: none;
}

.url a:link {
  color: rgb(34, 34, 34);
}

.url a:visited {
  color: rgb(153, 153, 153);
}

.url a:hover {
  color: #559df5;
}

.url a:active {
  color: rgb(153, 153, 153);
}
</style>

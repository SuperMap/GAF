<template>
  <div class="gutter-box">
    <a-row>
      <a-input-search class="search-style" @search="onSearch" />
    </a-row>
    <a-table
      :columns="columns"
      :row-key="(record) => record.id"
      :data-source="serviceList"
      :pagination="pagination"
      size="small"
      style="min-height: 32vh"
      :row-class-name="setRowClassName"
      bordered
      :custom-row="click"
      @change="handlePaginationChange"
    >
      <template slot="name" slot-scope="text">
        <a-tooltip placement="bottom">
          <template slot="title">
            <span>{{ text }}</span>
          </template>
          <span>{{ text }}</span>
        </a-tooltip>
      </template>
      <template slot="address" slot-scope="text">
        <a-tooltip placement="bottom">
          <template slot="title">
            <span>{{ text }}</span>
          </template>
          <span>{{ text }}</span>
        </a-tooltip>
      </template>
      <template slot="serviceType" slot-scope="text">
        <span v-if="text === 'RESTMAP'">地图服务</span>
        <span v-if="text === 'RESTDATA'">数据服务</span>
        <span v-if="text === 'RESTMAP3D'">三维服务</span>
      </template>
    </a-table>
  </div>
</template>

<script>
// @ts-nocheck
import { dataUrl } from '~/utils/GAFMapDataUtils'
const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
    width: '150px',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '名称',
    dataIndex: 'address',
    key: 'address',
    scopedSlots: { customRender: 'address' },
  },
  {
    title: '类型',
    dataIndex: 'serviceType',
    key: 'serviceType',
    width: '100px',
    scopedSlots: { customRender: 'serviceType' },
  },
]
export default {
  data() {
    return {
      rowId: '',
      columns,
      serviceList: [],
      pagination: {
        pageSize: 8,
        pageIndex: 1,
        total: 8,
        hideOnSinglePage: true,
      },
      layerServiceInfo: {},
    }
  },
  created() {
    this.loadMapService()
  },
  methods: {
    async loadMapService() {
      // const res = await this.$axios.$get('/manager/map/service')
      const res = await this.$axios.$get(dataUrl.ServiceInfoUrl, {
        params: {
          ServeSearchParameter: this.pagination,
        },
      })
      this.serviceList = []
      for (let i = 0; i < res.content.length; i++) {
        if (res.content[i].serviceType !== 'RESTDATA') {
          this.serviceList.push(res.content[i])
        }
      }
      this.pagination.total = res.total
    },
    click(record) {
      return {
        on: {
          click: () => {
            this.rowId = record.id
            this.layerServiceInfo = record
            this.$emit('onServiceClick', record)
          },
        },
      }
    },
    setRowClassName(record) {
      return record.id === this.rowId ? 'clickRowStyl' : ''
    },
    async onSearch(value) {
      const res = await this.$axios.$get(dataUrl.ServiceInfoUrl, {
        params: {
          ServeSearchParameter: {
            key: value,
            serviceType: '',
          },
        },
      })
      if (res == null) {
        this.$message({
          message: '查询失败',
          type: 'error',
        })
      } else {
        this.serviceList = []
        for (let i = 0; i < res.content.length; i++) {
          if (res.content[i].serviceType !== 'RESTDATA') {
            this.serviceList.push(res.content[i])
          }
        }
        this.pagination.total = this.serviceList.length
      }
    },
    handlePaginationChange(pagination) {
      const pager = { ...this.pagination }
      pager.pageIndex = pagination.current
      this.pagination = pager
      this.loadMapService()
    },
  },
}
</script>

<style lang="less" scoped>
.clickRowStyl {
  background-color: #00b4ed;
}
.ant-table-tbody > .clickRowStyl :hover > td {
  background-color: #00b4ed;
}
</style>
<style scoped>
.gutter-box >>> .ant-table-tbody > tr > td {
  max-width: 100px;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>

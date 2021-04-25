<template>
  <div>
    <gaf-table-layout>
      <template #actions>
        <a-button type="primary" @click="showAddService">
          新建地图应用
        </a-button>
      </template>
      <template #filter>
        <gaf-search @search="onSearch" />
      </template>
      <a-table
        :columns="columns"
        :data-source="dataSource"
        :pagination="pagination"
        :row-key="(record) => record.id"
        size="small"
        @change="onChange"
      >
        <template slot="type" slot-scope="text">
          <span v-if="text === 'MAP2D'">二维地图</span>
          <span v-if="text === 'MAP3D'">三维地图</span>
        </template>
        <template slot="createTime" slot-scope="text">
          <span>{{ getDate(text) }}</span>
        </template>
        <template slot="action" slot-scope="text, record">
          <a href="javascript:;" @click="() => mapServiceOpen(record)">
            <a-icon type="play-circle" />
            <span>打开</span>
          </a>
          <a-divider type="vertical" />
          <a href="javascript:;" @click="() => mapServiceEdit(record)">
            <a-icon type="edit" />
            <span>编辑</span>
          </a>
          <a-divider type="vertical" />
          <a-popconfirm
            title="是否删除？"
            ok-text="是"
            cancel-text="否"
            @confirm="() => mapServiceDelete(record)"
          >
            <a href="javascript:;">
              <a-icon type="delete"></a-icon>
              <span>删除</span>
            </a>
          </a-popconfirm>
        </template>
      </a-table>
      <gaf-modal
        v-model="visible"
        title="新建地图应用"
        :width="800"
        @ok="addMapItem"
      >
        <add-map-form ref="addModal" />
      </gaf-modal>
    </gaf-table-layout>
  </div>
</template>

<script>
// @ts-nocheck
import { formatDate } from 'gaf-web-common'
import { dataUrl } from '~/utils/GAFMapDataUtils'
import addMapForm from '../../manager/addMapForm'
const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
    sorter: (a, b) => (a.name < b.name ? -1 : a.name > b.name ? 1 : 0),
  },
  {
    title: '地图标识',
    dataIndex: 'code',
    key: 'code',
    sorter: (a, b) => (a.name < b.name ? -1 : a.name > b.name ? 1 : 0),
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    sorter: (a, b) =>
      a.createTime < b.createTime ? -1 : a.createTime > b.createTime ? 1 : 0,
    scopedSlots: { customRender: 'createTime' },
  },
  {
    title: '地图类型',
    dataIndex: 'type',
    key: 'type',
    filters: [
      { text: '二维地图', value: 'MAP2D' },
      { text: '三维地图', value: 'MAP3D' },
    ],
    onFilter: (value, record) => value === record.type,
    scopedSlots: { customRender: 'type' },
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    width: '80px',
    scopedSlots: { customRender: 'action' },
  },
]
export default {
  components: {
    addMapForm,
  },
  data() {
    return {
      columns,
      dataSource: [],
      pagination: {
        pageSize: 10,
        current: 1,
        total: 10,
        hideOnSinglePage: false,
      },
      visible: false,
      editVisible: false,
      selectedConfigInfo: {},
    }
  },
  created() {
    this.loadMapConfigs()
  },
  methods: {
    async loadMapConfigs() {
      const url = dataUrl.MapConfigUrl
      const res = await this.$axios.$get(url)
      this.dataSource = res.content
      this.pagination.total = res.total
    },
    getDate(d) {
      const time = formatDate(d)
      return time
    },
    onChange(pagination, filters) {
      if (filters.type && filters.type.length) {
        let count = 0
        const f = filters.type
        this.dataSource.forEach((item) => {
          if (f.includes(item.type)) {
            count++
          }
        })
        this.pagination.total = count
      } else {
        this.pagination.total = this.dataSource.length
      }
      const pager = { ...this.pagination }
      pager.current = pagination.current
      pager.pageSize = pagination.pageSize
      this.pagination = pager
    },
    showAddService() {
      this.visible = true
    },
    mapServiceOpen(row) {
      let mapType = 'map'
      if (row.type === 'MAP3D') {
        mapType = 'map3D'
      }
      const routeData = this.$router.resolve({
        path: `../${mapType}`,
        query: {
          code: row.code,
        },
      })
      window.open(routeData.href, '_blank')
    },
    mapServiceEdit(row) {
      this.$router.push({
        path: `../manager/MapConfig`,
        query: {
          code: row.code,
        },
      })
    },
    addMapItem() {
      const form = this.$refs.addModal.form
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const url = dataUrl.MapConfigUrl
        const r = await this.$axios.$get(url + `/${values.code}`)
        if (!r.isSuccessed) {
          this.$message.error('地图标识重复')
          return
        }
        const mapConfig = {
          name: values.mapName,
          code: values.code,
          type: values.type,
        }
        const result = await this.$axios.$post(url, mapConfig)
        if (result.isSuccessed) {
          this.$message.success('操作成功')
          this.loadMapConfigs()
          this.visible = false
          form.resetFields()
        } else {
          this.$message.error(result.message)
        }
      })
    },
    async onSearch(value) {
      const url = dataUrl.MapConfigUrl
      const res = await this.$axios.$get(url, {
        params: {
          ServeSearchParameter: {
            key: value,
          },
        },
      })
      this.dataSource = res.content
      this.pagination.total = res.total
    },
    async mapServiceDelete(row) {
      const res = await this.$axios.$delete(
        dataUrl.MapConfigUrl + `/${row.code}`
      )
      if (res.isSuccessed) {
        this.$message.success('删除成功')
        this.loadMapConfigs()
      }
    },
  },
}
</script>

<style scoped></style>

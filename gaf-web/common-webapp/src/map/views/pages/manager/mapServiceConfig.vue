<template>
  <div>
    <gaf-table-layout v-show="!showDetail">
      <template #actions>
        <a-button type="primary" @click="showAddService">新增</a-button>
      </template>
      <template #filter>
        <gaf-search @search="onSearch" />
      </template>
      <a-table
        :loading="loading"
        :columns="columns"
        :data-source="dataSource"
        :pagination="pagination"
        :row-key="(record) => record.id"
        size="small"
        bordered
        @change="onChange"
      >
        <template slot="index" slot-scope="text, record, index">
          <span>
            {{ (pagination.current - 1) * pagination.pageSize + index + 1 }}
          </span>
        </template>
        <span slot="address" slot-scope="text">
          <a :href="text" target="_blank">
            <span>{{ text }}</span>
          </a>
        </span>
        <span slot="serviceType" slot-scope="text">
          <span v-if="text === 'RESTMAP'">地图服务</span>
          <span v-if="text === 'RESTDATA'">数据服务</span>
          <span v-if="text === 'RESTMAP3D'">三维服务</span>
        </span>
        <span slot="action" slot-scope="text, record">
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
        </span>
      </a-table>
    </gaf-table-layout>
    <div v-show="showAdd">
      <add-map-service @back="closeAdd" @refresh="getMapServiceList" />
    </div>
    <div v-show="showEdit">
      <edit-map-service
        :map-service-info="mapServiceInfo"
        @back="closeEdit"
        @refresh="getMapServiceList"
      />
    </div>
  </div>
</template>

<script>
import { dataUrl } from '~/utils/GAFMapDataUtils'
import addMapService from '../../manager/addMapService'
import editMapService from '../../manager/editMapService'
export default {
  components: {
    addMapService,
    editMapService,
  },
  data() {
    return {
      loading: false,
      dataSource: [],
      visible: false,
      addVisible: false,
      serviceCheckResult: false,
      selectMapService: {},
      form: {},
      showInfo: [],
      columns: [
        {
          title: '序号',
          key: 'index',
          width: '40px',
          scopedSlots: { customRender: 'index' },
        },
        {
          title: '服务名称',
          dataIndex: 'name',
          key: 'name',
          sorter: (a, b) => (a.name < b.name ? -1 : a.name > b.name ? 1 : 0),
        },
        {
          title: '服务地址',
          dataIndex: 'address',
          key: 'address',
          sorter: (a, b) =>
            a.address < b.address ? -1 : a.address > b.address ? 1 : 0,
          scopedSlots: { customRender: 'address' },
        },
        {
          title: '图层名称',
          dataIndex: 'layerName',
          key: 'layerName',
          sorter: (a, b) =>
            a.layerName < b.layerName ? -1 : a.layerName > b.layerName ? 1 : 0,
        },
        {
          title: '服务类型',
          dataIndex: 'serviceType',
          key: 'serviceType',
          filters: [
            { text: '地图服务', value: 'RESTMAP' },
            { text: '数据服务', value: 'RESTDATA' },
            { text: '三维服务', value: 'RESTMAP3D' },
          ],
          onFilter: (value, record) => value === record.serviceType,
          scopedSlots: { customRender: 'serviceType' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          key: 'action',
          width: '140px',
          scopedSlots: { customRender: 'action' },
        },
      ],
      pagination: {
        pageSize: 10,
        current: 1,
        total: 10,
        hideOnSinglePage: false,
        showSizeChanger: false,
      },
      mapServiceInfo: {},
      showDetail: false,
      showAdd: false,
      showEdit: false,
    }
  },
  created() {
    this.getMapServiceList()
  },
  methods: {
    async getMapServiceList() {
      const res = await this.$axios.$get(dataUrl.ServiceInfoUrl)
      this.dataSource = res.content
      this.pagination.total = res.total
    },
    mapServiceEdit(row) {
      this.mapServiceInfo = row
      this.showEdit = true
      this.showDetail = true
    },
    showAddService() {
      this.showAdd = true
      this.showDetail = true
    },
    closeAdd() {
      this.showAdd = false
      this.showDetail = false
    },
    closeEdit() {
      this.showEdit = false
      this.showDetail = false
    },
    async mapServiceDelete(row) {
      const url = dataUrl.ServiceInfoUrl + `/${row.id}`
      const res = await this.$axios.$delete(url)
      if (res.isSuccessed) {
        if (this.dataSource.length > 0) {
          for (let i = 0; i < this.dataSource.length; i++) {
            if (this.dataSource[i].id === row.id) {
              this.dataSource.splice(i, 1)
            }
          }
        }
        this.$message.success('删除成功！')
      } else {
        this.$message.error('删除失败！')
      }
    },
    async onSearch(value) {
      let sType = ''
      if (value && value.includes('地图')) {
        sType = 'RESTMAP'
      } else if (value && value.includes('数据')) {
        sType = 'RESTDATA'
      } else if (value && value.includes('三维')) {
        sType = 'RESTMAP3D'
      }
      const res = await this.$axios.$get(dataUrl.ServiceInfoUrl, {
        params: {
          ServeSearchParameter: {
            key: value,
            serviceType: sType,
          },
        },
      })
      if (res == null) {
        this.$message({
          message: '查询失败',
          type: 'error',
        })
      } else {
        this.dataSource = res.content
        this.pagination.total = res.total
      }
    },
    async onChange(pagination, filters) {
      if (filters.serviceType && filters.serviceType.length) {
        let count = 0
        const f = filters.serviceType
        this.dataSource.forEach((item) => {
          if (f.includes(item.serviceType)) {
            count++
          }
        })
        this.pagination.total = count
      } else {
        const res = await this.$axios.$get(dataUrl.ServiceInfoUrl, {
          params: {
            ServeSearchParameter: {
              pageIndex: pagination.current,
              pageSize: pagination.pageSize,
            },
          },
        })
        this.dataSource = res.content
        this.pagination.total = res.total
      }
      const pager = { ...this.pagination }
      pager.current = pagination.current
      pager.pageSize = pagination.pageSize
      this.pagination = pager
    },
    handleOk() {
      const form = this.$refs.editModal.form
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const mapService = {
          name: values.name,
          address: values.address,
          layerName: values.layerName,
          serviceType: values.serviceType,
          description: values.description,
        }
        const url = dataUrl.ServiceInfoUrl + `/${this.mapServiceInfo.id}`
        const result = await this.$axios.$put(url, mapService)
        if (result.isSuccessed) {
          this.$message.success('操作成功')
          this.getMapServiceList()
          this.visible = false
          form.resetFields()
        } else {
          this.$message.error(result.message)
        }
      })
    },
    handleOkCancel() {
      this.form = {}
      this.showInfo = []
      this.visible = false
    },
    serviceAddressBlur(e) {
      this.serviceCheck(e)
      this.showServiceInfo(e.target.value)
    },
    async showServiceInfo(value) {
      if (value.includes('/maps/')) {
        const mapLayerJsonUrl = value + '.jsonp'
        const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
        this.showInfo[0] =
          '中心点：(' +
          mapLayerJson.center.x +
          ',' +
          mapLayerJson.center.y +
          ')'
        this.showInfo[1] =
          '全幅范围：左下(' +
          mapLayerJson.bounds.left +
          ',' +
          mapLayerJson.bounds.bottom +
          ') 右上(' +
          mapLayerJson.bounds.right +
          ',' +
          mapLayerJson.bounds.top +
          ')'
        this.showInfo[2] = '比例尺：' + mapLayerJson.scale
        this.showInfo[3] = '坐标系信息:' + mapLayerJson.prjCoordSys.name
        this.showInfo = [...this.showInfo]
      }
    },
    serviceCheck(e) {
      if (!e.target.value) {
        return
      }
      if (e.target.value.includes('localhost')) {
        this.$message.info('请使用真实的ip地址')
        return
      }
      this.$axios
        .get(e.target.value)
        .then(() => {
          this.setState({ serviceCheckResult: true })
        })
        .catch(() => {
          this.setState({ serviceCheckResult: false })
          this.$message.error('服务地址无效')
        })
    },
  },
}
</script>

<style scoped></style>

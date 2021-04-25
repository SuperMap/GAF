<template>
  <div class="gutter-box">
    <a-row>
      <a-col :span="12">
        <span style="font-size: large">定位配置</span>
      </a-col>
      <a-col :offset="6" :span="2">
        <a-tooltip placement="topLeft" title="添加">
          <a-icon type="plus" style="margin: 5px" @click="addVisible = true" />
        </a-tooltip>
      </a-col>
      <a-col :offset="1" :span="2">
        <a-tooltip placement="topLeft" title="删除">
          <a-popconfirm
            title="是否删除？"
            ok-text="是"
            cancel-text="否"
            @confirm="() => deleteSelection()"
          >
            <a href="javascript:;">
              <a-icon type="delete" style="margin: 5px" />
            </a>
          </a-popconfirm>
        </a-tooltip>
      </a-col>
    </a-row>
    <a-divider style="margin: 10px" />
    <div :style="divStyle">
      <a-table
        :columns="columns"
        :row-key="(record) => record.id"
        :data-source="data"
        :pagination="false"
        :show-header="false"
        :loading="loading"
        :custom-row="click"
        :row-class-name="setRowClassName"
        size="middle"
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
      </a-table>
    </div>
    <gaf-modal
      v-model="addVisible"
      title="新增定位"
      :width="800"
      :mask-closable="false"
      @ok="handleOk"
    >
      <new-layer-location ref="addLoc" :map-code="mapCode" />
    </gaf-modal>
  </div>
</template>

<script>
// @ts-nocheck
import { getLocationUrl } from '~/utils/GAFMapDataUtils'
import newLayerLocation from '../Location/newLayerLocation'
const columns = [
  {
    title: 'Name',
    dataIndex: 'name',
    sorter: true,
    scopedSlots: { customRender: 'name' },
  },
]
export default {
  components: {
    newLayerLocation,
  },
  model: {
    prop: 'refresh',
    event: 'change',
  },
  props: {
    refresh: {
      type: Boolean,
    },
    mapCode: {
      type: String,
      required: true,
      validator(value) {
        return value
      },
    },
  },
  data() {
    const { contentWidth, contentHeight } = this.$store.state
    return {
      width: contentWidth,
      height: contentHeight,
      columns,
      data: [],
      loading: false,
      pagination: {
        pageSize: 10,
        current: 1,
        total: 10,
        hideOnSinglePage: true,
      },
      rowId: '',
      addVisible: false,
    }
  },
  computed: {
    divStyle() {
      return `height:${this.height - 70}px;overflow-y:auto;overflow-x:hidden;`
    },
  },
  watch: {
    refresh: {
      handler() {
        this.getMapServiceList()
      },
    },
    immediate: true,
  },
  created() {
    this.getMapServiceList()
  },
  methods: {
    async getMapServiceList() {
      const serviceId = null
      const url = getLocationUrl(this.mapCode, serviceId)
      const res = await this.$axios.$get(url)
      if (res && res.data) {
        this.data = res.data
        this.pagination.total = this.data.length
      }
    },
    click(record) {
      return {
        on: {
          click: () => {
            this.rowId = record.id
            this.$emit('selectedDataService', record)
          },
        },
      }
    },
    setRowClassName(record) {
      return record.id === this.rowId ? 'clickRowStyl' : ''
    },
    async deleteSelection() {
      const serviceId = null
      const url = getLocationUrl(this.mapCode, serviceId) + `/${this.rowId}`
      const res = await this.$axios.$delete(url)
      if (res.isSuccessed) {
        this.$message.success('操作成功')
        this.getMapServiceList()
      } else {
        this.$message.error('删除失败')
      }
    },
    handleOk() {
      const form = this.$refs.addLoc.form
      const dataUrl = this.$refs.addLoc.dataUrl
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const locationInfo = {
          name: values.locationName,
          datasourceName: values.datasourceName,
          datasetName: values.datasetName,
          serviceType: values.serviceType,
          mapConfigCode: this.mapCode,
          layerserviceId: values.layerserviceId,
          locationField: values.locationField,
          locationCode: values.locationCode,
          dataUrl,
        }
        const serviceId = null
        const url = getLocationUrl(this.mapCode, serviceId)
        const result = await this.$axios.$post(url, locationInfo)
        if (result.isSuccessed) {
          this.$message.success('新增成功')
          this.getMapServiceList()
          this.addVisible = false
          form.resetFields()
        } else {
          this.$message.error('新增失败')
        }
      })
    },
    handlePaginationChange(pagination) {
      const pager = { ...this.pagination }
      pager.current = pagination.current
      this.pagination = pager
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
  border-bottom: none !important;
}
.div-range {
  max-height: 80vh;
  overflow-y: auto;
}
</style>

<template>
  <div class="gutter-box">
    <div>
      <a-table
        :columns="columns"
        :row-key="(record) => record.id"
        :data-source="data"
        :pagination="false"
        :show-header="false"
        :loading="loading"
        :custom-row="click"
        :row-class-name="setRowClassName"
        :scroll="{ y: 400 }"
        style="padding: 0 0 0 15px"
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
        <template slot="action" slot-scope="text, record">
          <a href="javascript:;" @click="() => settingSelection(record)">
            <a-icon type="setting" />
          </a>
        </template>
      </a-table>
    </div>
    <gaf-modal
      v-model="addFeature"
      title="新增要素识别"
      :width="800"
      :mask-closable="false"
      @ok="handleOk"
    >
      <new-feature-selection
        ref="addSelection"
        :refresh="newRefresh"
        :map-code="mapCode"
      />
    </gaf-modal>
    <gaf-modal
      v-model="addSpace"
      title="新增空间查询"
      :width="800"
      :mask-closable="false"
      @ok="handleAddSpaceOk"
    >
      <new-space-selection
        ref="addSpaceSel"
        :refresh="newRefresh"
        :map-code="mapCode"
      />
    </gaf-modal>
    <gaf-modal
      v-model="addAttribute"
      title="新增属性查询"
      :width="800"
      :mask-closable="false"
      @ok="handleAddAttributOk"
    >
      <new-attribute-selection
        ref="addAttributeSel"
        :refresh="newRefresh"
        :map-code="mapCode"
      />
    </gaf-modal>
  </div>
</template>

<script>
// @ts-nocheck
import { getSelectionUrl } from '~/utils/GAFMapDataUtils'
import newFeatureSelection from './newFeatureSelection'
import newSpaceSelection from './newSpaceSelection'
import newAttributeSelection from './newAttributeSelection'
const columns = [
  {
    title: 'Name',
    dataIndex: 'name',
    sorter: true,
    scopedSlots: { customRender: 'name' },
  },
  {
    title: 'action',
    dataIndex: 'action',
    sorter: true,
    width: '20px',
    scopedSlots: { customRender: 'action' },
  },
]
export default {
  components: {
    newFeatureSelection,
    newSpaceSelection,
    newAttributeSelection,
  },
  model: {
    prop: 'refresh',
    event: 'change',
  },
  props: {
    type: {
      type: String,
      required: false,
      default: 'Feature',
    },
    addClick: {
      type: Boolean,
    },
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
      pagination: {
        pageSize: 10,
        current: 1,
        total: 10,
        hideOnSinglePage: true,
      },
      loading: false,
      addFeature: false,
      addSpace: false,
      addAttribute: false,
      rowId: '',
      newRefresh: false,
    }
  },
  computed: {
    // divStyle() {
    //   return `height:${this.height - 100}px;overflow-y:auto;overflow-x:hidden;`
    // }
  },
  watch: {
    refresh: {
      handler() {
        this.loadFeatureSelection()
      },
    },
    addClick: {
      handler() {
        this.showAddForm()
      },
    },
    immediate: true,
  },
  created() {
    this.loadFeatureSelection()
  },
  methods: {
    async loadFeatureSelection() {
      const serviceId = null
      const type = this.type
      const url = getSelectionUrl(this.mapCode, serviceId)
      const res = await this.$axios.$get(url + `/selectiontype/${type}`)
      if (res && res.data) {
        this.data = [...res.data]
        this.pagination.total = this.data.length
      }
    },
    click(record) {
      return {
        on: {
          click: () => {
            this.rowId = record.id
            this.$emit('onServiceClick', record)
          },
        },
      }
    },
    setRowClassName(record) {
      return record.id === this.rowId ? 'clickRowStyl' : ''
    },
    showAddForm() {
      if (this.type === 'Feature') {
        this.addFeature = true
      } else if (this.type === 'Space') {
        this.addSpace = true
      } else if (this.type === 'Attribute') {
        this.addAttribute = true
      }
      this.newRefresh = !this.newRefresh
    },
    handleOk() {
      const form = this.$refs.addSelection.form
      const selectedRows = this.$refs.addSelection.selectedRows
      const dataUrl = this.$refs.addSelection.dataUrl
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const selectionInfo = {
          name: values.selectionName,
          subLayerName: values.subLayerName,
          type: 'Feature',
          mapConfigCode: this.mapCode,
          layerserviceId: values.layerserviceId,
          serviceUrl: values.serviceUrl,
          dataUrl,
          fields: selectedRows,
        }
        const serviceId = null
        const url = getSelectionUrl(this.mapCode, serviceId)
        const result = await this.$axios.$post(url, selectionInfo)
        if (result.isSuccessed) {
          this.$message.success('新增成功')
          this.loadFeatureSelection()
          this.addFeature = false
          form.resetFields()
        } else {
          this.$message.error('新增失败')
        }
      })
    },
    handleAddSpaceOk() {
      const form = this.$refs.addSpaceSel.form
      const selectedRows = this.$refs.addSpaceSel.selectedRows
      const titleField = this.$refs.addSpaceSel.titleField
      const style = this.$refs.addSpaceSel.style
      const dataUrl = this.$refs.addSpaceSel.dataUrl
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const selectionInfo = {
          name: values.selectionName,
          subLayerName: values.subLayerName,
          type: 'Space',
          style,
          mapConfigCode: this.mapCode,
          layerserviceId: values.layerserviceId,
          serviceUrl: values.serviceUrl,
          dataUrl,
          fields: selectedRows,
        }
        if (values.style === 'style2') {
          if (selectionInfo.fields && selectionInfo.fields.length) {
            for (let i = 0; i < selectionInfo.fields.length; i++) {
              selectionInfo.fields[i].showType = 'des'
            }
          }
          selectionInfo.fields.push(titleField)
        }
        const serviceId = null
        const url = getSelectionUrl(this.mapCode, serviceId)
        const result = await this.$axios.$post(url, selectionInfo)
        if (result.isSuccessed) {
          this.$message.success('新增成功')
          this.loadFeatureSelection()
          this.addSpace = false
          form.resetFields()
        } else {
          this.$message.error('新增失败')
        }
      })
    },
    handleAddAttributOk() {
      const form = this.$refs.addAttributeSel.form
      const selectedRows = this.$refs.addAttributeSel.selectedRows
      const showFields = this.$refs.addAttributeSel.showFieldRows
      const titleField = this.$refs.addAttributeSel.titleField
      const style = this.$refs.addAttributeSel.style
      const dataUrl = this.$refs.addAttributeSel.dataUrl
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const selectionInfo = {
          name: values.selectionName,
          subLayerName: values.subLayerName,
          type: 'Attribute',
          style,
          mapConfigCode: this.mapCode,
          layerserviceId: values.layerserviceId,
          serviceUrl: values.serviceUrl,
          dataUrl,
          fields: [],
        }
        selectionInfo.fields.push(...selectedRows)
        selectionInfo.fields.push(...showFields)
        selectionInfo.fields.push(titleField)
        const serviceId = null
        const url = getSelectionUrl(this.mapCode, serviceId)
        const result = await this.$axios.$post(url, selectionInfo)
        if (result.isSuccessed) {
          this.$message.success('新增成功')
          this.loadFeatureSelection()
          this.addAttribute = false
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
    settingSelection(row) {
      this.$emit('selectionItemClick', row)
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
  max-height: 70vh;
  overflow-y: auto;
}
</style>

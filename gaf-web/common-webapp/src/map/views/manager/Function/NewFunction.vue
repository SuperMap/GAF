<template>
  <div>
    <a-form layout="horizontal" :form="form">
      <a-row>
        <a-col :span="12">
          <a-form-item
            label="功能名称"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'functionName',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入功能名称!',
                    },
                  ],
                },
              ]"
              size="small"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            label="上级目录"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-select
              v-decorator="[
                'parentId',
                {
                  rules: [
                    {
                      required: true,
                      message: '请选择上级目录!',
                    },
                  ],
                },
              ]"
              size="small"
              allow-clear
              @change="parentChange"
            >
              <a-select-option v-for="item in directoryCatalog" :key="item.id">
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item
            label="顺序"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input-number
              v-decorator="['index']"
              size="small"
              style="width: 100%"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            label="图标"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-col :span="20">
              <a-input v-decorator="['icon']" size="small" />
            </a-col>
            <a-col :span="4">
              <a-button icon="setting" size="small" @click="showIcon = true" />
            </a-col>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-show="false">
        <a-col>
          <a-form-item
            label="功能路径"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input v-decorator="['actionURL']" size="small" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-show="false">
        <a-col>
          <a-form-item
            label="类型"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input v-decorator="['actionOptionType']" size="small" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col>
          <a-tabs default-active-key="RESTDATA" @change="tabsChange">
            <a-tab-pane key="1" tab="上传数据" disabled>敬请期待</a-tab-pane>
            <a-tab-pane key="RESTDATA" tab="数据服务">
              <a-table
                :columns="columns"
                :data-source="data"
                :custom-row="click"
                :row-class-name="setRowClassName"
              />
            </a-tab-pane>
            <a-tab-pane key="RESTAPI" tab="RESTAPI服务">
              <a-row>
                <a-col>
                  <a-form-item
                    label="服务API地址"
                    :label-col="labelCol"
                    :wrapper-col="valueCol"
                  >
                    <a-input size="small" @change="urlChange" />
                  </a-form-item>
                </a-col>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-col>
      </a-row>
    </a-form>
    <system-icon
      :visible="showIcon"
      @selectedIcon="selectedIcon"
      @closeIconSetting="showIcon = false"
    />
  </div>
</template>

<script>
// @ts-nocheck
import { getFunctionUrl, dataUrl } from '~/utils/GAFMapDataUtils'
import systemIcon from '../systemIcon'
const columns = [
  {
    title: '服务名称',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '地址',
    dataIndex: 'address',
    key: 'address',
    scopedSlots: { customRender: 'address' },
  },
]
const data = []
export default {
  components: {
    systemIcon,
  },
  model: {
    prop: 'parentCatalogNode',
    event: 'change',
  },
  props: {
    parentCatalogNode: {
      type: Object,
      default: null,
      required: false,
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
    return {
      showIcon: false,
      labelCol: { span: 5 },
      valueCol: { span: 18 },
      columns,
      data,
      pagination: {
        pageSize: 3,
        current: 1,
        total: 3,
        hideOnSinglePage: true,
      },
      selectedRowKeys: [],
      selectedRows: [],
      directoryCatalog: [],
      serviceList: [],
      parentNode: {},
      catalogIndex: 0,
      layerInfo: null,
      mapLayerInfo: null,
      rowId: '',
    }
  },
  watch: {
    parentCatalogNode: {
      handler(val) {
        this.parentNode = val
      },
      immediate: true,
    },
    refresh: {
      handler() {
        this.loadMapService()
        this.loadDirectoryCatalog()
      },
    },
    immediate: true,
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  created() {
    this.$nextTick(() => {
      this.form.setFieldsValue({
        actionOptionType: 'RESTDATA',
      })
    })
    this.loadMapService()
    this.loadDirectoryCatalog()
  },
  methods: {
    async loadDirectoryCatalog() {
      const url = getFunctionUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      this.directoryCatalog = []
      if (res && res.data) {
        this.setCatalogToDirectory(this.directoryCatalog, res.data)
      }
    },
    async loadMapService() {
      const res = await this.$axios.$get(dataUrl.ServiceInfoUrl, {
        params: {
          ServeSearchParameter: {
            serviceType: 'RESTDATA',
          },
        },
      })
      this.data = res.content
      this.pagination.total = res.total
    },
    // 获取所有目录 2
    setCatalogToDirectory(list, tree) {
      if (tree !== undefined && tree.length > 0) {
        for (let i = 0; i < tree.length; i++) {
          const item = tree[i]
          if (item.type === 'catalog') {
            list.push(item)
            this.setCatalogToDirectory(list, item.children)
          }
        }
      }
    },
    handlePaginationChange(pagination) {
      const pager = { ...this.pagination }
      pager.current = pagination.current
      this.pagination = pager
    },
    click(record) {
      return {
        on: {
          click: () => {
            this.form.setFieldsValue({
              actionURL: record.address,
            })
            this.rowId = record.id
          },
        },
      }
    },
    setRowClassName(record) {
      return record.id === this.rowId ? 'clickRowStyl' : ''
    },
    urlChange(e) {
      const url = e.target.value
      this.form.setFieldsValue({
        actionURL: url,
      })
    },
    tabsChange(activeKey) {
      this.form.setFieldsValue({
        actionOptionType: activeKey,
      })
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    selectedIcon(icon) {
      this.form.setFieldsValue({
        icon,
      })
    },
    parentChange(value) {
      this.form.setFieldsValue({
        parentId: value,
      })
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
  max-width: 95px;
  border-bottom: 0;
  text-align: center !important;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>

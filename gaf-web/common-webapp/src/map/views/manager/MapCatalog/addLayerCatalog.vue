<template>
  <a-layout class="gutter-box">
    <a-layout-sider style="background: #ffffff; margin: 5px; min-width: 350px">
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
        bordered
        :custom-row="click"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          selectedRows: selectedRows,
          onChange: onSelectChange,
        }"
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
        <template slot="serviceType" slot-scope="text">
          <span v-if="text === 'RESTMAP'">地图服务</span>
          <span v-if="text === 'RESTDATA'">数据服务</span>
          <span v-if="text === 'RESTMAP3D'">三维服务</span>
        </template>
      </a-table>
    </a-layout-sider>
    <a-layout-content style="background: #ffffff">
      <a-form layout="horizontal" :form="form">
        <a-form-item
          v-show="false"
          label="地图服务id"
          :label-col="labelCol"
          :wrapper-col="valueCol"
        >
          <a-input v-decorator="['layerServiceId']" size="small" />
        </a-form-item>
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
                    message: '请选择地图类型!',
                  },
                ],
              },
            ]"
            size="small"
            allow-clear
          >
            <a-select-option v-for="item in directoryCatalog" :key="item.id">
              {{ item.catalogName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="顺序" :label-col="labelCol" :wrapper-col="valueCol">
          <a-input-number
            v-decorator="[
              'catalogIndex',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入顺序!',
                  },
                ],
              },
            ]"
            size="small"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item>
          <div v-show="layerInfo !== null">
            <a-row>
              <a-col :offset="1" :span="22">
                <span>
                  图层名：{{ layerInfo === null ? '' : layerInfo.layerName }}
                </span>
              </a-col>
            </a-row>
            <a-row>
              <a-col :offset="1" :span="22">
                <span class="span-line">地址：</span>
                <a
                  :href="layerInfo === null ? '' : layerInfo.address"
                  target="_blank"
                >
                  <span class="span-line">
                    {{ layerInfo === null ? '' : layerInfo.address }}
                  </span>
                </a>
              </a-col>
            </a-row>
            <a-row>
              <a-col :offset="1" :span="22">
                <span>
                  类型：{{ layerInfo === null ? '' : layerInfo.serviceType }}
                </span>
              </a-col>
            </a-row>
          </div>
        </a-form-item>
      </a-form>
    </a-layout-content>
  </a-layout>
</template>

<script>
// @ts-nocheck
import { dataUrl, getLayerCatalogUrl } from '~/utils/GAFMapDataUtils'
const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '类型',
    dataIndex: 'serviceType',
    key: 'serviceType',
    scopedSlots: { customRender: 'serviceType' },
  },
]
export default {
  name: 'AddLayerCatalogModal',
  model: {
    prop: 'parentCatalogNode',
    event: 'change',
  },
  props: {
    parentCatalogNode: {
      type: Object,
      default: null,
      required: true,
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
      labelCol: { span: 5 },
      valueCol: { span: 18 },
      columns,
      pagination: {
        pageSize: 8,
        pageIndex: 1,
        total: 8,
        hideOnSinglePage: true,
      },
      selectedRowKeys: [],
      selectedRows: [],
      directoryCatalog: [],
      serviceList: [],
      parentNode: {},
      parentId: '',
      catalogIndex: 0,
      layerInfo: null,
    }
  },
  watch: {
    parentCatalogNode: {
      handler(val) {
        this.parentNode = val
        if (this.parentNode.$options) {
          const parentId = this.parentNode.$options.propsData.dataRef.slots.id
          this.$nextTick(function () {
            this.form.setFieldsValue({
              parentId,
            })
          })
          this.loadMaxIndex()
        }
      },
      immediate: true,
    },
    refresh: {
      handler() {
        Object.assign(this.$data, this.$options.data())
        this.loadDirectoryCatalog()
        this.loadMapService()
      },
    },
    immediate: true,
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  created() {
    this.loadDirectoryCatalog()
    this.loadMapService()
  },
  methods: {
    async loadDirectoryCatalog() {
      const url = getLayerCatalogUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      this.directoryCatalog = []
      this.setCatalogToDirectory(this.directoryCatalog, res.data)
      const parentValue = this.form.getFieldValue('parentId')
      if (
        (parentValue === '' || parentValue === undefined) &&
        this.directoryCatalog.length
      ) {
        this.form.setFieldsValue({
          parentId: this.directoryCatalog[0].id,
        })
      }
    },
    async loadMapService() {
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
    loadMaxIndex() {
      let maxIndex = 0
      if (this.parentNode.$options.propsData.dataRef.children) {
        const childrens = this.parentNode.$options.propsData.dataRef.children
        for (let i = 0; i < childrens.length; i++) {
          const nIndex = childrens[i].slots.catalogIndex
          if (nIndex >= maxIndex) {
            maxIndex = nIndex + 1
          }
        }
      }
      this.$nextTick(function () {
        this.form.setFieldsValue({
          catalogIndex: maxIndex,
        })
      })
    },
    // 获取所有目录 2
    setCatalogToDirectory(list, tree) {
      if (tree !== undefined && tree.length > 0) {
        for (let i = 0; i < tree.length; i++) {
          const item = tree[i]
          if (item.slots.catalogType === 'directory') {
            list.push(item.slots)
            this.setCatalogToDirectory(list, item.children)
          }
        }
      }
    },
    handlePaginationChange(pagination) {
      const pager = { ...this.pagination }
      pager.pageIndex = pagination.current
      this.pagination = pager
      this.loadMapService()
    },
    click(record) {
      return {
        on: {
          click: () => {
            this.form.setFieldsValue({
              catalogName: record.layerName,
              serviceName: record.name,
              layerServiceId: record.id,
            })
            this.layerInfo = record
          },
        },
      }
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
  },
}
</script>

<style scoped>
.gutter-box >>> .ant-table-tbody > tr > td {
  max-width: 95px;
  border-bottom: 0;
  text-align: center !important;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.search-style {
  width: 100%;
  padding: 0 0 10px;
}
</style>

<template>
  <div>
    <div>
      <a-modal
        v-model="visible"
        title="已关联的服务"
        :mask="true"
        :width="950"
        :centered="true"
        :footer="null"
        destroy-on-close
      >
        <div>
          <button
            class="btn-fun blue"
            style="float: left;margin-top: -8px"
            @click="onClick"
            >
            <span><a-icon type="link" />&nbsp;&nbsp;关联</span>
            </button
          >
        </div>
        <linked-data-service-list
          v-if="visible"
          ref="linkedList"
          :webgis-service-id="webgisService.gisServiceId"
          :webgis-service-types="webgisServiceTypes"
        ></linked-data-service-list>
        <!-- <template slot="footer">

        </template> -->
      </a-modal>
    </div>
    <div>
      <a-modal
        v-model="selectVisible"
        title="选择服务"
        :mask="false"
        :width="1000"
        :centered="true"
        :confirm-loading="confirmLoading"
        destroy-on-close
        @ok="onOk"
      >
        <muti-select-data-service-list
          v-if="selectVisible"
          :webgis-service-id="webgisService.gisServiceId"
          :webgis-service-types="webgisServiceTypes"
          @onSelectedChange="onSelectedChange"
        ></muti-select-data-service-list>
      </a-modal>
    </div>
  </div>
</template>
<script>
    import LinkedDataServiceList from './LinkedDataServiceList.vue'
    import MutiSelectDataServiceList from './MutiSelectDataServiceList.vue'

    export default {
  components: { LinkedDataServiceList, MutiSelectDataServiceList },
  model: {
    prop: 'linkedVisible',
    event: 'change',
  },
  props: {
    linkedVisible: {
      type: Boolean,
      default: false,
    },
    webgisService: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      // 已关联的服务模态框是否可见
      visible: false,
      // 选择服务的模态框是否可见
      selectVisible: false,
      // 缓存选择的服务的id
      tmpSelectedRowKeys: [],
      confirmLoading: false,
      // 服务类型Map
      webgisServiceTypes: new Map(),
      options: [],
    }
  },
  watch: {
    visible(newValue) {
      this.$emit('change', newValue)
    },
    linkedVisible(newValue) {
      this.visible = newValue
    },
    selectVisible(newValue) {
      if (!newValue) {
        this.tmpSelectedRowKeys = []
      }
    },
  },
  created() {
    this.getWebgisServiceTypes()
  },
  methods: {
    // 点击选择服务时
    onClick() {
      this.selectVisible = true
    },
    // 返回
    handleCancel() {
      this.visible = false
    },
    // 当选择的服务改变
    onSelectedChange(selectedRowKeys) {
      this.tmpSelectedRowKeys = selectedRowKeys
    },
    // 批量新增关联关系
    async onOk() {
      if (this.tmpSelectedRowKeys.length > 0) {
        const copy = [...this.tmpSelectedRowKeys]
        const data = copy.map((item) => {
          return {
            gisDataServiceId: item,
            gisServiceId: this.webgisService.gisServiceId,
          }
        }, this)
        const url = '/map/webgis-service-associations/batch'
        this.confirmLoading = true
        const res = await this.$axios.$post(url, data)
        if (res.isSuccessed) {
          // 刷新
          this.selectVisible = false
          // 清空缓存
          this.$refs.linkedList.getLinkedServiceList()
        } else {
          this.$message.error(`关联失败,原因:${res.message}`)
        }
        this.confirmLoading = false
      } else {
        this.selectVisible = false
        // 清空缓存
      }
    },
    async getWebgisServiceTypes() {
      const url = `/sys-mgt/sys-dicts/ServiceType/tree`
      const res = await this.$axios.$get(url)
      const allOptions = [{ value: '-1', label: '所有服务类型' }]
      if (res.isSuccessed) {
        const typeList = res.data
        if (typeList && typeList.length > 0) {
          const map = new Map()
          typeList.forEach((element) => {
            map.set(element.value, element.label)
          })
          this.webgisServiceTypes = map
          allOptions.push(
            ...typeList.map((item) => {
              return item
            })
          )
        }
      } else {
        this.$message.error('加载服务类型失败,原因：' + res.message)
      }
      this.options = allOptions
    },
  },
}
</script>

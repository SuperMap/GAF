<template>
  <div>
    <a-row>
      <a-col :span="12">
        <span style="font-size: large">功能目录</span>
      </a-col>
      <a-col :span="3">
        <a-tooltip placement="topLeft" title="新增目录">
          <a-icon
            type="folder-add"
            style="margin: 5px"
            @click="addCatalogVisible = true"
          />
        </a-tooltip>
      </a-col>
      <a-col :span="3">
        <a-tooltip placement="topLeft" title="新增功能">
          <a-icon type="plus" style="margin: 5px" @click="addFunctionClick" />
        </a-tooltip>
      </a-col>
      <a-col :span="3">
        <a-tooltip placement="topLeft" title="删除">
          <a-popconfirm
            title="是否删除？"
            ok-text="是"
            cancel-text="否"
            @confirm="() => deleteCatalogNode()"
          >
            <a href="javascript:;">
              <a-icon type="delete" style="margin: 5px" />
            </a>
          </a-popconfirm>
        </a-tooltip>
      </a-col>
      <a-col :span="3">
        <a-tooltip placement="topLeft" title="设置样式">
          <a-icon
            type="setting"
            style="margin: 5px"
            @click="editFunctionStyle"
          />
        </a-tooltip>
      </a-col>
    </a-row>
    <a-divider style="margin: 10px" />
    <div :style="divStyle">
      <a-menu mode="inline" @click="onMenuClick">
        <template v-for="item in functionData">
          <a-menu-item v-if="!item.children.length" :key="item.id">
            <a-icon :type="item.icon" />
            <span>{{ item.name }}</span>
          </a-menu-item>
          <a-sub-menu v-else :key="item.id" @titleClick="onMenuClick">
            <span slot="title">
              <a-icon :type="item.icon" />
              {{ item.name }}
            </span>
            <template v-for="subItem in item.children">
              <a-menu-item :key="subItem.id">
                <a-icon :type="subItem.icon" />
                <span>{{ subItem.name }}</span>
              </a-menu-item>
            </template>
          </a-sub-menu>
        </template>
      </a-menu>
    </div>
    <div>
      <gaf-modal
        title="新增目录"
        :visible="addCatalogVisible"
        @cancel="addCatalogVisible = false"
        @ok="addCatalogOk"
      >
        <new-function-catalog ref="addModal" :map-code="mapCode" />
      </gaf-modal>
      <gaf-modal
        title="新增功能"
        :visible="addLayerVisible"
        :width="800"
        :hight="600"
        @cancel="addLayerVisible = false"
        @ok="addLeafOk"
      >
        <new-function
          ref="addLayerModal"
          :refresh="addRefresh"
          :map-code="mapCode"
        />
      </gaf-modal>
    </div>
  </div>
</template>
<script>
// @ts-nocheck
import { getFunctionUrl } from '~/utils/GAFMapDataUtils'
import NewFunctionCatalog from './NewFunctionCatalog'
import NewFunction from './NewFunction'
export default {
  name: 'FunctionCatalog',
  components: {
    NewFunctionCatalog,
    NewFunction,
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
      addLayerVisible: false,
      addCatalogVisible: false,
      functionData: [],
      selectedKey: '',
      addRefresh: false,
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
        this.loadFunctionCatalog()
      },
    },
    immediate: true,
  },
  created() {
    this.loadFunctionCatalog()
  },
  methods: {
    async loadFunctionCatalog() {
      const url = getFunctionUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      if (res && res.data) {
        this.functionData = res.data
      }
    },
    // 添加目录
    addCatalog() {
      this.addCatalogVisible = true
    },
    addLayerCatalog() {
      this.addLayerVisible = true
    },
    addCatalogOk() {
      const form = this.$refs.addModal.form
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        if (!values.parentId) {
          values.parentId = '0'
        }
        const mapFunction = {
          name: values.catalogName,
          parentId: values.parentId,
          icon: values.icon,
          type: 'catalog',
          mapConfigCode: this.mapCode,
        }
        const url = getFunctionUrl(this.mapCode)
        const result = await this.$axios.$post(url, mapFunction)
        if (result.isSuccessed) {
          this.$message.success('添加成功')
          this.loadFunctionCatalog()
          this.addCatalogVisible = false
          form.resetFields()
        } else {
          this.$message.error(result.message)
        }
      })
    },
    addLeafOk() {
      const form = this.$refs.addLayerModal.form
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        if (!values.parentId) {
          values.parentId = '0'
        }
        const mapFunction = {
          name: values.functionName,
          parentId: values.parentId,
          index: values.index,
          icon: values.icon,
          actionURL: values.actionURL,
          actionOptionType: values.actionOptionType,
          type: 'leaf',
          mapConfigCode: this.mapCode,
        }
        const url = getFunctionUrl(this.mapCode)
        const result = await this.$axios.$post(url, mapFunction)
        if (result.isSuccessed) {
          this.$message.success('添加成功')
          this.loadFunctionCatalog()
          this.addLayerVisible = false
          const array = ['functionName', 'parentId', 'index', 'icon']
          form.resetFields(array)
        } else {
          this.$message.error(result.message)
        }
      })
    },
    onMenuClick(item) {
      const key = item.key
      this.selectedKey = key
      this.$emit('onMenuClick', item)
    },
    async deleteCatalogNode() {
      const url = getFunctionUrl(this.mapCode)
      const res = await this.$axios.$delete(url + `/${this.selectedKey}`)
      if (res.isSuccessed) {
        this.loadFunctionCatalog()
        this.$message.success('删除成功')
      } else {
        this.$message.error('删除失败')
      }
    },
    editFunctionStyle() {
      this.$emit('editFunctionStyle')
    },
    addFunctionClick() {
      this.addLayerVisible = true
      this.addRefresh = !this.addRefresh
    },
  },
}
</script>

<style scoped>
.div-range {
  max-height: 80vh;
  overflow-y: auto;
}
</style>

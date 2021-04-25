<template>
  <div class="treeList">
    <div v-if="allDataList.length || dataList.length" class="layer-list">
      <div v-show="visible" class="layer-list-box">
        <gaf-map-layer-tree
          :all-checkable="true"
          :leafnode-checkable="false"
          :some-node-checkable="false"
          :replace-fields="replaceFields"
          :data-list="allDataList"
          :search-input-show="true"
          :opacity-control="true"
          :location-control="true"
          :checked-layer-keys="checkedKeys"
          :check="onTreeNodeChecked"
          :select="onSelect"
          :expanded-keys="defaultExpandedKeys"
          icon-class-name="icon-butoumingdu2"
        />
      </div>
      <div class="layer-list-btn" @click="handleListBtn">
        <span>
          <template v-if="visible">
            <i class="iconfont icon-arr-up-copy"></i> 收回图层列表
          </template>
          <template v-else>
            <i class="iconfont icon-arr-left-copy"></i>展开图层列表
          </template>
          <!-- <div class="customLayer" @click.stop="handleCustomLayer">
            <i class="iconfont icon-jiahao"></i>
          </div> -->
        </span>
      </div>
    </div>
    <gaf-map-draggable
      :visible="customLayerVisible"
      :width="460"
      class="customLayerDraggable"
      title="自定义图层"
      @cancel="handleCustomLayerClose"
    >
      <div class="optionItem">
        <p class="title">服务名称:</p>
        <div class="content">
          <input v-model="customLayerName" placeholder="请输入服务名称" />
        </div>
      </div>
      <div class="optionItem">
        <p class="title">服务地址:</p>
        <div class="content">
          <input
            v-model="customLayerUrl"
            :placeholder="urlPlaceholder"
            type="url"
            @change="handleUrlChange"
          />
        </div>
      </div>
      <div class="optionItem">
        <p class="title">服务类型:</p>
        <div class="content">
          <a-radio-group v-model="serverType" :options="typeOptions" />
        </div>
      </div>
      <div class="optionItem">
        <p class="title">挂载目录:</p>
        <div class="content">
          <a-tree-select
            v-model="mountNodeValue"
            :dropdown-style="{ maxHeight: '200px', overflow: 'auto' }"
            :tree-data="treeData"
            :replace-fields="replaceFields"
            placeholder="请选择目录"
            style="width: 100%"
            tree-default-expand-all
          >
          </a-tree-select>
        </div>
      </div>
      <div class="setDiv">
        <button class="setButton" @click="setCustomLayer">添加</button>
      </div>
    </gaf-map-draggable>
  </div>
</template>

<script>
import { bus } from '~/utils/cimBus'
const recursion = (array, id, pid, pidv) => {
  if (!Array.isArray(array)) {
    return array
  }
  const data = array.map((a) => ({ ...a }))
  const tempArr = data.filter((a) => a[pid] === pidv)
  tempArr.forEach((a) => {
    a.children = recursion(data, id, pid, a[id])
    a.children = a.children.length > 0 ? a.children : null
    return a
  })
  return tempArr.sort((a, b) => a.order - b.order)
}
export default {
  name: 'LayerList',
  props: {
    dataList: {
      type: Array,
      default: () => [],
    },
    visible: {
      type: Boolean,
      default: () => false,
    },
    checkedLayerKeys: {
      type: Array,
      default: () => [],
    },
    defaultExpandedKeys: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      checkedKeys: this.checkedLayerKeys || [],
      replaceFields: {
        title: 'resourceName',
        key: 'resourceId',
        value: 'resourceId',
      },
      allDataList: [...this.dataList],
      selectedKey: '',
      layerList: [], // 选中的图层
      customLayerVisible: false, // 自定义弹窗弹窗控制
      selectedItem: null, // 选中控制透明度的图层
      mountNodeValue: undefined, // 挂载节点的值
      customLayerName: '', // 自定义图层名称
      customLayerUrl: '', // 自定义图层地址
      urlPlaceholder:
        'http://{host}/iserver/services/map-jingjin/rest/maps/京津地区地图',
      serverType: '二维图层',
      typeOptions: ['二维图层', '三维图层'],
      legendKey: '',
    }
  },
  computed: {
    treeData() {
      const datasource = this.allDataList
        .filter((item) => {
          return !item.resourceLocation
        })
        .map((item) => {
          item.scopedSlots = { title: 'title' }
          return item
        })
      return recursion(datasource, 'resourceId', 'pid', '')
    },
  },
  watch: {
    serverType(value) {
      if (value === '二维图层') {
        this.urlPlaceholder =
          'http://{host}/iserver/services/map-jingjin/rest/maps/京津地区地图'
      } else {
        this.urlPlaceholder =
          'http://{host}/iserver/services/3D-cim_gis/rest/realspace/datas/WDGC'
      }
    },
    customLayerName(val) {
      this.customLayerName = decodeURIComponent(val)
    },
    customLayerUrl(val) {
      this.customLayerUrl = decodeURIComponent(val)
    },
    checkedKeys(val) {
      this.checkedKeysToLayers(val)
    },
  },
  beforeMount() {
   bus.$on('LayerTreeShow', (val) => {
      this.checkedKeys = val
    })
  },
  mounted() {
    this.checkedKeysToLayers(this.checkedLayerKeys)
  },
  methods: {
    checkedKeysToLayers(keys) {
      if (keys.length) {
        window.checkedLayerKeys = keys
        const layerList = []
        keys.forEach((key) => {
          const data = this.allDataList.find((item) => {
            return item.resourceId.toString() === key.toString()
          })
          if (data) {
            layerList.push(data)
          }
        })
        this.layerList = layerList
       bus.$emit('onCheckedLayerList', layerList)
      }
    },
    handleUrlChange() {
      // this.customLayerUrl = decodeURIComponent(this.customLayerUrl)
    },
    setCustomLayer() {
      const mountNodeValue = this.mountNodeValue
      const customLayerUrl = this.customLayerUrl
      const customLayerName = this.customLayerName
      const serverType = this.serverType
      const strRegex = '^((https|http|ftp|rtsp|mms)?://)'
      const re = new RegExp(strRegex)
      const result = re.test(customLayerUrl)
      const contain =
        customLayerUrl.includes('/rest/realspace/datas/') ||
        customLayerUrl.includes('/rest/maps/')
      if (!customLayerName) {
        this.$message.warning('服务名称不能为空')
        return
      }
      if (!customLayerUrl) {
        this.$message.warning('服务地址不能为空')
        return
      }
      if (!(result && contain)) {
        this.$message.warning('服务地址无效')
        return
      }
      if (!customLayerUrl) {
        this.$message.warning(' 挂载目录不能为空，请选择有效的目录')
        return
      }
      const info = {
        sourceType: 'SUPERMAP',
        sourceTypeName: 'SuperMap',
        opacity: 0,
        pid: '',
      }
      info.resourceId = this.getResourceId()
      info.pid = mountNodeValue || ''
      info.resourceName =
        customLayerName ||
        customLayerUrl.substring(customLayerUrl.lastIndexOf('/') + 1)
      info.resourceLocation = customLayerUrl
      if (serverType === '二维图层') {
        info.resourceTagCN = '地图服务'
        info.resourceTag = 'RESTMAP'
      } else {
        info.resourceTagCN = '三维服务'
        info.resourceTag = 'RESTREALSPACE'
      }
      this.allDataList = [...this.allDataList, info]
      this.customLayerVisible = false
      this.resetCustomLayer()
    },
    resetCustomLayer() {
      this.customLayerUrl = ''
      this.serverType = '二维图层'
      this.mountNodeValue = undefined
    },
    getResourceId() {
      const data = this.allDataList
      let id = 0
      data.forEach((item) => {
        if (item.resourceId > id) id = item.resourceId
      })
      return id + 1
    },
    handleCustomLayer() {
      this.customLayerVisible = true
    },
    handleCustomLayerClose() {
      this.customLayerVisible = false
    },
    handleListBtn() {
      if (this.visible) {
        this.layerOpacityVisible = false
      }
      this.$emit('onLayerListToggle', !this.visible)
      if (bus) {
       bus.$emit('onLayerListToggle', !this.visible)
      }
    },
    onTreeNodeChecked(checkedKeys, info) {
      window.checkedLayerKeys = checkedKeys
      const self = this
      self.layerOpacityVisible = false
      const layerList = []
      let flag = false
      checkedKeys.forEach((key) => {
        const data = self.allDataList.find((item) => {
          return item.resourceId.toString() === key.toString()
        })
        if (data) {
          layerList.push(data)
        }
        if (info.checked) {
          if (data.legendData) {
            self.$store.commit('setlegendList', data.legendData)
            self.legendKey = key
            flag = true
          }
        } else if (key === self.legendKey) {
          flag = true
        }
      })
      if (!flag) {
        this.$store.commit('setlegendList', [])
      }
      self.layerList = layerList
      bus.$emit('onCheckedLayerList', layerList)
    },
    onSelect(selectedKeys) {
      if (selectedKeys[0]) {
        this.selectedKey = selectedKeys[0]
      }
      let currentNode
      if (this.selectedKey) {
        currentNode = this.layerList.find(
          (layer) => layer.resourceId === this.selectedKey
        )
        if (currentNode) {
          window.SMWEBGIS = window.SMWEBGIS || {}
          window.SMWEBGIS.currentLocateService = currentNode
        }
      }
      // const layers = window.SMWEBGIS.TreeLayers[this.selectedKey]
      // console.log('layers', layers)
      // if (Array.isArray(layers)) {
      //   let rectangA = {}
      //   let rectangB = {}
      //   const result = {}
      //   rectangA = layers[0].layerBounds || layers[0].rectangle
      //   layers.forEach(layer => {
      //     rectangB = layer.layerBounds || layer.rectangle
      //     window.Cesium.Rectangle.union(rectangA, rectangB, result)
      //   })
      //   window.scene.camera.flyTo({
      //     destination: result
      //   })
      // } else {
      //   if (currentNode && currentNode.cameraCoordinate) {
      //     const toolMethods = this.$mapActions
      //     toolMethods.flyTo(currentNode.cameraCoordinate)
      //     return
      //   } else if (layers.layerBounds || layers.rectangle) {
      //     window.viewer.flyTo(layers)
      //     return
      //   }
      //   window.scene.camera.flyTo({ destination: layers })
      // }
    },
  },
}
</script>
<style lang="less" scope>
#treeComponent .ant-input {
  color: var(--font-color, #fff);
  background-color: transparent;
}
input::-webkit-input-placeholder {
  color: var(--font-color, #fff);
}
.layerList {
  .layerListBtn {
    width: 100%;
    height: 36px;
    // background: url(../../../../static/img/nav.png) center 0 no-repeat;
    border: 1px solid transparent;

    .title {
      cursor: pointer;
      width: 100%;
      height: 100%;
      font-size: 15px;
      line-height: 32px;
      color: var(--active-color, #0ffaff);
      text-align: center;
    }
  }
}
.layer-list {
  position: absolute;
  top: 10%;
  left: auto !important;
  right: 20px;
  width: 200px;
  background: var(--bg-color, rgba(25, 40, 58, 0.3));
  z-index: 100;
  padding-bottom: 36px;
  max-height: calc(100vh - 100px);
  .left {
    top: 0px;
    left: -280px;
    margin-left: 0px;
    .title {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .draggable .content {
    padding: 2px 0 10px;
  }
}
.layer-list:before {
  content: '';
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  width: 10px;
  height: 10px;
  border: 1px var(--active-color, #0ffaff) solid;
  border-bottom: 0;
  border-right: 0;
  z-index: 1000;
}
.layer-list:after {
  content: '';
  display: block;
  position: absolute;
  top: 0;
  right: 0;
  width: 10px;
  height: 10px;
  border: 1px var(--active-color, #0ffaff) solid;
  border-bottom: 0;
  border-left: 0;
  z-index: 1000;
}
.layer-list-box {
  width: 100%;
  padding: 15px;
  color: var(--font-color, #fff);
  border: 1px solid transparent;
  border-bottom: 0;
  position: relative;
  overflow-x: hidden;
  overflow-y: auto;
}
.layer-list-btn {
  width: 100%;
  height: 36px;
  // background: url(../../static/img/nav/nav.png) center 0 no-repeat;
  background: linear-gradient(
    to top,
    var(--active-bg-color, rgba(0, 250, 255, 0.1)),
    transparent
  );
  border: 1px solid transparent;
  position: absolute;
  bottom: 0;
  left: 0;
  z-index: 1000000000;
  .customLayer {
    display: inline-block;
  }
}
.layer-list-btn span {
  float: left;
  width: 100%;
  height: 100%;
  font-size: 15px;
  line-height: 32px;
  color: var(--active-color, #0ffaff);
  text-align: center;
  cursor: pointer;
}
.layer-list-btn span i.iconfont {
  position: relative;
  top: 2px;
  margin-right: 2px;
  font-size: 18px;
}
.searchInput {
  background: transparent;
  .ant-input {
    color: var(--font-color, #d9d9d9);
    background: transparent;
    border: 1px solid #545c64;
  }
  .ant-input:hover {
    border: 1px solid var(--font-color, #d9d9d9) !important;
  }

  .ant-input:focus {
    border: 1px solid var(--font-color, #d9d9d9) !important;
  }
}
.treeList {
  #draggable {
    top: 10%;
    right: 300px;
    left: unset;
  }
  .layerOpacityDraggable,
  .left {
    position: fixed;
    .left {
      top: 20px;
      right: 300px;
    }
    .title {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}

.customLayerDraggable {
  position: fixed;
  top: 20px;
  left: 100px;
  input,
  textarea,
  select,
  button {
    outline: none;
  }
  input {
    background: transparent;
    border: 1px solid var(--font-color, #fff);
    border-radius: 4px;
    width: 100%;
    padding: 4px;
  }
  input:hover,
  input:focus {
    border: 1px solid var(--hover-color, #0ffaff);
    border-radius: 4px;
  }
  .optionItem {
    width: 100%;
    margin-bottom: 20px;
    color: var(--font-color, #fff);
    line-height: 24px;
    .customizePlan {
      display: flex;
      flex-direction: row;
      .bgColor {
        flex: 1;
      }
      .boderColor {
        flex: 1;
      }
    }
    .title {
      width: 100%;
      height: 30px;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    }
    .content {
      margin-top: 15px;
      .select {
        width: 50%;
      }
    }
  }
  .setDiv {
    overflow: hidden;
    .setButton {
      border: none;
      float: right;
      width: 100%;
      display: block;
      height: 28px;
      background: #1fb17a;
      color: #fff;
      line-height: 28px;
      font-size: 13px;
      text-align: center;
      width: 75px;
    }
  }
}

.ant-select-focused .ant-select-selection,
.ant-select-selection:focus,
.ant-select-selection:hover,
.ant-select-selection:active {
  border: 1px solid var(--active-color, rgba(0, 250, 255, 0.5));
}
.ant-select-tree li .ant-select-tree-node-content-wrapper {
  color: var(--font-color, #fff);
}
.ant-select-tree li .ant-select-tree-node-content-wrapper:hover {
  color: #fff;
  background: var(--active-bg-color, rgba(0, 250, 255, 0.5));
}
.ant-select-tree
  li
  .ant-select-tree-node-content-wrapper.ant-select-tree-node-selected {
  color: var(--font-color, #fff);
  background: var(--active-bg-color, rgba(0, 250, 255, 0.5));
}
.ant-popover-placement-top > .ant-popover-content > .ant-popover-arrow,
.ant-popover-placement-topLeft > .ant-popover-content > .ant-popover-arrow,
.ant-popover-placement-topRight > .ant-popover-content > .ant-popover-arrow {
  border-top-color: transparent;
  border-right-color: rgba(25, 40, 58, 0.9);
  border-bottom-color: rgba(25, 40, 58, 0.9);
  border-left-color: transparent;
}
.ant-popover-inner {
  background: var(--bg-color, rgba(25, 40, 58, 0.9));
  .ant-popover-title {
    color: var(--font-color, #fff);
  }
}
.ant-tooltip-open {
  z-index: 10000;
}
li.ant-tree-treenode-disabled > span:not(.ant-tree-switcher),
li.ant-tree-treenode-disabled > .ant-tree-node-content-wrapper,
li.ant-tree-treenode-disabled > .ant-tree-node-content-wrapper span {
  color: #81868a;
  cursor: not-allowed;
}
.ant-tree-checkbox-disabled .ant-tree-checkbox-inner {
  // background-color: #81868a !important;
  border-color: #81868a !important;
}
</style>

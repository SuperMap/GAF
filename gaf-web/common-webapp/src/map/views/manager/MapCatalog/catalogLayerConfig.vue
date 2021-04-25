<template>
  <div class="gutter-box">
    <div>
      <a-divider orientation="left" style="margin: 0px">设置</a-divider>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>名称：</span>
        </a-col>
        <a-col :span="16">
          <a-input v-model="catalogInfo.catalogName" size="small" />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>上级目录：</span>
        </a-col>
        <a-col :span="16">
          <a-select
            v-model="catalogInfo.parentId"
            allow-clear
            style="width: 100%"
            size="small"
          >
            <a-select-option v-for="item in directoryCatalog" :key="item.id">
              {{ item.catalogName }}
            </a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>顺序：</span>
        </a-col>
        <a-col :span="16">
          <a-input-number
            v-model="catalogInfo.catalogIndex"
            size="small"
            style="width: 100%"
          />
        </a-col>
      </a-row>
    </div>
    <div v-show="catalogInfo.catalogType === 'layer'">
      <a-row>
        <a-col :span="9">
          <span>是否基础图层：</span>
        </a-col>
        <a-col>
          <a-switch v-model="catalogInfo.isBaseLayer" size="small">
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-col>
      </a-row>
    </div>
    <div v-show="catalogInfo.catalogType === 'layer'">
      <a-divider orientation="left">图层信息</a-divider>
      <a-row>
        <a-col>
          <span>
            图层名：{{
              catalogInfo.layerInfo === null
                ? ''
                : catalogInfo.layerInfo.layerName
            }}
          </span>
        </a-col>
      </a-row>
      <a-row>
        <a-col>
          <span>地址：</span>
          <a
            :href="
              catalogInfo.layerInfo === null
                ? ''
                : catalogInfo.layerInfo.address
            "
            target="_blank"
          >
            <span class="span-line">
              {{
                catalogInfo.layerInfo === null
                  ? ''
                  : catalogInfo.layerInfo.address
              }}
            </span>
          </a>
        </a-col>
      </a-row>
      <a-row>
        <a-col>
          <span v-if="catalogInfo.layerInfo === null"></span>
          <span v-else-if="catalogInfo.layerInfo.serviceType === 'RESTMAP'">
            类型：地图服务
          </span>
          <span v-else-if="catalogInfo.layerInfo.serviceType === 'RESTDATA'">
            类型：数据服务
          </span>
          <span v-else-if="catalogInfo.layerInfo.serviceType === 'RESTMAP3D'">
            类型：三维服务
          </span>
        </a-col>
      </a-row>
      <div v-show="mapLayerInfo !== null">
        <a-row>
          <a-col>
            中心点：{{ mapLayerInfo !== null ? mapLayerInfo.center : '' }}
          </a-col>
        </a-row>
        <a-row>
          <a-col> 地图范围： </a-col>
        </a-row>
        <a-row>
          <a-col :offset="2">
            左：{{ mapLayerInfo !== null ? mapLayerInfo.left : '' }}
          </a-col>
        </a-row>
        <a-row>
          <a-col :offset="2">
            上：{{ mapLayerInfo !== null ? mapLayerInfo.top : '' }}
          </a-col>
        </a-row>
        <a-row>
          <a-col :offset="2">
            右：{{ mapLayerInfo !== null ? mapLayerInfo.right : '' }}
          </a-col>
        </a-row>
        <a-row>
          <a-col :offset="2">
            下：{{ mapLayerInfo !== null ? mapLayerInfo.bottom : '' }}
          </a-col>
        </a-row>
        <a-row>
          <a-col>
            投影信息：{{
              mapLayerInfo !== null ? mapLayerInfo.prjcoordsys : ''
            }}
          </a-col>
        </a-row>
        <a-row>
          <a-col>
            比例尺：{{ mapLayerInfo !== null ? mapLayerInfo.scale : '' }}
          </a-col>
        </a-row>
      </div>
    </div>
    <div
      :style="{
        position: 'fixed',
        bottom: 0,
        padding: '10px 16px',
        textAlign: 'right',
        right: 0,
        background: 'rgba(0, 0, 0, 0.0)',
        borderRadius: '0 0 4px 4px',
      }"
    >
      <a-button type="primary" size="small" @click="onSubmit"> 确定 </a-button>
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import { getLayerCatalogUrl } from '~/utils/GAFMapDataUtils'
export default {
  components: {},
  model: {
    prop: 'layerCatalog',
    event: 'change',
  },
  props: {
    layerCatalog: {
      type: Object,
      required: true,
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
      parentCatalogName: '',
      directoryCatalog: [],
      catalogInfo: {
        id: '',
        parentId: '',
        catalogType: '',
        layerServiceId: '',
        catalogName: '',
        catalogIndex: 0,
        isBaseLayer: false,
        layerInfo: {},
      },
      mapLayerInfo: {},
    }
  },
  watch: {
    layerCatalog(val) {
      this.catalogInfo = val
      const pId = val.parentId
      if (pId !== undefined && pId !== null && pId.length > 0) {
        this.setParentCatalogName(val.parentId)
      } else {
        this.parentCatalogName = ''
      }
      this.mapLayerInfo = null
      if (val.catalogType === 'layer' && val.layerInfo) {
        const url = val.layerInfo.address
        if (url.includes('/maps/')) {
          this.getLayerServiceInfo(url)
        }
      }
      this.getDirectoryCatalog()
    },
  },
  methods: {
    // 获取所有目录 1
    async getDirectoryCatalog() {
      const url = getLayerCatalogUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      this.directoryCatalog = []
      this.setCatalogToDirectory(this.directoryCatalog, res.data)
    },
    // 获取父节点名称
    async setParentCatalogName(val) {
      const url = getLayerCatalogUrl(this.mapCode)
      const res = await this.$axios.$get(url + `/${val}`)
      this.parentCatalogName = res.data.catalogName
    },
    // 获取地图信息
    async getLayerServiceInfo(url) {
      const mapLayerJsonUrl = url + '.jsonp'
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson) {
        this.mapLayerInfo = {}
        if (mapLayerJson.center) {
          this.mapLayerInfo.center =
            '(' + mapLayerJson.center.x + ',' + mapLayerJson.center.y + ')'
        }
        if (mapLayerJson.bounds) {
          this.mapLayerInfo.left = mapLayerJson.bounds.left
          this.mapLayerInfo.bottom = mapLayerJson.bounds.bottom
          this.mapLayerInfo.right = mapLayerJson.bounds.right
          this.mapLayerInfo.top = mapLayerJson.bounds.top
        }
        if (mapLayerJson.prjCoordSys) {
          this.mapLayerInfo.prjcoordsys = mapLayerJson.prjCoordSys.name
        }
        if (mapLayerJson.scale) {
          this.mapLayerInfo.scale = mapLayerJson.scale.toFixed(10)
        }
      }
    },
    updateCatalogInfo() {
      const url = getLayerCatalogUrl(this.mapCode)
      this.$axios.$put(url, this.catalogInfo)
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
    async onSubmit() {
      const url = getLayerCatalogUrl(this.mapCode)
      const res = await this.$axios.$put(url, this.catalogInfo)
      if (res.isSuccessed) {
        this.$message.success('编辑成功！')
        this.$emit('refresh')
      } else {
        this.$message.error('编辑失败')
      }
    },
  },
}
</script>

<style scoped>
.gutter-box >>> .ant-row {
  padding: 5px 5px 5px 10px;
}
.span-line {
  word-wrap: break-word;
}
</style>

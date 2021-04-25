<template>
  <div class="background-color">
    <a-breadcrumb separator=">">
      <a-breadcrumb-item>
        <a @click="$emit('back') && $emit('refresh')">服务列表</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>服务编辑</a-breadcrumb-item>
    </a-breadcrumb>
    <a-layout class="background-color">
      <a-layout-content class="background-color">
        <a-form layout="horizontal" :form="form">
          <a-form-item
            label="服务名称"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'name',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入服务名称!',
                    },
                  ],
                },
              ]"
              size="small"
            />
          </a-form-item>
          <a-form-item
            label="图层名称"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'layerName',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入图层名称!',
                    },
                  ],
                },
              ]"
              size="small"
            />
          </a-form-item>
          <a-form-item
            label="服务类型"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-select
              v-decorator="[
                'serviceType',
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
              :disabled="true"
              @change="change"
            >
              <a-select-option value="RESTDATA">数据服务</a-select-option>
              <a-select-option value="RESTMAP">地图服务</a-select-option>
              <a-select-option value="RESTMAP3D">三维服务</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="服务地址"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'address',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入服务地址!',
                    },
                  ],
                },
              ]"
              size="small"
              :disabled="disabled"
              @blur="serviceCheck"
            />
          </a-form-item>
          <a-form-item
            label="备注"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-textarea v-decorator="['description']" size="small" autosize />
          </a-form-item>
          <a-form-item>
            <a-form-item>
              <a-row>
                <a-col :offset="19" :span="2">
                  <a-button type="primary" size="small" @click="onSubmit">
                    确定
                  </a-button>
                </a-col>
              </a-row>
            </a-form-item>
          </a-form-item>
          <a-form-item :label-col="labelCol" :wrapper-col="valueCol">
            <div v-show="mapLayerInfo !== null">
              <a-row>
                <a-col :offset="4">
                  <span style="font-size: medium">描述: </span>
                  <span>
                    包含当前地图的所有相关状态信息，如中心点、比例尺、全幅范围等。
                  </span>
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="20">
                  中心点：{{ mapLayerInfo !== null ? mapLayerInfo.center : '' }}
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="20"> 地图范围： </a-col>
              </a-row>
              <a-row>
                <a-col :offset="5" :span="8">
                  左：{{ mapLayerInfo !== null ? mapLayerInfo.left : '' }}
                </a-col>
                <a-col :offset="2" :span="8">
                  上：{{ mapLayerInfo !== null ? mapLayerInfo.top : '' }}
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="5" :span="8">
                  右：{{ mapLayerInfo !== null ? mapLayerInfo.right : '' }}
                </a-col>
                <a-col :offset="2" :span="8">
                  下：{{ mapLayerInfo !== null ? mapLayerInfo.bottom : '' }}
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="20">
                  坐标系信息：{{
                    mapLayerInfo !== null ? mapLayerInfo.prjcoordsys : ''
                  }}
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="20">
                  比例尺：{{ mapLayerInfo !== null ? mapLayerInfo.scale : '' }}
                </a-col>
              </a-row>
            </div>
          </a-form-item>
        </a-form>
      </a-layout-content>
      <a-layout-sider class="sider-style background-color" :width="400">
        <span style="font-size: medium">地图预览</span>
        <a-card hoverable style="width: 250px; height: 250px">
          <img slot="cover" alt="example" :src="imageUrl" />
        </a-card>
        <a-row>
          <span style="font-size: medium">表述格式</span>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.xml'" target="_blank">
            <span>xml</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.json'" target="_blank">
            <span>json</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.rjson'" target="_blank">
            <span>rjson</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.html'" target="_blank">
            <span>html</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.jsonp'" target="_blank">
            <span>jsonp</span>
          </a>
        </a-row>
        <div v-show="type !== 'RESTDATA'">
          <a-row>
            <a :href="serviceUrl + '.kml'" target="_blank">
              <span>kml</span>
            </a>
          </a-row>
          <a-row>
            <a :href="serviceUrl + '.javascript'" target="_blank">
              <span>javascript</span>
            </a>
          </a-row>
          <a-row>
            <a :href="serviceUrl + '.ol3'" target="_blank">
              <span>ol3</span>
            </a>
          </a-row>
          <a-row>
            <a :href="serviceUrl + '.vectortile'" target="_blank">
              <span>vectortile</span>
            </a>
          </a-row>
          <a-row>
            <a :href="serviceUrl + '.tianditu'" target="_blank">
              <span>tianditu</span>
            </a>
          </a-row>
          <a-row>
            <a :href="serviceUrl + '.leaflet'" target="_blank">
              <span>leaflet</span>
            </a>
          </a-row>
          <a-row>
            <a :href="serviceUrl + '.mbgl'" target="_blank">
              <span>mbgl</span>
            </a>
          </a-row>
          <a-row>
            <a :href="serviceUrl + '.webgl3d'" target="_blank">
              <span>webgl3d</span>
            </a>
          </a-row>
        </div>
      </a-layout-sider>
    </a-layout>
  </div>
</template>

<script>
import { dataUrl } from '~/utils/GAFMapDataUtils'
import exampleImage from '~/assets/example.png'
export default {
  props: {
    mapServiceInfo: {
      type: Object,
      default: null,
      required: true,
    },
  },
  data() {
    return {
      labelCol: { span: 4 },
      valueCol: { span: 16 },
      mapLayerInfo: null,
      type: '',
      imageUrl: exampleImage,
      serviceUrl: '',
      serviceId: '',
      disabled: true,
    }
  },
  watch: {
    mapServiceInfo: {
      handler(val) {
        if (val) {
          this.mapLayerInfo = null
          this.serviceId = val.id
          this.serviceUrl = val.address
          this.type = val.serviceType
          this.showLayerServiceInfo(val.address)
          if (!val.instanceId) {
            this.disabled = false
          }
          this.$nextTick(function () {
            this.form.setFieldsValue({
              name: val.name,
              address: val.address,
              layerName: val.layerName,
              serviceType: val.serviceType,
              description: val.description,
            })
          })
        }
      },
      immediate: true,
    },
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  methods: {
    serviceCheck(e) {
      this.showLayerServiceInfo(e.target.value)
      if (!e.target.value) {
        return
      }
      if (e.target.value.includes('localhost')) {
        this.$message.info('请使用真实的ip地址')
        return
      }
      const u = /rest$/
      if (this.type === 'RESTDATA') {
        if (!u.test(e.target.value)) {
          const arr = [
            {
              message: '数据服务地址到/rest为止',
              field: 'address',
            },
          ]
          this.form.setFields({
            address: { value: e.target.value, errors: arr },
          })
        }
      }
    },
    change(value) {
      this.type = value
    },
    async showLayerServiceInfo(url) {
      this.imageUrl = url + '/zxyTileImage.png'
      if (url && url.includes('/maps/')) {
        const mapLayerJsonUrl = url + '.jsonp'
        const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
        if (
          mapLayerJson.center &&
          mapLayerJson.bounds &&
          mapLayerJson.prjCoordSys &&
          mapLayerJson.scale
        ) {
          this.mapLayerInfo = {}
          this.mapLayerInfo.center =
            '(' + mapLayerJson.center.x + ',' + mapLayerJson.center.y + ')'
          this.mapLayerInfo.left = mapLayerJson.bounds.left
          this.mapLayerInfo.bottom = mapLayerJson.bounds.bottom
          this.mapLayerInfo.right = mapLayerJson.bounds.right
          this.mapLayerInfo.top = mapLayerJson.bounds.top
          this.mapLayerInfo.prjcoordsys = mapLayerJson.prjCoordSys.name
          this.mapLayerInfo.scale = mapLayerJson.scale.toFixed(10)
        }
      } else {
        this.mapLayerInfo = null
      }
    },
    onSubmit() {
      this.form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const urll = decodeURIComponent(values.address)
        const mapService = {
          name: values.name,
          address: urll,
          serverUrl: urll,
          layerName: values.layerName,
          serviceType: values.serviceType,
          description: values.description,
        }
        const url = dataUrl.ServiceInfoUrl + `/${this.serviceId}`
        const result = await this.$axios.$put(url, mapService)
        if (result.isSuccessed) {
          this.$message.success('操作成功')
          this.$emit('back')
          this.$emit('refresh')
        } else {
          this.$message.error('操作失败')
        }
      })
    },
  },
}
</script>

<style scoped>
.sider-style {
  min-width: 400px;
  max-width: 400px;
  height: 90vh;
  text-align: left;
}
.background-color {
  background: #ffffff;
}
</style>

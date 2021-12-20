<template>
  <div class="page-container">
    <a-form
      :form="addOrEditForm"
      layout="horizontal"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 15 }"
    >
      <div class="page-container-box">
        <a-form-item v-if="operation === 3" label="关联的服务名">
          <a-input v-model="serviceName" :disabled="true" />
        </a-form-item>
        <a-form-item v-if="operation === 3" label="关联的服务地址">
          <a-input v-model="address" :disabled="true" />
        </a-form-item>
        <a-form-item label="名称">
          <a-input
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '名称不能为空',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item v-show="false" label="所属目录">
          <a-input
            v-decorator="[
              'layerCatalogId',
              { rules: [{ required: true, message: '所属目录不能为空' }] },
            ]"
            :disabled="operation === 1"
            placeholder="请输入所属目录"
            allow-clear
          />
        </a-form-item>
        <a-form-item v-show="false" label="GIS服务">
          <a-input
            v-decorator="[
              'gisServiceId',
              { rules: [{ required: true, message: 'GIS服务不能为空' }] },
            ]"
          >
          </a-input>
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            v-decorator="['sortSn']"
            :disabled="operation === 1"
          />
        </a-form-item>
        <a-form-item label="初始加载">
          <a-radio-group
            v-decorator="['initLoad', { initialValue: true }]"
            :disabled="operation === 1"
            button-style="solid"
          >
            <a-radio-button :value="true"> 是 </a-radio-button>
            <a-radio-button :value="false"> 否 </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            v-decorator="['description']"
            :disabled="operation === 1"
            placeholder="请输入描述"
            auto-size
          />
        </a-form-item>
      </div>
    </a-form>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  components: {},
  props: {
    editData: {
      type: Object,
      default: null,
    },
    operation: {
      type: Number,
      default: 0,
    },
    layerCatalogId: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      dataId: '',
      serviceName: '',
      address: '',
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const {
      name,
      gisServiceId,
      sortSn,
      initLoad,
      description,
      serviceName,
      address,
    } = this.editData
    this.serviceName = serviceName
    this.address = address
    this.dataId = this.editData.catalogLayerId
    this.addOrEditForm.setFieldsValue({
      name,
      gisServiceId,
      sortSn,
      initLoad,
      description,
      layerCatalogId: this.layerCatalogId,
    })
  },
  methods: {
    moment,
    submitForm(callback) {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/map/webgis-catalog-layers/`
        const data = this.addOrEditForm.getFieldsValue()
        if (this.dataId) {
          url = url + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
          callback(rst.data.isSuccessed)
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
          callback(rst.data.isSuccessed)
        }
      })
    },
  },
}
</script>

<style lang="less" scoped>
.page-container {
  width: 100%;
  height: 100%;
}

.page-container-box {
  height: 100%;
}
</style>

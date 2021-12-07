<template>
  <div class="page-container">
    <div class="grid-container">
    <div class="drawer-header">
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    </div>
    <div class="drawer-content">
      <a-form
        :form="addOrEditForm"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 19 }"
        layout="horizontal"
      >
        <a-form-item v-show="false" label="API组id">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'apiCatalogId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入API组id'
                  }
                ]
              }
            ]"
            placeholder="请输入API组id"
            allow-clear
          />
        </a-form-item>
        <!-- v-decorator="['apiGroupPath', { initialValue: '无' }]" -->
        <a-form-item label="API分组路径">
          <!-- <a-input :disabled="true" v-model="apiGroupPathName" /> -->
          <a-tree-select
            :disabled="operation !== 3"
            :tree-data="dataOfTree"
            placeholder="请选择分组"
            :replaceFields="{children:'children',title:'title',key:'key',value: 'key'}"
            v-decorator="['apiCatalogId']"
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item label="名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入名称'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入名称"
          />
        </a-form-item>
        <a-form-item label="路由路径">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="[
              'routeUrl',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入路由路径'
                  }
                ]
              }
            ]"
            placeholder="请输入路由路径"
            auto-size
          />
        </a-form-item>
        <a-form-item label="方法">
          <a-select
            :disabled="operation === 1"
            v-decorator="[
              'method',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择方法'
                  }
                ],
                initialValue: '1'
              }
            ]"
          >
            <a-select-option
              v-for="i in methodList"
              :key="i.value"
              :value="i.value"
            >
              {{ i.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="类型">
          <a-radio-group
            :disabled="operation === 1"
            v-decorator="[
              'type',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择类型'
                  }
                ],
                initialValue: '1'
              }
            ]"
            button-style="solid"
          >
            <a-radio-button value="1">
              系统资源
            </a-radio-button>
            <a-radio-button value="2">
              第三方资源
            </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 1"
            :precision="0"
            :min="1"
            v-decorator="[
              'sortSn',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入排序序号'
                  }
                ]
              }
            ]"
          />
        </a-form-item>
        <a-form-item v-show="operation === 1" label="创建时间">
          <a-date-picker v-decorator="['createdTime']" show-time disabled />
        </a-form-item>
        <a-form-item v-show="operation === 1" label="创建人">
          <a-input v-decorator="['createdBy']" allow-clear disabled />
        </a-form-item>
        <a-form-item v-show="operation === 1" label="修改时间">
          <a-date-picker v-decorator="['updatedTime']" show-time disabled />
        </a-form-item>
        <a-form-item v-show="operation === 1" label="修改人">
          <a-input v-decorator="['updatedBy']" allow-clear disabled />
        </a-form-item>
        
      </a-form>
    </div>
    <div class="drawer-footer">
      <div v-show="operation === 1" class="drawer-footer-div">
        <button @click="backToList" class="cancel-modal"
          >{{this.operation === 1 ? "返回" : "取消"}}</button
        >
      </div>
      <div style="text-align:center;" class="drawer-footer-div" v-show="operation !== 1">
        <button @click="backToList" class="cancel-modal">取消</button>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a-button
          @click="submitForm"
          type="primary"
          :loading="loading"
          class="submit-gray"
        >
          确定
        </a-button>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  props: {
    title: {
      type: String,
      default: ''
    },
    editData: {
      type: Object,
      default: null
    },
    operation: {
      type: Number,
      default: 0
    },
    apiGroupId: {
      type: String,
      default: ''
    },
    apiGroupPath: {
      type: Array,
      default: () => []
    },
    dataOfTree: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dataId: '',
      methodList: [
        {
          name: 'GET',
          value: '1'
        },
        {
          name: 'POST',
          value: '2'
        },
        {
          name: 'PUT',
          value: '3'
        },
        {
          name: 'DELETE',
          value: '4'
        }
      ],
      loading: false
    }
  },
  computed: {
    apiGroupPathName: function() {
      if (this.apiGroupPath && this.apiGroupPath.length > 0) {
        const pathName = this.apiGroupPath
          .map(function(item) {
            return item.title
          })
          .join('/')
        return pathName
      } else {
        return '无'
      }
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.resourceApiId
    delete copyData.resourceApiId
    if (copyData.createdTime)
      copyData.createdTime = moment(new Date(copyData.createdTime))
    if (copyData.updatedTime)
      copyData.updatedTime = moment(new Date(copyData.updatedTime))
    this.addOrEditForm.setFieldsValue({ ...copyData })
    this.addOrEditForm.setFieldsValue({
      apiCatalogId: this.apiGroupId
    })
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = '/authority/auth-resource-apis/'
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        if (this.dataId) {
          url = url + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.loading = false
        this.addOrEditForm.resetFields()
        this.$emit('submit')
      })
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    }
  }
}
</script>
<style scoped>
.ant-switch-checked {
  background-color: rgba(94, 192, 94, 0.5);
}
</style>

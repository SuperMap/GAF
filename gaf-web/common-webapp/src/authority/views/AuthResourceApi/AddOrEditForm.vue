<template>
  <div class="page-container">
    <template>
      <a-breadcrumb separator=">">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    <div class="page-container-box">
      <a-form
        :form="addOrEditForm"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 15 }"
        layout="horizontal"
      >
        <a-form-item label="所属组件id">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'sysComponentId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入所属组件id'
                  }
                ]
              }
            ]"
            placeholder="请输入所属组件id"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="API组id">
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
                  }
                ]
              }
            ]"
            placeholder="请输入名称"
            allow-clear
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
              应用组件资源
            </a-radio-button>
            <a-radio-button value="2">
              第三方资源
            </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="状态">
          <a-switch
            :disabled="operation === 1"
            v-decorator="[
              'status',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择状态'
                  }
                ],
                valuePropName: 'checked',
                initialValue: true
              }
            ]"
            checked-children="启用"
            un-checked-children="禁用"
          />
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
        <div class="btn-div">
          <button @click="submitForm" class="submit-gray">
            确定
          </button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <button @click="backToList" class="cancel-modal">取消</button>
        </div>
      </a-form>
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
      ]
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    this.dataId = this.editData.resourceApiId
    delete this.editData.resourceApiId
    if (this.editData.createdTime)
      this.editData.createdTime = moment(
        this.editData.createdTime,
        'YYYY-MM-DD hh:mm:ss'
      )
    if (this.editData.updatedTime)
      this.editData.updatedTime = moment(
        this.editData.updatedTime,
        'YYYY-MM-DD hh:mm:ss'
      )
    this.addOrEditForm.setFieldsValue({ ...this.editData })
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields(async (err, values) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = '/authority/auth-resource-apis/'
        const data = this.addOrEditForm.getFieldsValue()
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

<style lang="less" scoped>
button {
  width: 80px;
  font-size: 12px;
  cursor: pointer;
}

.page-container {
  width: 100%;
  height: 100%;
}

.page-container-box {
  height: 100%;
}

.btn-div {
  text-align: center;
  margin: 15px 0;
}
</style>

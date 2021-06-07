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
        <a-form-item v-show="operation === 1" label="所属组件">
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
        <a-form-item v-show="operation === 1" label="API分组">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'moduleCatalogId',
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
        <a-form-item label="类型">
          <a-select
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
          >
            <a-select-option
              v-for="i in typeList"
              :key="i.value"
              :value="i.value"
            >
              {{ i.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="地址">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="[
              'moduleUrl',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入地址'
                  }
                ]
              }
            ]"
            placeholder="请输入地址"
            auto-size
          />
        </a-form-item>
        <a-form-item label="图标地址">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="['iconUrl']"
            placeholder="请输入图标地址"
            auto-size
          />
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 1"
            :precision="0"
            :min="1"
            v-decorator="['sortSn', { initialValue: 1 }]"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="['description']"
            placeholder="请输入描述"
            auto-size
          />
        </a-form-item>
        <div v-show="operation === 1">
          <a-form-item label="创建时间">
            <a-date-picker
              v-decorator="['createdTime']"
              show-time
              placeholder="请选择创建时间"
              disabled
            />
          </a-form-item>
          <a-form-item label="创建人">
            <a-input
              v-decorator="['createdBy']"
              placeholder="请输入创建人"
              allow-clear
              disabled
            />
          </a-form-item>
          <a-form-item label="修改时间">
            <a-date-picker
              v-decorator="['updatedTime']"
              show-time
              placeholder="请选择修改时间"
              disabled
            />
          </a-form-item>
          <a-form-item label="修改人">
            <a-input
              v-decorator="['updatedBy']"
              placeholder="请输入修改人"
              allow-clear
              disabled
            />
          </a-form-item>
        </div>
        <div class="btn-div">
          <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
            确定
          </a-button>
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
    },
    componentId: {
      type: String,
      default: ''
    },
    moduleGrpId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dataId: '',
      typeList: [
        {
          name: '单页',
          value: '1'
        },
        {
          name: '嵌入页',
          value: '2'
        },
        {
          name: '新窗口',
          value: '3'
        },
        {
          name: '概览',
          value: '4'
        }
      ],
      loading: false,
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    this.dataId = this.editData.resourceModuleId
    delete this.editData.resourceModuleId
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
    this.addOrEditForm.setFieldsValue({
      sysComponentId: this.componentId,
      moduleCatalogId: this.moduleGrpId
    })
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields(async (err, values) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = '/authority/auth-resource-modules/'
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
        this.loading = true
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

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
        hide-required-mark
      >
        <a-form-item label="模块id">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'resourceModuleId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入模块id'
                  }
                ]
              }
            ]"
            placeholder="请输入模块id"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="接口资源id">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'resourceApiId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入接口资源id'
                  }
                ]
              }
            ]"
            placeholder="请输入接口资源id"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="状态">
          <a-radio-group
            :disabled="operation === 1"
            v-decorator="['status']"
            button-style="solid"
          >
            <a-radio-button value="true">
              是
            </a-radio-button>
            <a-radio-button value="false">
              否
            </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 1"
            :precision="0"
            :min="1"
            v-decorator="['sortSn']"
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
        <div v-if="operation === 1">
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
    }
  },
  data() {
    return {
      dataId: '',
      loading: false
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    this.dataId = this.editData.moduleApiId
    delete this.editData.moduleApiId
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
        let url = '/authority/auth-module-apis/'
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

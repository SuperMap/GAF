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
        layout="horizontal"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 19 }"
        hide-required-mark
      >
        <a-form-item v-show="false" label="父级id">
          <a-input
            :disabled="operation === 1"
            v-decorator="['pid']"
            placeholder="请输入父级id"
            allow-clear
          />
        </a-form-item>
         <a-form-item v-show="false" label="字典类别编码">
          <a-input
            :disabled="operation === 1"
            v-decorator="['dictCode']"
            placeholder="请输入字典类别编码"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'dictName',
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
            allow-clear
          />
        </a-form-item>
        <a-form-item label="字典值">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'dictValue',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入字典值'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入值"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="序号">
          <a-input-number
            :disabled="operation === 1"
            v-decorator="['seq']"
            :precision="0"
            :min="1"
          />
        </a-form-item>
        <a-form-item label="可见性">
          <a-switch
            :disabled="operation === 1"
            v-decorator="['visibility',{valuePropName: 'checked',initialValue: true}]"
          >
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-form-item>
        <a-form-item label="扩展属性">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="[
              'extProperties',
              {
                rules: [
                  {
                    message: '请输入符合标准格式的json格式的字符串',
                    validator: extValidater
                  }
                ]
              }
            ]"
            placeholder='请输入符合标准格式的json格式字符串，如{"xxx": "xxx", "xxxx": "xxxx"}'
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="[
              'dictDesc',
              {
                rules: [
                  {
                    max: 500,
                    message: '长度不能超过500个字符'
                  }
                ]
              }
            ]"
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
              v-decorator="[
                'createdBy'            ]"
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
              v-decorator="[
                'updatedBy'            ]"
              placeholder="请输入修改人"
              allow-clear
              disabled
            />
          </a-form-item>
        </div>
      </a-form>
    </div>
    <div class="drawer-footer">
      <button @click="backToList" class="cancel-modal">{{this.operation === 1 ? "返回" : "取消"}}</button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
        确定
      </a-button>
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
	const copyData = { ...this.editData }
    this.dataId = copyData.dataDictId
    delete copyData.dataDictId
    delete copyData.status
    delete copyData.tenantId
    if(this.operation !== 1) {
      delete copyData.createdTime
      delete copyData.updatedTime
      delete copyData.createdBy
      delete copyData.updatedBy
    }
    if (copyData.createdTime)
        copyData.createdTime = moment(new Date(copyData.createdTime))
    if (copyData.updatedTime)
      copyData.updatedTime = moment(new Date(copyData.updatedTime))
    this.addOrEditForm.setFieldsValue({ ...copyData })
  },
  methods: {
    moment,
    extValidater(rule, value, callback) {
      if(!value || value.trim() === '') {
        callback()
      } else {
        try{
          JSON.parse(value)
          callback()
        } catch (error) {
          callback(false)
        }
      }
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          return false
        }
        let url = `/sys-mgt/sys-dicts/`
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        if (this.dataId) {
          url = url  + this.dataId
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

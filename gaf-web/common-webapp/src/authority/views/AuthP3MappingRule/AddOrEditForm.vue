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
        <a-form-item label="规则名称">
          <a-input
            :disabled="isdisabled"
            v-decorator="[
              'mappingRuleName',
              {
                rules: [
                  {
                    required: true,
                    message: '规则名称不能为空'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ],
                initialValue: ''
              }
            ]"
            placeholder="请输入规则名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="第三方组件">
          <a-select
            :disabled="isdisabled"
            v-decorator="[
              'p3ComponentId',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择第三方组件'
                  }
                ],
                initialValue: ''
              }
            ]"
            :options="options"
            allow-clear
          >
            <a-select-option value="">-请选择第三方组件-</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="映射类型">
          <a-select
            :disabled="isdisabled"
            v-decorator="[
              'mappingType',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择映射类型'
                  }
                ],
                initialValue: ''
              }
            ]"
            allow-clear
          >
            <a-select-option value="">-请选择映射类型-</a-select-option>
            <a-select-option value="1">-租户-</a-select-option>
            <a-select-option value="2">-部门-</a-select-option>
            <a-select-option value="3">-用户-</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="同步方式">
          <a-select
            :disabled="isdisabled"
            v-decorator="[
              'mappingMethod',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择同步方式'
                  }
                ],
                initialValue: ''
              }
            ]"
            allow-clear
          >
            <a-select-option value="">-请选择同步方式-</a-select-option>
            <a-select-option value="1">-推-</a-select-option>
            <a-select-option value="2">-拉-</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="同步实现类">
          <a-input
            :disabled="isdisabled"
            v-decorator="['mappingImpl']"
            placeholder="请输入同步实现类"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="其他参数">
          <a-textarea
            :disabled="isdisabled"
            :value="value"
            v-decorator="[
              'extraParamJson',
              {
                rules: [
                  {
                    message: '请输入符合标准格式的json格式的字符串',
                    validator: jsonValidater
                  }
                ]
              }
            ]"
            placeholder="请输入其他参数"
            auto-size
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            :disabled="isdisabled"
            v-decorator="['description']"
            placeholder="请输入描述"
            auto-size
          />
        </a-form-item>
        <div v-if="operation === 1">
          <a-form-item label="创建时间">
            <a-date-picker
              :disabled="isdisabled"
              v-decorator="['createdTime']"
              show-time
              placeholder="请选择创建时间"
            />
          </a-form-item>
        </div>
        <div v-if="operation === 1">
          <a-form-item label="创建人">
            <a-input
              :disabled="isdisabled"
              v-decorator="['createdBy']"
              placeholder="请输入创建人"
              allow-clear
            />
          </a-form-item>
        </div>
        <div v-if="operation === 1">
          <a-form-item label="修改时间">
            <a-date-picker
              :disabled="isdisabled"
              v-decorator="['updatedTime']"
              show-time
              placeholder="请选择修改时间"
            />
          </a-form-item>
        </div>
        <div v-if="operation === 1">
          <a-form-item label="修改人">
            <a-input
              :disabled="isdisabled"
              v-decorator="['updatedBy']"
              placeholder="请输入修改人"
              allow-clear
            />
          </a-form-item>
        </div>
        <div class="btn-div">
          <button @click="submitForm" class="submit-gray">
            确定
          </button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <button @click="backToList" class="cancel-modal">{{this.operation === 1 ? "返回" : "取消"}}</button>
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
      isdisabled: false,
      options: [],
      value: ''
    }
  },
  beforeMount() {
    if (this.operation === 1) {
      this.isdisabled = true
    } else {
      this.disabled = false
    }
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.mappingRuleId
    delete copyData.mappingRuleId
    delete copyData.status
    if (this.editData.createdTime)
      this.editData.createdTime = moment(new Date(this.editData.createdTime))
    if (this.editData.updatedTime)
      this.editData.updatedTime = moment(new Date(this.editData.updatedTime))
    this.addOrEditForm.setFieldsValue({ ...copyData })
    this.addDataOption()
  },
  methods: {
    moment,
    jsonValidater(rule, value, callback) {
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
    async addDataOption() {
      const url = `/authority/sys-components/options`
      const res = await this.$axios.get(url)
      console.log(res)
      this.options = res.data.data
      console.log(this.options)
    },
    submitForm() {
      console.log(this.value)
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/authority/auth-p3-mapping-rule/`
        const data = this.addOrEditForm.getFieldsValue()
        if (this.dataId) {
          url = url + this.dataId
          const rst = await this.$axios.put(url, data)
          console.log('rst', rst)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          console.log(rst)
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

<template>
  <div class="page-container">
    <div class="page-container-box">
      <a-form
        :form="addOrEditForm"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 15 }"
        layout="horizontal"
      >
        <a-form-item label="用户名">
          <a-input
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入用户名'
                  },
                  {
                    validator: (rule, value, callback) => {
                      if (!/^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$/.test(value)) {
                        rule.message = '请输入符合字母数字下划线格式的内容！'
                        callback(rule.message)
                      }
                      callback()
                    }
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ],
                validateFirst: true
              }
            ]"
            placeholder="请输入管理员用户名"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="真实姓名">
          <a-input
            v-decorator="[
              'realName',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入真实姓名'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入真实姓名"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="身份证号">
          <a-input
            v-decorator="[
              'idNumber',
              {
                rules: [
                  {
                    required: true,
                    validator: (rule, value, callback) => {
                      if (
                        value &&
                        !/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(value)
                      ) {
                        rule.message = '请输入符合身份证格式的内容！'
                        callback(rule.message)
                      }
                      callback()
                    }
                  }
                ]
              }
            ]"
            placeholder="请输入身份证号"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="手机">
          <a-input
            v-decorator="[
              'mobileNumber',
              {
                rules: [
                  {
                    required: true,
                    validator: (rule, value, callback) => {
                      if (
                        value &&
                        !/^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\d{8}$/.test(
                          value
                        )
                      ) {
                        rule.message = '请输入符合手机号格式的内容！'
                        callback(rule.message)
                      }
                      callback()
                    }
                  }
                ]
              }
            ]"
            placeholder="请输入手机号"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="邮箱">
          <a-input
            v-decorator="[
              'email',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入邮箱'
                  },
                  {
                    validator: emailValidator
                  }
                ]
              }
            ]"
            placeholder="请输入邮箱"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="地址">
          <a-input v-decorator="[
              'address',
              {
                rules: [
                  {
                    max: 500,
                    message: '长度不能超过500个字符'
                  }
                ]
              }
            ]" 
            allow-clear 
          />
        </a-form-item>
        <a-form-item label="状态">
          <a-switch
            v-decorator="[
              'status',
              { valuePropName: 'checked', initialValue: true }
            ]"
            checked-children="启用"
            un-checked-children="禁用"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-decorator="[
            'description',
            {
              rules: [
                {
                  max: 500,
                  message: '长度不能超过500个字符'
                }
              ]
            }
          ]" auto-size />
        </a-form-item>
        <div style="text-align: center; margin-top: 15px;">
          <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
            确定
          </a-button>
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
  data() {
    return {
      dataId: '',
      loading: false
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields((err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        const adminInfo = this.addOrEditForm.getFieldsValue()
        this.loading = true
        this.$emit('submit', adminInfo)
        this.loading = false
      })
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    },
    // 邮箱格式
    emailValidator(rule, value, callback) {
      if (
        !/^([a-zA-Z0-9]+[_|\-|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(
          value
        )
      ) {
        rule.message = '请输入符合邮箱格式的内容！'
        callback(rule.message)
      }
      callback()
    }
  }
}
</script>

<style lang="less" scoped>
button {
  width: 100px;
  font-size: 12px;
  cursor: pointer;
}
</style>

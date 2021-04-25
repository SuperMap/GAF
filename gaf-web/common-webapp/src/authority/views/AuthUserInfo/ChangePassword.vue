<template>
  <div class="form-center">
    <a-form
      :form="form"
      :label-col="{ span: 5, offset: 2 }"
      :wrapper-col="{ span: 13, offset: 1 }"
      layout="horizontal"
    >
      <a-form-item label="旧密码:" has-feedback>
        <a-input
          v-decorator="[
            'oldPassword',
            {
              rules: [
                {
                  required: true,
                  message: '旧密码不能为空'
                },
                {
                  max: 50,
                  message: '旧密码不能超过50个字符'
                }
              ]
            }
          ]"
          type="password"
        />
      </a-form-item>
      <a-form-item label="新密码:" has-feedback>
        <a-input
          v-decorator="[
            'newPassword',
            {
              rules: [
                {
                  required: true,
                  message: '新密码不能为空'
                },
                {
                  validator: validateToNextPassword
                }
              ]
            }
          ]"
          type="password"
        />
      </a-form-item>
      <a-form-item label="确认新密码:" has-feedback>
        <a-input
          v-decorator="[
            'confirmPassword',
            {
              rules: [
                {
                  required: true,
                  message: '密码不能为空'
                },
                {
                  validator: compareToFirstPassword
                }
              ]
            }
          ]"
          @blur="handleConfirmBlur"
          type="password"
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
export default {
  props: {},
  data() {
    return {
      confirmDirty: false
    }
  },
  computed: {},
  watch: {},
  created() {
    this.form = this.$form.createForm(this, { name: 'changePassword' })
  },
  beforeMount() {},
  mounted() {},
  methods: {
    handleConfirmBlur(e) {
      const value = e.target.value
      this.confirmDirty = this.confirmDirty || !!value
    },
    compareToFirstPassword(rule, value, callback) {
      const form = this.form
      if (value && value !== form.getFieldValue('newPassword')) {
        callback('新密码与确认密码不相等')
      } else {
        callback()
      }
    },
    validateToNextPassword(rule, value, callback) {
      const form = this.form
      if (value && this.confirmDirty) {
        form.validateFields(['confirmPassword'], { force: true })
      }
      if (
        value &&
        !/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d$@$!%*#?&]{8,25}$/.test(value)
      ) {
        callback('8-25个字符，至少1个字母和1个数字')
      } else {
        callback()
      }
    },
    submitForm() {}
  }
}
</script>

<style lang="less" scoped>
.form-center {
  margin: auto;
  width: 100%;
  padding: 10px;
}
</style>

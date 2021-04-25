<template>
  <a-form :form="form" layout="horizontal">
    <a-form-item :label-col="labelCol" :wrapper-col="valueCol" label="名称">
      <a-input
        v-decorator="[
          'name',
          {
            rules: [
              {
                required: true,
                message: '请输入角色名称!'
              }
            ]
          }
        ]"
      />
    </a-form-item>
    <a-form-item :label-col="labelCol" :wrapper-col="valueCol" label="描述">
      <a-input v-decorator="['desc']" type="textarea" />
    </a-form-item>
  </a-form>
</template>

<script>
export default {
  name: 'AddEditForm',
  props: {
    roleItem: {
      type: Object,
      required: false
    }
  },
  data() {
    return {
      labelCol: { span: 5 },
      valueCol: { span: 18 },
      form: this.$form.createForm(this)
    }
  },
  watch: {
    roleItem: {
      handler(val) {
        if (val) {
          this.$nextTick(function() {
            this.form.setFieldsValue({
              name: val.name,
              desc: val.desc
            })
          })
        }
      },
      immediate: true
    }
  }
}
</script>

<style scoped></style>

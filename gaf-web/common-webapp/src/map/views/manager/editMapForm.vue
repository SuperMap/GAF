<template>
  <div>
    <a-form layout="horizontal" :form="form">
      <a-form-item
        label="服务名称"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-input
          v-decorator="[
            'name',
            {
              rules: [
                {
                  required: true,
                  message: '请输入服务名称!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
      <a-form-item
        label="服务名称"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-input
          v-decorator="[
            'code',
            {
              rules: [
                {
                  required: true,
                  message: '请输入标识!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
export default {
  model: {
    prop: 'mapConfigInfo',
    event: 'change',
  },
  props: {
    mapConfigInfo: {
      type: Object,
      default: null,
      required: true,
    },
  },
  data() {
    return {}
  },
  watch: {
    mapConfigInfo: {
      handler(val) {
        this.form.setFieldsValue({
          name: val.name,
          code: val.code,
        })
      },
    },
    immediate: true,
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
}
</script>

<style scoped></style>

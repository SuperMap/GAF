<template>
  <div>
    <a-form :form="form">
      <a-form-item v-bind="itemLayout" label="标题">
        <a-input
          v-decorator="[
            'title',
            {
              rules: [
                {
                  required: true,
                  message: '请输入标题!',
                },
              ],
            },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="itemLayout" label="描述">
        <a-input
          v-decorator="[
            'desc',
            {
              rules: [
                {
                  required: true,
                  message: '请输入描述!',
                },
              ],
            },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="itemLayout" label="链接图标">
        <a-button
          v-decorator="[
            'icon',
            {
              rules: [
                {
                  required: true,
                  message: '请选择图标!',
                },
              ],
              valuePropName: 'icon',
            },
          ]"
          @click="visible = true"
        />
      </a-form-item>
    </a-form>
    <gaf-icon v-model="visible" @select="selectedHandle" />
  </div>
</template>

<script>
export default {
  name: 'NodeForm',
  props: {
    item: {
      type: Object,
      required: false,
      default: () => {},
    },
  },
  data() {
    return {
      itemLayout: {
        labelCol: {
          span: 5,
        },
        wrapperCol: {
          span: 19,
        },
      },
      visible: false,
    }
  },
  watch: {
    item: {
      handler({ title, desc, icon }) {
        this.$nextTick(function () {
          this.form.setFieldsValue({ title, desc, icon })
        })
      },
      immediate: true,
    },
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  methods: {
    selectedHandle(icon) {
      // this.$set(this.item, 'icon', icon)
      this.visible = false
      this.form.setFieldsValue({ icon })
    },
  },
}
</script>

<style scoped></style>

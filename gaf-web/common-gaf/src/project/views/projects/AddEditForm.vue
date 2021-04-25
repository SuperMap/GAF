<template>
  <div class="page-modal-box">
    <a-form
      :form="form"
      layout="horizontal"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 15 }"
    >
      <a-form-item label="工程组">
        <a-select
          v-decorator="[
            'projGroupId',
            {
              rules: [
                {
                  required: true,
                  message: '请选择工程组！',
                },
              ],
              initialValue: `${
                editData && editData.projGroupId
                  ? editData.projGroupId
                  : projGroup
              }`,
            },
          ]"
          placeholder="请选择工程组"
          @change="projGroupChange"
          :disabled="operation === 3"
        >
          <a-select-option
            v-for="(item, index) in projGroupList"
            :key="index"
            :value="item.projGroupId"
          >
            {{ item.projGroupName }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="名称">
        <a-input
          v-decorator="[
            'projName',
            {
              rules: [
                {
                  required: true,
                  message: '请填写工程名称!',
                },
                {
                  validator: duplicateValidator,
                },
              ],
              initialValue: `${
                editData && editData.projName ? editData.projName : ''
              }`,
              validateFirst: true,
              trigger: 'blur',
              validateTrigger: 'blur',
            },
          ]"
          placeholder="请填写工程名称"
          @change="projNameChange"
          :disabled="operation === 3"
        />
      </a-form-item>
      <a-form-item label="描述">
        <a-input
          v-decorator="[
            'description',
            {
              initialValue: `${
                editData && editData.description ? editData.description : ''
              }`,
            },
          ]"
          type="textarea"
          placeholder="请填写工程描述"
          :disabled="operation === 3"
        />
      </a-form-item>
      <a-form-item label="权限">
        <a-select
          v-decorator="[
            'visibility',
            {
              initialValue: `${
                editData && editData.visibility
                  ? editData.visibility
                  : 'private'
              }`,
            },
          ]"
          placeholder="请选择工程权限"
          :disabled="operation === 3"
        >
          <a-select-option value="private">私有</a-select-option>
          <a-select-option value="public">公开</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="工程模板">
        <a-select
          :disabled="operation === 3"
          v-decorator="[
            'projCodeTemplateId',
            {
              rules: [
                {
                  required: true,
                  message: '请选择工程模板!',
                },
              ],
              initialValue: `${
                editData && editData.projTemplateId
                  ? editData.projTemplateId
                  : template
              }`,
            },
          ]"
          placeholder="请选择工程模板"
          @change="tmplChange"
        >
          <a-select-option
            v-for="tmpl of templates"
            :key="tmpl.projCodeTemplateId"
            :value="tmpl.projCodeTemplateId"
            >{{ tmpl.templateNameCn }}</a-select-option
          >
        </a-select>
      </a-form-item>
      <a-form-item v-if="operation === 3" label="git地址">
        <a-input
          :disabled="operation === 3"
          placeholder="无"
          v-decorator="[
            'gitUrl',
            {
              initialValue: `${
                editData && editData.gitUrl ? editData.gitUrl : ''
              }`,
            },
          ]"
        />
      </a-form-item>
      <a-form-item v-if="operation === 3" label="是否已创建">
        <a-switch
          default-checked
          :disabled="operation === 3"
          style="margin-bottom:5px;background: rgb(24, 144, 255);"
          checked-children="已创建"
          un-checked-children="未创建"
          :checked="editData.status === 2"
        />
      </a-form-item>
      <a-form-item v-if="operation === 3" label="参数组信息">
        <button class="btn-edit" @click="onClick" style="width: 100px;height: 26px;line-height: 20px;">参数组信息</button>
      </a-form-item>
    </a-form>

    <div class="action">
      <a-button v-if="operation !== 3" class="submit-gray" @click="next"> 下一步 </a-button>
      <a-button class="cancel-modal" @click="cancel"> {{ operation === 3 ? '返回' : '取消' }}</a-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AddEditForm',
  props: {
    editData: {
      type: Object,
      default: null,
    },
    operation: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      form: this.$form.createForm(this, { name: 'general' }),
      templates: [],
      projGroupList: [],
      template: '',
      projGroup: '',
      hasProjGroupChanged: false,
      hasProjNameChanged: false,
    }
  },
  mounted() {
    this.getProjGroups()
    this.getTemplates()
    // this.form.setFieldsValue({ ...this.editData })
  },
  methods: {
    // 初始化获取模版信息
    async getTemplates() {
      const res = await this.$axios.$get('/proj/projcodetemplate/projtype')
      if (res.successed) {
        this.templates = res.data
        this.template = res.data[0].projCodeTemplateId
      } else {
        this.$message.error(`初始化工程模板信息失败,原因:${res.message}`)
      }
    },
    // 初始化 获取工程组信息
    async getProjGroups() {
      const url = '/proj/groups?parentPaths=gaf-dev'
      const res = await this.$axios.$get(url)
      if (res.successed) {
        this.projGroupList = res.data
        this.projGroup = res.data[0].projGroupId
      } else {
        this.$message.error(
          res.message !== '' ? res.message : '初始化工程组信息失败！',
          1
        )
      }
    },
    cancel() {
      this.hasProjGroupChanged = false
      this.hasProjNameChanged = false
      this.$emit('cancel')
    },
    next() {
      let data = {}
      this.form
        .validateFields((err) => {
          if (!err) {
            data = this.form.getFieldsValue()
            this.hasProjGroupChanged = false
            this.hasProjNameChanged = false
            this.$emit('changeStep', data)
          } else {
            return false
          }
        })
        .then(() => {})
        .catch((err) => this.$message.error(err, 1))
    },
    onClick() {
      let data = {}
      data = this.form.getFieldsValue()
      this.$emit('changeStep', data)
    },
    async duplicateValidator(rule, value, callback) {
      const projName = this.form.getFieldValue('projName')
      const groupId = this.form.getFieldValue('projGroupId')
      if (this.operation === 2)
        if (!this.hasProjGroupChanged || !this.hasProjNameChanged) return true
      const url = `/proj/dev/duplicatename?projGroupId=${groupId}&projName=${projName}`
      const res = await this.$axios.get(url)
      if (res.data.successed) {
        if (res.data.data) {
          callback('同一工程组下存在同名工程！')
        }
      }
      callback()
    },
    tmplChange(value) {
      this.template = value
      this.form.setFieldsValue({ projTemplateId: value })
    },
    projGroupChange(value) {
      this.projGroup = value
      this.hasProjGroupChanged = true
      this.form.setFieldsValue({ projGroupId: value })
    },
    projNameChange(event) {
      this.hasProjNameChanged = true
      this.form.setFieldsValue({ projName: event.target.value })
    },
  },
}
</script>

<style scoped>
.action {
  text-align: center;
}
button {
  width: 80px;
  font-size: 12px;
  cursor: pointer;
  margin: 10px 5px;
}
</style>

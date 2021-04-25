<template>
  <div class="page-container">
    <a-form
      :form="addOrEditForm"
      layout="horizontal"
      style="height: fit-content"
      :label-col="{ span: 7 }"
      :wrapper-col="{ span: 15 }"
    >
      <a-form-item v-show="false" label="模板ID">
        <a-input
          v-decorator="[
            'codeTemplateId',
            {
              rules: [
                {
                  required: true,
                  message: '请输入模板ID！',
                },
              ],
            },
          ]"
          :disabled="true"
          placeholder="请输入模板ID"
        />
      </a-form-item>
      <a-form-item label="模板名">
        <a-input
          v-decorator="[
            'templateName',
            {
              rules: [
                {
                  required: true,
                  message: '请输入模板名！',
                },
              ],
            },
          ]"
          placeholder="请输入模板名"
          allow-clear
          :disabled="true"
        />
      </a-form-item>
      <a-form-item label="参数分组名称">
        <a-input
          v-decorator="[
            'paramGroupName',
            {
              rules: [
                {
                  required: true,
                  message: '请输入参数分组名称！',
                },
              ],
            },
          ]"
          placeholder="请输入参数分组名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="参数分组中文名称">
        <a-input
          v-decorator="[
            'paramGroupNameCn',
            {
              rules: [
                {
                  required: true,
                  message: '请输入参数分组中文名称！',
                },
              ],
            },
          ]"
          placeholder="请输入参数分组中文名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="参数组是否必须">
        <a-select
          v-decorator="[
            'isRequired',
            {
              rules: [
                {
                  required: true,
                  message: '请选择参数组是否必须！',
                },
              ],
              initialValue: 'true',
            },
          ]"
          placeholder="请选择参数组是否必须"
        >
          <a-select-option
            v-for="(isRequired, index) in isGrpRequiredOptions"
            :key="index"
            :value="isRequired.required"
          >
            {{ isRequired.description }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="描述">
        <a-textarea
          v-decorator="['description']"
          placeholder="请输入描述"
          style="height: 66px"
        />
      </a-form-item>

      <div class="btns">
        <a-button type="primary" @click="submitForm"> 确定 </a-button>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a-button @click="backToList">取消</a-button>
      </div>
    </a-form>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: '',
    },
    grpList: {
      type: Array,
      default() {
        return []
      },
    },
    editData: {
      type: Object,
      default: null,
    },
    templateName: {
      type: String,
      default: '',
    },
    templateId: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      dataId: '',
      isGrpExist: false,
      isGrpRequiredOptions: [
        {
          required: 'false',
          description: '否',
        },
        {
          required: 'true',
          description: '是',
        },
      ],
    }
  },
  watch: {
    editData() {
      this.dataId = this.editData.codeTemplateParamGrpId
      if (!this.dataId) {
        this.addOrEditForm.resetFields()
      }
      this.addOrEditForm.setFieldsValue({
        codeTemplateId: this.templateId,
        templateName: this.templateName,
      })
      if (this.dataId) {
        this.addOrEditForm.setFieldsValue({
          paramGroupName: this.editData.paramGroupName,
          paramGroupNameCn: this.editData.paramGroupNameCn,
          description: this.editData.description,
          isRequired: this.editData.isRequired.toString(),
        })
      }
    },
  },
  mounted() {
    this.dataId = this.editData.codeTemplateParamGrpId
    if (!this.dataId) {
      this.addOrEditForm.resetFields()
    }
    let formData = {}
    if (this.dataId) {
      formData = {
        codeTemplateId: this.templateId,
        templateName: this.templateName,
        paramGroupName: this.editData.paramGroupName,
        paramGroupNameCn: this.editData.paramGroupNameCn,
        description: this.editData.description,
        isRequired: this.editData.isRequired.toString(),
      }
    } else {
      formData = {
        codeTemplateId: this.templateId,
        templateName: this.templateName,
        // isRequired: 'false'
      }
    }
    this.addOrEditForm.setFieldsValue(formData)
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  methods: {
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = '/proj/paramgroup'
        const data = this.addOrEditForm.getFieldsValue()
        if (this.dataId) {
          url = url + '/' + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.successed) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          // 检查参数组是否已经存在
          if (this.grpList !== null && this.grpList.length > 0) {
            if (
              this.grpList.some((item) => {
                if (
                  item.paramGroupName ===
                  this.addOrEditForm.getFieldsValue().paramGroupName
                ) {
                  this.$message.info('分组已存在,请重新输入！')
                  return true
                }
              })
            ) {
              return false
            }
          }
          const rst = await this.$axios.post(url, data)
          if (rst.data.successed) {
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
    },
  },
}
</script>

<style scoped>
button {
  width: 80px;
  vertical-align: middle;
  font-size: 12px;
  margin: 15px 0;
}

.btns {
  display: flex;
  justify-content: center;
  align-items: center;
}

.page-container {
  height: 100%;
}

.ant-form-item {
  margin-bottom: 15px;
}
</style>

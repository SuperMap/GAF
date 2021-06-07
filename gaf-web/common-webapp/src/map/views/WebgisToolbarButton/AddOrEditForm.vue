<template>
  <div class="page-container">
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="tree-catalog" style="line-height: 15px"
          ><span class="vertical-line">| </span>{{ title }}</a-breadcrumb-item
        >
      </a-breadcrumb>
    </template>
    <div class="page-modal-box">
      <a-form
        :form="addOrEditForm"
        layout="horizontal"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 15 }"
        hide-required-mark
      >
        <a-form-item style="width: 300px" class="float-left" label="工具条">
          <a-input
            v-decorator="[
              'toolbarId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入工具条',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入工具条"
            allow-clear
          />
        </a-form-item>
        <a-form-item style="width: 300px" class="float-right" label="按钮">
          <a-input
            v-decorator="[
              'buttonId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入按钮',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入按钮"
            allow-clear
          />
        </a-form-item>
        <a-form-item style="width: 300px" class="float-left" label="图标">
          <a-input
            v-decorator="['icon']"
            :disabled="operation === 1"
            placeholder="请输入图标"
            allow-clear
          />
        </a-form-item>
        <a-form-item style="width: 300px" class="float-right" label="参数">
          <a-input
            v-decorator="['params']"
            :disabled="operation === 1"
            placeholder="请输入参数"
            allow-clear
          />
        </a-form-item>
        <a-form-item style="width: 300px" class="float-left" label="其它方法">
          <a-select
            v-decorator="['actions']"
            :disabled="operation === 1"
            allow-clear
          >
            <a-select-option value="">-请选择其它方法-</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          style="width: 300px"
          class="float-right"
          label="二次单击关闭"
        >
          <a-radio-group
            v-decorator="['toggle']"
            :disabled="operation === 1"
            button-style="solid"
          >
            <a-radio-button value="true"> 是 </a-radio-button>
            <a-radio-button value="false"> 否 </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item
          style="width: 300px"
          class="float-left"
          label="关闭其他面板"
        >
          <a-radio-group
            v-decorator="['closeOtherPanel']"
            :disabled="operation === 1"
            button-style="solid"
          >
            <a-radio-button value="true"> 是 </a-radio-button>
            <a-radio-button value="false"> 否 </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item style="width: 300px" class="float-right" label="更多属性">
          <a-textarea
            v-decorator="['moreProperties']"
            :disabled="operation === 1"
            placeholder="请输入更多属性"
            auto-size
          />
        </a-form-item>
        <a-form-item style="width: 300px" class="float-left" label="排序序号">
          <a-input-number
            v-decorator="['sortSn']"
            :precision="0"
            :min="1"
            :disabled="operation === 1"
          />
        </a-form-item>
        <a-form-item style="width: 300px" class="float-right" label="描述">
          <a-textarea
            v-decorator="['description']"
            :disabled="operation === 1"
            placeholder="请输入描述"
            auto-size
          />
        </a-form-item>
        <div v-if="operation === 1">
          <a-form-item style="width: 300px" label="创建时间" class="float-left">
            <a-date-picker
              v-decorator="['createdTime']"
              show-time
              placeholder="请选择创建时间"
              disabled
            />
          </a-form-item>
          <a-form-item style="width: 300px" label="创建人" class="float-right">
            <a-input
              v-decorator="['createdBy']"
              placeholder="请输入创建人"
              allow-clear
              disabled
            />
          </a-form-item>
          <a-form-item style="width: 300px" class="float-left" label="修改时间">
            <a-date-picker
              v-decorator="['updatedTime']"
              show-time
              placeholder="请选择修改时间"
              disabled
            />
          </a-form-item>
          <a-form-item style="width: 300px" class="float-right" label="修改人">
            <a-input
              v-decorator="['updatedBy']"
              placeholder="请输入修改人"
              allow-clear
              disabled
            />
          </a-form-item>
        </div>
        <div>
          <a-button
            style="margin-top: 30px; left: 450px"
            class="submit-gray"
            type="primary"
            :loading="loading"
            @click="submitForm"
          >
            确定
          </a-button>
          <button
            style="top: 30px; right: 15px"
            class="cancel-modal float-right"
            @click="backToList"
            >取消</button
          >
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
      default: '',
    },
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
      dataId: '',
      loading: false,
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.toolbarButtonId
    delete copyData.toolbarButtonId
    delete copyData.status
    if (this.operation !== 1) {
      delete copyData.createdBy
      delete copyData.updatedBy
      delete copyData.createdTime
      delete copyData.createdBy
    }
    if (copyData.createdTime)
      copyData.createdTime = moment(new Date(copyData.createdTime))
    if (copyData.updatedTime)
      copyData.updatedTime = moment(new Date(copyData.updatedTime))
    this.addOrEditForm.setFieldsValue({ ...copyData })
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/map/webgis-toolbar-buttons/`
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
    },
  },
}
</script>

<style scoped>
.ant-col-5 {
  width: 32%;
}
</style>

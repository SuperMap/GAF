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
        <a-form-item label="字段名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'fieldName',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入名称'
                  },
                  {
                    max: 30,
                    message: '长度不能超过30个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="字段别名">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'fieldAlias',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入名称'
                  },
                  {
                    max: 30,
                    message: '长度不能超过30个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入别名"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="字段类型">
          <a-tree-select
            v-decorator="[
              'fieldType',
              {
                rules: [
                  {
                    required: true,
                    message: '字段类型不能为空'
                  }
                ],
              }
            ]"
            :treeData="treeData"
            placeholder="请选择字段类型"
            tree-node-filter-prop="title"
            :replaceFields="{children:'children', title:'name', key:'code' }"
            show-search
            tree-default-expand-all
            allow-clear
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item label="默认值">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'fieldDefault',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入字典值'
                  },
                  {
                    max: 50,
                    message: '长度不能超过50个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入值"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="字段长度">
          <a-input-number
            :precision="0"
            :min="1"
            v-decorator="['fieldLength']"
          />
        </a-form-item>
        <a-form-item label="字段精度">
          <a-input-number
            :precision="0"
            :min="1"
            v-decorator="['fieldPrecision']"
          />
        </a-form-item>
        <a-form-item label="排序">
          <a-input-number
            :precision="0"
            :min="1"
            v-decorator="['sortSn']"
          />
        </a-form-item>
        <a-form-item label="非空">
          <a-switch
            :disabled="operation === 1"
            v-decorator="['fieldNotNull',{valuePropName: 'checked',initialValue: false}]"
          >
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-form-item>
        <a-form-item label="主键">
          <a-switch
            :disabled="operation === 1"
            v-decorator="['fieldPrimaryKey',{valuePropName: 'checked',initialValue: false}]"
          >
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea
            :disabled="operation === 1"
            :rows="4"
            v-decorator="[
              'description',
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
      <div class="drawer-footer-div">
      <button @click="backToList" class="cancel-modal">{{this.operation === 1 ? "返回" : "取消"}}</button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
        确定
      </a-button>
      </div>
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
    },
    tableId: {
      type: String,
      default: ''
    },
    treeData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dataId: '',
      loading: false,
      
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
    // this.getTreeData()
    // if (mainCompent.modelData.modelType !== 'sdx')
  },
  mounted() {
    console.log(this.mainCompent, 'modelData')
    const copyData = { ...this.editData }
    this.dataId = copyData.fieldId
    delete copyData.status
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
        let url = `/data-mgt/model-manage/fields/`
        const data = this.addOrEditForm.getFieldsValue()
        data['tableId'] = this.tableId
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
    },
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

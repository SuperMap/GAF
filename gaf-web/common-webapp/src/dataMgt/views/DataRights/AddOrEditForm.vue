<template>
  <div class="page-container">
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    <div class="page-container-box">
      <a-form
        :form="addOrEditForm"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 13 }"
        layout="horizontal"
      >
        <a-form-item label="标识">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'dataPermissionId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入标识'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入标识"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入名称'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="类型">
          <a-select
            :disabled="operation === 1"
            v-decorator="[
              'type',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择类型'
                  }
                ],
              }
            ]"
            placeholder="请选择类型"
            @change="onSelectChange"
          >
            <a-select-option
              v-for="i in typeList"
              :key="i.value"
              :value="i.value"
            >
              {{ i.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="规则/受限字段">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'value',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入名称'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item v-if="openTable" label="表作用域">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'tableScope',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入表作用域'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入表作用域"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="mapperid作用域">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'mapperidScope',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入mapperid作用域'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入mapperid作用域"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            :disabled="operation === 1"
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
        <div v-show="operation === 1">
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
              v-decorator="['createdBy']"
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
              v-decorator="['updatedBy']"
              placeholder="请输入修改人"
              allow-clear
              disabled
            />
          </a-form-item>
        </div>
        <div class="btn-div">
          <a-button @click="submitForm" type="primary" :loading="loading" v-show="operation !== 1" class="submit-gray">
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
    componentId: {
      type: String,
      default: ''
    },
    moduleGrpId: {
      type: String,
      default: ''
    },
    moduleGroupPath: {
      type: Array,
      default: () => []
    },
    dataOfTree: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dataId: '',
      typeList: [
        {
          name: '查询规则',
          value: 0
        },
        {
          name: '字段权限',
          value: 1
        }
      ],
      loading: false,
      openTable: true
    }
  },
  computed: {
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.resourceModuleId
    delete copyData.resourceModuleId
    delete copyData.status
    if (copyData.createdTime)
      copyData.createdTime = moment(new Date(copyData.createdTime))
    if (copyData.updatedTime)
      copyData.updatedTime = moment(new Date(copyData.updatedTime))
    this.addOrEditForm.setFieldsValue({ ...copyData })
    this.addOrEditForm.setFieldsValue({
      sysComponentId: this.componentId,
      moduleCatalogId: this.moduleGrpId
    })
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = '/authority/auth-data-permissions/'
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
    onSelectChange(value) {
      console.log(value)
      if(value === 1) {
        this.openTable = false
      } else {
        this.openTable = true
      }
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

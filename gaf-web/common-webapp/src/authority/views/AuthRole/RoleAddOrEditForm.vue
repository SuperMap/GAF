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
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 18 }"
        layout="horizontal"
      >
        <a-form-item v-show="false" label="所属租户id">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'tenantId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入所属租户id'
                  }
                ]
              }
            ]"
            allow-clear
          />
        </a-form-item>
        <a-form-item v-show="false" label="角色分组id">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'roleCatalogId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入API组id'
                  }
                ]
              }
            ]"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="角色分组路径">
          <!-- <a-input :disabled="true" v-model="roleGroupPathName" /> -->
          <a-tree-select
            :disabled="operation !== 3"
            :tree-data="dataOfTree"
            placeholder="请选择分组"
            :replaceFields="{children:'children',title:'title',key:'key',value: 'key'}"
            v-decorator="['roleCatalogId']"
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item label="名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'roleName',
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
        <a-form-item label="编码">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="[
              'code',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入编码'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入编码"
            auto-size
          />
        </a-form-item>
        <a-form-item label="类型">
          <a-select
            :disabled="true"
            v-decorator="[
              'type',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择类型'
                  }
                ],
                initialValue: '3'
              }
            ]"
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
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 1"
            :precision="0"
            :min="1"
            v-decorator="[
              'sortSn',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入排序序号'
                  }
                ]
              }
            ]"
          />
        </a-form-item>
        <a-form-item v-show="operation === 1" label="创建时间">
          <a-date-picker v-decorator="['createdTime']" show-time disabled />
        </a-form-item>
        <a-form-item v-show="operation === 1" label="创建人">
          <a-input v-decorator="['createdBy']" allow-clear disabled />
        </a-form-item>
        <a-form-item v-show="operation === 1" label="修改时间">
          <a-date-picker v-decorator="['updatedTime']" show-time disabled />
        </a-form-item>
        <a-form-item v-show="operation === 1" label="修改人">
          <a-input v-decorator="['updatedBy']" allow-clear disabled />
        </a-form-item>
        
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
    tenantId: {
      type: String,
      default: ''
    },
    roleGrpId: {
      type: String,
      default: ''
    },
    roleGroupPath: {
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
          name: '组件内置',
          value: '1'
        },
        {
          name: '平台级',
          value: '2'
        },
        {
          name: '租户级',
          value: '3'
        }
      ],
      loading: false,
    }
  },
  computed: {
    roleGroupPathName: function() {
      if (this.roleGroupPath && this.roleGroupPath.length > 0) {
        const pathName = this.roleGroupPath
          .map(function(item) {
            return item.title
          })
          .join('/')
        return pathName
      } else {
        return '无'
      }
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = this.editData
    this.dataId = copyData.roleId
    delete copyData.roleId
    if (copyData.createdTime) {
      copyData.createdTime = moment(new Date(copyData.createdTime))
    }
    if (copyData.updatedTime) {
      copyData.updatedTime = moment(new Date(copyData.updatedTime))
    }
    this.addOrEditForm.setFieldsValue({ ...copyData })
    this.addOrEditForm.setFieldsValue({
      tenantId: this.tenantId,
      roleCatalogId: this.roleGrpId,
      groupPath: this.groupPath
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
        let url = '/authority/auth-roles/'
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
    }
  }
}
</script>

<style scoped>
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

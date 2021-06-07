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
        <a-form-item v-show="false" label="是否为空间数据库">
          <a-switch
            v-decorator="[
              'isSdx',
              { valuePropName: 'checked', initialValue: false }
            ]"
            checked-children="是"
            un-checked-children="否"
          />
        </a-form-item>
        <a-form-item label="数据源名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'dsName',
              {
                rules: [
                  {
                    required: true,
                    message: '数据源名称不能为空'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入数据源名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="类型">
          <a-select
            :disabled="operation === 1"
            v-decorator="[
              'typeCode',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择类型'
                  }
                ],
                initialValue: 'POSTGRESQL'
              }
            ]"
            :options="options"
            placeholder="请选择类型"
            default-active-first-option
          >
          </a-select>
        </a-form-item>
        <a-form-item label="地址">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'addr',
              {
                rules: [
                  {
                    required: true,
                    message: '地址不能为空'
                  },
                  {
                    max: 500,
                    message: '长度不能超过500个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入地址"
            allow-clear
          />
        </a-form-item>
        <!-- <a-form-item label="端口">
          <a-input-number
            :disabled="operation === 1"
            v-decorator="[
              'port',
              {
                rules: [
                  {
                    required: true,
                    message: '端口不能为空'
                  }
                ]
              }
            ]"
          />
        </a-form-item> -->
        <a-form-item label="数据库名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'dbName',
              {
                rules: [
                  {
                    required: true,
                    message: '数据库名称不能为空'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入数据库名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="用户名">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'userName',
              {
                rules: [
                  {
                    required: true,
                    message: '用户名不能为空'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入用户名"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="密码">
          <a-input-password
            :disabled="operation === 1"
            v-decorator="[
              'password',
              {
                rules: [
                  {
                    required: true,
                    message: '密码不能为空'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入密码"
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
          <a-button
            @click="testConnect"
            type="primary"
            :loading="loading1"
            class="submit-gray"
          >
            测试连接
          </a-button>
          &nbsp;&nbsp;
          <a-button @click="submitForm" type="primary" :loading="loading2" class="submit-gray">
            确定
          </a-button>
          &nbsp;&nbsp;
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
    }
  },
  data() {
    return {
      dataId: '',
      options: [
        { value: 'POSTGRESQL', label: 'POSTGRESQL' },
        { value: 'MYSQL', label: 'MYSQL' },
        { value: 'ORACLE', label: 'ORACLE' },
        { value: 'SQLSERVER', label: 'SQLSERVER' }
      ],
      loading1: false,
      loading2: false,
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.datasourceId
    delete copyData.datasourceId
    delete copyData.sortSn
    delete copyData.status
    delete copyData.tenantId
    if (this.operation !== 1) {
      delete copyData.createdTime
      delete copyData.updatedTime
      delete copyData.createdBy
      delete copyData.updatedBy
    }
    if (this.editData.createdTime)
      this.editData.createdTime = moment(new Date(this.editData.createdTime))
    if (this.editData.updatedTime)
      this.editData.updatedTime = moment(new Date(this.editData.updatedTime))
    this.addOrEditForm.setFieldsValue({ ...copyData })
  },
  methods: {
    moment,
    testConnect() {
      this.addOrEditForm.validateFields(async err => {
        if (!err) {
          const url =
            '/sys-mgt/sys-resource-datasources/connection-param/check'
          const data = this.addOrEditForm.getFieldsValue()
          this.loading1 = true
          const res = await this.$axios.$post(url, {
            datasourceType: data.typeCode,
            host: data.addr,
            port: data.port,
            username: data.userName,
            password: data.password,
            databaseOrServiceName: data.dbName
          })
          if (res.isSuccessed) {
            this.$message.success('测试连接成功')
          } else {
            this.$message.error(`${res.message}`)
          }
          this.loading1 = false
        }
      })
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/sys-mgt/sys-resource-datasources/`
        const data = this.addOrEditForm.getFieldsValue()
        this.loading2 = true
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
        this.loading2 = false
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

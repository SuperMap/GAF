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
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 17 }"
        hide-required-mark
      >
        <a-form-item v-if="isSdx" label="数据源类型">
          <a-tree-select
            v-decorator="[
              'type',
              {
                rules: [
                  {
                    required: true,
                    message: '数据源类型不能为空'
                  }
                ],
              }
            ]"
            @change="typeChange"
            :treeData="treeData"
            placeholder="请选择数据源类型"
            tree-node-filter-prop="title"
            show-search
            tree-default-expand-all
            allow-clear
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item v-if="isSdx" label="是否为数据源模板">
          <a-switch v-model="isTemplate" checked-children="是" un-checked-children="否"/>
        </a-form-item>
        <!-- <a-form-item label="地址">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'server',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入地址'
                  },
                  {
                    max: 30,
                    message: '长度不能超过30个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入地址"
            allow-clear
          />
        </a-form-item> -->
        <a-form-item label="数据源">
           <a-tree-select
            v-decorator="[
              'datasourceId',
              {
                rules: [
                  {
                    required: true,
                    message: '数据源类型不能为空'
                  }
                ],
              }
            ]"
            :treeData="dbNameTreeData"
            :replaceFields="{children:'children', title:'dsName', key:'datasourceId' , value: 'datasourceId'}"
            placeholder="请选择数据源类型"
            tree-node-filter-prop="title"
            show-search
            tree-default-expand-all
            allow-clear
          />
        </a-form-item>
        <!-- <a-form-item label="用户名">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'username',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入用户名'
                  },
                  {
                    max: 50,
                    message: '长度不能超过50个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入用户名"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="密码">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'password',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入密码'
                  },
                  {
                    max: 50,
                    message: '长度不能超过50个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入密码"
            allow-clear
          />
        </a-form-item> -->
        <a-form-item v-if="!showPname" label="物理表名">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'physicsName',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入物理表名'
                  },
                  {
                    max: 30,
                    message: '长度不能超过30个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入物理表名"
            allow-clear
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
      type: [String, Array],
      default: ''
    },
  },
  inject: ['mainCompent'],
  data() {
    return {
      dataId: '',
      loading: false,
      treeData: [],
      dbNameTreeData: [],
      isTemplate: false,
      typeValue: ''
    }
  },
  watch: {
    isTemplate: function() {
      console.log(this)
      if (this.typeValue) {
        this.getDbNameTreeData()
      }
    }
  },
  computed: {
    isSdx() {
      return this.mainCompent.modelData.modelType === 'sdx'
    },
    showPname() {
      return this.tableId instanceof Array
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
    this.getTreeData()
    if (!this.isSdx) {
      this.getDbNameTreeData()
    }
  },
  mounted() {
    console.log(this.mainCompent, 'modelData')
    const copyData = { ...this.editData }
    this.dataId = copyData.fieldId
    delete copyData.dataDictId
    delete copyData.status
    delete copyData.tenantId
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
        let url = `/data-mgt/model-manage/physics/physicalization-batch`
        const data = this.addOrEditForm.getFieldsValue()
        if (this.mainCompent.modelData.modelType !== 'sdx') {
          data.type = this.mainCompent.modelData.modelType
        }
        const physicalData = []
        if (this.tableId instanceof Array) {
          this.tableId.forEach(element => {
            physicalData.push({tableId: element, ...data})
          });
        } else {
          data['tableId'] = this.tableId
          physicalData.push(data)
        }
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
          const rst = await this.$axios.post(url, physicalData)
          if (rst.data.data.failed.length === 0) {
            this.$message.success('添加成功')
          } else {
            let errorMessage = ''
            rst.data.data.failed.forEach(item => {
              errorMessage += `${item.mmPhysics.physicsName}:${item.message}\n`
            })
            this.$message.error(`添加失败,原因:${rst.data.message}${errorMessage}`)
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
    async getTreeData() {
      const url = `/sys-mgt/sys-dicts/DataSourceType/tree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.treeData = res.data
      } else {
        this.$message.error('加载API分组树失败,原因：' + res.message)
      }
    },
    typeChange(value) {
      console.log(value)
      this.typeValue = value
      this.getDbNameTreeData()
    },
    async getDbNameTreeData() {
      let url = `/data-mgt/sys-resource-datasources?typeCodes=${this.typeValue}&isSdx=${this.isSdx}&pageNum=0&pageSize=0`
      if(!this.isSdx) {
        url = `/data-mgt/sys-resource-datasources?typeCodes=${this.mainCompent.modelData.modelType}&isSdx=${this.isSdx}&pageNum=0&pageSize=0`
      }
      if (this.isTemplate) {
        url = url + "&isTemplate=" + this.isTemplate
      }
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        console.log(res.data)
        this.dbNameTreeData = res.data.content
      } else {
        this.$message.error('加载API分组树失败,原因：' + res.message)
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

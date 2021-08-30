<template>
  <div class="page-container">
    <div class="grid-container">
    <div class="drawer-content">
      <a-form
        :form="addOrEditForm"
        layout="horizontal"
        :label-col="{ span: 2 }"
        :wrapper-col="{ span: 19 }"
        hide-required-mark
      >
        <a-form-item label="列">
          <a-textarea
            :disabled="operation === 1"
            :rows="4"
            v-decorator="[
              'fields',
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
            readonly
          />
        </a-form-item>
        <a-form-item label="其他信息">
          <a-textarea
            readonly
            :disabled="operation === 1"
            :rows="4"
            v-decorator="[
              'otherInfo',
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
    data: {
      type: Object,
      default: null
    }
  },
  inject: ['infoBox'],
  data() {
    return {
      dataId: '',
      loading: false,
      
    }
  },
  watch: {
    fieldsInfo: function(newValue) {
      console.log('bianle')
      this.addOrEditForm.setFieldsValue({ fields: newValue})
    }
  },
  computed: {
    fieldsInfo: function() {
      let fieldsInfo = ''
      this.data.spatialInfo.fields.forEach(item => {
        fieldsInfo = fieldsInfo +  item.fieldName + '\n'
      })
      console.log('bbb')
      return fieldsInfo
    },
    otherInfo: function() {
      let otherInfo = ''
      if(this.infoBox.data.spatialInfo.prjCoordSys) {
        otherInfo = 
    `坐标系: ${this.infoBox.data.spatialInfo.prjCoordSys.name} 
边界:
  bottom: ${this.infoBox.data.spatialInfo.basic.bounds.bottom} 
  left: ${this.infoBox.data.spatialInfo.basic.bounds.bottom} 
  right: ${this.infoBox.data.spatialInfo.basic.bounds.right} 
  top: ${this.infoBox.data.spatialInfo.basic.bounds.top}`
      }
      return otherInfo
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
    // this.getTreeData()
    // if (mainCompent.modelData.modelType !== 'sdx')
  },
  mounted() {
    console.log(this.infoBox, 'modelData')
    const copyData = {}
    copyData['fields'] = this.fieldsInfo
    copyData['otherInfo'] =  this.otherInfo
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
.drawer-content {
  height: auto;
}
</style>

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
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="表名称">
          <a-input
            v-decorator="[
              'tableName',
              {
                rules: [
                  {
                    required: true,
                    message: '字典编码不能为空'
                  },
                  {
                    max: 30,
                    message: '长度不能超过30个字符'
                  },
                  {
                    pattern: /^[^\u4e00-\u9fa5]+$/,
                    message: '不能使用中文字符',
                  },
                  {
                    pattern: /^[a-z0-9]+$/,
                    message: '只能小写字母和数字',
                  },
                ]
              }
            ]"
            placeholder="一般为英文"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            v-decorator="['sortSn']"
            :precision="0"
            :min="1"
          />
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
        <div v-if="isSdx">
        <a-form-item  label="类型">
          <a-select
            v-decorator="[
              'sdxInfo.type',
              {
                rules: [
                  {
                    required: true,
                    message: '类型不能为空'
                  }
                ],
              }
            ]"
            :options="options"
            placeholder="请选择字段类型"
            allow-clear
            @change="selectChange"
          >
          </a-select>
        </a-form-item>
        <a-form-item label="编码类型">
          <a-select
            v-decorator="[
              'sdxInfo.encodeType',
              {
                rules: [
                  {
                    required: true,
                    message: '类型不能为空'
                  }
                ],
              }
            ]"
            :options="encodeTypeOptions"
            placeholder="请选择字段类型"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="坐标系">
          <!-- <a-select
            v-decorator="[
              'sdxInfo.PrjCoordSys',
              {
                rules: [
                  {
                    required: true,
                    message: '坐标系不能为空'
                  }
                ],
              }
            ]"
            :options="optionsPrjCoordSys"
            placeholder="请选择字段类型"
            allow-clear
          /> -->
          <a-tree-select
            v-decorator="[
              'sdxInfo.prjCoordSys',
              {
                rules: [
                  {
                    required: true,
                    message: '坐标系不能为空'
                  }
                ],
              }
            ]"
            :treeData="optionsPrjCoordSys"
            placeholder="请选择坐标系"
            tree-node-filter-prop="title"
            :replaceFields="{children:'children', title:'title', key:'key', value: 'value'}"
            :tree-default-expanded-keys="['1']"
            show-search
            allow-clear
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item v-if="isGridOrImage" label="高级设置">
          <a-button @click="sdxClick">高级设置</a-button>
        </a-form-item>
        </div>
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
      <button v-if="operation === 2" @click="handleBack" class="cancel-modal">
        取消
      </button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
        {{submitButtonText}}
      </a-button>
      </div>
    </div>
    </div>
    <a-modal
      v-model="isShow"
      :width="800"
      title="新建数据集"
      @ok="handleOk"
      @cancel="handleCancel"
      destroy-on-close
    >
      <table-form-special 
        ref="tableFormSpecial" 
        :selectValue="selectValue"
        @backTableForm="backTableForm"
        :ExtendedpProperties="ExtendedpProperties"
      ></table-form-special>
    </a-modal>
  </div>
</template>

<script>
  import moment from 'moment'
  import tableFormSpecial from './TableForm-special'
  export default {
    components: {
      tableFormSpecial
    },
  props: {
    modelId: {
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
    optionsPrjCoordSys: {
      type: Array,
      default: () => []
    }
  },
  inject: ['mainCompent'],
  computed: {
    submitButtonText() {
      if (this.operation === 3) {
        return '保存'
      } else if (this.operation === 2) {
        return '添加'
      } else {
        return ''
      }
    },
    title() {
      if (this.operation === 3) {
        return '编辑表'
      } else if (this.operation === 2) {
        return '新增表'
      } else {
        return ''
      }
    },
    isSdx() {
      return this.mainCompent.modelData.modelType === 'sdx'
    },
  },
  data() {
    return {
      isShow: false,
      isGridOrImage: false,
      selectValue: '',
      dataId: '',
      treeData: [],
      loading: false,
      open: false,
      titlePhysicalization: '',
      options: [
        {value: 'POINT', label: '点'},
        {value: 'LINE', label: '线'},
        {value: 'REGION', label: '面'},
        {value: 'TEXT', label: '文本'},
        {value: 'CAD', label: 'CAD'},
        {value: 'TABULAR', label: '属性表'},
        {value: 'POINT3D', label: '三维点'},
        {value: 'LINE3D', label: '三维线'},
        {value: 'REGION3D', label: '三维面'},
        {value: 'MODEL', label: '模型'},
        {value: 'IMAGE', label: '影像'},
        {value: 'GRID', label: '栅格'},
        {value: 'MOSAIC', label: '镶嵌'},
      ],
      ExtendedpProperties: {
        //像素分块
        blockSizeOption: 'BS_64',
        //边界
        bounds: {
          bottom: -200,
          left: -200,
          top: 200,
          right: 200
        },
        //编码方式
        encodeType: 'NONE',
        //最大值
        maxValue: 10000,
        //最小值
        minValue: -1000,
        //名称
        name: '',
        //空值
        noValue: -9999,
        //像素格式
        pixelFormat: 'DOUBLE',
        //X分辨率
        width: 800,
        //Y分辨率
        height: 800,
        //波段数
        BandCount: 1

      },
      encodeTypeOptions: [
        {value: 'NONE', label: '未编码'},
        {value: 'DCT', label: 'DCT'},
        {value: 'SGL', label: 'SGL'},
        {value: 'LZW', label: 'LZW'}
      ]
    }
  },
  watch: {
    editData: function(newEditData) {
      console.log(newEditData)
      this.dataId = newEditData.tableId
      delete newEditData.tableId
      delete newEditData.createdTime
      delete newEditData.updatedTime
      delete newEditData.createdBy
      delete newEditData.updatedBy
      if (this.isSdx && this.operation === 3) {
        newEditData.sdxInfo = JSON.parse(newEditData.sdxInfo)
        
        if (newEditData.sdxInfo.type === 'IMAGE' || newEditData.sdxInfo.type === 'GRID') {
          this.ExtendedpProperties = newEditData.sdxInfo
          this.isGridOrImage = true
        } else {
          this.isGridOrImage = false
        }
      }
      this.addOrEditForm.setFieldsValue({ ...newEditData })

    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.tableId
    delete copyData.tableId
    delete copyData.status
    delete copyData.createdTime
    delete copyData.updatedTime
    delete copyData.createdBy
    delete copyData.updatedBy
    if (this.isSdx && this.operation === 3) {
      copyData.sdxInfo = JSON.parse(copyData.sdxInfo)
      
      if (copyData.sdxInfo.type === 'IMAGE' || copyData.sdxInfo.type === 'GRID') {
        this.ExtendedpProperties = copyData.sdxInfo
        this.isGridOrImage = true
      }
    }
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
        let url = `/data-mgt/model-manage/logic-tables/`
        const data = this.addOrEditForm.getFieldsValue()
        data['modelId'] = this.modelId
        if (this.isSdx) {
          if (data.sdxInfo.type === 'IMAGE' || data.sdxInfo.type === 'GRID' ) {
            data.sdxInfo = {...data.sdxInfo, ...this.ExtendedpProperties}
          }
          data.sdxInfo = JSON.stringify(data.sdxInfo)
        }
        this.loading = true
        if (this.dataId) {
          url = url  + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
            this.$emit('update-success', {...rst.data.data})
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
            this.$emit('add-success', {...rst.data.data})
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.loading = false
        this.addOrEditForm.resetFields()
        this.$emit('back')
      })
    },
    async deleteData() {
      if (this.dataId) {
          const url = `/data-mgt/model-manage/logic-tables/` + this.dataId
          const rst = await this.$axios.delete(url)
          if (rst.data.isSuccessed) {
            this.$message.success('删除成功')
            this.clear()
            this.$emit('delete-success', {...rst.data.data})
          } else {
            this.$message.error('删除失败，原因: ' + rst.data.message)
          }
      }
    },
    resetFormFields() {
      this.addOrEditForm.resetFields()
    },
    clear() {
      this.dataId = null
      this.resetFormFields()
    },
    handleBack() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    },
    selectChange(value) {
      if (value === 'IMAGE' || value === 'GRID') {
        if (value === 'IMAGE')  {
          this.ExtendedpProperties.pixelFormat = 'RGBA'
        }
        this.ExtendedpProperties.name = 'New_' + Math.random().toFixed(4) * 10000
        this.selectValue = value
        this.isGridOrImage = true
        this.isShow = true
      } else {
        this.isGridOrImage = false
      }
    },
    sdxClick() {
      this.isShow = true
    },
    handleOk() {
      this.$refs.tableFormSpecial.submitForm()
    },
    handleCancel() {
      this.$refs.tableFormSpecial.handleBack()
    },
    backTableForm(data) {
      this.isShow = false
      this.ExtendedpProperties = data
    },
  }
}
</script>

<style lang="less" scoped>
.page-container {
  width: 100%;
  height: 100%;
}

.page-container-box {
  height: 100%;
}

// .btn-div {
//   text-align: center;
//   margin: 15px 0;
// }

.form-foot {
  position: relative;
  left: 32%;
}
</style>

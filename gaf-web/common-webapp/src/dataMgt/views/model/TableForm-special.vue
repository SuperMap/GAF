<template>
  <div class="page-container">
    <div class="grid-container">
    <div class="drawer-content">
      <a-form
        :form="addOrEditForm"
        layout="horizontal"
        :label-col="{ span: 7 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-row>
          <a-col :span="11">
            <span class="colHead">基本信息</span>
            <a-form-item label="数据集名称">
              <a-input
                v-decorator="[
                  'name',
                  {
                    rules: [
                      {
                        required: true,
                        message: '数据集名称不能为空'
                      },
                      {
                        max: 30,
                        message: '长度不能超过30个字符'
                      },
                    ]
                  }
                ]"
                placeholder="请输入数据集名称"
                allow-clear
              />
            </a-form-item>
            
          </a-col>
          <a-col :span="11">
            <span class="colHead">分辨率信息</span>
            <a-form-item label="X分辨率">
              <a-input-number
                v-decorator="[
                  'widthX',
                  {
                    rules: [
                      {
                        required: true,
                        message: 'X分辨率不能为空'
                      }
                    ],
                    initialValue: 5
                  }
                ]"
                @change="changeX"
                :precision="2"
                :min="0.01"
                placeholder="请输入X分辨率"
                allow-clear
              />
            </a-form-item>
            <a-form-item label="Y分辨率">
              <a-input-number
                v-decorator="[
                  'heightY',
                  {
                    rules: [
                      {
                        required: true,
                        message: 'Y分辨率不能为空'
                      }
                    ],
                    initialValue: 0.5
                  }
                ]"
                :precision="2"
                :min="0.01"
                placeholder="请输入Y分辨率"
                @change="changeY"
                allow-clear
              />
            </a-form-item>
            <a-form-item label="行数">
              <a-input-number
                v-decorator="[
                  'height',
                  {
                    rules: [
                      {
                        required: true,
                        message: '行数不能为空'
                      }
                    ],
                    initialValue: 800
                  }
                ]"
                :disabled="true"
                :precision="0"
                :min="1"
                placeholder="请输入行数"
                allow-clear
              />
            </a-form-item>
            <a-form-item label="列数">
              <a-input-number
                v-decorator="[
                  'width',
                  {
                    rules: [
                      {
                        required: true,
                        message: '列数不能为空'
                      }
                    ],
                    initialValue: 800
                  }
                ]"
                :disabled="true"
                :precision="0"
                :min="1"
                placeholder="请输入列数"
                allow-clear
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="11">
            <span class="colHead">数据集范围</span>
            <a-form-item label="左">
              <a-input-number
                v-decorator="['bounds.left', {
                  rules: [
                      {
                        required: true,
                        message: '不能为空'
                      }
                    ]
                }]"
                :precision="1"
              />
            </a-form-item>
            <a-form-item label="下">
              <a-input-number
                v-decorator="['bounds.bottom', {
                  rules: [
                      {
                        required: true,
                        message: '不能为空'
                      }
                    ]
                }]"
                :precision="1"
              />
            </a-form-item>
            <a-form-item label="右">
              <a-input-number
                v-decorator="['bounds.right', {
                  rules: [
                      {
                        required: true,
                        message: '不能为空'
                      }
                    ]
                }]"
                :precision="1"
              />
            </a-form-item>
            <a-form-item label="上">
              <a-input-number
                v-decorator="['bounds.top', {
                  rules: [
                      {
                        required: true,
                        message: '不能为空'
                      }
                    ]
                }]"
                :precision="1"
              />
            </a-form-item>
          </a-col>
          <a-col :span="11">
            <span class="colHead">图像信息</span>
            <a-form-item label="栅格分块">
              <a-select
                v-decorator="[
                  'blockSizeOption',
                  {
                    rules: [
                      {
                        required: true,
                        message: '栅格分块不能为空'
                      }
                    ],
                  }
                ]"
                :options="blockSizeOptionOptions"
                placeholder="请选择栅格分块"
                allow-clear
              />
            </a-form-item>
            <a-form-item label="像素格式">
              <a-select
                v-decorator="[
                  'pixelFormat',
                  {
                    rules: [
                      {
                        required: true,
                        message: '像素格式不能为空'
                      }
                    ],
                  }
                ]"
                :options="pixelFormatOptions"
                placeholder="请选择像素格式"
                allow-clear
              />
            </a-form-item>
            <a-form-item v-if="isGrid"   label="空值">
              <a-input-number
                v-decorator="['noValue', {
                  rules: [
                      {
                        required: true,
                        message: '不能为空'
                      }
                    ]
                }]"
                :precision="0"
                :min="1"
              />
            </a-form-item>
            <a-form-item v-if="isGrid" label="最大值">
              <a-input-number
                v-decorator="['maxValue', {
                  rules: [
                      {
                        required: true,
                        message: '不能为空'
                      }
                    ]
                }]"
                :precision="1"
              />
            </a-form-item>
            <a-form-item v-if="isGrid" label="最小值">
              <a-input-number
                v-decorator="['minValue', {
                  rules: [
                      {
                        required: true,
                        message: '不能为空'
                      }
                    ]
                }]"
                :precision="1"
              />
            </a-form-item>
            <a-form-item v-if="isImage" label="波段数">
              <a-input-number
                v-decorator="['BandCount', {
                  rules: [
                      {
                        required: true,
                        message: '不能为空'
                      }
                    ]
                }]"
                :precision="0"
                :min="1"
              />
            </a-form-item>
          </a-col>
        </a-row>
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
    components: {
    },
  props: {
    selectValue: {
      type: String,
      default: ''
    },
    ExtendedpProperties: {
      type: Object,
      default: null
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
    isImage() {
      return this.selectValue === 'IMAGE' || this.ExtendedpProperties.type === 'IMAGE'
    },
    isGrid() {
      return this.selectValue === 'GRID' || this.ExtendedpProperties.type === 'GRID'
    }
  },
  data() {
    return {
      dataId: '',
      treeData: [],
      loading: false,
      open: false,
      titlePhysicalization: '',
      pixelFormatOptions: [
        {value: 'UBIT1', label: '1位无符号'},
        {value: 'UBIT4', label: '4位无符号'},
        {value: 'UBIT8', label: '8位无符号'},
        {value: 'UBIT16', label: '16位无符号'},
        {value: 'UBIT32', label: '32位无符号'},
        {value: 'BIT8', label: '8位'},
        {value: 'BIT16', label: '16位'},
        {value: 'BIT32', label: '32位'},
        {value: 'BIT64', label: '64位'},
        {value: 'SINGLE', label: '单精度浮点'},
        {value: 'DOUBLE', label: '双精度浮点'},
      ],
      blockSizeOptionOptions: [
        {value: 'BS_64', label: '64*64'},
        {value: 'BS_128', label: '128*128'},
        {value: 'BS_256', label: '256*256'},
        {value: 'BS_512', label: '512*512'},
        {value: 'BS_1024', label: '1024*1024'}
      ]
    }
  },
  watch: {
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    console.log(this.ExtendedpProperties, 's')
    if (this.selectValue === 'IMAGE') {
      this.pixelFormatOptions = this.pixelFormatOptions.concat([{value: 'RGB', label: '24位真彩色'},
        {value: 'RGBA', label: '32位真彩色'}])
    }
    this.ExtendedpProperties.widthX = 400 / this.ExtendedpProperties.width
    this.ExtendedpProperties.heightY = 400 / this.ExtendedpProperties.height
    this.addOrEditForm.setFieldsValue({ ...this.ExtendedpProperties })
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        const data = this.addOrEditForm.getFieldsValue()
        delete data.widthX
        delete data.heightY
        this.loading = false
        this.addOrEditForm.resetFields()
        this.$emit('backTableForm', data)
      })
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
    changeX(value) {
      this.addOrEditForm.setFieldsValue({width: 400 / value})
    },
    changeY(value) {
      this.addOrEditForm.setFieldsValue({height: 400 / value})
    }
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
/deep/ .ant-form.ant-form-horizontal > .ant-row:first-child {
  margin-bottom: 25px
}
/deep/ .ant-row .ant-col-11:first-child {
  margin-right: 20px;
}
/deep/ .ant-col-11 {
  height: 220px;
  border: 1px solid #ccc;
  border-radius: 10px;
  // margin-bottom: 5px;
  padding-top: 15px;
}
/deep/ .ant-form-item {
  margin-bottom: 0px;
}
.colHead {
  display: inline-block;
  position: absolute;
  left: 13px;
  top: -13px;
  background: #fff;
}
/deep/ .ant-input-number {
  width: 100%;
}
</style>

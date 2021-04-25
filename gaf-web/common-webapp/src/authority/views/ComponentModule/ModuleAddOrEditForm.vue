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
        <a-form-item v-show="false" label="所属组件id">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'sysComponentId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入所属组件id'
                  }
                ]
              }
            ]"
            placeholder="请输入所属组件id"
            allow-clear
          />
        </a-form-item>
        <a-form-item v-show="false" label="API分组id">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'moduleCatalogId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入API组id'
                  }
                ]
              }
            ]"
            placeholder="请输入API组id"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="模块分组">
          <a-tree-select
            :disabled="operation !== 3"
            :tree-data="dataOfTree"
            placeholder="请选择分组"
            :replaceFields="replaceFields"
            v-decorator="['moduleCatalogId']"
          >
          </a-tree-select>
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
                  }
                ]
              }
            ]"
            placeholder="请输入名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="英文名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="['nameEn']"
            placeholder="请输入英文名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="路径">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'path',
              {
                rules: [
                  {
                    required: true,
                    message: '路径不能为空'
                  }
                ]
              }
            ]"
            placeholder="请输入路径"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="图标地址">
          <!-- <a-textarea
            :disabled="operation === 1"
            v-decorator="['iconUrl']"
            placeholder="请输入图标地址"
            auto-size
          /> -->
          <gaf-icon-picker v-decorator="['iconUrl']" placeholder="请选择图标"></gaf-icon-picker>
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 1"
            v-decorator="['sortSn', { initialValue: 1 }]"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="['description']"
            placeholder="请输入描述"
            auto-size
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
                initialValue: '1'
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
        <a-form-item label="地址">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="[
              'moduleUrl',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入地址'
                  }
                ]
              }
            ]"
            placeholder="请输入地址"
            auto-size
          />
        </a-form-item>
        <a-form-item label="打开方式">
          <a-radio-group
            :disabled="operation === 1"
            v-decorator="[
              'target',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入地址'
                  }
                ],
                initialValue: '0'
              }
            ]"
            button-style="solid"
          >
            <a-radio-button value="0">
              当前窗口
            </a-radio-button>
            <a-radio-button value="1">
              新窗口打开
            </a-radio-button>
          </a-radio-group>
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
          <button @click="submitForm" v-show="operation !== 1" class="submit-gray">
            确定
          </button>
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
          name: '平台内页面',
          value: '1'
        },
        {
          name: '外部页面',
          value: '2'
        }
      ],
      replaceFields: {
        children:'children',
        title:'title',
        key:'key',
        value: 'key'
      }
    }
  },
  computed: {
    moduleGroupPathName: function() {
      if (this.moduleGroupPath && this.moduleGroupPath.length > 0) {
        const pathName = this.moduleGroupPath
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
        let url = '/authority/auth-resource-modules/'
        const data = this.addOrEditForm.getFieldsValue()
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
        this.addOrEditForm.resetFields()
        this.$emit('submit')
      })
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    },
    addDisabled(data) {
      let datatree = JSON.parse(JSON.stringify(data))
      datatree.forEach(item => {
        if (item.children.length > 0){
          item.disabled = true
          this.addDisabled(item.children)
        }
      })
      return datatree
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

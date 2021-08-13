<template>
  <div class="page-container">
    <div class="grid-container">
      <button @click="clear" class="btn-fun blue btn-16">
          <a-icon type="plus-circle" /><span>添加模型</span>
        </button>
    <div class="drawer-header">
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    </div>
    <div>
      <a-form
        :form="addOrEditForm"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 19 }"
        layout="horizontal"
      >
        <a-form-item label="模型名称">
          <a-input
            v-decorator="[
              'modelName',
              {
                rules: [
                  {
                    required: true,
                    message: '模型名称不能为空'
                  }
                ],
              }
            ]"
            allow-clear
            placeholder="请输入模型名称"
          >
          </a-input>
        </a-form-item>
        <a-form-item label="数据库类型">
          <a-tree-select
            v-decorator="[
              'modelType',
              {
                rules: [
                  {
                    required: true,
                    message: '数据库类型不能为空'
                  }
                ],
              }
            ]"
            :treeData="treeData"
            placeholder="请选择数据库类型"
            tree-node-filter-prop="title"
            show-search
            tree-default-expand-all
            allow-clear
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item label="标识">
          <a-input
            v-decorator="[
              'modelCode',
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
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 'detail'"
            :precision="0"
            :min="1"
            v-decorator="['sortSn']"
          />
        </a-form-item>
        <!-- <a-form-item label="备注">
          <a-textarea
            :disabled="operation === 'detail'"
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
            placeholder="请输入备注"
            auto-size
          />
        </a-form-item> -->
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
        <a-popconfirm
          @confirm="() => deleteData()"
          title="删除后无法恢复，确认是否继续?"
          v-if="operation === 'edit'"
          ok-text="确认"
          cancel-text="取消"
        >
          <button class="cancel-modal">删除</button>
        </a-popconfirm>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <button v-if="operation === 'add'" @click="handleBack" class="cancel-modal">
        取消
      </button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
        {{ submitButtonText }}
      </a-button>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import treeUtil from '../../../common/utils/TreeUtil'
import dictUtil from "../../../common/utils/DictUtil"

import moment from 'moment'

export default {
  props: {
    // 当parentId为'0'或为空时表示一级分类目录
    editData: {
      type: Object,
      default: null
    },
    // add表示新增 edit表示表示编辑 detail表示详情
    operation: {
      type: String,
      default: 'add'
    },
    rootNode: {
      type: String,
      dafault: ''
    }
  },
  data() {
    return {
      dataId: null,
      treeData: [{
        key: "1", 
        dictTypeCode: "DataSourceType", 
        label: "空间数据库", 
        value: "1", 
        seq: 1, 
        visibility: true, 
        dictDesc: null, 
        extProperties: null, 
        disabled: true,
        children: [
          {
            key: "sdx", 
            dictTypeCode: "DataSourceType", 
            label: "sdx", 
            value: "sdx", 
            seq: 1, 
            visibility: true, 
            dictDesc: null, 
            extProperties: null, 
            children: null, 
            level: null, 
            parentId: "1", 
            isLeaf: true
          }
        ], 
        level: null, 
        parentId: "0", 
        isLeaf: false
      },
      {
        key: "2", 
        dictTypeCode: "ORDINARY_DATASOURCE_TYPE", 
        label: "非空间数据库", 
        value: "2", 
        seq: 1, 
        visibility: true, 
        dictDesc: null, 
        extProperties: null, 
        children: null, 
        level: null, 
        parentId: "0", 
        disabled: true,
        isLeaf: false
      }],
      loading: false
    }
  },
  computed: {
    submitButtonText() {
      if (this.operation === 'edit') {
        return '保存'
      } else if (this.operation === 'add') {
        return '添加'
      } else {
        return ''
      }
    },
    title() {
      if (this.operation === 'edit') {
        return '编辑模型'
      } else if (this.operation === 'add') {
        return '新增模型'
      } else {
        return ''
      }
    }
  },
  watch: {
    editData(newValue) {
      const copyData = { ...newValue }
      this.dataId = copyData.modelId
      delete copyData.modelId
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
    }
  },
  created() {
    this.getTreeData()
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    console.log(this.editData)
    const copyData = { ...this.editData }
    this.dataId = copyData.modelId
    delete copyData.modelId
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
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/data-mgt/model-manage/models/`
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        if (this.dataId) {
          url = url + this.dataId
          const { modelId, sortSn, modelType, modelName, modelCode } = data
          const rst = await this.$axios.put(url, {
            modelId,
            sortSn,
            modelType,
            modelName,
            modelCode
          })
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
            this.$emit('update-success', rst.data.data)
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
            this.$emit('add-success', rst.data.data)
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.loading = false
      })
    },
    async deleteData() {
      if (this.dataId) {
          const url = `/data-mgt/model-manage/models/` + this.dataId
          const rst = await this.$axios.delete(url)
          if (rst.data.isSuccessed) {
            this.$message.success('删除成功')
            this.$emit('delete-success', rst.data.data)
            this.clear()
          } else {
            this.$message.error('删除失败，原因: ' + rst.data.message)
          }
      }
    },
    resetFormFields() {
      this.addOrEditForm.resetFields()
    },
    // setFormFields(fieldsValue) {
    //   if (fieldsValue) {
    //     this.addOrEditForm.setFieldsValue(fieldsValue)
    //   }
    // },
    clear() {
      this.$emit('changeOperation')
      this.dataId = null
      this.resetFormFields()
    },
    // 根据树节点key，禁用key对应的节点及其子节点,其他被禁用的节点恢复
    disableNodeWithChlidren(treeList, key ) {
      treeUtil.deepFirstTraverseTree({key: '0'}, treeList, (parentNode, node)=> {
        Object.assign(node, {disabled: false})
      })
      treeUtil.deepFirstTraverseTree({key: '0'}, treeList, (parentNode, node)=> {
        if(node.key === key || parentNode.disabled) {
          Object.assign(node, {disabled: true})
        }
      })
    },
    clearDisable(treeList) {
      treeUtil.deepFirstTraverseTree({key: '0'}, treeList, (parentNode, node)=> {
        Object.assign(node, {disabled: false})
      })
    },
    // async getTreeDataAndSet() {
    //   const catalogNodes = await this.getDicCatalogs()
    //   if(catalogNodes) {
    //     catalogNodes.forEach(element => {
    //       element.value = element.key
    //       element.disabled = false
    //     });
    //     this.treeData = treeUtil.convertToTree({key: '0'}, catalogNodes)
    //   }
    // },
    async getDicCatalogs() {
      const url = `/sys-mgt/sys-catalogs/nodes/type/7`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`查询失败,原因: ${res.message}`)
        return null
      }
    },
    handleBack() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    },
    async getTreeData() {
      const url = `/sys-mgt/sys-dicts/ORDINARY_DATASOURCE_TYPE/tree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        dictUtil.deleteEmptyChildrenAttr(res.data)
        this.treeData[1].children = res.data
      } else {
        this.$message.error('加载API分组树失败,原因：' + res.message)
      }
    },
  }
}
</script>

<style  lang="less" scoped>
.form-foot {
  position: relative;
  left: 30%;
}
.sysbtn-style {
  text-align: center;
  margin: 10px auto;
  width: 100px;
  font-size: 12px;
  cursor: pointer;
}

</style>

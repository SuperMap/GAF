<template>
  <div>
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    <div class="page-modal-box">
      <a-form
        :form="addOrEditForm"
        layout="horizontal"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 15 }"
        hide-required-mark
      >
        <a-form-item label="名称">
          <a-input
            v-model="dataNode.name"
            placeholder="请输入名称+'/'"
            allow-clear
          />
        </a-form-item>

        <!-- <a-form-item v-show="false" label="父级id">
          <a-input
            :disabled="operation === 1"
            v-decorator="['pid']"
            placeholder="请输入父级id"
            allow-clear
          />
        </a-form-item>
         <a-form-item v-show="false" label="字典类别编码">
          <a-input
            :disabled="operation === 1"
            v-decorator="['dictCode']"
            placeholder="请输入字典类别编码"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'dictName',
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
        <a-form-item label="字典值">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'dictValue'            ]"
            placeholder="请输入值"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="序号">
          <a-input-number
            :disabled="operation === 1"
            v-decorator="['seq']"
            :precision="0"
            :min="1"
          />
        </a-form-item>
        <a-form-item label="可见性">
          <a-switch
            :disabled="operation === 1"
            v-decorator="['visibility',{valuePropName: 'checked',initialValue: true}]"
          >
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-form-item>
        <a-form-item label="扩展属性">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="['extProperties']"
            placeholder="请输入json格式字符串"
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="['dictDesc']"
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
          </a-form-item> -->
        <!-- </div> -->
        <div class="btn-div">
          <button @click="submitForm" class="submit-gray">
            确定
          </button>
          &nbsp;&nbsp;&nbsp;
          <button @click="backToList" class="cancel-modal">取消</button>
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
      type: Array,
      default: () => []
    },
    operation: {
      type: Number,
      default: 0
    },
    name: {
      type: String,
      dafault: ''
    }
  },
  data() {
    return {
      dataId: '',
      dataNode: {
        name: '',
        children: [],
        type: "commonPrefix"
      }
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
	const copyData = { ...this.editData }
    this.dataId = copyData.dataDictId
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
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          return false
        }
        // let url = `/sys-mgt/sys-dicts/`
        // const data = this.addOrEditForm.getFieldsValue()
        // if (this.dataId) {
        //   url = url  + this.dataId
        //   const rst = await this.$axios.put(url, data)
        //   if (rst.data.isSuccessed) {
        //     this.$message.success('更新成功')
        //   } else {
        //     this.$message.error(`更新失败,原因:${rst.data.message}`)
        //   }
        // } else {
        //   const rst = await this.$axios.post(url, data)
        //   if (rst.data.isSuccessed) {
        //     this.$message.success('添加成功')
        //   } else {
        //     this.$message.error(`添加失败,原因:${rst.data.message}`)
        //   }
        // }
        if (this.operation === 2){
          this.dataNode.name = this.name + (this.dataNode.name.endsWith('/') ? this.dataNode.name : (this.dataNode.name + '/'))
        }
        this.editData.push(this.dataNode)
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

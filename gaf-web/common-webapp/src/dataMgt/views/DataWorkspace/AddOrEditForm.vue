<template>
  <div class="page-container">
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
        <a-form-item label="工作空间类型">
          <a-cascader
            :disabled="operation === 1 || operation === 3"
            v-decorator="[
              'typeCode',
              {
                rules: [
                  {
                    required: true,
                    message: '工作空间类型不能为空'
                  }
                ]
              }
            ]"
            :options="option"
            placeholder="请选择工作空间类型"
            @change="onChange"
          />
        </a-form-item>
        <a-form-item  v-if="!isfiletype" label="工作空间名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'wsName',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入工作空间名称'
                  }
                ]
              }
            ]"
            placeholder="请输入工作空间名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item  :label="isfiletype ? '工作空间文件' : '服务器地址'">
          <a-input
            :disabled="(operation === 3 || isfiletype) ? true : false"
            v-decorator="[
              'server',
              {
                rules: [
                  {
                    required: true,
                    message: isfiletype ? '请输入工作空间文件' : '请输入服务器地址'
                  }
                ]
              }
            ]"
            :placeholder="isfiletype ? '请输入工作空间文件' : '请输入服务器地址'"
            allow-clear
            :style=" isfiletype ? 'width:85%' : ''"
          />
          <gaf-upload
            :override="operation === 3"
            :path="operation === 3 ? editData.server : undefined"
            v-if="isfiletype"
            accept=".smwu,.smxu"
            text="选择" :dir="dirPath"
            minioServiceUrl="/storage/file-storage/"
            @uploadComplate="uploadChange"
          ></gaf-upload>
        </a-form-item>
        <!-- <a-form-item v-if="!isfiletype" label="服务器地址">
          <a-input
            :disabled="operation === 1 || operation === 3"
            v-decorator="[
              'server',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入服务器地址'
                  }
                ]
              }
            ]"
            placeholder="请输入服务器地址"
            allow-clear
          />
        </a-form-item> -->

        <a-form-item v-if="!isfiletype" label="数据库名称">
          <a-input
            :disabled="operation === 1 || operation === 3"
            v-decorator="[
              'database'            ]"
            placeholder="请输入数据库名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item v-if="!isfiletype" label="用户名">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'userName'            ]"
            placeholder="请输入用户名"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="密码">
          <a-input-password
            :disabled="operation === 1"
            v-decorator="['password']"
            placeholder="请输入密码"
          />
        </a-form-item>
        <div v-if="false">
          <a-form-item label="描述">
            <a-textarea
              :disabled="operation === 1"
              v-decorator="['description']"
              placeholder="请输入描述"
              auto-size
            />
          </a-form-item>
          <a-form-item label="状态">
            <a-radio-group
              :disabled="operation === 1"
              v-decorator="['status']"
              button-style="solid"
            >
              <a-radio-button value="true">
                是
              </a-radio-button>
              <a-radio-button value="false">
                否
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="创建时间">
            <a-date-picker
              :disabled="operation === 1"
              v-decorator="['createdTime']"
              show-time
              placeholder="请选择创建时间"
            />
          </a-form-item>
          <a-form-item label="创建人">
            <a-input
              :disabled="operation === 1"
              v-decorator="[
                'createdBy'            ]"
              placeholder="请输入创建人"
              allow-clear
            />
          </a-form-item>
          <a-form-item label="修改时间">
            <a-date-picker
              :disabled="operation === 1"
              v-decorator="['updatedTime']"
              show-time
              placeholder="请选择修改时间"
            />
          </a-form-item>
          <a-form-item label="修改人">
            <a-input
              :disabled="operation === 1"
              v-decorator="[
                'updatedBy'            ]"
              placeholder="请输入修改人"
              allow-clear
            />
          </a-form-item>
        </div>
        <div v-if="operation === 1">
          <a-form-item v-if="operation === 1" label="发布状态">
            <a-switch v-decorator="['published']" checked-children="已发布" un-checked-children="未发布" :checked="editData.published" />
          </a-form-item>
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
        <div class="btn-div">
          <a-button @click="submitForm" class="submit-gray">
            确定
          </a-button>
          <a-button @click="backToList" class="cancel-modal">取消</a-button>
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
    option: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dirPath: 'datas/',
      dataId: '',
      //是否文件类型
      isfiletype: false,
      //回显时级联选择器的路径数据
      optiontypeCode: []
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
    if (this.operation !== 2) {
      //回显时得到级联选择器路径数据  根据字典类别值和具体要得到路径的值
      this.getPath('WorkSpaceType', this.editData.typeCode)
    }
  },
  mounted() {
    //回显数据处理
	const copyData = { ...this.editData }
    this.dataId = copyData.workspaceId
    delete copyData.workspaceId
    delete copyData.tenantId
	if(this.operation !== 1) {
		delete copyData.createdTime
		delete copyData.updatedTime
		delete copyData.createdBy
		delete copyData.updatedBy
	}
  copyData.typeCode = this.optiontypeCode
	if (copyData.createdTime)
    copyData.createdTime = moment(new Date(copyData.createdTime))
  if (copyData.updatedTime)
    copyData.updatedTime = moment(new Date(copyData.updatedTime))
  this.$nextTick(() => {
    this.addOrEditForm.setFieldsValue({ ...copyData })
  })
  if (this.operation !== 2) {
      //回显时得到级联选择器路径数据  根据字典类别值和具体要得到路径的值
      this.getPath('WorkSpaceType', this.editData.typeCode)
    }
  },
  methods: {
    //上传组件uploadComplate事件
    uploadChange(file) {
      this.addOrEditForm.setFieldsValue({
        server: file.name
      })
    },
    moment,
    //表单提交
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/data-mgt/data-workspaces/`
        const data = this.addOrEditForm.getFieldsValue()
        data.typeCode = data.typeCode.slice(-1).join()
        if (this.isfiletype) {
          data.wsName = data.server
          data.server = this.dirPath + data.server
        }
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
        this.addOrEditForm.resetFields()
        this.$emit('submit')
      })
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    },
    //级联选择change方法
    onChange(value) {
      if (value[0] === "file") {
        this.isfiletype = true
      } else {
        this.isfiletype = false
      }
    },
    //回显时得到级联选择器的路径
    async getPath(dictTypeCode, value) {
      const url = `/sys-mgt/sys-dicts/${dictTypeCode}/${value}/path`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.optiontypeCode = res.data.map(item => item.value)
        if (this.optiontypeCode[0] === "file") {
          this.isfiletype = true

        } else {
          this.isfiletype = false
        }
        this.addOrEditForm.setFieldsValue({
          typeCode: this.optiontypeCode,
          server: this.editData.server,
          password: this.editData.password
        })
      } else {
        this.$message.error('加载路径失败,原因：' + res.message)
      }
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
</style>

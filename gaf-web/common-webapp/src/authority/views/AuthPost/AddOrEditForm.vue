<template>
  <div class="page-container">
    <div class="title">
      <template>
        <a-breadcrumb separator=">">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
      </template>
    </div>
    <a-form
      :form="addOrEditForm"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 13 }"
      layout="horizontal"
    >
      <a-form-item v-show="false" label="所属部门id">
        <a-input v-decorator="['departmentId']" allow-clear />
      </a-form-item>
      <a-form-item label="所属部门">
        <a-input
          :disabled="true"
          v-decorator="['departmentName']"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="名称">
        <a-input
          :disabled="operation === 1"
          v-decorator="[
            'postName',
            {
              rules: [
                {
                  required: true,
                  message: '名称不能为空！'
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
      <a-form-item label="排序序号">
        <a-input-number
          :disabled="operation === 1"
          v-decorator="['sortSn']"
          :precision="0"
          :min="1"
        />
      </a-form-item>
      <a-form-item label="英文名称">
        <a-input
          :disabled="operation === 1"
          v-decorator="[
            'nameEn',
            {
              rules: [
                {
                  max: 255,
                  message: '长度不能超过255个字符'
                }
              ]
            }
          ]"
          placeholder="请输入英文名称"
          allow-clear
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
      <div class="btn-div">
        <button @click="submitForm" class="submit-gray">
          {{ operation === 2 ? '新增' : '保存' }}
        </button>
        <button @click="cancelDelete" class="cancel-modal">
          {{ operation === 2 ? '取消' : '删除' }}
        </button>
      </div>
    </a-form>
  </div>
</template>

<script>
export default {
  props: {
    editData: {
      type: Object,
      default: null
    },
    // 2 新增状态 3 编辑状态
    operation: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      dataId: ''
    }
  },
  computed: {
    title() {
      if (this.operation === 3) {
        return '编辑岗位'
      } else if (this.operation === 2) {
        return '新增岗位'
      } else {
        return ''
      }
    }
  },
  watch: {
    editData: function(newEditData) {
      console.log(newEditData,'data')
      this.setEditData(newEditData)
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    this.setEditData(this.editData)
  },
  methods: {
    setEditData(newEditData) {
      this.dataId = newEditData.postId
      delete newEditData.postId
      delete newEditData.code
      delete newEditData.createdBy
      delete newEditData.status
      delete newEditData.tenantId
      delete newEditData.updatedBy
      delete newEditData.updatedTime
      delete newEditData.createdTime
      this.addOrEditForm.setFieldsValue({ ...newEditData })
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/authority/auth-posts`
        const data = this.addOrEditForm.getFieldsValue()
        if (this.dataId) {
          url = url + '/' + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
            this.$emit('updatePostSuccess', rst.data.data)
          } else {
            this.$message.error('更新失败:' + rst.data.message)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
            this.$emit('addPostSuccess', rst.data.data)
          } else {
            this.$message.error('添加失败:' + rst.data.message)
          }
        }
      })
    },
    async cancelDelete() {
      if (this.operation === 2) {
        this.$emit(
          'cancleWhenAddPost',
          this.addOrEditForm.getFieldValue('departmentId')
        )
      } else if (this.operation === 3) {
        if (this.dataId) {
          const departmentId = this.addOrEditForm.getFieldValue('departmentId')
          if (departmentId) {
            const url = `/authority/auth-posts/${departmentId}/${this.dataId}`
            const rst = await this.$axios.delete(url)
            if (rst.data.isSuccessed) {
              this.$message.success('删除成功')
              this.$emit('deletePostSuccess', rst.data.data)
            } else {
              this.$message.error(`删除失败,原因：${rst.data.message}`)
            }
          }
        }
      }
    },
    resetFields() {
      this.addOrEditForm.resetFields()
    }
  }
}
</script>

<style lang="less" scoped>
button {
  width: 80px;
  font-size: 12px;
  cursor: pointer;
  margin-right: 10px;
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
.title {
  font-size: 14px;
  margin-top: 5px;
}
</style>

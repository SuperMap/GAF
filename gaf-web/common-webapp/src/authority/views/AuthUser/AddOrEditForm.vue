<template>
  <div class="page-single">
    <template>
      <a-breadcrumb separator=">">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    <div class="page-container-box">
      <a-form
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 14 }"
        :form="addOrEditForm"
        layout="horizontal"
      >
        <a-row>
          <a-col :span="12">
            <a-form-item label="用户名:">
          <a-input
            :disabled="operation === 1 || operation === 3"
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入用户名'
                  },
                  {
                    validator: (rule, value, callback) => {
                      if (!/^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$/.test(value)) {
                        rule.message = '请输入符合字母数字下划线格式的内容！'
                        callback(rule.message)
                      }
                      callback()
                    }
                  }
                ],
                validateFirst: true
              }
            ]"
            placeholder="请输入登录用户名"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="真实姓名">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'realName',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入真实姓名'
                  },
                  {
                    validator: (rule, value, callback) => {
                      if (!/^[\u4e00-\u9fa5]+$/.test(value)) {
                        rule.message = '请输入符合中文格式的内容！'
                        callback(rule.message)
                      }
                      callback()
                    }
                  }
                ],
                validateFirst: true
              }
            ]"
            placeholder="请输入中文姓名"
            allow-clear
          />
        </a-form-item>
        <a-form-item
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 6 }"
          v-if="operation === 3"
          label="密码"
        >
          <a-button @click="resetPassword">
            重置密码
          </a-button>
        </a-form-item>
        <a-form-item label="邮箱">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'email',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入邮箱'
                  },
                  {
                    validator: emailValidator
                  }
                ],
                initialValue: ''
              }
            ]"
            placeholder="请输入邮箱"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 1"
            :precision="0"
            :min="1"
            v-decorator="['sortSn']"
          />
        </a-form-item>
        <a-form-item label="身份证号">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'idNumber',
              {
                rules: [
                  {
                    required: false,
                    validator: (rule, value, callback) => {
                      if (
                        value &&
                        !/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(value)
                      ) {
                        rule.message = '请输入符合身份证格式的内容！'
                        callback(rule.message)
                      }
                      callback()
                    }
                  }
                ]
              }
            ]"
            placeholder="请输入身份证号"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="手机号">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'mobileNumber',
              {
                rules: [
                  {
                    required: false,
                    validator: (rule, value, callback) => {
                      if (
                        value &&
                        !/^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\d{8}$/.test(
                          value
                        )
                      ) {
                        rule.message = '请输入符合手机号格式的内容！'
                        callback(rule.message)
                      }
                      callback()
                    }
                  }
                ]
              }
            ]"
            placeholder="请输入手机号"
            allow-clear
          />
        </a-form-item>
        
          </a-col>
          <a-col :span="12">
            <a-form-item label="地址">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'address',
              {
                rules: [
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
        <a-form-item v-show="false" label="部门">
          <a-input
            :disabled="true"
            v-decorator="[
              'departmentName',
              {
                rules: [
                  {
                    required: true,
                    message: '部门'
                  }
                ],
                initialValue: ``
              }
            ]"
            allow-clear
            placeholder="部门"
          >
          </a-input>
        </a-form-item>
        <a-form-item label="部门">
          <a-tree-select
            :disabled="operation === 1"
            v-decorator="[
              'departmentId',
              {
                rules: [
                  {
                    required: true,
                    message: '部门不能为空'
                  }
                ]
              }
            ]"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="treeData"
            @change="departmentChange"
            tree-node-filter-prop="title"
            show-search
            style="width: 100%"
            placeholder="请选择一个部门"
            allow-clear
            tree-default-expand-all
          />
        </a-form-item>
            <a-form-item label="岗位">
          <a-select
            :disabled="operation === 1"
            v-decorator="['postId']"
            allow-clear
            placeholder="请选择岗位"
          >
            <a-select-option
              v-for="(item, index) in postList"
              :key="index"
              :value="item.postId"
            >
              {{ item.postName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="授权截止时间">
          <a-date-picker
            :disabled="operation === 1"
            v-decorator="['expirationTime']"
            show-time
            placeholder="请选择授权截止时间"
          />
        </a-form-item>
        <a-form-item label="是否第三方">
          <a-radio-group
            :disabled="operation === 1"
            v-decorator="['isThirdParty', { initialValue: false }]"
            button-style="solid"
          >
            <a-radio-button :value="true">
              是
            </a-radio-button>
            <a-radio-button :value="false">
              否
            </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="状态">
          <a-switch
            v-decorator="[
              'status',
              { valuePropName: 'checked', initialValue: true }
            ]"
            :disabled="operation === 2"
            @click="statusClick"
            checked-children="启用"
            un-checked-children="禁用"
            size="small"
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
        <a-form-item v-show="operation === 3" label="上次登录时间">
          <a-date-picker
            :disabled="true"
            v-decorator="['lastLoginTime']"
            show-time
            placeholder="请选择上次登录时间"
          />
        </a-form-item>
          </a-col>
        </a-row>
        <!-- <div v-if="operation === 1">
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
        </div> -->
        <div class="btn-div">
          <button @click="submitForm" class="submit-gray">
            {{this.operation === 2 ? "确定" : "保存"}}
          </button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <button @click="cancle" class="cancel-modal" v-show="operation !== 3">{{this.operation === 1 ? "返回" : "取消"}}</button>
        </div>
      </a-form>
    </div>
  </div>
</template>

<script>
import moment from 'moment'

export default {
  props: {
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
      postList: [],
      treeData: []
    }
  },
  computed: {
    title() {
      if (this.operation === 3) {
        return '编辑用户'
      } else if (this.operation === 2) {
        return '新增用户'
      } else {
        return ''
      }
    }
  },
  watch: {
    editData: function(newEditData) {
      this.setEditData(newEditData)
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    this.setEditData(this.editData)
    this.getDepartmentTreeAndSet()
  },
  methods: {
    moment,
    convertToTree(nodeList) {
      if (!nodeList || nodeList.length <= 0) {
        return []
      }
      const rootParent = { key: '0' }
      const children = this.getChildren(rootParent, nodeList)
      return children || []
    },
    getChildren(root, all) {
      const collect = all
        .filter(treeNode => {
          return treeNode.parentId === root.key
        })
        .map(function(treeNode) {
          treeNode.children = this.getChildren(treeNode, all)
          return treeNode
        }, this)
        .sort(function(a, b) {
          return a.sortSn - b.sortSn
        })
      return collect
    },
    deepFirstTraverseTree(parentNode, data, callback) {
      if (data) {
        for (let i = 0; i < data.length; i++) {
          const node = data[i]
          callback(parentNode, node)
          if (node.children) {
            this.deepFirstTraverseTree(node, node.children, callback)
          }
        }
      }
    },
    setEditData(newEditData) {
      this.dataId = newEditData.userId
      delete newEditData.userId
      delete newEditData.tenantId
      if (this.operation === 3) {
        delete newEditData.password
      }
      delete newEditData.createdTime
      delete newEditData.createdBy
      delete newEditData.updatedTime
      delete newEditData.updatedBy
      delete newEditData.postName
      if (newEditData.lastLoginTime) {
        newEditData.lastLoginTime = moment(new Date(newEditData.lastLoginTime))
      }
      if (newEditData.expirationTime) {
        newEditData.expirationTime = moment(
          new Date(newEditData.expirationTime)
        )
      }
      if (newEditData.departmentId) {
        this.getPostList(newEditData.departmentId)
      }
      this.addOrEditForm.setFieldsValue({ ...this.editData })
    },
    // 重置密码
    async resetPassword() {
      if (this.dataId) {
        const url = `/authority/auth-users/${this.dataId}/reset-password`
        const res = await this.$axios.post(url)
        if (res.data.isSuccessed) {
          this.$message.success('密码重置成功,请通知用户注意查看邮箱！')
        } else {
          this.$message.error(`密码重置失败,原因:${res.data.message}`)
        }
      }
    },
    // 邮箱格式
    emailValidator(rule, value, callback) {
      if (
        !/^[A-Za-z0-9\u4e00-\u9fa5_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(
          value
        )
      ) {
        rule.message = '请输入符合邮箱格式的内容！'
        callback(rule.message)
      }
      callback()
    },
    departmentChange(value) {
      this.addOrEditForm.setFieldsValue({ postId: null })
      this.getPostList(value)
    },
    // 获取部门树
    async getDepartmentTreeAndSet() {
      const url = `/authority/auth-departments/list-tree-node`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        const treeData = this.convertToTree(res.data)
        this.deepFirstTraverseTree(
          { key: '0' },
          treeData,
          (parentNode, node) => {
            node.value = node.key
          }
        )
        this.treeData = treeData
      } else {
        this.$message.error(`获取取部门信息失败，原因:${res.message}`)
      }
    },
    // 获取岗位
    async getPostList(departmentId) {
      const url = `/authority/auth-posts/list?departmentId=${departmentId}`
      const res = await this.$axios.get(url)
      if (res.data.isSuccessed) {
        this.postList = res.data.data
      } else {
        this.$message.error(`获取岗位列表失败,原因：${res.data.message}`)
      }
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/authority/auth-users/`
        const data = this.addOrEditForm.getFieldsValue()
        if (this.dataId) {
          url = url + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
            this.$emit('updateUserSuccess', rst.data.data)
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
            this.$emit('addUserSuccess', rst.data.data)
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
      })
    },
    cancle() {
      if (this.operation === 2) {
        this.$emit('cancleWhenAddUser', this.addOrEditForm.getFieldsValue())
      } else if (this.operation === 3) {
        this.$emit('cancleWhenUpdateUser', this.addOrEditForm.getFieldsValue())
      }
    },
    statusClick(value) {
      if (!value) {
        this.addOrEditForm.setFieldsValue({ status: !value })
        const form = this.addOrEditForm
        const thisObj = this
        this.$confirm({
          title: '确定要禁用该用户吗',
          content: () => (
            <div style="color:red;">
              禁用该用户会清空用户绑定的所有岗位、兼职和角色，该用户无法登录
            </div>
          ),
          okText: '确认',
          cancelText: '取消',
          async onOk() {
            if (thisObj.dataId) {
              const url = `/authority/auth-users/${thisObj.dataId}`
              const rst = await thisObj.$axios.delete(url)
              if (rst.data.isSuccessed) {
                thisObj.$message.success('禁用成功')
                form.setFieldsValue({ status: value })
                thisObj.$emit('deleteUserSuccess', rst.data.data)
              } else {
                thisObj.$message.error(`禁用失败,原因：${rst.data.message}`)
              }
            }
          },
          onCancel() {},
          centered: true,
          class: 'test'
        })
      }
    },
    resetFields() {
      this.addOrEditForm.resetFields()
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
  // overflow-y: scroll
}

.btn-div {
  text-align: center;
  margin: 15px 0;
}
.ant-switch-checked {
  background-color: rgba(94, 192, 94, 0.5);
}
</style>

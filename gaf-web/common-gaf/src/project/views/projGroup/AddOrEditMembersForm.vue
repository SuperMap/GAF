<template>
  <div>
    <a-form
      class="page-modal-box"
      style="width: 70%; margin-left: 11%"
      :form="addOrEditMembersForm"
      layout="horizontal"
    >
      <a-form-item label="工程组名称">
        <a-input v-decorator="['groupName']" disabled />
      </a-form-item>
      <a-form-item label="成员名称">
        <div class="role-users-select">
          <a-select
            v-decorator="['projCodeBaseUserId']"
            :disabled="userSelectDisabled"
            :filter-option="filterOption"
            show-search
            placeholder="请选择人员"
            option-filter-prop="children"
            @change="handleChange"
          >
            <a-select-option
              v-for="t in users"
              :key="t.projCodeBaseUserId"
              :value="t.projCodeBaseUserId"
              >{{ t.userName }}</a-select-option
            >
          </a-select>
        </div>
      </a-form-item>
      <a-form-item label="角色">
        <a-select v-decorator="['accessLevel']">
          <a-select-option
            v-for="item in accessLevelEnum"
            :key="item.value"
            :value="item.value"
          >
            {{ item.name }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <div>
        <a-button
          style="margin: 15px 0 15px 56%"
          class="submit-gray"
          type="primary"
          :loading="loading1"
          @click="submitForm"
        >
          确定
        </a-button>
        <a-button
          class="cancel-modal"
          @click="
            () => {
              addOrEditMembersForm.setFieldsValue({
                projCodeBaseUserId: '',
              })
              $emit('cancel')
            }
          "
        >
          取消
        </a-button>
      </div>
    </a-form>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: '',
    },
    recordItem: {
      type: Object,
      default: null,
    },
    editUserData: {
      type: Object,
      default: null,
    },
    pageVisible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      dataId: '',
      tenantName: '',
      users: [],
      userSelectDisabled: false,
      accessLevelEnum: [
        {
          name: '访客',
          value: 10,
        },
        {
          name: '报告者',
          value: 20,
        },
        {
          name: '开发者',
          value: 30,
        },
        {
          name: '维护者',
          value: 40,
        },
      ],
      loading1: false,
    }
  },
  watch: {
    recordItem: {
      handler(val) {
        if (val) {
          this.recordItem = val
          this.$nextTick(function () {
            this.addOrEditMembersForm.setFieldsValue({
              groupName: val.projGroupName,
            })
          })
        }
      },
      immediate: true,
    },
    editUserData: {
      handler(val) {
        if (val) {
          this.editUserData = val
        }
      },
      immediate: true,
    },
    title: {
      handler(val) {
        if (val) {
          this.title = val
        }
      },
      immediate: true,
    },
    pageVisible: {
      handler(val) {
        if (val) {
          if (this.title === '编辑工程组成员') {
            this.$nextTick(function () {
              this.userSelectDisabled = true
              this.addOrEditMembersForm.setFieldsValue({
                projCodeBaseUserId: this.editUserData.userId,
                accessLevel: this.editUserData.projRole,
              })
            })
          } else {
            this.$nextTick(function () {
              this.userSelectDisabled = false
              this.addOrEditMembersForm.setFieldsValue({
                projCodeBaseUserId: '',
                accessLevel: 10,
              })
            })
          }
        }
      },
      immediate: true,
    },
  },
  beforeMount() {
    this.addOrEditMembersForm = this.$form.createForm(this, {
      name: 'addOrEditMembersForm',
    })
  },
  mounted() {
    this.addOrEditMembersForm.setFieldsValue({
      accessLevel: 10,
    })
    this.loadUsersList()
  },
  methods: {
    submitForm() {
      this.addOrEditMembersForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        const data = this.addOrEditMembersForm.getFieldsValue()
        this.loading1 = true
        if (data.projCodeBaseUserId === '') {
          this.$message.info('请选择人员')
          return
        }
        if (this.title === '编辑工程组成员') {
          const url = `/proj/groups/${this.recordItem.projGroupId}/members/${this.editUserData.projGroupMemberId}?accessLevel=${data.accessLevel}`
          const rst = await this.$axios.put(url)
          if (rst.data.isSuccessed) {
            this.$message.success('保存成功')
          } else {
            this.$message.error(`保存失败,原因:${rst.data.message}`)
          }
        } else {
          const url = `/proj/groups/${this.recordItem.projGroupId}/members?projCodeBaseUserId=${data.projCodeBaseUserId}&accessLevel=${data.accessLevel}`
          const rst = await this.$axios.post(url)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.loading1 = false
        this.$emit('submit')
      })
    },
    async loadUsersList() {
      const url = '/proj/code/users'
      this.loading = true
      const result = await this.$axios.$get(url)
      this.loading = false
      this.users = result.data
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text
        .toLowerCase()
        .includes(input.toLowerCase())
    },
    handleChange() {},
  },
}
</script>

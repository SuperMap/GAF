<template>
  <div class="SystemMenu">
    <gaf-modal
      :title="title"
      :visible="dialogVisible"
      @cancel="cancel"
      @ok="createMenu"
    >
      <a-form>
        <a-form-item v-bind="formItemLayout" label="菜单类型">
          <a-radio-group
            v-model="menuType"
            :options="options"
            @change="onChange"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="菜单名称">
          <a-input-group compact>
            <a-select v-model="nameType" style="width: 90px">
              <a-select-option value="1">内置</a-select-option>
              <a-select-option value="2">自定义</a-select-option>
            </a-select>
            <a-input
              v-if="nameType === '2'"
              v-model="formData.name"
              v-decorator="[
                'formData.name',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入菜单名称!',
                    },
                  ],
                },
              ]"
              style="width: 260px"
            />
            <a-select
              v-if="nameType === '1'"
              v-model="formData.name"
              v-decorator="[formData.name]"
              :filter-option="filterOption"
              show-search
              style="width: 260px"
              @change="(v) => (formData.name = v)"
            >
              <a-select-option
                v-for="m of menusLang"
                :key="m.name"
                :value="m.name"
              >
                {{ m.value }}
              </a-select-option>
            </a-select>
          </a-input-group>
        </a-form-item>
        <a-form-item v-show="isChild" v-bind="formItemLayout" label="父级菜单">
          <a-tree-select
            v-model="formData.pname"
            v-decorator="[
              'formData.pname',
              {
                initialValue: [''],
                rules: [
                  {
                    type: 'array',
                    required: true,
                    message: '请选择父级菜单!',
                  },
                ],
              },
            ]"
            :tree-data="menuList"
            @change="onSelectChange"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="路径">
          <a-input
            v-model="formData.path"
            v-decorator="[
              'path',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入菜单路径!',
                  },
                ],
              },
            ]"
            style="width: 350px"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="图标">
          <a-input
            v-model="formData.icon"
            v-decorator="[
              'icon',
              {
                rules: [
                  {
                    required: false,
                    message: '!',
                  },
                ],
              },
            ]"
            style="width: 300px"
          />
          <a-button icon="setting" @click="onClick" />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="排序">
          <a-input
            v-model="formData.order"
            v-decorator="[
              'order',
              {
                rules: [
                  {
                    required: false,
                    message: '!',
                  },
                ],
              },
            ]"
            style="width: 120px"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="是否嵌入地址">
          <a-switch
            v-decorator="['switch', { valuePropName: 'checked' }]"
            :checked="formData.isEmbed"
            @change="onSwitchChange"
          />
        </a-form-item>
        <a-form-item
          v-show="isEmbedUrl"
          v-bind="formItemLayout"
          label="嵌入地址"
        >
          <a-input
            v-model="formData.embedUrl"
            v-decorator="[
              'order',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入嵌入地址!',
                  },
                ],
              },
            ]"
            type="textarea"
            style="width: 350px"
          />
        </a-form-item>
        <a-form-item
          v-show="isEmbedUrl"
          v-bind="formItemLayout"
          label="嵌入地址打开方式"
        >
          <a-select
            v-show="isEmbedUrl"
            v-model="formData.target"
            v-decorator="[formData.target]"
            :filter-option="filterOption"
            style="width: 350px"
          >
            <a-select-option value="0">门户中打开</a-select-option>
            <a-select-option value="1">打开新页面</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </gaf-modal>
    <gaf-icon
      :visible="isShowIconSetting"
      @select="selectedIcon"
      @cancel="isShowIconSetting = false"
    />
  </div>
</template>

<script>
export default {
  name: 'SystemMenu',
  props: {
    menudata: {
      type: Object,
      default() {
        return {}
      },
    },
    isShowMenuManagement: {
      type: Boolean,
    },
  },
  data() {
    const routes = this.$store.getters.routes
    const menusLang = []
    const mkeys = Object.keys(routes)
    mkeys.forEach((mk) => {
      if (!mk.endsWith('_desc')) {
        menusLang.push({ name: mk, value: routes[mk] })
      }
    })
    return {
      title: '创建菜单',
      nameType: '1',
      menuType: '0',
      value: undefined,
      menuList: [],
      isChild: false,
      isEmbedUrl: false,
      isShowIconSetting: false,
      menusLang,
      formData: {
        id: '',
        name: '',
        pid: '',
        path: '',
        icon: '',
        target: '0',
        order: 1,
        visible: true,
        isEmbed: false,
        embedUrl: '',
      },
      options: [
        {
          value: '0',
          label: '一级菜单',
        },
        {
          value: '1',
          label: '子菜单',
        },
      ],
      dialogVisible: false,
      confirmDirty: false,
      autoCompleteResult: [],
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      },
      tailFormItemLayout: {
        wrapperCol: {
          xs: {
            span: 24,
            offset: 0,
          },
          sm: {
            span: 16,
            offset: 8,
          },
        },
      },
    }
  },
  beforeCreate() {
    // this.form = this.$form.createForm(this);
  },
  created() {
    this.dialogVisible = this.isShowMenuManagement
    this.menuList = this.menudata.menuList
    if (!this.menudata.isCreate) {
      this.title = '修改菜单'
      if (this.menudata.data.pid === '') {
        this.menuType = '0'
        this.isChild = false
      } else {
        this.menuType = '1'
        this.isChild = true
        this.menuList = this.menudata.menuList
        this.value = this.menudata.data.pid
      }

      this.formData.id = this.menudata.data.id
      this.formData.name = this.menudata.data.name
      this.formData.pname = this.menudata.data.name
      this.formData.pid = this.menudata.data.pid
      this.formData.path = this.menudata.data.path
      this.formData.icon = this.menudata.data.icon
      this.formData.taget = this.menudata.data.taget
      this.formData.order = this.menudata.data.order
      this.formData.visible = this.menudata.data.visible
      this.formData.isEmbed = this.menudata.data.embed
      this.formData.embedUrl = this.menudata.data.embedUrl
      this.isEmbedUrl = this.formData.isEmbed
    }
    if (this.menudata.isCreateChildrenMenu) {
      this.title = '创建子菜单'
      this.menuType = '1'
      this.isChild = true
      this.formData.path = this.menudata.data.path
      this.formData.pid = this.menudata.data.id
      this.formData.target = '0'
      this.value = this.menudata.data.name
    }
  },
  mounted() {
    this.$nextTick(() => {
      const emptyText = document.getElementsByClassName(
        'el-transfer-panel__empty'
      )
      for (const i in emptyText) {
        if (emptyText[i].textContent != null)
          emptyText[i].textContent = '暂无数据'
      }
      const emptyLabel = document.getElementsByClassName(
        'el-transfer-panel__item'
      )
      for (let j = 0; j < emptyLabel.length - 1; j++) {
        emptyLabel[j].style.width = '90%'
      }
    })
  },
  methods: {
    async createMenu() {
      if (!this.menudata.isCreate) {
        this.updateMenu()
        return
      }
      let res
      try {
        res = await this.$axios.$post('/portal/menus/', this.formData)
      } catch (e) {}
      if (res.success === false) {
        this.$message.error(res.msg)
      } else {
        this.$message.success('创建成功')
        this.$emit('closeMenuManagement', true)
      }
    },
    async updateMenu() {
      let res
      try {
        res = await this.$axios.$put(
          '/portal/menus/' + this.formData.id,
          this.formData
        )
      } catch (e) {}
      if (res.success === false) {
        this.$message.error(res.msg)
      } else {
        this.$message.success('修改成功')
        this.$emit('closeMenuManagement', true)
      }
    },
    cancel() {
      this.$emit('closeMenuManagement', false)
    },
    handleClose() {
      this.$emit('closeMenuManagement', false)
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text
        .toLowerCase()
        .includes(input.toLowerCase())
    },
    handleChange(value, direction, movedKeys) {
      this.$nextTick(() => {
        const emptyLabel = document.getElementsByClassName(
          'el-transfer-panel__item'
        )
        for (let j = 0; j < emptyLabel.length - 1; j++) {
          emptyLabel[j].style.width = '90%'
        }
      })
    },
    handleSubmit(e) {
      e.preventDefault()
      this.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
        }
      })
    },
    handleConfirmBlur(e) {
      const value = e.target.value
      this.confirmDirty = this.confirmDirty || !!value
    },
    compareToFirstPassword(rule, value) {
      const form = this.form
      if (value && value !== form.getFieldValue('password')) {
        // callback('Two passwords that you enter is inconsistent!')
      } else {
        // call
        // back()
      }
    },
    onChange() {
      if (this.menuType === '0') {
        this.isChild = false
        this.formData.pid = ''
      } else if (this.menuType === '1') {
        this.isChild = true
        this.menuList = this.menudata.menuList
      }
    },
    handleNameType(value) {
      this.nameType = value
    },
    onSelectChange(value) {
      console.log(value)
      this.formData.pid = value
    },
    handleSelectChange(value) {
      console.log(value)
      this.formData.icon = value
    },
    onSwitchChange(checked) {
      console.log(`a-switch to ${checked}`)
      this.formData.isEmbed = checked
      this.isEmbedUrl = checked
    },
    onClick() {
      this.isShowIconSetting = true
    },
    closeIconSetting() {
      this.isShowIconSetting = false
    },
    selectedIcon(value) {
      this.formData.icon = value
      this.isShowIconSetting = false
    },
  },
}
</script>

<style scoped>
.border {
  border: 2px solid #f5f8fd;
  width: 100%;
  padding: 15px;
  margin-bottom: 20px;
}
.borderText {
  margin-top: -25px;
  background: white;
  width: 100px;
  text-align: center;
}
</style>

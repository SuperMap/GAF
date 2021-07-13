<template>
  <div>
    <a-collapse :bordered="false" default-active-key="1" accordion>
      <a-collapse-panel key="1" header="布局设置">
        <a-form>
          <a-form-item v-bind="formItemLayout" label="菜单布局">
            <a-select v-model="formData.layoutType" class="ipt-width">
              <a-select-option value="group">默认</a-select-option>
              <a-select-option value="top">顶部</a-select-option>
              <a-select-option value="left">侧栏</a-select-option>
              <a-select-option value="topLeft">顶部侧栏</a-select-option>
              <a-select-option value="system">子系统模式</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="是否多页签">
            <a-switch v-model="formData.multipleTabs" />
          </a-form-item>
        </a-form>
      </a-collapse-panel>
      <a-collapse-panel key="2" header="门户设置">
        <a-form>
          <a-form-item v-bind="formItemLayout" label="登录地址">
            <a-input v-model="formData.loginAdress" class="ipt-width" />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="登出地址">
            <a-input v-model="formData.logoutAdress" class="ipt-width" />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="个人信息地址">
            <a-input v-model="formData.profileAdress" class="ipt-width" />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="门户图标">
            <a-input v-model="formData.logo" class="ipt-width" disabled />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="门户标题">
            <a-input v-model="formData.title" class="ipt-width" />
          </a-form-item>
        </a-form>
      </a-collapse-panel>
      <a-collapse-panel key="3" header="主题设置">
        <a-form>
          <a-form-item v-bind="formItemLayout" label="主题色">
            <div class="span-box">
              <span class="span c1" @click="changeTheme('#545c64')" />
              <span class="span c2" @click="changeTheme('#722ED1')" />
              <span class="span c3" @click="changeTheme('#1890FF')" />
              <span class="span c4" @click="changeTheme('#992777')" />
              <span class="span c5" @click="changeTheme('#2F54EB')" />
              <a-input v-model="formData.color" class="ipt-width" />
            </div>
          </a-form-item>
        </a-form>
      </a-collapse-panel>
    </a-collapse>
    <a-button icon="save" type="primary" class="save" @click="save">
      保存
    </a-button>
  </div>
</template>

<script>
export default {
  name: 'PortalSetting',
  data() {
    return {
      formData: { ...this.$store.getters.config },
      formItemLayout: {
        labelCol: { span: 3 },
        wrapperCol: { span: 8 },
      },
    }
  },
  computed: {
    config() {
      return this.$store.getters.config
    },
  },
  watch: {
    'formData.layoutType'(layoutType) {
      this.$store.commit('updateConfig', { ...this.formData, layoutType })
    },
    'formData.multipleTabs'(multipleTabs) {
      this.$store.commit('updateConfig', { ...this.formData, multipleTabs })
    },
    'formData.title'(title) {
      this.$store.commit('updateConfig', { ...this.formData, title })
    },
    'formData.color'(color) {
      this.$store.commit('updateConfig', { ...this.formData, color })
      const style = document.querySelector(':root').style
      if (style) {
        style.setProperty('--theme-color', color)
      }
    },
    config(val) {
      this.formData = { ...val }
    },
  },
  methods: {
    async save() {
      let res
      try {
        res = await this.$axios.$post(
          '/portal/manager/customization/',
          this.formData
        )
      // eslint-disable-next-line no-empty
      } catch (e) {}
      if (res.success === false) {
        this.$message.error(res.msg)
      } else {
        this.$message.success('修改成功')
      }
    },
    changeTheme(color) {
      color && (this.formData.color = color)
    },
  },
}
</script>

<style scoped>
.ipt-width {
  width: 260px;
}
.save {
  margin-top: 35px;
  margin-left: 50%;
}
.span-box {
  display: flex;
}
.span {
  display: inline-block;
  width: 60px;
  height: 32px;
  cursor: pointer;
  margin-right: 15px;
}
.c1 {
  background-color: #545c64;
}
.c2 {
  background-color: #722ed1;
}
.c3 {
  background-color: #1890ff;
}
.c4 {
  background-color: #992777;
}
.c5 {
  background-color: #2f54eb;
}
</style>

<template>
  <div loading class="form-container">
    <a-form
      ref="configSeverInfoForm"
      :model="configSeverInfoForm"
      :rules="rules"
      laba-width="100px"
      laba-position="top"
      status-icon
    >
      <a-row>
        <a-col :span="8">
          <a-form-item prop="application" label="服务名称">
            <a-auto-complete
              v-model="configSeverInfoForm.application"
              placeholder="请输入服务名称"
              class="form-input"
            ></a-auto-complete>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item prop="profile" label="环境">
            <a-auto-complete
              v-model="configSeverInfoForm.profile"
              placeholder="请输入服务环境(默认为prod)"
              class="form-input"
            ></a-auto-complete>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item prop="label" label="版本">
            <a-auto-complete
              v-model="configSeverInfoForm.label"
              placeholder="请输入服务配置版本（默认为master）"
              class="form-input"
            ></a-auto-complete>
          </a-form-item>
        </a-col>
      </a-row>
      <a-divider></a-divider>
      <a-collapse>
        <transition name="configpane">
          <a-collapse>
            <a-collapse-panel class="gaf-collapse">
              <template slot="header">
                <template>
                  服务配置
                </template>
                <template>
                  配置服务的相关信息
                </template>
              </template>
              <a-row>
                <config-server-info-form
                  v-model="configSeverInfoForm.propertyInfo"
                  :show-property-remove-button="true"
                >
                </config-server-info-form>
              </a-row>
              <a-button @click="addKvRow(configSeverInfoForm.propertyInfo)">
                <a-icon type="plus" />
                添加配置映射
              </a-button>
            </a-collapse-panel>
          </a-collapse>
        </transition>
      </a-collapse>
      <a-form-item>
        <div class="center-inline">
          <a-row>
            <a-button
              @click="saveConfigServerInfo(configSeverInfoForm)"
              type="primary"
              icon="a-icon-position"
              class="submit-gray"
            >
              保存
            </a-button>
          </a-row>
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import ConfigServerInfoForm, {
  addKvRow,
  checkKv
} from '../../views/config/ConfigServerInfoForm'

export default {
  components: {
    ConfigServerInfoForm
  },
  model: {
    prop: 'visible',
    event: 'close'
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data: function() {
    return {
      showMoreConfig: false,
      configSeverInfoForm: {
        propertyInfo: [],
        application: '',
        profile: '',
        label: '',
        tenantId: this.$store.getters.tenant.id || 'SYSTEM',
        configPropertiesVos: []
      },
      rules: {
        application: [{ required: true, message: '请输入服务名称' }],
        profile: [{ required: true, message: '请输入服务环境' }],
        label: [{ required: true, message: '请输入服务分支' }]
      }
    }
  },
  computed: {
    ...mapGetters(['tenants', 'tenant'])
  },
  methods: {
    addKvRow,
    // 点击保存新增配置
    saveConfigServerInfo: function(configInfo) {
      const validateRes = this.configInfoValidate(configInfo)
      if (!validateRes) {
        this.submitForm()
      } else {
        this.$message.error(validateRes)
      }
    },
    submitForm: async function() {
      this.loading = true
      const params = this.formatterParams()
      const res = await this.$axios.post(
        '/srv-governance/config/configurations',
        params
      )
      const { isSuccessed, message } = res.data
      if (isSuccessed) {
        this.$message.success(message)
        window.location.hash = '/config'
      } else {
        this.$message.error(message)
      }
      this.loading = false
    },
    // 验证参数对象的合法性
    configInfoValidate: function(infos) {
      let message = null
      if (!infos.application) {
        message = '请输入服务名称'
        return message
      } else if (infos.propertyInfo.length > 0) {
        message = checkKv(infos.propertyInfo)
      } else {
        message = '请输入配置键值对'
      }
      if (!infos.label) {
        this.configSeverInfoForm.label = 'master'
      }
      if (!infos.profile) {
        this.configSeverInfoForm.profile = 'prod'
      }
      return message
    },
    // 封装参数
    formatterParams: function() {
      const param = {
        application: this.configSeverInfoForm.application,
        profile: this.configSeverInfoForm.profile,
        label: this.configSeverInfoForm.label,
        tenantId: this.configSeverInfoForm.tenantId,
        configPropertiesVos: this.configSeverInfoForm.propertyInfo
      }
      return param
    }
  }
}
</script>
<style scoped>
.form-container {
  padding: 16px;
}
.form-input {
  width: 300px;
}
.center-inline {
  text-align: center;
  height: 40px;
  line-height: 40px;
}
.configpane-enter-active,
.configpane-leave-active {
  max-height: 800px;
  transition: all 0.5s;
}
.configpane-enter,
.configpane-leave-to {
  max-height: 0px;
}
.coll-span {
  margin: 24px 0px;
}
.collspan-container {
  overflow: hidden;
}
.a-collapse {
  border: 0px;
}
.gaf-collapse {
  margin-top: 10px;
}
</style>

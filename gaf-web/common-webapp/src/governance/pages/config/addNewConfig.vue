<template>
  <div class="page-single">
    <gaf-detail>
      <template>
        <a-breadcrumb separator=">">
          <span class="vertical-line">| </span>
          <a-breadcrumb-item class="text-bolder">新增服务配置</a-breadcrumb-item>
          <!-- <a-breadcrumb-item>
            <a @click="$emit('back') && $emit('refresh')">配置管理列表</a>
          </a-breadcrumb-item>
          <a-breadcrumb-item>新增服务配置</a-breadcrumb-item> -->
        </a-breadcrumb>
      </template>
      <div>
        <a-form>
          <a-row>
            <!--<a-form-item
              label="配置服务名称"
              :label-col="{ span: 2 }"
              :wrapper-col="{ span: 6 }"
            >
              <a-select
                show-search
                placeholder="请选择一个服务"
                option-filter-prop="children"
                :filter-option="filterOption"
                label-in-value
                @change="changeSelectedApplication"
              >
                <a-select-option
                  v-for="(s, idx) in serviceList"
                  :key="idx"
                  :value="s.name"
                  >{{ s.name }}</a-select-option
                >
              </a-select>
            </a-form-item>-->
            <a-col :span="8">
              <a-form-item prop="application" label="服务名称">
                <a-auto-complete
                  v-model="configSeverInfoForm.application"
                  placeholder="请输入服务名称"
                  class="form-input"
                ></a-auto-complete>
              </a-form-item>
            </a-col>
            <a-col :span="1"></a-col>
            <a-col v-if="tenantList.length > 0" :span="8">
              <a-form-item prop="tenantId" label="租户ID">
                <a-select
                  :filter-option="filterOption"
                  @change="changeSelectedApplication"
                  show-search
                  placeholder="请选择租户ID"
                  option-filter-prop="children"
                >
                  <a-select-option key="system" value="system"
                    >系统服务</a-select-option
                  >
                  <a-select-option key="gaf" value="gaf"
                    >平台基础服务</a-select-option
                  >
                  <a-select-option
                    v-for="(s, idx) in tenantList"
                    :key="idx"
                    :value="s.id"
                    >{{ s.name }}</a-select-option
                  >
                </a-select>
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
                      :propertyInfo="configSeverInfoForm.propertyInfo"
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
                  class="submit-gray"
                >
                  保存
                </a-button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <a-button
                  @click="$emit('back') && $emit('refresh')"
                  type="primary"
                  class="cancel-modal"
                >
                  取消
                </a-button>
              </a-row>
            </div>
          </a-form-item>
        </a-form>
      </div>
    </gaf-detail>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import ConfigServerInfoForm, {
  addKvRow,
  checkKv,
  clearKvRow
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
    },
    tenantList: {
      type: Array,
      default: () => []
    },
    showTenantList: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      application: '',
      showMoreConfig: false,
      configSeverInfoForm: {
        propertyInfo: [],
        application: '',
        profile: '',
        label: '',
        tenantId: this.$store.getters.tenant.id,
        properties: []
      },
      serviceList: []
    }
  },
  computed: {
    ...mapGetters(['tenants', 'tenant'])
  },
  mounted: function() {
    this.loadServiceList()
  },
  methods: {
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      )
    },
    clearKvRow,
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
        '/srv-governance/config/configurations/batchadd',
        params
      )
      const { isSuccessed, message } = res.data
      if (isSuccessed) {
        this.$message.success(message)
        // window.location.hash = '/config'
      } else {
        this.$message.error(message)
      }
      this.loading = false
      this.configSeverInfoForm = {
        propertyInfo: [],
        application: '',
        profile: '',
        label: '',
        tenantId: this.$store.getters.tenant.id,
        properties: []
      }
      this.$emit('refresh')
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
        tenantId: this.configSeverInfoForm.tenantId || 'system',
        configPropertiesVos: this.configSeverInfoForm.propertyInfo
      }
      return param
    },
    changeSelectedApplication: function(sec) {
      this.configSeverInfoForm.tenantId = sec
    },
    async loadServiceList() {
      const res = await this.$axios.get(`/srv-governance/config/servicenames`)
      const list = res.data.data
      this.serviceList = list
    }
  }
}
</script>
<style scoped>
.form-container {
  padding: 16px;
}
.center-inline {
  text-align: center;
  height: 40px;
  line-height: 40px;
  margin-top: 20px;

}
</style>

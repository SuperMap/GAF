<template>
  <div class="main-container">
    <gaf-detail>
      <template>
        <a-breadcrumb separator=">">
          <!-- <a-breadcrumb-item>
            <a @click="$emit('back') && $emit('refresh')">配置管理列表</a>
          </a-breadcrumb-item> -->
          <span class="vertical-line">| </span>
          <a-breadcrumb-item
            v-if="
              !updateConfigVisable && !addConfigVisable && !showFormRemoveButton
            "
            class="text-bolder"
            >配置详情</a-breadcrumb-item
          >
          <a-breadcrumb-item
            v-if="
              updateConfigVisable && !addConfigVisable && showFormRemoveButton
            "
            class="text-bolder"
            >配置修改</a-breadcrumb-item
          >
          <a-breadcrumb-item
            v-if="
              !updateConfigVisable && addConfigVisable && showFormRemoveButton
            "
            class="text-bolder"
            >配置新增</a-breadcrumb-item
          >
        </a-breadcrumb>
      </template>
      <div>
        <template>
          <div>
            <config-server-info-form
              v-if="configInfo.length"
              v-model="configInfo"
              :show-property-remove-button="showFormRemoveButton"
            >
            </config-server-info-form>
            <div v-else class="empty-text">
              暂无配置项，请先添加配置映射
            </div>
            <!--<a-row
              v-if="addConfigVisable || updateConfigVisable"
              class="gaf-row"
            >
              <a-button type="primary" @click="addKvRow(configInfo)">
                <a-icon type="plus" />
                添加
              </a-button>
              <a-button
                v-if="updateConfigVisable"
                type="primary"
                :disabled="!configInfo.length"
                @click="updateConfig"
              >
                保存
              </a-button>
              <a-button
                v-if="addConfigVisable"
                type="primary"
                :disabled="!configInfo.length"
                @click="addConfig"
              >
                保存
              </a-button>
            </a-row>-->
          </div>
        </template>
        <div align="center">
          <a-button
            v-if="addConfigVisable || updateConfigVisable"
            @click="addKvRow(configInfo)"
            type="primary"
            class="submit-gray"
          >
            <a-icon type="plus" />
            添加
          </a-button>
          <a-button
            v-if="updateConfigVisable"
            :disabled="!configInfo.length"
            @click="updateConfig"
            type="primary"
            class="submit-gray"
          >
            保存
          </a-button>
          <a-button
            v-if="addConfigVisable"
            :disabled="!configInfo.length"
            @click="addConfig"
            type="primary"
            class="submit-gray"
          >
            保存
          </a-button>
          <a-button
            @click="$emit('back') && $emit('refresh')"
            type="primary"
            class="cancel-modal"
          >
            取消
          </a-button>
        </div>
      </div>
    </gaf-detail>
  </div>
</template>

<script>
import ConfigServerInfoForm, { addKvRow } from './ConfigServerInfoForm'
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
    configServerInfo: {
      type: Object,
      default: () => ({})
    },
    updateConfigVisable: {
      type: Boolean,
      default: false
    },
    addConfigVisable: {
      type: Boolean,
      default: false
    },
    showFormRemoveButton: {
      type: Boolean,
      default: true
    },
    refreshLoadConfigServerInfoList: {
      type: Function,
      default: null
    }
  },
  data: function() {
    return {
      loading: false,
      configInfo: [],
      deleteConfigs: []
    }
  },
  watch: {
    visible: function(nextVisible) {
      this.configInfo = []
      if (nextVisible) {
        this.loading = true
        if (!this.addConfigVisable && nextVisible) {
          this.configServerInfo.configPropertiesVos.forEach(p => {
            this.configInfo.push(p)
          })
        }
        this.loading = false
      }
    }
  },
  methods: {
    addKvRow,
    updateConfig: async function() {
      const errorMessage = this.kvValicator(this.configInfo)
      if (errorMessage) {
        await this.$message.error(errorMessage)
        return
      }
      this.loading = true
      // 先删除移除的配置项
      const propertiesTemp = this.configServerInfo.configPropertiesVos
      const configInfoTemp = this.configInfo
      const configRemovedtemp = []
      if (propertiesTemp.length > configInfoTemp.length) {
        propertiesTemp.forEach(p => {
          let removedProperty = true
          configInfoTemp.forEach(cp => {
            if (p.id === cp.id) {
              removedProperty = false
            }
          })
          if (removedProperty) {
            configRemovedtemp.push(p)
          }
        })
      }
      if (configRemovedtemp.length > 0) {
        configRemovedtemp.forEach(c => {
          this.$configServerApi.delConfigServerInfo(c.id)
        })
      }
      await this.$axios.$put(
        '/srv-governance/config/batchupdate',
        this.formatterParams(this.configInfo)
      )
      this.$emit('close', false)
      this.loading = false
      this.refreshLoadConfigServerInfoList()
      // TODO 成功验证
      await this.$message.success('操作成功！')
    },
    addConfig: async function() {
      const errorMessage = this.kvValicator(this.configInfo)
      if (errorMessage) {
        await this.$message.error(errorMessage)
        return
      }
      this.loading = true
      await this.$axios.post(
        '/srv-governance/config/configurations/batchadd',
        this.formatterParams(this.configInfo)
      )
      this.$emit('close', false)
      this.loading = false
      // TODO 成功验证
      this.refreshLoadConfigServerInfoList()
      await this.$message.success('操作成功！')
    },
    formatterParams: function(configs) {
      const configServerInfoTemp = {}
      configServerInfoTemp.application = this.configServerInfo.application
      configServerInfoTemp.tenantId = this.configServerInfo.tenantId
      configServerInfoTemp.profile = this.configServerInfo.profile
      configServerInfoTemp.label = this.configServerInfo.label
      configServerInfoTemp.configPropertiesVos = configs
      return configServerInfoTemp
    },
    kvValicator: function(configs) {
      let message = null
      configs.forEach(c => {
        if (
          (!c.propertyKey && c.propertyValue) ||
          (c.propertyKey && !c.propertyValue) ||
          (!c.propertyKey && !c.propertyValue)
        ) {
          message = '请输入完整的键值对'
        }
      })
      return message
    }
  }
}
</script>
<style scoped>
.main-container {
  padding: 16px;
}
</style>

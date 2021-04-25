<template>
  <div>
    <div v-if="data.list.length === 0" class="placeholder">
      选中左侧控件，拖动到这里
    </div>
    <a-button-group v-if="data.list.length > 0" class="actions">
      <a-button type="primary" icon="save" @click="saveConfigInfo"
        >保存</a-button
      >
      <a-popconfirm
        title="设为默认，当前定制信息将覆盖门户默认配置信息，确认是否要同步设置为默认？"
        ok-text="是"
        cancel-text="否"
        @confirm="saveConfig2Default"
      >
        <a-button type="primary" icon="swap">设为默认</a-button>
      </a-popconfirm>
      <a-popconfirm
        title="恢复默认，门户默认配置信息将覆盖当前定制信息，确认是否要恢复？"
        ok-text="是"
        cancel-text="否"
        @confirm="saveDefault2Config"
      >
        <a-button type="primary" icon="swap">恢复默认</a-button>
      </a-popconfirm>
    </a-button-group>
    <sortable-control-list :data="data" class="control-list" />
  </div>
</template>

<script>
import SortableControlList from './SortableControlList'
export default {
  name: 'GafDesigner',
  components: {
    SortableControlList,
  },
  data() {
    return {
      data: {
        list: JSON.parse(this.$store.state.config.configInfo),
      },
      defaultConfigInfo: {
        list: JSON.parse(this.$store.state.config.defaultConfigInfo),
      }
    }
  },
  methods: {
    async saveConfig2Default() {
      const customizationInfo = {
        configInfo: JSON.stringify(this.data.list),
      }
      const mr = await this.$axios.$post(
        
        '/portal/manager/customization/home/default',
        customizationInfo
      )
      this.$message[mr.isSuccessed ? 'success' : 'error'](mr.message)
    },
    async saveDefault2Config() {
      const mr = await this.$axios.$post(
        '/portal/manager/customization/home/config'
      )
      this.data.list = this.defaultConfigInfo.list
      this.$message[mr.isSuccessed ? 'success' : 'error'](mr.message)
    },
    async saveConfigInfo() {
      const customizationInfo = {
        configInfo: JSON.stringify(this.data.list),
      }
      const mr = await this.$axios.$post(
        '/portal/manager/customization/home',
        customizationInfo
      )
      this.$message[mr.isSuccessed ? 'success' : 'error'](mr.message)
    },
  },
}
</script>

<style scoped lang="less">
.placeholder {
  position: absolute;
  text-align: center;
  top: 0;
  left: 0;
  border: dashed 1px #dcdcdc;
  height: 360px;
  line-height: 360px;
  width: 100%;
  color: gray;
}
.actions {
  float: right;
  margin: 5px 15px 5px;
}
.control-list {
  clear: both;
}
</style>

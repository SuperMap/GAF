<template>
  <div class="page-single">
    <div class="breadcrumb">
      <a-breadcrumb :routes="routes">
        <template
          slot="itemRender"
          slot-scope="{ route, params, routes, paths }"
        >
          <span v-if="routes.indexOf(route) === routes.length - 1">
            {{ route.breadcrumbName }}
          </span>
          <a
            v-else
            href="javascript:;"
            @click.stop="() => spanClick(route, params, routes, paths)"
          >
            {{ route.breadcrumbName }}
          </a>
        </template>
      </a-breadcrumb>
    </div>
    <div class="tabbleName"><span>{{ data.recordName }}</span></div>
    <div class="tabs">
      <a-tabs default-active-key="1" @change="callback">
        <a-tab-pane key="1" tab="属性" forceRender>
          <attribute v-if="tabActiveValue === '1'" :data="data" ref="attribute" />
        </a-tab-pane>
        <a-tab-pane key="2" tab="血缘">
          <bloodKinship />
        </a-tab-pane>
        <a-tab-pane key="3" tab="模式" forceRender>
          <pattern-box ref="pattern"/>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>
<script>
import patternBox from "./pattern"
import attribute from "./attribute"
import bloodKinship from "./bloodKinship"

export default {
  components: {
    patternBox,
    attribute,
    bloodKinship,
  },
  provide() {
    return {
      infoBox: this
    }
  },
  props: {
    data: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      routes: [
        {
          path: 'StorageManagement',
          breadcrumbName: '配置管理',
        },
        {
          path: '1',
          breadcrumbName: '属性',
        },
      ],
      tabName: ['属性', '血缘', '模式'],
      map: new Map(),
      treeData: [],
      tabActiveValue: '1',
      commonDataSource: {
        POSTGRESQL: '1',
        ORACLE: '2',
        MYSQL: '3',
        SQL_SERVER: '4'
      }

    };
  },
  mounted() {
    this.routes[0].breadcrumbName = this.data.recordName
    if (this.data.spatialInfo.prjCoordSys) {
      this.getTreeData('sdx')
    } else {
      this.getTreeData(this.commonDataSource[this.data.recordType])
    }
  },
  methods: {
    callback(key) {
      this.tabActiveValue = key
      this.routes[1].path = key
      this.routes[1].breadcrumbName = this.tabName[key-1]
    },
    spanClick() {
      this.$emit('changeOpenInfobox')
    },
    updatedData() {
      this.$emit('updatedData')
    },
    async getTreeData(type) {
      const url = `/data-mgt/model-manage/fields/type-infos?modelType=${type}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.treeData = res.data
        this.treeData.forEach(item => {
          item['value'] = item.code
          this.map.set(item.code, item.name)
        })
      } else {
        this.$message.error('加载字段类型失败,原因：' + res.message)
      }
      this.$refs.pattern.loading = false
    },
  },
  
};
</script>

<style scoped>
.breadcrumb {
  position: relative;
  top: 10px;
  padding: 0 16px;
  z-index: 10;
  line-height: 21px;
}
.breadcrumb > div {
  display: inline-block;
}
.tabbleName {
  margin: 25px 15px 10px 30px;
  font-size: 20px;
  font-weight: bold;
}
.tabs {
  margin: 0 15px;
}
</style>
<template>
  <div>
    <!-- <gaf-map-sky-box /> -->
    <gaf-map-basic-element />
    <gaf-map-tool-bar
      :content="content"
      type="horizontal"
      positon="bottom"
    ></gaf-map-tool-bar>
    <gaf-map-tool-bar
      :content="contentRight"
      type="vertical"
      positon="bottomRight"
    ></gaf-map-tool-bar>
    <gaf-map-theme :theme-plans="themePlans" />
    <nuxt />
  </div>
</template>
<script>
import Vue from "vue";
import { bus } from "~/utils/cimBus";
export default {
  props: {
    contentRight: {
      type: Array,
      default: () => [],
    },
    content: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      themePlans: window.SMWEBGIS.theme.themePlans,
      supportBackEnd: window.SMWEBGIS.supportBackEnd || false,
      menuShow: false,
    };
  },
  computed: {
    loaded() {
      return this.$store.state.loaded;
    },
  },
  beforeCreate() {
    Vue.prototype.$bus = bus;
  },
  async beforeMount() {
    if (this.supportBackEnd) {
      try {
        // 新后端
        const res = await this.$api.mapAppRest.getMenus();
        if (res.isSuccessed) {
          const data = res.data.pageList;
          if (data) {
            const menuInfo = data.map((item, index) => {
              const newItem = {};
              newItem.id = item.gisAppId;
              newItem.title = item.name;
              newItem.order = item.appAliasEn === "home" ? 0 : index + 1;
              newItem.path =
                "/" + (item.appAliasEn === "home" ? "" : item.appAliasEn);
              newItem.icon = item.icon || "setting";
              newItem.iconClass = item.iconClass || "icon-home-fill";
              newItem.pid = item.pid || "";
              newItem.type = item.type || "link";
              newItem.name = item.appAliasEn;
              return newItem;
            });
            localStorage.setItem("menuInfo", JSON.stringify(menuInfo));
            this.menuShow = true;
          }
        }
      } catch (error) {
        this.$message.warn(error);
      }
    } else {
      this.menuShow = true;
    }
  },
  async mounted() {
    // const href = window.location.href.replace('/#/', '')
    // const data = await this.$axios.$get(href + '/config/dir.json')
    // const { one, two } = data
    // window.SMWEBGIS.panel.splitScreen = {
    //   one,
    //   two,
    // }
    // const color = window.SMWEBGIS.theme.themePlans[0].color
    // for (const item in color) {
    //   document.documentElement.style.setProperty(item, color[item])
    // }
  },
};
</script>
<style scoped lang="less">
.map-header {
  position: absolute;
  z-index: 2000;
  display: flex;
  left: 0;
  top: 0;
  right: 0;
  align-items: baseline;
  flex-flow: row nowrap;
  .nav-menu {
    transform: translateY(-75%);
  }
}
.bottom-right {
  bottom: 90px;
}
</style>
<style>
.cesium-performanceDisplay-defaultContainer {
  z-index: 9999;
}
</style>
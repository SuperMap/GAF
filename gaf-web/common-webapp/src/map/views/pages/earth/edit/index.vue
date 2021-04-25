<template>
  <div>
    <gaf-map-viewer :scene="scene" :base-map="baseMap" :layer-list="layerList">
    </gaf-map-viewer>
    <div class="resourceTree" style="color: #fff">
      <gaf-resource-tree
        url="http://0.0.0.0:9003/img/treeData.json"
        @select="onSelect"
        @check="onCheck"
      />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      baseMap:
        'https://iserver.supermap.io/iserver/services/map-world/rest/maps/世界地图_Night',
      scene: {
        url:
          'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace',

        name: '',
      },
      layerList: [],
    }
  },
  methods: {
    onCheck(info, nodes) {
      const layer2d = nodes.filter((item) => {
        return item.type === 'RESTMAP'
      })
      this.layerList = layer2d
      if (info.type === 'RESTREALSPACE') {
        this.scene = info
      }
    },
  },
}
</script>
<style scoped>
.resourceTree {
  width: 200px;
  position: absolute;
  top: 10px;
  left: 10px;
  color: #fff;
  background-color: #fff;
  border: 1px solid #414141;
  opacity: 0.8;
  border-radius: 10px;
}
</style>

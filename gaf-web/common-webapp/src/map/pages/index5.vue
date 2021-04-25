<template>
  <gaf-map-viewer :sm-scene-list="smSceneList" :sm-layer-list="smLayerList">
    <gaf-map-draggable title="资源目录树" :visible="true">
      <div class="resourceTree">
        <gaf-map-tree
          :replace-fields="replaceFields"
          :data-list="allDataList"
          :check="onTreeNodeChecked"
          :select="onSelect"
          :all-checkable="false"
          :leafnode-checkable="true"
          :somen-node-checkable="false"
        />
      </div>
    </gaf-map-draggable>
  </gaf-map-viewer>
</template>
<script>
export default {
  data() {
    return {
      allDataList: [
        {
          resourceId: 1,
          resourceName: '2维图层',
          pid: '',
        },
        {
          resourceId: 2,
          resourceName: '3维数据',
          pid: '',
        },
        {
          resourceId: 7169,
          pid: 2,
          location: true,
          resourceName: 'CBD',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
          resourceTag: 'RESTREALSPACE',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 0,
        },
        {
          resourceId: 7170,
          pid: 2,
          location: true,
          resourceName: 'BIM',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/3D-wireFrame/rest/realspace/scenes/wireFrame',
          resourceTag: 'RESTREALSPACE',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 0,
        },
        {
          resourceId: 7171,
          pid: 2,
          location: true,
          resourceName: 'niaocao',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/3D-OlympicGreen20200416/rest/realspace/scenes/%E6%9C%AA%E5%91%BD%E5%90%8D%E5%9C%BA%E6%99%AF',
          resourceTag: 'RESTREALSPACE',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 0,
        },
        {
          resourceId: 7165,
          pid: 1,
          location: true,
          resourceName: 'zhufeng',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-zhufeng/rest/maps/pol1_P_1%40zhufeng',
          resourceTag: 'RESTMAP',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 0.6,
        },
        {
          resourceId: 7166,
          pid: 1,
          location: true,
          resourceName: 'China400',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-China400/rest/maps/China400',
          resourceTag: 'RESTMAP',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 0,
        },
        {
          resourceId: 7167,
          pid: 1,
          location: true,
          resourceName: 'WorldMap',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-sample/rest/maps/WorldMap',
          resourceTag: 'RESTMAP',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 0.6,
        },
      ],
      replaceFields: {
        title: 'resourceName',
        key: 'resourceId',
      },
      layerList: [],
    }
  },
  computed: {
    smSceneList() {
      return this.layerList.filter(
        (item) => item.resourceTag === 'RESTREALSPACE'
      )
    },
    smLayerList() {
      return this.layerList.filter((item) => item.resourceTag === 'RESTMAP')
    },
  },
  methods: {
    onTreeNodeChecked(checkedKeys) {
      const layerList = []
      checkedKeys.forEach((key) => {
        const data = this.allDataList.find((item) => {
          return item.resourceId.toString() === key.toString()
        })
        if (data) {
          layerList.push(data)
        }
      })
      this.layerList = layerList
    },
    onSelect() {},
  },
}
</script>
<style scoped>
.draggable > .header {
  background: rgba(38, 38, 38, 0.75);
}
.resourceTree {
  position: relative;
  z-index: 1;
  background: rgba(38, 38, 38, 0.75);
  max-height: 500px;
  overflow: auto;
}
</style>

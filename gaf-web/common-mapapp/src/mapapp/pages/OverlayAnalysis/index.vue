<template>
    <gaf-map-viewer-origin :sm-layer-list="smLayerList">
      <div class="search">
        <gaf-map-app-query :server-datas="serverDatas" />
      </div>
    </gaf-map-viewer-origin>
</template>

<script>
export default {
      data() {
        return {
          smLayerList: [
            {
              resourceId: 7000,
              resourceLocation:
                'https://iserver.supermap.io/iserver/services/map-world/rest/maps/World',
              resourceName: 'World',
            },
            {
              resourceId: 7001,
              resourceLocation:
                'http://iserver.gaf-dev.chengdu/iserver/services/map-Ws_Rec322/rest/maps/rec_1%40Ds_Rec322',
              resourceName: '京津地区地图',
              location: true,
            },
          ],
          serverDatas: [
            {
              host:
                'http://192.168.11.172:30712/iserver/services/spatialAnalysis-Ws_Rec322/restjsr/spatialanalyst',
              title: '叠加分析',
              overLay: 'rec_1@Ds_Rec322',
              // 叠加操作，可选值为 clip（裁剪）、erase（擦除）、identity（同一）、intersect（相交）、union（合并）、update（更新）、XOR（对称差）
              operation: 'CLIP',
              tolerance: 0,
              returnContent: false,
              dataReturnOption: {
                dataReturnMode: 'RECORDSET_ONLY',
                expectCount: 100,
                deleteExistResultDataset: false,
              },
            },
          ],
        }
      },
    }
</script>
<style scoped>
/* 叠加分析 */
.search {
      margin-left: 15%;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 5px;
      color: rgba(0, 0, 0, 0.65);
      padding: 10px;
    }
    .ant-btn.ant-btn-loading:not(.ant-btn-circle):not(.ant-btn-circle-outline):not(.ant-btn-icon-only) {
      position: absolute;
      padding-left: auto;
      pointer-events: none;
    }
</style>

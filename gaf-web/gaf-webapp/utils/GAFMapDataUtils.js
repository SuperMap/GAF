/**
 * GAF地图数据读取公共方法
 * 内容项：
 * 1、GAF地图的底图信息
 * 2、GAF地图工具条内容信息
 * 3、图层目录树结构数据
 * 4、功能目录树结构信息
 */
/**
 * 地图数据路径，后面考虑将路径定义放到配置文件中
 */
export const dataUrl = {
  // 地图控件路径
  MapControlUrl: '/map/manager/maps/{param}/mapcontrols',
  // 图层目录路径
  LayerCatalogUrl: '/map/manager/maps/{param}/mapcontrols/resourcecatalog',
  // 基础底图路径
  BaseLayerUrl: '/map/manager/maps/{param}/baselayer',
  // 服务信息路径
  ServiceInfoUrl: '/map/manager/services',
  // 地图服务路径
  MapConfigUrl: '/map/manager/maps',
}
// 地图控件路径
const getMapControlUrl = (mapCode) => {
  return `/map/manager/maps/${mapCode}/mapcontrols`
}
// 图层目录路径
const getLayerCatalogUrl = (mapCode) => {
  return `/map/manager/maps/${mapCode}/mapcontrols/resourcecatalog`
}
// 基础底图路径
const getBaseLayerUrl = (mapCode) => {
  return `/map/manager/maps/${mapCode}/baselayer`
}
// 功能目录路径
const getFunctionUrl = (mapCode) => {
  return `/map/manager/maps/${mapCode}/mapcontrols/functioncatalog`
}
// 工具条路径
const getMapToolUrl = (mapCode) => {
  return `/map/manager/maps/${mapCode}/mapcontrols/maptools`
}
// 定位服务路径
const getLocationUrl = (mapCode, ServiceId) => {
  return `/map/manager/maps/${mapCode}/service/${ServiceId}/locationinfos`
}
// 查询服务路径
const getSelectionUrl = (mapCode, ServiceId) => {
  return `/map/manager/maps/${mapCode}/service/${ServiceId}/selectinfos`
}
/**
 * 读取地图控件
 * @param {地图标识} mapCode
 */
function getMapControls(mapCode) {
  const that = this
  return new Promise((resolve) => {
    if (mapCode) {
      // const url = dataUrl.MapControlUrl.replace('{param}', mapCode)
      const url = getMapControlUrl(mapCode)
      that.$axios
        .$get(url)
        .then((resultData) => {
          resolve(resultData.data)
        })
        .catch((error) => {
          console.error('查询地图控件失败：' + error)
        })
    } else {
      console.error('查询地图控件失败：请输入地图标识')
    }
  })
}
/**
 * 获取基础图层信息
 * @param {地图标识} mapCode
 */
async function getBaseLayer(mapCode) {
  if (mapCode) {
    const url = dataUrl.BaseLayerUrl.replace('{param}', mapCode)
    const baselayerdata = await this.$axios.$get(url)
    if (baselayerdata && Object.keys(baselayerdata).length > 0) {
      return baselayerdata.data
    } else {
      console.error('查询底图失败：底图配置信息不存在')
    }
  } else {
    console.error('查询底图失败：请输入地图标识')
  }
}
/**
 * 将后端返回的目录列表转换为树结构
 */
function getMapTreeByCatalogInfo(parentId, catalogList) {
  const mapTree = []
  debugger
  if (catalogList && catalogList.length) {
    for (let i = 0; i < catalogList.length; i++) {
      const catalog = catalogList[i]
      if (parentId === catalog.parentId) {
        const treeItem = {
          title: '',
          key: '',
          slots: {},
          scopedSlots: {
            icon: '',
          },
          children: [],
        }
        treeItem.title = catalog.catalogName
        treeItem.key = catalog.id
        treeItem.scopedSlots.icon = catalog.catalogType
        treeItem.slots = catalog
        treeItem.children = getMapTreeByCatalogInfo(catalog.id, catalogList)
        mapTree.push(treeItem)
      }
    }
  }
  return mapTree
}
export {
  getBaseLayer,
  getMapControls,
  getMapTreeByCatalogInfo,
  getMapControlUrl,
  getLayerCatalogUrl,
  getBaseLayerUrl,
  getFunctionUrl,
  getMapToolUrl,
  getLocationUrl,
  getSelectionUrl,
}

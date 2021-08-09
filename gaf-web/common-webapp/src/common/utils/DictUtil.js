/**
 * 
 * 该类基于TreeUtil，处理后端传回的字典数据。
 * 例如处理树形结构的字典数据，剔除非必要的属性，用于前端的级联选择器或者树形选择器，防止报错
 * 
 */

import treeUtil from './TreeUtil'

/**
 * 遍历字典数据树每个节点
 * @param {array}} dictTreeList 字典数据树
 * @param {function} callback 回调函数
 */
function forEach(dictTreeList, callback) {
  treeUtil.forEach(dictTreeList, callback)
}

/**
 * 删除树节点为Children属性值空的Children属性
 * @param {array} dictTreeList 字典数据树
 */
function deleteEmptyChildrenAttr(dictTreeList) {
  treeUtil.forEach(dictTreeList, node=> {
    if(!node.children || node.children.length == 0) {
      delete node.children
    }
  })
}

/**
 * 将字典数据树转换为 字典值(value)到字典标签(label)的映射,即Map
 * @param {array} dictTreeList  字典数据树
 * @returns Map 字典数据树所有节点value到label的映射
 */
function toMap(dictTreeList) {
  const map = new Map()
  treeUtil.forEach(dictTreeList, node => {
    map.set(node.value,node.label)
  })
  return map
}

/**
 * 将字典数据转换为 字典值(value)到字典标签(label)的映射,即Map
 * @param {array} dictList 字典数据列表
 * @returns Map 字典数据所有项 value到label的映射
 */
function listToMap(dictList) {
  const map = new Map()
  if(dictList) {
    dictList.forEach(e=>{
      map.set(e.value,e.label)
    })
  }
  return map
}

/**
 * 删除dictTreeList中节点属性
 * 注意：将会改变源数据dictTreeList
 * @param {array}} dictTreeList 字典数据树 
 * @param {array} attrNames 要保留的节点数据
 */
function adjustAttrForCascader(dictTreeList, attrNames = ['value','label','children','isLeaf']) {
  retainAttr(dictTreeList, attrNames)
  deleteEmptyChildrenAttr(dictTreeList)
}

function retainAttr (dictTreeList, attrNames) {
    const callback = node => {
      for(let attrName in node) {
        if(!attrNames.includes(attrName)) {
          delete node[attrName]
        }
      }
    }
    treeUtil.forEach(dictTreeList, callback)
}
/**
 * 拷贝字典数据树
 * @param {array} dictTreeList 字典数据树
 * @returns 拷贝后的字典数据树
 */
function copy (dictTreeList) {
  if(!dictTreeList || dictTreeList.length == 0) return []
  const rootParentId = dictTreeList[0].parentId
  const nodeList = treeUtil.toList(dictTreeList)
  return treeUtil.convertToTree({key: rootParentId}, nodeList ,treeUtil.defaultConfigMerge({sortSn:'seq'}))
}

export default {
  forEach,
  deleteEmptyChildrenAttr,
  toMap,
  listToMap,
  adjustAttrForCascader,
  retainAttr,
  copy
}







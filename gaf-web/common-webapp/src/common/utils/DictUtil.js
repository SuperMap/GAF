/**
 * 
 * 该类基于TreeUtil，处理后端传回的字典数据。
 * 例如处理树形结构的字典数据，剔除非必要的属性，用于前端的级联选择器或者树形选择器，防止报错
 * 
 */

import treeUtil from './TreeUtil'

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
  adjustAttrForCascader,
  retainAttr,
  copy
}







/**
 * 注意：节点的属性需要有key(唯一键，标识节点),title(名称)，parentId(父节id),sortSn(同级排序)，children(子节点数组)
 */

/**
 * 将节点列表转换为树形结构，
 * 注意：节点的属性需要有key(唯一键，标识节点),title(名称)，parentId(父节id),sortSn(同级排序)，children(子节点数组)
 * @param {object} rootParent 当前节点列表的根节点的父节点
 * @param {string} nodeList 当前节点列表
 * @return {Array} 转换后的树结构的节点， 若节点列表数据为空或者转换后为空树则返回空数组
 */
function convertToTree(rootParent, nodeList) {
  return convertToCustomSortTree(rootParent, nodeList, (a , b) => a.sortSn - b.sortSn)
}

/**
 * 将节点列表转换为树形结构，
 * 注意：节点的属性需要有key(唯一键，标识节点),title(名称)，parentId(父节id),sortSn(同级排序)，children(子节点数组)
 * @param {object} rootParent 当前节点列表的根节点的父节点
 * @param {string} nodeList 当前节点列表
 * @param {function} compare 比较函数 传入同级的两个节点
 * @return {Array} 转换后的树结构的节点， 若节点列表数据为空或者转换后为空树则返回空数组
 */
function convertToCustomSortTree(rootParent, nodeList , compare) {
  if (!nodeList || nodeList.length <= 0) {
    return []
  }
  const children = getCustomSortChildren(rootParent, nodeList, compare)
  return children || []
}

// function getChildren(root, all) {
//     return getCustomSortChildren(root, all, (a , b) => a.sortSn - b.sortSn)
// }

function getCustomSortChildren(root, all, compare) {
  const collect = all
    .filter((treeNode) => {
      return treeNode.parentId === root.key
    })
    .map(function (treeNode) {
      treeNode.children = getCustomSortChildren(treeNode, all, compare)
      return treeNode
    })
    .sort(compare)
  return collect
}




/**
 * 获取树中的某个节点
 * @param {array} treeList 树结构的节点列表
 * @param {*} key 树节点的key(唯一键，节点标识)
 * @return {*} 树节点node 若未找到则返回null
 */
function getNodeByKey(treeList, key) {
  if (treeList) {
    for (let i = 0; i < treeList.length; i++) {
      const node = treeList[i]
      if (node.key === key) {
        return node
      }
      if (node.children) {
        const hit = getNodeByKey(node.children, key)
        if (hit != null) {
          return hit
        } else {
          continue
        }
      }
    }
  }
  return null
}

/**
 * 遍历树
 * @param {object} parentNode 父节点
 * @param {*} treeList 父节点下的树结构的子节点列表
 * @param {function} callback 回调函数，参数是parentNode(父节点) ,node(当前正在遍历的节点)
 */
function deepFirstTraverseTree(parentNode, treeList, callback) {
  if (treeList) {
    for (let i = 0; i < treeList.length; i++) {
      const node = treeList[i]
      callback(parentNode, node)
      if (node.children) {
        deepFirstTraverseTree(node, node.children, callback)
      }
    }
  }
}

/**
 * 查询节点在树中的路径，包括当前节点
 * @param {array} treeList 树结构的节点列表
 * @param {*} key 节点的唯一键，节点标识
 * @return {array} 路径 若treeList为空或者未查到到key对应的节点则返回空数组，
 */
function getNodePath(treeList, key) {
  const path = []
  const hit = getPath(treeList, key, path)
  if (hit != null && path.length > 0) {
    return path
  } else {
    return []
  }
}

function getPath(treeList, key, path) {
  if (treeList) {
    for (let i = 0; i < treeList.length; i++) {
      const node = treeList[i]
      path.push(node)
      if (node.key === key) {
        return node
      }
      if (node.children) {
        const hit = getPath(node.children, key, path)
        if (hit != null) {
          return hit
        }
      }
      path.pop()
    }
  }
  return null
}

/**
 * 若未查询到父节点，或者值为key的节点为根节点时，或者未查询到值为key的节点则返回null
 * @param {array} treeList 树结构的节点列表
 * @param {*} key 节点的唯一键，节点标识
 * @return 若未查询到则返回null
 */
function getParentNode(treeList, key) {
  const parentPath = getParentPath(treeList, key)
  return parentPath.length === 0 ? null : parentPath[parentPath.length - 1]
}

/**
 * 查询节点在树中的父路径，不包括当前节点
 * @param {array} treeList 树结构的节点列表
 * @param {*} key  节点的唯一键，节点标识
 * @return {array} 父路径 若treeList为空或者未查到到key对应的节点则返回空数组，若参数key对应的节点为根节点则返回空数组
 */
function getParentPath(treeList, key) {
  const path = []
  const hit = getPath(treeList, key, path)
  if (hit != null && path.length > 1) {
    path.pop()
    return path
  } else {
    return []
  }
}

export default {
  convertToTree,
  convertToCustomSortTree,
  getNodeByKey,
  deepFirstTraverseTree,
  getNodePath,
  getParentPath,
  getParentNode,
}

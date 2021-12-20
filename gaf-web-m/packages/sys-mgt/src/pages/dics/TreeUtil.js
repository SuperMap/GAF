/**
 * 注意：节点的默认属性为key(唯一键，标识节点)，parentId(父节id),sortSn(同级排序)，children(子节点数组)
 * 每个方法都有节点属性名的默认配置
 * 所有的默认配置为
 * const DEFAULT_CONFIG = {
 *   key: 'key',
 *   parentId: 'parentId',
 *   sortSn: 'sortSn',
 *   children: 'children'
 * }
 *
 */
// // 暂时不对外导出，需要时可对外导出，也可独立打包发布。
// 1 内部直接导入
// 在common-webapp内部,直接导入文件使用:
// import TreeUtil from '相对路径'

// 2. 使用示例：
// // 内部直接导入
// import TreeUtil from '相对路径'

// function getTree () {
//   const tree = [
//     {
//       key: '1',
//       title: '节点1',
//       children: [
//         {
//           key: '1-1',
//           title: '节点1-1'
//         },
//         {
//           key: '1-2',
//           title: '节点1-2',
//           children: [
//             {
//               key: '1-2-1',
//               title: '节点1-2-1'
//             }
//           ]
//         }
//       ]
//     },
//     {
//       key: '2',
//       title: '节点2',
//       children: [
//         {
//           key: '2-1',
//           title: '节点2-1'
//         }
//       ]
//     }
//   ]
//   return tree
// }

// function getList () {
//   const list = [
//     {
//       key: '1',
//       title: '节点1',
//       pid: '',
//     },
//     {
//       key: '1-1',
//       title: '节点1-1',
//       pid: '1'
//     },
//     {
//       key: '1-2',
//       title: '节点1-2',
//       pid: '1'
//     },
//     {
//       key: '2',
//       title: '节点2',
//       pid: ''
//     },
//     {
//       key: '2-1',
//       title: '节点2-1',
//       pid: '2'
//     }
//   ]
//   return list
// }

// // 创建一个实例，因为数据里的pid属性名与默认值parentId不同，所以需要传递该配置项
// const instance = TreeUtil.createInstance({ parentId : 'pid' })

// // 列表转树 不创建实例
// function testConvertToTree () {
//   const list = getList()
//   const tree = TreeUtil.convertToTree({key: ''},list, TreeUtil.defaultConfigMerge({ parentId: 'pid' }))
//   // 或者
//   const config = {
//     key: 'key',
//     parentId: 'pid',
//     sortSn: 'sortSn',
//     children: 'children'
//   }
//   const tree2 = TreeUtil.convertToTree({key: ''},list, config)
// }

// // 列表转树 使用实例
// function testConvertToTree2 () {
//   const tree = = instance.convertToTree({key: ''},list)
// }

// // 深度优先遍历树  不使用实例
// function testDeepFirstTraverseTree() {
//   const tree = getTree()
//   const callback = (parentNode,node) => {console.log('parentNode '+ JSON.stringify(parentNode) + "\n node :" + JSON.stringify(node))}
//   TreeUtil.deepFirstTraverseTree({key: ''}, tree, callback)
//   // 参数parentNode可以传null
// }

// // 深度优先遍历树  使用实例
// function testDeepFirstTraverseTree2() {
//   const tree = getTree()
//   const callback = (parentNode,node) => {console.log('parentNode '+ JSON.stringify(parentNode) + "\n node :" + JSON.stringify(node))}
//   instance.deepFirstTraverseTree({key: ''}, tree, callback)
//   // 参数parentNode可以传null
// }

/**
 * 将节点列表转换为树形结构，
 * 注意：节点的属性需要有key(唯一键，标识节点)，parentId(父节id),sortSn(同级排序)，children(子节点数组)
 * @param {object} rootParent 当前节点列表的根节点的父节点
 * @param {string} nodeList 当前节点列表
 * @return {Array} 转换后的树结构的节点， 若节点列表数据为空或者转换后为空树则返回空数组
 */
function convertToTree(rootParent, nodeList, config = {key:'key',parentId:'parentId',sortSn:'sortSn', children:'children'}) {
  return convertToCustomSortTree(rootParent, nodeList, (a , b) => a[config.sortSn] - b[config.sortSn], config)
}

/**
 * 将节点列表转换为树形结构，
 * 注意：节点的属性需要有key(唯一键，标识节点)，parentId(父节id),sortSn(同级排序)，children(子节点数组)
 * @param {object} rootParent 当前节点列表的根节点的父节点
 * @param {string} nodeList 当前节点列表
 * @param {function} compare 比较函数 传入同级的两个节点
 * @return {Array} 转换后的树结构的节点， 若节点列表数据为空或者转换后为空树则返回空数组
 */
function convertToCustomSortTree(rootParent, nodeList , compare, config = {key:'key',parentId:'parentId',children:'children'}) {
  if (!nodeList || nodeList.length <= 0) {
    return []
  }
  const children = getCustomSortChildren(rootParent, nodeList, compare, config)
  return children || []
}

function getCustomSortChildren(root, all, compare, config = {key:'key',parentId:'parentId',children:'children'}) {
  const collect = all
    .filter((treeNode) => {
      return treeNode[config.parentId] === root[config.key]
    })
    .map(function (treeNode) {
      treeNode[config.children] = getCustomSortChildren(treeNode, all, compare, config)
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
function getNodeByKey(treeList, key, config = {key:'key',children:'children'}) {
  if (treeList) {
    for (let i = 0; i < treeList.length; i++) {
      const node = treeList[i]
      if (node[config.key] === key) {
        return node
      }
      if (node[config.children]) {
        const hit = getNodeByKey(node[config.children], key , config)
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
function deepFirstTraverseTree(parentNode, treeList, callback, config = {children:'children'}) {
  if (treeList) {
    for (let i = 0; i < treeList.length; i++) {
      const node = treeList[i]
      callback(parentNode, node)
      if (node[config.children]) {
        deepFirstTraverseTree(node, node[config.children], callback, config)
      }
    }
  }
}

/**
 * 遍历树
 * @param {array} treeList 树结构的节点列表
 * @param {function} callback 回调函数，参数是node(当前正在遍历的节点)
 */
 function forEach(treeList, callback, config = {children:'children'}) {
  if (treeList) {
    for (let i = 0; i < treeList.length; i++) {
      const node = treeList[i]
      callback( node)
      forEach(node[config.children], callback, config)
    }
  }
}

/**
 * 将树转为列表
 * @param {object} treeList 树
 * @param {object} config 节点属性名配置 默认{children:'children'}
 * @returns {array} 节点数组
 */
function toList(treeList, config = {children:'children'}) {
  const result = []
  forEach(treeList, (node)=> {
    result.push({... node})
  },config)
  result.forEach(e=> delete e[config.children])
  return result
}



/**
 * 查询节点在树中的路径，包括当前节点
 * @param {array} treeList 树结构的节点列表
 * @param {*} key 节点的唯一键，节点标识
 * @return {array} 路径 若treeList为空或者未查到到key对应的节点则返回空数组，
 */
function getNodePath(treeList, key, config = {key:'key',children:'children'}) {
  const path = []
  const hit = getPath(treeList, key, path, config)
  if (hit != null && path.length > 0) {
    return path
  } else {
    return []
  }
}

function getPath(treeList, key, path, config = {key:'key',children:'children'}) {
  if (treeList) {
    for (let i = 0; i < treeList.length; i++) {
      const node = treeList[i]
      path.push(node)
      if (node[config.key] === key) {
        return node
      }
      if (node[config.children]) {
        const hit = getPath(node[config.children], key, path)
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
function getParentNode(treeList, key, config = {key:'key',children:'children'}) {
  const parentPath = getParentPath(treeList, key, config)
  return parentPath.length === 0 ? null : parentPath[parentPath.length - 1]
}

/**
 * 查询节点在树中的父路径，不包括当前节点
 * @param {array} treeList 树结构的节点列表
 * @param {*} key  节点的唯一键，节点标识
 * @return {array} 父路径 若treeList为空或者未查到到key对应的节点则返回空数组，若参数key对应的节点为根节点则返回空数组
 */
function getParentPath(treeList, key, config) {
  const path = []
  const hit = getPath(treeList, key, path, config)
  if (hit != null && path.length > 1) {
    path.pop()
    return path
  } else {
    return []
  }
}

const tools = {
  convertToTree,
  convertToCustomSortTree,
  toList,
  getNodeByKey,
  forEach,
  deepFirstTraverseTree,
  getNodePath,
  getParentPath,
  getParentNode
}
const DEFAULT_CONFIG = {
  key: 'key',
  parentId: 'parentId',
  sortSn: 'sortSn',
  children: 'children'
}

const treeUtil = {
  ... tools,
  defaultConfigMerge(config) {
    const finalConfig = {}
    Object.assign(finalConfig, DEFAULT_CONFIG, config)
    return finalConfig
  },
  createInstance (config) {
    const obj = {}
    const finalConfig = {}
    Object.assign(finalConfig, DEFAULT_CONFIG, config)
    for (const key in tools) {
      const func = tools[key]
      obj[key] = (...args) => func(...args, finalConfig)
    }
    return obj
  }
}

export default treeUtil

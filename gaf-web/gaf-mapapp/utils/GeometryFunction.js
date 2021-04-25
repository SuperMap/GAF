/*
 * Method: islandHoleHandlerForFeature。
 * 要素岛洞处理。
 *
 * 多面中，一个子面包含另一个子面，则被包含子面处理为岛洞。
 *
 * Parameters:
 * multiPolygon - {<SuperMap.Feature.Vector>} 需要进行岛洞处理的要素。
 *
 * Returns:
 * {<SuperMap.Feature.Vector>} 处理后的要素。
 */
function islandHoleHandlerForFeature(feature) {
  if (
    feature.geometry instanceof SuperMap.Geometry.MultiPolygon &&
    feature.geometry.components.length > 1
  ) {
    const newGeometry = islandHoleHandlerForMultiPolygon(feature.geometry)
    feature.geometry = newGeometry
  }
  return feature

  /*
   * Method: islandHoleHandlerForMultiPolygon
   * 处理误判为岛洞的多面。
   *
   * iClient 在解析 iServer 数据时，默认将面要素处理为 MultiPolygon 类型，但有的面要素带有岛洞，
   * 这种情况下应该做特殊处理，本函数可以对一个多面进行岛洞处理，并返回新的多面。
   *
   * Parameters:
   * multiPolygon - {<SuperMap.Geometry.MultiPolygon>} 需要进行岛洞处理的多面。
   *
   * Returns:
   * {<SuperMap.Geometry.MultiPolygon>} 处理后的多面。
   */
  function islandHoleHandlerForMultiPolygon(multiPolygon) {
    if (
      multiPolygon instanceof SuperMap.Geometry.MultiPolygon &&
      multiPolygon.components.length > 1
    ) {
      const mPTmp = multiPolygon.clone()
      const componentsPolygons = mPTmp.components

      // 洞面关系数组
      const polygonHoleGroup = []

      for (let k = 0, len = componentsPolygons.length; k < len; k++) {
        const geoPolygon = componentsPolygons[k]

        // 不处理已经是岛洞的面
        if (geoPolygon.components.length === 1) {
          const lineRings = geoPolygon.components[0]

          // 将每个点放到面中进行判断
          for (let j = 0, len1 = componentsPolygons.length; j < len1; j++) {
            if (componentsPolygons[j].components.length !== 1) continue

            if (j !== k) {
              const polygonGeoComp =
                componentsPolygons[j].components[0].components

              // 假设此面为岛洞
              let isAllPoiIn = true

              for (
                let i = 0, len2 = geoPolygon.components.length;
                i < len2;
                i++
              ) {
                const point = lineRings.components[i]
                if (isPointInPoly(point, polygonGeoComp) === false) {
                  isAllPoiIn = false
                  continue
                }
              }

              // 确定并记录洞面关系
              if (isAllPoiIn === true) {
                const polygonHole = [j, k]
                polygonHoleGroup.push(polygonHole)
              }
            }
          }
        } else {
          continue
        }
      }

      // 根据洞面信息重构多面 Geometry。

      const bPsTmp = []
      const hPsTmp = []
      for (let m = 0, len3 = polygonHoleGroup.length; m < len3; m++) {
        bPsTmp.push(polygonHoleGroup[m][0])
        hPsTmp.push(polygonHoleGroup[m][1])
      }

      // 岛洞基础面
      const bPs = delRepeatInArray(bPsTmp)
      // 洞面
      const hPs = delRepeatInArray(hPsTmp)

      // 独立面
      const iPs = []
      // 查找独立面
      for (
        let isIPs = 0, compLen = componentsPolygons.length;
        isIPs < compLen;
        isIPs++
      ) {
        let isNoHP = true

        for (let o = 0, len = bPs.length; o < len; o++) {
          if (isIPs === bPs[o]) {
            isNoHP = false
            break
          }
        }

        if (isNoHP === true) {
          for (let o = 0, len = hPs.length; o < len; o++) {
            if (isIPs === hPs[o]) {
              isNoHP = false
              break
            }
          }
        }

        if (isNoHP === true) {
          iPs.push(isIPs)
        }
      }

      // 新洞面信息
      const hpInfo = []

      // 组织新geometry所需要的信息
      for (let o = 0, len4 = bPs.length; o < len4; o++) {
        const ph = []
        ph.push(bPs[o])
        for (let m = 0, len3 = polygonHoleGroup.length; m < len3; m++) {
          if (bPs[o] === polygonHoleGroup[m][0]) {
            ph.push(polygonHoleGroup[m][1])
          }
        }

        if (ph.length > 1) {
          hpInfo.push(ph)
        }
      }

      const newComponents = []
      // 岛洞子面处理
      for (let m = 0, len3 = hpInfo.length; m < len3; m++) {
        const geoP = hpInfo[m]
        const newLineRings = []
        for (let n = 0, len6 = geoP.length; n < len6; n++) {
          newLineRings.push(componentsPolygons[geoP[n]].components[0])
        }
        const newGeoPolygon = new SuperMap.Geometry.Polygon(newLineRings)
        newComponents.push(newGeoPolygon)
      }

      // 独立子面处理
      for (let s = 0, len7 = iPs.length; s < len7; s++) {
        const is = iPs[s]
        newComponents.push(componentsPolygons[is])
      }

      multiPolygon.components = newComponents
    }

    return multiPolygon
  }

  /*
   * Method: delRepeatInArray
   * 删除数组中的重复元素。
   *
   * Parameters:
   * arr - {Array} 要进行重复元素删除的数组。
   *
   * Returns:
   * {Array} 无重复元素的数组。
   */
  function delRepeatInArray(arr) {
    const newArray = []
    const provisionalTable = {}
    for (let i = 0, a; (a = arr[i]) != null; i++) {
      if (!provisionalTable[a]) {
        newArray.push(a)
        provisionalTable[a] = true
      }
    }
    return newArray
  }

  /*
   * Method: PointInPoly
   * 判断一个点是否在多边形里面。(射线法)
   *
   * Parameters:
   * pt - {Object} 需要判定的点对象，该对象含有属性x(横坐标)，属性y(纵坐标)。
   * poly - {Array(Objecy)}  多边形节点数组。例如一个四边形：[{"x":1,"y":1},{"x":3,"y":1},{"x":6,"y":4},{"x":2,"y":10},{"x":1,"y":1}]。
   *
   * Returns:
   * {Boolean} 点是否在多边形内。
   */
  function isPointInPoly(pt, poly) {
    let isIn = false
    for (let i = -1, l = poly.length, j = l - 1; ++i < l; j = i)
      ((poly[i].y <= pt.y && pt.y < poly[j].y) ||
        (poly[j].y <= pt.y && pt.y < poly[i].y)) &&
        pt.x <
          ((poly[j].x - poly[i].x) * (pt.y - poly[i].y)) /
            (poly[j].y - poly[i].y) +
            poly[i].x &&
        (isIn = !isIn)
    return isIn
  }

  // 辅助函数 -end
}
export { islandHoleHandlerForFeature }

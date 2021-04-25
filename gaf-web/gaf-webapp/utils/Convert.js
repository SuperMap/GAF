const cesiumToSuperMap = {
  convertPoint(Cesium, SuperMap, point) {
    if (!Cesium || !SuperMap || !point) {
      return undefined
    }
    const lonlatPoint = Cesium.Cartographic.fromCartesian(point)
    const x = Cesium.Math.toDegrees(lonlatPoint.longitude)
    const y = Cesium.Math.toDegrees(lonlatPoint.latitude)
    if (x && y) {
      return new SuperMap.Geometry.Point(x, y)
    }
    return undefined
  },
  convertPolyline(Cesium, SuperMap, polyline) {
    if (!Cesium || !SuperMap || !polyline) {
      return undefined
    }
    const points = polyline.positions
    if (points && Array.isArray(points) && points.length >= 2) {
      const arr = []
      for (let i = 0, j = points.length; i < j; i++) {
        const point = this.convertPoint(Cesium, SuperMap, points[i])
        if (point) {
          arr.push(point)
        }
      }
      return new SuperMap.Geometry.LineString(arr)
    }
    return undefined
  },
  convertPolygon(Cesium, SuperMap, polygon) {
    if (!Cesium || !SuperMap || !polygon) {
      return undefined
    }
    const points = polygon.positions
    if (points && Array.isArray(points) && points.length >= 3) {
      const arr = []
      for (let i = 0, j = points.length; i < j; i++) {
        const point = this.convertPoint(Cesium, SuperMap, points[i])
        if (point) {
          arr.push(point)
        }
      }
      const linearRing = new SuperMap.Geometry.LinearRing(arr)
      return new SuperMap.Geometry.Polygon(linearRing)
    }
    return undefined
  },
}

const superMapToCesium = {
  geometryToEntity(Cesium, SuperMap, geometry) {
    const className = geometry.CLASS_NAME
    if (className === 'SuperMap.Geometry.MultiPolygon') {
      return this.multipolygonToEntities(Cesium, SuperMap, geometry)
    } else if (className === 'SuperMap.Geometry.Polygon') {
      return [this.polygonToEntity(Cesium, SuperMap, geometry)]
    }
    return undefined
  },
  polygonToEntity(Cesium, SuperMap, geometry) {
    const hierarchy = {}
    for (let i = 0, j = geometry.components.length; i < j; i++) {
      if (i === 0) {
        hierarchy.positions = this.GeometrytoDegreesArray(
          Cesium,
          SuperMap,
          geometry.components[i]
        )
      } else {
        if (!hierarchy.holes) {
          hierarchy.holes = []
        }
        hierarchy.holes.push({
          positions: this.GeometrytoDegreesArray(
            Cesium,
            SuperMap,
            geometry.components[i]
          ),
        })
      }
    }
    return new Cesium.Entity({
      polygon: {
        hierarchy,
        material: Cesium.Color.BLUE.withAlpha(0.5),
      },
      depthTestEnabled: false,
    })
  },
  multipolygonToEntities(Cesium, SuperMap, geometry) {
    const components = geometry.components
    const resultEntities = []
    let entity = null
    for (const item in components) {
      entity = this.polygonToEntity(Cesium, SuperMap, components[item])
      resultEntities.push(entity)
    }
    return resultEntities
  },
  GeometrytoDegreesArray(Cesium, SuperMap, geometry) {
    const vertices = geometry.getVertices()
    const degreesArr = []
    for (const o in vertices) {
      degreesArr.push(vertices[o].x, vertices[o].y)
    }
    return Cesium.Cartesian3.fromDegreesArray(degreesArr)
  },
}
export { cesiumToSuperMap, superMapToCesium }

<template>
  <span v-show="showPop">
    <gaf-map-cesium-popup
      ref="CesiumPopup"
      :map="map"
      :activie="showPop"
      :title="identityInfo.title"
      :data-source="dataSource"
      @close="showPop = false"
    />
  </span>
</template>

<script>
let selectedEntity
let oldPosition
export default {
  name: 'RangeThemeMap',
  props: {
    map: {
      type: Object,
      required: false,
      default() {
        return {}
      },
    },
    // 后端获取的数据
    sourceData: {
      type: Object,
      required: false,
      default() {
        return {}
      },
    },
  },
  data() {
    return {
      // 解析后的数据
      attributeData: [],
      //  point polyline  polygon 类型
      dataType: '',
      // 色带
      colorRamp: [],
      // 查询到的数据
      featuresData: {},
      // 解析后数据 暂时存的是集合对象
      visualData: [],
      // 自定义颜色
      customColor: [],
      // 单值符号化的字段和字段索引
      symbolField: '',
      symbolFieldIndex: -1,
      // 标注的信息
      labelInfo: {},
      // 标注的字段索引
      labelFieldIndex: -1,
      // 解析后的属性识别信息（可见性与字段）
      identityInfo: {},
      // 要素识别的时候绑定到属性框的数据 PopupList
      dataSource: [],
      // 用于关闭属性表的
      isClosed: true,
      // 点击的位置
      curPosition: null,
      // 分段颜色
      intervalColor: [],
      // 分段个数
      groupCount: -1,
      // 是否显示气泡属性控制
      showPop: false,
    }
  },
  watch: {},
  methods: {
    // 根据不同的数据类型去展示数据
    display() {
      if (this.sourceData.visualizationInfo.dataVisualizationSetting === '') {
        this.$message.error('专题图符号化配置错误，请重新配置')
      } else if (this.sourceData.actionOptionType === 'RESTDATA') {
        this.doRestData()
      } else if (this.sourceData.actionOptionType === 'RESTAPI') {
        this.doRestAPI()
      }
    },
    doRestData() {
      const url = this.sourceData.actionURL + '/data/'
      const dataSetting = JSON.parse(
        this.sourceData.visualizationInfo.dataVisualizationSetting
      )
      const dataSourceName = dataSetting.dataSourceName
      const dataSetName = dataSetting.datasetName
      this.intervalColor = dataSetting.color
      this.groupCount = dataSetting.groupCount
      this.symbolField = dataSetting.themeField.toLocaleUpperCase()
      this.labelInfo = dataSetting.label
      this.identityInfo = dataSetting.property
      this.title = this.identityInfo.title
      const getFeatureParam = new SuperMap.REST.FilterParameter({})
      const getFeatureBySQLParams = new SuperMap.REST.GetFeaturesBySQLParameters(
        {
          queryParameter: getFeatureParam,
          toIndex: -1,
          datasetNames: [dataSourceName + ':' + dataSetName],
        }
      )
      const getFeatureBySQLService = new SuperMap.REST.GetFeaturesBySQLService(
        url,
        {
          eventListeners: {
            processCompleted: this.onQueryComplete,
            processFailed: this.processFailed,
          },
        }
      )
      getFeatureBySQLService.processAsync(getFeatureBySQLParams)
    },
    onQueryComplete(queryEventArgs) {
      const selectedFeatures = queryEventArgs.originResult.features
      this.map.entities.removeAll()
      this.featuresData = selectedFeatures
      if (this.featuresData && this.featuresData.length > 0) {
        this.symbolFieldIndex = this.featuresData[0].fieldNames.findIndex(
          (p) => p === this.symbolField
        )
        if (this.labelInfo.visible === true) {
          this.labelFieldIndex = this.featuresData[0].fieldNames.findIndex(
            (p) => p === this.labelInfo.field.toLocaleUpperCase()
          )
        }
        if (this.symbolFieldIndex > -1) {
          this.processFeaturesData(this.featuresData)
        }
      }
    },
    processFailed(queryEventArgs) {
      this.$message.error('查询失败！')
    },

    /*
      获取要素识别信息
      @identity 要素识别信息this.indentityInfo属性值
    */
    addIdentityInfo(feature, identity) {
      if (identity.visible === true) {
        const obj = {}
        for (let m = 0; m < identity.fields.length; m++) {
          const index = feature.fieldNames.findIndex(
            (p) => p === identity.fields[m].name.toLocaleUpperCase()
          )
          obj[identity.fields[m].name] = feature.fieldValues[index]
        }
        return obj
      }
    },
    // 根据数据类型处理数据
    processFeaturesData(features) {
      if (features && features.length > 0) {
        const featureType = features[0].geometry.type
        switch (featureType) {
          case 'REGION':
            this.processFeaturesDataPolygonA(features)
            break
          case 'LINE':
            this.processFeaturesDataPolylineA(features)
            break
          case 'POINT':
            this.processFeaturesDataPointA(features)
            break
        }
        // 取消添加实体后存在的默认单击事件
        this.map.cesiumWidget.screenSpaceEventHandler.removeInputAction(
          Cesium.ScreenSpaceEventType.LEFT_CLICK
        )
        // 注册事件
        this.map.cesiumWidget.screenSpaceEventHandler.setInputAction(
          this.pickAndSelectObject,
          Cesium.ScreenSpaceEventType.LEFT_CLICK
        )
        this.map.scene.postRender.addEventListener(this.setPosition)
      }
    },

    /*
      利用平均分段方法 设置色带的颜色
      参数 1所有的要素 2符号化的字段索引 3分段个数 4分段颜色的数组
      返回值的是色带的数组对象
    */
    setRampColor(features, fieldIndex, intervalNum, colorArr) {
      const arr = []
      for (let i = 0; i < features.length; i++) {
        arr.push(features[i].fieldValues[fieldIndex])
      }
      return this.getRangeArr(arr, intervalNum, colorArr)
    },
    setRampColorRestAPI(features, fieldName, intervalNum, colorArr) {
      const arr = []
      for (let i = 0; i < features.length; i++) {
        arr.push(features[i].attributes[fieldName])
      }
      return this.getRangeArr(arr, intervalNum, colorArr)
    },
    // 获取色带
    getRangeArr(arr, intervalNum, colorArr) {
      const maxValue = Math.max.apply(null, arr)
      const minValue = Math.min.apply(null, arr)
      const rangeArr = []
      // 间断值
      const intervalValue = (maxValue - minValue) / intervalNum
      for (let n = 0; n < intervalNum - 1; n++) {
        rangeArr.push({
          startValue: minValue + intervalValue * n,
          endValue: minValue + intervalValue * (n + 1),
          color: colorArr[n],
        })
      }
      rangeArr.push({
        startValue: minValue + intervalValue * (intervalNum - 1),
        endValue: maxValue + 1,
        color: colorArr[intervalNum - 1],
      })
      return rangeArr
    },
    // 获取填充的颜色
    getFillColor(colorArr, fieldValue) {
      const findObj = colorArr.find(
        (p) => p.startValue <= fieldValue && p.endValue > fieldValue
      )
      let color = Cesium.Color.YELLOW
      if (findObj) {
        color = Cesium.Color.fromCssColorString(findObj.color)
      }
      return color
    },
    // 单击实体对象 事件
    pickAndSelectObject(e) {
      this.curPosition = this.map.scene.pickPosition(e.position)
      const picked = this.map.scene.pick(e.position)
      if (picked) {
        const entity = Cesium.defaultValue(picked.id, picked.primitive.id)
        if (entity instanceof Cesium.Entity) {
          selectedEntity = entity
        }
      } else {
        selectedEntity = undefined
      }
      if (selectedEntity) {
        const selectedInfo = selectedEntity.featureData
        this.dataSource = this.convertDataSource(selectedInfo)
        this.showPop = true
      }
    },
    // 设置显示的位置 调用组件中的方法
    setPosition() {
      if (
        this.sourceData.visualizationInfo &&
        this.sourceData.visualizationInfo.visualizationType ===
          'RangeThemeLayer'
      ) {
        if (selectedEntity && this.curPosition) {
          if (
            this.$refs.CesiumPopup &&
            this.$refs.CesiumPopup.setTablePosition
          ) {
            this.$refs.CesiumPopup.setTablePosition(this.curPosition)
            oldPosition = this.curPosition
          }
        } else if (oldPosition) {
          this.$refs.CesiumPopup.setTablePosition(oldPosition)
        }
      }
    },
    // 转换成符合要素识别绑定的数据格式 PopupList组件
    convertDataSource(datasource) {
      const arr = []
      for (const key in datasource) {
        arr.push({
          name: key,
          value: datasource[key],
        })
      }
      return arr
    },
    // RESTAPI数据处理
    async doRestAPI() {
      // 处理数据
      this.apiData = await this.$axios.$get(this.sourceData.actionURL)
      const dataSetting = JSON.parse(
        this.sourceData.visualizationInfo.dataVisualizationSetting
      )
      this.intervalColor = dataSetting.color
      this.groupCount = dataSetting.groupCount
      this.symbolField = dataSetting.themeField
      this.labelInfo = dataSetting.label
      this.identityInfo = dataSetting.property
      this.title = this.identityInfo.title
      this.map.entities.removeAll()
      if (this.apiData && this.apiData.length > 0) {
        const featureType = this.apiData[0].type
        switch (featureType) {
          case 'POINT':
            this.processFeaturesDataPointRESTAPIA(this.apiData)
            break
          case 'LINE':
            this.processFeaturesDataPolylineRESTAPIA(this.apiData)
            break
          case 'REGION':
            this.processFeaturesDataPolygonRESTAPIA(this.apiData)
            break
        }
        this.map.cesiumWidget.screenSpaceEventHandler.removeInputAction(
          Cesium.ScreenSpaceEventType.LEFT_CLICK
        )
        this.map.cesiumWidget.screenSpaceEventHandler.setInputAction(
          this.pickAndSelectObject,
          Cesium.ScreenSpaceEventType.LEFT_CLICK
        )
        this.map.scene.postRender.addEventListener(this.setPosition)
      }
    },
    addIdentityInfoRESTAPI(feature, identity) {
      if (identity.visible === true) {
        const obj = {}
        for (let m = 0; m < identity.fields.length; m++) {
          obj[identity.fields[m].name] =
            feature.attributes[identity.fields[m].name]
        }
        return obj
      }
    },

    // 将获取的点进一步解析成绘制实体的格式（经度,纬度,经度,纬度,经度,纬度）
    getPointsArrByPoints(points) {
      const result = []
      points.forEach((p) => {
        result.push(p.x, p.y)
      })
      return result
    },

    addPolygonA(map, pointArr, idIndex, fillColor, lineColor, featureData) {
      map.entities.add({
        id: idIndex,
        featureData,
        polygon: {
          hierarchy: Cesium.Cartesian3.fromDegreesArray(pointArr),
          height: 100,
          material: fillColor,
          outline: true,
          outlineColor: lineColor,
          outlineWidth: 10,
        },
      })
    },
    // 关于label的参数都应该存在labelInfo中
    addPolygonLabelA(
      map,
      pointArr,
      idIndex,
      fillColor,
      lineColor,
      featureData,
      labelPoint,
      labelValue,
      labelInfo
    ) {
      map.entities.add({
        id: idIndex,
        featureData,
        position: Cesium.Cartesian3.fromDegrees(
          labelPoint.x,
          labelPoint.y,
          1000
        ),
        polygon: {
          hierarchy: Cesium.Cartesian3.fromDegreesArray(pointArr),
          height: 100,
          material: fillColor,
          outline: true,
          outlineColor: lineColor,
          outlineWidth: 2,
        },
        label: {
          text: labelValue,
          font: '25px 微软雅黑',
          verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
          horizontalOrigin: Cesium.HorizontalOrigin.LEFT,
          fillColor: Cesium.Color.fromCssColorString(labelInfo.color), // 字体颜色
          showBackground: true,
          backgroundColor: Cesium.Color.fromBytes(255, 128, 128, 100),
          outline: true,
          outlineWidth: 1,
          outlineColor: Cesium.Color.RED,
          // style: Cesium.LabelStyle.FILL
        },
      })
    },

    // 面要素全部当作单体的处理方式
    processFeaturesDataPolygonA(features) {
      const colorArr = this.setRampColor(
        features,
        this.symbolFieldIndex,
        this.groupCount,
        this.intervalColor
      )
      // 环的总数 记录数组个数
      let sumPart = 0
      for (let i = 0; i < features.length; i++) {
        const fieldValue = features[i].fieldValues[this.symbolFieldIndex]
        const fillColor = this.getFillColor(colorArr, fieldValue)
        let m = 0
        // 节点总数
        let pointSum = 0
        for (let p = 0; p < features[i].geometry.parts.length; p++) {
          const pointArr = []
          for (
            m = pointSum;
            m < pointSum + features[i].geometry.parts[p];
            m++
          ) {
            pointArr.push(
              features[i].geometry.points[m].x,
              features[i].geometry.points[m].y
            )
          }
          pointSum = pointSum + features[i].geometry.parts[p]
          // 获取要素识别的信息
          const featureData = this.addIdentityInfo(
            features[i],
            this.identityInfo
          )
          const labelValue =
            this.labelFieldIndex === -1
              ? ''
              : features[i].fieldValues[this.labelFieldIndex]
          if (this.labelInfo.visible === true) {
            this.addPolygonLabelA(
              this.map,
              pointArr,
              p + sumPart,
              fillColor,
              fillColor,
              featureData,
              { x: pointArr[0], y: pointArr[1] },
              labelValue,
              this.labelInfo
            )
          } else {
            this.addPolygonA(
              this.map,
              pointArr,
              fillColor,
              p + sumPart,
              featureData
            )
          }
        }
        sumPart = sumPart + features[i].geometry.parts.length
      }
    },
    processFeaturesDataPolygonRESTAPIA(features) {
      const colorArr = this.setRampColorRestAPI(
        features,
        this.symbolField,
        this.groupCount,
        this.intervalColor
      )
      for (let i = 0; i < features.length; i++) {
        const fieldValue = features[i].attributes[this.symbolField]
        const fillColor = this.getFillColor(colorArr, fieldValue)
        const pointArr = this.getPointsArrByPoints(features[i].points)
        const labelValue = features[i].attributes[this.labelInfo.field]
        // 获取要素识别的信息
        const featureData = this.addIdentityInfoRESTAPI(
          features[i],
          this.identityInfo
        )
        if (this.labelInfo.visible === false) {
          this.addPolygonA(this.map, pointArr, fillColor, i, featureData)
        } else {
          this.addPolygonLabelA(
            this.map,
            pointArr,
            i,
            fillColor,
            fillColor,
            featureData,
            features[i].centerPoint,
            labelValue,
            this.labelInfo
          )
        }
      }
    },

    /*
      添加点实体
      参数 地图 点 点颜色 点索引 识别的数据信息
    * */
    addPointA(map, point, pointColor, idIndex, featureData) {
      map.entities.add({
        id: idIndex,
        position: Cesium.Cartesian3.fromDegrees(point.x, point.y, 1000),
        featureData,
        point: {
          color: pointColor,
          pixelSize: 20,
          heightReference: Cesium.HeightReference.RELATIVE_TO_GROUND,
        },
      })
    },
    /*
     * 添加点实体 含标注
     * 参数 地图 点 点颜色 点索引 识别的数据信息 标注的内容 标注信息
     * */
    addPointLabelA(
      map,
      point,
      pointColor,
      idIndex,
      featureData,
      labelValue,
      labelInfo
    ) {
      map.entities.add({
        id: idIndex,
        position: Cesium.Cartesian3.fromDegrees(point.x, point.y, 1000),
        featureData,
        point: {
          color: pointColor,
          pixelSize: 20,
          heightReference: Cesium.HeightReference.RELATIVE_TO_GROUND,
          featureData,
        },
        label: {
          text: labelValue,
          font: '25px 微软雅黑',
          verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
          horizontalOrigin: Cesium.HorizontalOrigin.LEFT,
          fillColor: Cesium.Color.fromCssColorString(labelInfo.color), // 字体颜色
          showBackground: true,
          // backgroundColor: Cesium.Color.fromBytes(255, 128, 128, 100),
          backgroundColor: Cesium.Color.fromBytes(0, 0, 0, 50),
          outlineWidth: 20,
          outlineColor: Cesium.Color.YELLOW,
          style: Cesium.LabelStyle.FILL,
        },
      })
    },
    processFeaturesDataPointA(features) {
      const colorArr = this.setRampColor(
        features,
        this.symbolFieldIndex,
        this.groupCount,
        this.intervalColor
      )
      for (let i = 0; i < features.length; i++) {
        const point = features[i].geometry.center
        // 获取字段值并获取颜色
        const fieldValue = features[i].fieldValues[this.symbolFieldIndex]
        const pointColor = this.getFillColor(colorArr, fieldValue)
        // 获取要素识别的信息
        const featureData = this.addIdentityInfo(features[i], this.identityInfo)
        const labelValue =
          this.labelFieldIndex === -1
            ? ''
            : features[i].fieldValues[this.labelFieldIndex]
        if (this.labelInfo.visible === true) {
          this.addPointLabelA(
            this.map,
            point,
            pointColor,
            i,
            featureData,
            labelValue,
            this.labelInfo
          )
        } else {
          this.addPointA(this.map, point, pointColor, i, featureData)
        }
      }
    },
    processFeaturesDataPointRESTAPIA(features) {
      const colorArr = this.setRampColorRestAPI(
        features,
        this.symbolFieldIndex,
        this.groupCount,
        this.intervalColor
      )
      for (let i = 0; i < features.length; i++) {
        const point = features[i].geometry.center
        // 获取字段值并获取颜色
        const fieldValue = features[i].attributes[this.symbolField]
        const pointColor = this.getFillColor(colorArr, fieldValue)
        const labelValue = features[i].attributes[this.labelInfo.field]
        // 获取要素识别的信息
        const featureData = this.addIdentityInfoRESTAPI(
          features[i],
          this.identityInfo
        )
        if (this.labelInfo.visible === true) {
          this.addPointLabelA(
            this.map,
            point,
            pointColor,
            i,
            featureData,
            labelValue,
            this.labelInfo
          )
        } else {
          this.addPointA(this.map, point, pointColor, i, featureData)
        }
      }
    },

    addLineA(map, pointArr, lineColor, idIndex, featureData) {
      map.entities.add({
        id: idIndex,
        position: Cesium.Cartesian3.fromDegrees(pointArr[0], pointArr[1], 1000),
        featureData,
        polyline: {
          positions: Cesium.Cartesian3.fromDegreesArray(pointArr),
          width: 5,
          material: lineColor,
        },
      })
    },
    addLineLabelA(
      map,
      pointArr,
      lineColor,
      idIndex,
      featureData,
      labelValue,
      labelInfo
    ) {
      map.entities.add({
        id: idIndex,
        position: Cesium.Cartesian3.fromDegrees(pointArr[0], pointArr[1], 0),
        featureData,
        polyline: {
          positions: Cesium.Cartesian3.fromDegreesArray(pointArr),
          width: 2,
          material: lineColor,
        },
        label: {
          text: labelValue,
          font: '25px 微软雅黑',
          verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
          horizontalOrigin: Cesium.HorizontalOrigin.LEFT,
          fillColor: Cesium.Color.fromCssColorString(labelInfo.color), // 字体颜色
          showBackground: true,
          backgroundColor: Cesium.Color.fromBytes(255, 128, 128),
          outlineWidth: 1,
          outlineColor: Cesium.Color.RED,
          style: Cesium.LabelStyle.FILL,
        },
      })
    },
    processFeaturesDataPolylineA(features) {
      const colorArr = this.setRampColor(
        features,
        this.symbolFieldIndex,
        this.groupCount,
        this.intervalColor
      )
      // 环的总数 记录数组个数
      let sumPart = 0
      for (let i = 0; i < features.length; i++) {
        const fieldValue = features[i].fieldValues[this.symbolFieldIndex]
        const lineColor = this.getFillColor(colorArr, fieldValue)
        let m = 0
        // 节点总数
        let pointSum = 0
        for (let p = 0; p < features[i].geometry.parts.length; p++) {
          const pointArr = []
          for (
            m = pointSum;
            m < pointSum + features[i].geometry.parts[p];
            m++
          ) {
            pointArr.push(
              features[i].geometry.points[m].x,
              features[i].geometry.points[m].y
            )
          }
          pointSum = pointSum + features[i].geometry.parts[p]
          // 获取要素识别的信息
          const featureData = this.addIdentityInfo(
            features[i],
            this.identityInfo
          )
          const labelValue =
            this.labelFieldIndex === -1
              ? ''
              : features[i].fieldValues[this.labelFieldIndex]
          if (this.labelInfo.visible === true) {
            this.addLineLabelA(
              this.map,
              pointArr,
              lineColor,
              p + sumPart,
              featureData,
              labelValue,
              this.labelInfo
            )
          } else {
            this.addLineA(
              this.map,
              pointArr,
              lineColor,
              p + sumPart,
              featureData
            )
          }
        }
        sumPart = sumPart + features[i].geometry.parts.length
      }
    },
    processFeaturesDataPolylineRESTAPIA(features) {
      const colorArr = this.setRampColorRestAPI(
        features,
        this.symbolFieldIndex,
        this.groupCount,
        this.intervalColor
      )
      // 环的总数 记录数组个数
      let sumPart = 0
      for (let i = 0; i < features.length; i++) {
        const fieldValue = features[i].attributes[this.symbolField]
        const lineColor = this.getFillColor(colorArr, fieldValue)
        let m = 0
        // 节点总数
        let pointSum = 0
        for (let p = 0; p < features[i].geometry.parts.length; p++) {
          const pointArr = []
          for (
            m = pointSum;
            m < pointSum + features[i].geometry.parts[p];
            m++
          ) {
            pointArr.push(
              features[i].geometry.points[m].x,
              features[i].geometry.points[m].y
            )
          }
          pointSum = pointSum + features[i].geometry.parts[p]
          // 获取要素识别的信息
          const featureData = this.addIdentityInfo(
            features[i],
            this.identityInfo
          )
          const labelValue = features[i].attributes[this.labelInfo.field]
          if (this.labelInfo.visible === true) {
            this.addLineLabelA(
              this.map,
              pointArr,
              lineColor,
              p + sumPart,
              featureData,
              labelValue,
              this.labelInfo
            )
          } else {
            this.addLineA(
              this.map,
              pointArr,
              lineColor,
              p + sumPart,
              featureData
            )
          }
        }
        sumPart = sumPart + features[i].geometry.parts.length
      }
    },
  },
}
</script>

<style scoped></style>

  name: 'GafMapViewer',
  data() {
    return {
      loaded: false,
      mapApp: null,
      location: null,
      layerList:[]
    }
  },
  beforeCreate() {
    Vue.prototype.$bus = new Vue()
    Vue.prototype.$mapActions = gafmapui.mapActions
  },
  computed: {
    smSceneList() {
      return this.layerList.filter(item => item.resourceTag === 'RESTREALSPACE')
    },
    smLayerList() {
      return this.layerList.filter(item => item.resourceTag === 'RESTMAP')
    },
    tiandituLayerList() {
      return this.layerList.filter(
        item => item.sourceType === 'MAPWORLD' && item.isBaseLayer === true
      )
    }
  },
  watch: {
    location(val) {
      this.$mapActions.flyTo(val)
    }
  },
  methods: {
    getQueryVariable(variable) {
      let query = window.location.search.substring(1);
      let vars = query.split("&");
      for (let i = 0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if (pair[0] == variable) { return pair[1]; }
      }
    },
    async loadAppConfig() {
      const previewCode = this.getQueryVariable("previewCode")
      let url = '${configUrl}'
      if (previewCode != undefined) {
        url += '?previewCode=' + previewCode
      }
      const response = await axios.get(url)
      const mapApp = response.data.data;
      this.location = mapApp.location
      if(mapApp.baseLayer){
        this.layerList.push(mapApp.baseLayer)
      }
      this.mapApp = mapApp
    },
    onTreeNodeChecked(checkedKeys, info) {
      const self = this
      const layerList = []
      checkedKeys.forEach(key => {
        const data = self.mapApp.resourceTree.allDataList.find(item => {
          return item.resourceId.toString() === key.toString()
        })
        if (data) {
          layerList.push(data)
        }
      })
      if (self.mapApp.baseLayer) {
        layerList.unshift(self.mapApp.baseLayer)
      }
      self.layerList = layerList
    },
    onSelect(selectedKeys, info) {
      const self = this
      const selectedKey = selectedKeys[0]
      const layerList = self.mapApp.resourceTree.allDataList.map(item => {
        if (item.resourceId === selectedKey) {
          const newLayer = { ...item, location: true }
          return newLayer
        }
        return item
      })
      const currentNode = layerList.find(
        layer => layer.resourceId === selectedKey
      )
      if (this.mapApp.baseLayer) {
        layerList.unshift(this.mapApp.baseLayer)
      }
      this.layerList = layerList
    },
    onViewerLoaded() {
      this.loaded = true
    }
  },
  beforeMount() {
    this.loadAppConfig()
  }

export default {
  Sm3dMeasure: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-measure />
    </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/srtm_54_07@zhufeng',
              resourceName: ''
            },
            {
              location: true,
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/image',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dViewshed: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-viewshed />
  </gaf-map-viewer>`,
    js: `export default {
    data() {
      return {
        smSceneList: [
          {
            location: true,
            resourceLocation:
            'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
            resourceName: ''
          }
        ]
      }
    }
  }`,
    css: ``,
  },
  Sm3dSkyline: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-skyline />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dProfile: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-profile />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dSightline: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-sightline />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3Shadowquery: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-shadowquery />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dGeologicBodyAnalysis: {
    html: `<gaf-map-viewer>
    <GafMapGeologicBodyAnalysics :modelUrls="modelUrls" />
  </gaf-map-viewer>`,
    js: `export default {
      mounted() {
        window.scene.camera.setView(this.cameraPosition)
      },
      data() {
        return {
          modelUrls: [
            'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer1/features/1.json',
            'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer2/features/1.json',
            'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer3/features/1.json',
            'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer4/features/1.json',
            'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer5/features/1.json',
            'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer6/features/1.json'
          ],
          cameraPosition: {
            destination: new Cesium.Cartesian3(
              -2167835.4408299956,
              4423497.534529096,
              4095839.2845661934
            ),
            orientation: {
              heading: 4.029329438295484,
              pitch: -0.23796647219353817,
              roll: 8.994289757424667e-10
            }
          }
        }
      }
    }`,
    css: ``,
  },
  Sm3dTerrainOperation: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-terrain-operation />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/srtm_54_07@zhufeng',
              resourceName: ''
            },
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/image',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dTerrainFlood: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-terrain-flood />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/srtm_54_07@zhufeng',
              resourceName: ''
            },
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/image',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dTerrainSlope: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-terrain-slope />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/srtm_54_07@zhufeng',
              resourceName: ''
            },
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/image',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dTerrainIsoline: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-terrain-isoline />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/srtm_54_07@zhufeng',
              resourceName: ''
            },
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/image',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dClipBox: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-clip-box />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dClipPlane: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-clip-plane />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dClipCross: {
    html: `  <gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-clip-cross />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
  Sm3dClipPolygon: {
    html: `  <gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-clip-polygon />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              location: true,
              resourceLocation:
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceName: ''
            }
          ]
        }
      }
    }`,
    css: ``,
  },
}

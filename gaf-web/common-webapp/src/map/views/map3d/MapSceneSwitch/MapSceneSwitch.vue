<template>
  <div class="MapSceneSwitch">
    <a-modal v-model="visible" title="服务地址" @ok="handleOk">
      <!-- <input type="text" v-model="urlValue" /> -->
      <a-input v-model="urlValue" placeholder="请输入相应的服务地址" />
    </a-modal>
    <p>资源列表：</p>
    <ul v-show="listData.length">
      <li
        v-for="(item, index) in listData"
        :key="index"
        @click="handleListItemClick(item.location)"
      >
        {{ item.name }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'MapSceneSwitch',
  props: {},
  data() {
    return {
      visible: false,
      type: '',
      urlValue: '',
      listData: [],
    }
  },

  methods: {
    handleOk() {
      this.handleBaseMapUrlSubmit()
      this.visible = false
      this.urlValue = ''
    },
    handleModleShow(type) {
      this.type = type
      this.visible = true
    },
    handleListItemClick(urlValue) {
      this.$emit('getExchangeValue', this.type, urlValue)
    },
    async handleBaseMapUrlSubmit() {
      const urlValue = this.urlValue
      if (this.type === 'baseMap') {
        if (urlValue.search('/maps') >= 0) {
          const url =
            'https://iserver.supermap.io/iserver/services/map-world/rest/maps/世界地图_Night'
          this.$emit('getExchangeValue', this.type, url)
        } else {
          const result = await this.$axios.$get(
            'https://iserver.supermap.io/iserver/services/map-world/rest/maps.json'
          )  
          this.listData = result.map((item) => {
            return { name: item.name, location: item.path }
          })
        }
      } else if (this.type === 'scene') {
        const reg = RegExp('/scenes/')
        let url = urlValue
        if (reg.test(urlValue)) {
          url = urlValue.split('/scenes/')[0]
        }
        const result = await this.$axios.$get(url + '/scenes.json')
        if (result && result.length > 0) {
          if (result.length > 0) {
            this.listData = result.map((item) => {
              return { name: item.name, location: item.path }
            })
          } else {
            this.$emit('getExchangeValue', this.type, this.urlValue)
          }
        }
      }
    },
  },
}
</script>

<template>
  <gaf-map-split-screen :type="params" :one="one" :two="two" />
</template>

<script>
export default {
  name: 'GafMapSplitScreens',
  props: {
    params: {
      type: String,
      default: () => '',
    },
  },
  data() {
    return {
      one: window.SMWEBGIS.dirs.splitScreen.data.one,
      two: window.SMWEBGIS.dirs.splitScreen.data.two,
    }
  },
  async beforeCreate() {
    const data = await this.$axios.$get(
      'http://192.168.192.232/config/dir.json'
    )
    const { one, two } = data
    this.one = one
    this.two = two
  }
}
</script>

<style></style>

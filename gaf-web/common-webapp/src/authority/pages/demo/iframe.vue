<template>
  <iframe
    :width="width"
    :height="height"
    src="https://www.baidu.com"
    frameborder="0"
  ></iframe>
</template>

<script>
export default {
  name: 'IFrame',
  computed: {
    width() {
      return this.$store.state.contentWidth
    },
    height() {
      return this.$store.state.contentHeight
    }
  }
}
</script>

<style scoped></style>

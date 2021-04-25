<template>
  <span />
</template>

<script>
export default {
  name: 'HeatMap',
  props: {
    map: {
      type: Object,
      required: false,
      default() {
        return {}
      },
    },
  },
}
</script>

<style scoped></style>

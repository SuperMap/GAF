<template>
  <gaf-chart :options="options" :height="attr.height" />
</template>

<script>
export default {
  name: 'PlatChart',
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      options: null,
    }
  },
  computed: {
    attr() {
      return this.data.attr || {}
    },
  },
  watch: {
    'attr.url': {
      handler(val) {
        if (val) {
          this.__timeoutid && window.clearTimeout(this.__timeoutid)
          window.setTimeout(async () => {
            const options = await this.$axios.$get(val)
            this.options = options
          }, 300)
        }
      },
      immediate: true,
    },
  },
}
</script>

<style scoped></style>

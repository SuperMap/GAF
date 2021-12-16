<template>
  <component :is="gafPageLayout" />
</template>

<script>
import DefaultLayout from './layouts/default'
import BlankLayout from './layouts/blank'
export default {
  components: {
    BlankLayout,
    DefaultLayout
  },
  data () {
    return {
      gafPageLayout: this.getPageLayout()
    }
  },
  watch: {
    $route () {
      this.gafPageLayout = this.getPageLayout()
    }
  },
  methods: {
    getPageLayout () {
      if (this.$route.path === '/login') {
        return 'blank-layout'
      } else {
        return 'default-layout'
      }
    }
  }
}
</script>

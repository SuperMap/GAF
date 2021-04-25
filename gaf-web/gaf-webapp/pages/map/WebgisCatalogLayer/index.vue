<template>
  <webgis-catalog-layer />
</template>
<script>
import { WebgisCatalogLayer } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    WebgisCatalogLayer,
  },
}
</script>

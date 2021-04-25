<template>
  <webgis-resource-catalog />
</template>
<script>
import { WebgisResourceCatalog } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    WebgisResourceCatalog,
  },
}
</script>

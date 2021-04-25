<template>
  <webgis-toolbar />
</template>
<script>
import { WebgisToolbar } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    WebgisToolbar,
  },
}
</script>

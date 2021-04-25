<template>
  <webgis-basic-app />
</template>
<script>
import { WebgisBasicApp } from 'common-mapapp'
export default {
  middleware: 'auth',
  components: {
    WebgisBasicApp,
  },
}
</script>

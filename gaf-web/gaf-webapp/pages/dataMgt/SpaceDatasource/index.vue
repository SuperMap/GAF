<template>
  <space-datasource />
</template>
<script>
import { SpaceDatasource } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    SpaceDatasource,
  },
}
</script>

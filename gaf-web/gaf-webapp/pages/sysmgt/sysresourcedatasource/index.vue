<template>
  <sys-resource-datasource />
</template>
<script>
import { SysResourceDatasource } from 'common-webbase'

export default {
  middleware: 'auth',
  components: {
    SysResourceDatasource,
  },
}
</script>

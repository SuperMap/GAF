<template>
  <sys-component />
</template>
<script>
import { SysComponent } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    SysComponent,
  },
}
</script>

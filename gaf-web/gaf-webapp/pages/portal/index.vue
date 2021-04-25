<template>
  <portal-login />
</template>
<script>
import { portalLogin } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    portalLogin,
  },
}
</script>

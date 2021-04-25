<template>
  <menus />
</template>
<script>
import { menus } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    menus,
  },
}
</script>

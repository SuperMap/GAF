<template>
  <Menu />
</template>
<script>
import { Menu } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    Menu,
  },
}
</script>

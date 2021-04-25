<template>
  <component-module />
</template>
<script>
import { ComponentModule } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    ComponentModule,
  },
}
</script>

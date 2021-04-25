<template>
  <code-template />
</template>
<script>
import { codeTemplate } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    codeTemplate,
  },
}
</script>

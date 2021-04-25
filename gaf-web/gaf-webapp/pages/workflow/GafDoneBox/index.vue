<template>
  <gaf-done-box />
</template>
<script>
import { GafDoneBox } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafDoneBox,
  },
}
</script>

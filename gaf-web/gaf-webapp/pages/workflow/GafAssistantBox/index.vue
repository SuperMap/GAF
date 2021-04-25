<template>
  <gaf-assistant-box />
</template>
<script>
import { GafAssistantBox } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafAssistantBox,
  },
}
</script>

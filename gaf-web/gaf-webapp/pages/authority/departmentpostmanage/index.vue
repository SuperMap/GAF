<template>
  <department-post-manage />
</template>
<script>
import { DepartmentPostManage } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    DepartmentPostManage,
  },
}
</script>

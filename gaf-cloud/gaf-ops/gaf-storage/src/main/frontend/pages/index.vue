<template>
  <div>
    <!--<system-admin v-if="isAdmin" />
    <tenant-admin v-else-if="isTenant" />
    <common-user v-else-if="isUser" />
     {{ Math.random() }}-->
    <!-- <with-login v-if="isLogin" /> -->
    <!-- <my-webgis-app v-if="isLogin" /> -->
    <!-- <no-login v-else /> -->
    <storage></storage>
  </div>
</template>
<script>
// import { MyWebgisApp } from 'common-mapapp'
// import NoLogin from '../views/home/NoLogin'
// import WithLogin from '../views/home/WithLogin'
import storage from '../pages/storage/StorageManagement'
/* import SystemAdmin from '../views/home/SystemAdmin'
import TenantAdmin from '../views/home/TenantAdmin'
import CommonUser from '../views/home/CommonUser' */
export default {
  layout: 'storage',
  components: {
    // NoLogin,
    // WithLogin,
    storage,
    // MyWebgisApp,
    /* SystemAdmin,
    TenantAdmin,
    CommonUser, */
  },
  computed: {
    isAdmin() {
      return this.$store.getters.isAdmin
    },
    isTenant() {
      return this.$store.getters.isTenant
    },
    isUser() {
      return this.$store.getters.isUser
    },
    isLogin() {
      return this.$store.getters.isLogin
    },
  },
}
</script>

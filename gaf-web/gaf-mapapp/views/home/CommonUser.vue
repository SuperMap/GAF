<template>
  <a-row>
    <a-col :span="16">
      <a-row>
        <a-col :span="24">
          <a-card class="box-card" title="个人信息">
            <a slot="extra" href="//doc.supermap.pub" target="_blank">
              帮助指引
            </a>
            <a-row :gutter="160">
              <a-col :span="12">
                <dl class="person-info">
                  <dt>登录账号</dt>
                  <dd>
                    <nuxt-link to="/users/profile">
                      {{ user.userName }}
                    </nuxt-link>
                  </dd>
                  <dt>真实姓名</dt>
                  <dd>{{ user.realName || 'anonymous' }}</dd>
                  <!--                  <dt>APPID</dt>-->
                  <!--                  <dd>{{ user.APPID }}</dd>-->
                </dl>
              </a-col>
              <a-col :span="12">
                <dl class="person-info">
                  <!--                  <dt>认证状态</dt>-->
                  <!--                  <dd>-->
                  <!--                    {{ '未认证' }}-->
                  <!--                    <a href=""><span class="el-icon-edit"/></a>-->
                  <!--                  </dd>-->
                  <dt>所属组织</dt>
                  <dd>{{ user.groups && user.groups.join(',') }}</dd>
                </dl>
              </a-col>
            </a-row>
          </a-card>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="24">
          <a-card class="box-card" title="我的应用">
            <func-list />
          </a-card>
        </a-col>
      </a-row>
    </a-col>
    <a-col :span="8">
      <a-card class="box-card" title="通知公告">
        <notice />
      </a-card>
      <a-card class="box-card" title="产品日志">
        <product-log />
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import Notice from './components/Notice'
import FuncList from './components/FuncList'
import ProductLog from './components/ProductLog'
export default {
  components: {
    Notice,
    FuncList,
    ProductLog,
  },
  data() {
    return {
      activeName: 'first',
      user: this.$store.state.user,
    }
  },
}
</script>
<style scoped>
.box-card {
  margin-bottom: 15px;
  border-width: 0;
  box-shadow: none;
}
.gong-gao {
  list-style: none;
}
.gong-gao > li {
  margin-bottom: 10px;
}
.person-info > dt {
  float: left;
  width: 160px;
  overflow: hidden;
  clear: left;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 10px;
}
.person-info > dd {
  margin-left: 180px;
  text-align: right;
  margin-bottom: 10px;
}
.quick-link {
  text-align: center;
  margin-top: 50px;
}
.flex {
  display: flex;
  justify-content: space-around;
}
.resource-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 25%;
}
.flex-vertical {
  display: flex;
  flex-direction: column;
}
.title {
  font-size: 15px;
  font-weight: bold;
}
</style>

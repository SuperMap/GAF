<template>
  <a-row>
    <a-col :span="16">
      <a-row>
        <a-col :span="24">
          <a-card class="box-card" title="平台概况">
            <resource-monitor />
          </a-card>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="24">
          <a-card class="box-card" title="GAF一站式企业协同研发云">
            <div class="">
              <a-steps :current="5" progress-dot>
                <a-step title="需求"></a-step>
                <a-step title="编码"></a-step>
                <a-step title="测试"></a-step>
                <a-step title="发布"></a-step>
                <a-step title="反馈"></a-step>
              </a-steps>
            </div>
            <div class="quick-link">
              <a-link href="">快速开始</a-link>
              <a-link href="">自定义配置</a-link>
            </div>
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
      <a-card class="box-card" title="个人信息">
        <dl class="person-info">
          <dt>登录账号</dt>
          <dd>
            <nuxt-link to="/users/profile">
              {{ user.userName }}
            </nuxt-link>
          </dd>
          <dt>真实姓名</dt>
          <dd>{{ user.realName || 'anonymous' }}</dd>
          <!--          <dt>APPID</dt>-->
          <!--          <dd>{{ user.APPID }}</dd>-->
          <!--          <dt>认证状态</dt>-->
          <!--          <dd>-->
          <!--            {{ '未认证' }}-->
          <!--            <a href=""><span class="el-icon-edit"/></a>-->
          <!--          </dd>-->
          <dt>所属组织</dt>
          <dd>{{ user.groups && user.groups.join(',') }}</dd>
        </dl>
      </a-card>
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
import ProductLog from './components/ProductLog'
import FuncList from './components/FuncList'
import ResourceMonitor from './components/ResourceMonitor'
export default {
  components: {
    Notice,
    FuncList,
    ProductLog,
    ResourceMonitor,
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

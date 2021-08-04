<template>
  <div :style="bgStyle" class="gaf-login-bg">
    <!-- <img src="~/assets/img/keycloak-bg.png" class="" /> -->
    <img :src="loginTitle" class="gaf-img" />
    <div class="gaf-login-box">
      <div id="components-form-demo-normal-login-div">
        <a-form
          id="components-form-demo-normal-login"
          :form="form"
          class="login-form"
          layout="horizontal"
        >
          <a-form-item :label-col="{ span: 5 }" label="用户名">
            <a-input
              autocomplete="off"
              v-decorator="[
                'username',
                {
                  rules: [
                    { required: true, message: '请输入用户名!' }
                  ]
                }
              ]"
              placeholder="用户名"
            >
              <a-icon
                slot="prefix"
                type="user"
                style="color: rgba(242, 241, 241, 1)"
              />
            </a-input>
          </a-form-item>
          <a-form-item :label-col="{ span: 4 }" label="密码">
            <a-input
              v-decorator="[
                'password',
                {
                  rules: [
                    { required: true, message: '请输入密码!' }
                  ]
                }
              ]"
              @pressEnter="handleSubmit()"
              type="password"
              placeholder="密码"
            >
              <a-icon
                slot="prefix"
                type="lock"
                style="color: rgba(242, 241, 241, 1)"
              />
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button
              @click="handleSubmit()"
              type="primary"
              class="login-form-button"
            >
              登 录
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
      <a-modal :width="360" :footer="null" v-model="visible" title="滑动验证">
        <slide-verify
          ref="slideblock"
          :l="42"
          :r="10"
          :w="310"
          :h="155"
          :imgs="bgimgs"
          @refresh="onRefresh"
          @success="onSuccess"
          @fail="onFail"
          slider-text="向右滑动"
        ></slide-verify>
      </a-modal>
    </div>
</template>


<script>
import bg from "../../img/bg.png"
import qs from 'qs'

export default {
  props: {
    loginTitle: {
      type: String,
      default: ''
    },
    loginBg: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      bgimgs: [bg],
      slideValidated: false,
      validated: false,
      visible: false
    }
  },
  computed: {
    bgStyle() {
      return `background-image:url(${this.loginBg})`
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'normal_login' })
  },
  methods: {
    showModal() {
      this.visible = true
    },
    onSuccess() {
      console.log('滑块验证成功')
      this.slideValidated = true
      this.visible = false
      this.$refs.slideblock.reset()
      this.$message.success('滑块验证成功', 2)
      this.handleSubmit()
    },
    onFail() {},
    onRefresh() {},
    handleSubmit() {
      this.form.validateFields(err => {
        if (!err) {
          console.log('校验成功')
          this.validated = true
        } else {
          console.log('校验失败')
        }
      })
      if (!this.validated) {
        return
      }
      if (!this.slideValidated) {
        this.showModal()
        return
      }

      console.log('发送请求')
      const username = this.form.getFieldValue('username')
      const password = this.form.getFieldValue('password')
      const param = new URLSearchParams()
      param.append('username', username)
      param.append('password', password)
      const query = this.$route.query
      let url = '/authentication/login/username_password' + '?' + qs.stringify(query)
      this.$axios.$post(url, param).then(res => {
        if (res.status === 302) {
          


          const url = '/portal/user/profile/detail'
          this.$axios.$get(url).then(res2=> {
            this.$store.commit('updateGlobalInfo', res2)
            window.localStorage.setItem('luser', JSON.stringify(res2.user))
            location.href = res.data
          })
        } else if (res.status === 401) {
          this.$message.error('账户密码验证失败', 2)
        } else {
          this.$message.error('账户密码验证失败', 2)
        }
      })
    }
  }
}
</script>
<style scoped>
.gaf-login-slide {
  background-color: #ffffff;
  bottom: 320px;
  right: 150px;
  position: absolute;
}

h1 {
  text-align: center;
  padding-top: 60px;
  /* color: ; */
}
.gaf-login-bg {
  /* z-index: -1; */
  /* width: 100%;
  height: 150%;
  top: -200px; */
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  /* background-image: url(~assets/img/bg.p/z/ng); */
  background: no-repeat center top;
  background-size: cover;
}
.gaf-login-box {
  background-color: #212121;
  width: 400px;
  /* margin: 180px auto; */
  /* margin-right: 150px; */
  padding: 100px 50px 50px 50px;
  /* margin-top: 80px; */

  position: absolute;
  right: 180px;
  top: 200px;

  border-radius: 0px;
  height: 420px;

  opacity: 0.7;
}
.gaf-img {
  position: absolute;
  right: 220px;
  top: 230px;
  bottom: 0;
  width: 320px;
  z-index: 1;
}
#components-form-demo-normal-login .login-form {
  max-width: 300px;
}
#components-form-demo-normal-login .login-form-button {
  margin-top: 40px;
  margin-bottom: 40px;
  width: 100%;
  background: #329DFF;
  height: 40px;
  border-radius: 0px;
}
/deep/ .ant-form-item label {
    color: #FFFFFF;
}
/deep/ .ant-form-item-required::before {
  color: #FFFFFF;
}
/deep/ .ant-input-affix-wrapper .ant-input:not(:first-child) {
    background: #222222;
    padding-left: 30px;
    border-radius: 0px;
}
/deep/ [data-v-1c231103] .ant-input-affix-wrapper .ant-input:not(:first-child) {
    border: 1px solid #434343;
    box-shadow: 0px 3px 27px 0px rgb(4 0 0 / 33%);
    opacity: 0.9;
    background: #222222;
    padding-left: 30px;
    border-radius: 0px;
    color: #FFFFFF
}
</style>

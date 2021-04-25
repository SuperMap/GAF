import VueCookies from 'vue-cookies'
export default function ({ redirect }) {
  const token = VueCookies.get('CUSTOM_SESSION_ID')
  if (!token) {
    redirect('/authentication/login')
  }
}

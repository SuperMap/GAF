/**
 * Created by huanl on 2019/10/8
 */
import { message } from 'ant-design-vue'
import { proxy, token } from '../project.config.json'

const devProxy = []
const keys = Object.keys(proxy)
for (const key of keys) {
  devProxy.push({
    startsWith: proxy[key].startsWith,
    baseURL: key.substr(0, key.length - 1),
  })
}
export default ({ app }) => {
  app.context.token = token
  app.context.devProxy = devProxy
  app.context.message = message
}

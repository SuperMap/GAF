/**
 * Created by huanl on 2019/10/8
 */
import { proxy } from '../../../project.config.json'

export { token } from '../../../project.config.json'
export const devProxy = []
const keys = Object.keys(proxy)
for (const key of keys) {
  devProxy.push({
    startsWith: proxy[key].startsWith,
    baseURL: key.substr(0, key.length - 1)
  })
}

import http from '@gaf/axios'
import { token, devProxy } from './proxy'
const isDev = process.env.NODE_ENV === 'development'
export default http(isDev, devProxy, token)

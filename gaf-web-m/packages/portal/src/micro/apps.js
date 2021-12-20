const isDev = process.env.NODE_ENV === 'development'
const apps = [
  /**
   * name: 微应用名称 - 具有唯一性
   * entry: 微应用入口 - 通过该地址加载微应用
   * container: 微应用挂载节点 - 微应用加载完成后将挂载在该节点上
   * activeRule: 微应用触发的路由规则 - 触发路由规则后将加载该微应用
   */
  {
    name: 'apps-log',
    entry: isDev ? 'http://localhost:10201' : '/apps-log/',
    container: '#frame',
    activeRule: '/log'
  },
  {
    name: 'apps-config',
    entry: isDev ? 'http://localhost:10202' : '/apps-config/',
    container: '#frame',
    activeRule: '/config'
  },
  {
    name: 'apps-map',
    entry: isDev ? 'http://localhost:10203' : '/apps-map/',
    container: '#frame',
    activeRule: '/map'
  },
  {
    name: 'apps-data-mgt',
    entry: isDev ? 'http://localhost:10224' : '/apps-data-mgt/',
    container: '#frame',
    activeRule: '/dataMgt'
  },
  {
    name: 'apps-sys-mgt',
    entry: isDev ? 'http://localhost:10209' : '/apps-sys-mgt/',
    container: '#frame',
    activeRule: '/sysMgt'
  }
]

export default apps

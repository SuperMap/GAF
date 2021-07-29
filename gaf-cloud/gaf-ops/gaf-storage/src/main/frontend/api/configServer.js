export const ConfigServerUrl = '/srv-governance/config'
export const ConfigServerApi = (axios) => {
  // 获取配置列表
  const getConfigServerInfoList = (tenantId, serviceName, profile, label) =>
    axios.$get(`${ConfigServerUrl}/configurations?tenantId=${tenantId}`)
  // 删除配置信息
  const delConfigServerInfo = (id) =>
    axios.$delete(`${ConfigServerUrl}/configurations/${id}`)
  // 添加配置信息
  const addConfigServerInfo = (params) =>
    axios.$post(
      `${ConfigServerUrl}/configurations/batchadd?parameter=${params}`
    )
  // 更新配置信息
  const reloadConfiguration = (application) =>
    axios.$post(
      `${ConfigServerUrl}/configurations/refresh?application=${application}`
    )
  // 获取服务列表
  const getServiceList = (eurekaIp, eurekaPort) =>
    axios.$get(
      `${ConfigServerUrl}/servicenames?eurekaIp=${eurekaIp}&eurekaPort=${eurekaPort}`
    )

  return {
    getConfigServerInfoList,
    delConfigServerInfo,
    addConfigServerInfo,
    reloadConfiguration,
    getServiceList,
  }
}

<template>
  <div class="paramsDiv">
    <div class="monitor-title">
      <a-row>
        <a-col :span="12">{{ title }} </a-col>
        <a-col :span="12" class="monitor-title-back">
          <a id="back" href="javascript:;" @click="back">
            <a-icon type="arrow-left" />
            上一步
          </a>
        </a-col>
      </a-row>
    </div>
    <div class="container">
      <a-table
        :data-source="paramsGroupList"
        :columns="groupColumns"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        :pagination="false"
        row-key="paramGroupNameCn"
        size="small"
        align="center"
        style="width: 260px"
      >
        <!-- 参数组 -->
        <template slot="paramGroupNameCn" slot-scope="text, record, index">
          <a
            :id="`a${index}`"
            href="javascript:;"
            :class="{ 'click-a': selectedGroupIndex === index }"
            @click="handleClick(record)"
          >
            {{ record.paramGroupNameCn }}
          </a>
        </template>
      </a-table>
      <!-- 数据源选择下拉框 -->
      <div v-show="showSelect" class="datasource-select">
        <span>选择数据源:</span> &nbsp;&nbsp;&nbsp;
        <a-select
          :disabled="operation === 3"
          v-model="selectedDataSourceId"
          style="width: 200px"
          :options="options"
          placeholder="选择后自动填写以下参数"
          @select="onSelect"
        >
          <!-- <a-select-option  value="234"
            >gaf-测试环境数据源连接</a-select-option
          >
          <a-select-option siz="small" value="123"
            >gaf-正式环境数据源连接</a-select-option
          > -->
        </a-select>
      </div>
      <a-table
        :data-source="paramsList"
        :columns="paramColumns"
        :pagination="false"
        row-key="paramNameCn"
        size="small"
        align="center"
        style="width: 920px"
        :scroll="{ x: false, y: false }"
      >
        <!-- 参数名 -->
        <template slot="paramName" slot-scope="text, record">
          <span>
            {{ record.paramNameCn }}
          </span>
        </template>
        <!-- 参数值 -->
        <!-- :default-value="record.defaultValue" -->
        <template slot="paramValue" slot-scope="text, record">
          <a-input
            :disabled="operation === 3"
            v-model="record.defaultValue"
            placeholder="请输入参数值"
            size="small"
            allow-clear
            :type="
              record.paramName === 'spring_datasource_password'
                ? 'password'
                : 'text'
            "
            style="width: 200px"
            @change="inputChange(record, $event)"
            @blur="inputChange(record, $event)"
          >
          </a-input>
        </template>
        <!-- 描述 -->
        <template slot="paramDescription" slot-scope="text, record">
          <a-tooltip class="shortcutTip">
            <template slot="title">
              {{ record.description }}
            </template>
            <span class="description">
              {{ record.description }}
            </span>
          </a-tooltip>
        </template>
      </a-table>
    </div>

    <div class="footer">
      <a-button v-if="operation !== 3" type="primary" @click="handleCreate"> 创建工程 </a-button>
      <a-button v-if="operation !== 3" style="background: #f4f4f5" @click="handleSubmit">
        保存配置
      </a-button>
      <a-button v-if="operation !== 3" class="cancel-modal" @click="handleCancel">取消</a-button>
      <a-button v-if="operation === 3" class="cancel-modal" @click="back">返回</a-button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    projData: {
      type: Object,
      default: () => {},
    },
    title: {
      type: String,
      default: '',
    },
    operation: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      // 输入错误提示
      inputError: '',
      // 参数组列表
      paramsGroupList: [],
      // 参数列表
      paramsList: [],
      // 列表显示字段
      groupColumns: [
        {
          title: '参数组',
          dataIndex: 'paramGroupNameCn',
          key: 'paramGroupNameCn',
          scopedSlots: { customRender: 'paramGroupNameCn' },
        },
      ],
      paramColumns: [
        {
          title: '参数设置',
          children: [
            {
              title: '参数名',
              dataIndex: 'paramNameCn',
              key: 'paramNameCn',
              scopedSlots: { customRender: 'paramName' },
            },
            {
              title: '参数值',
              dataIndex: 'defaultValue',
              key: 'defaultValue',
              scopedSlots: { customRender: 'paramValue' },
            },
            {
              title: '描述',
              dataIndex: 'paramDescription',
              key: 'paramDescription',
              ellipsis: true,
              scopedSlots: { customRender: 'paramDescription' },
            },
          ],
        },
      ],
      selectedRowKeys: [],
      submitData: {},
      selectedGroupIndex: -1,
      requiredRowKeys: null,
      // 控制数据源下拉框是否显示
      showSelect: false,
      selectedDataSourceId: undefined,
      options: [],
      /* options: [
        {
          value: 'd1',
          label: 'gaf测试环境数据源连接',
          key: 'd1',
          datasourceId: 'd1',
          dsName: 'gaf测试环境数据源连接',
          type: '1',
          addr: '192.168.192.100',
          port: 32050,
          dbName: 'gaf',
          userName: 'admin1',
          password: '1234561sdf'
        },
        {
          value: 'd2',
          label: 'gaf正式环境数据源连接',
          key: 'd2',
          datasourceId: 'd2',
          dsName: 'gaf正式环境数据源连接',
          type: '4',
          addr: '192.168.192.100',
          port: 32050,
          dbName: 'gaf',
          userName: 'admin2',
          password: '123456sdfsafdf'
        },
        {
          value: 'd3',
          label: 'gaf3正式环境数据源连接',
          key: 'd3',
          datasourceId: 'd3',
          dsName: 'gaf3正式环境数据源连接',
          type: '5',
          addr: '192.168.192.100',
          port: 32050,
          dbName: 'gaf',
          userName: 'admin3',
          password: '123'
        },
        {
          value: 'd4',
          label: 'gaf4正式环境数据源连接',
          key: 'd4',
          datasourceId: 'd4',
          dsName: 'gaf正式环境数据源连接',
          type: '6',
          addr: '192.168.192.100',
          port: 32050,
          dbName: 'gaf',
          userName: 'admin4',
          password: '123456'
        }
      ] */
      // 1:POSTGRESQL,2:HBASE,3:文件型,4:MYSQL，5:ORACLE,6:SQLSERVER…';
    }
  },
  watch: {
    projData: {
      handler(newVal) {
        if (newVal) this.getParamsGroupList()
      },
    },
    deep: true,
  },
  beforeMount() {
    this.getParamsGroupList()
  },
  methods: {
    keyDown(event) {
      if (event.keyCode === 13) this.handleSubmit()
      else if (event.keyCode === 27) this.cancel()
    },
    back() {
      this.submitData = {}
      this.paramsGroupList = []
      this.paramsList = []
      this.selectedRowKeys = []
      this.$emit('back')
    },
    resetSelectedDataSourceId() {
      this.selectedDataSourceId = undefined
    },
    async getOptions() {
      const url =
        '/data-mgt/sys-resource-datasources/options?typeCodes=1&typeCodes=2&typeCodes=3&typeCodes=4'
      const res = await this.$axios.$get(url)
      // console.log(res)
      if (res.isSuccessed) {
        this.options = res.data
      } else {
        this.$message(`获取数据源可选项失败，原因：${res.message}`)
      }
    },
    async getParamsGroupList() {
      this.showSelect = false // 控制数据源下拉框是否显示
      this.submitData.groups = {}
      this.requiredRowKeys = new Set([])
      const url = `/proj/paramgroup/groupandparam/${this.projData.projTemplateId}`
      const res = await this.$axios.get(url)
      let settingParams = null
      if (this.operation === 2 && this.projData)
        if (this.projData.settingParams) {
          settingParams = JSON.parse(this.projData.settingParams)
          // 回显数据源
          if (settingParams.dsInfo && settingParams.dsInfo.datasourceId) {
            this.selectedDataSourceId = settingParams.dsInfo.datasourceId
          }
        }
      if (res.data.successed) {
        this.paramsGroupList = res.data.data
        this.$nextTick(() => {
          if (this.paramsGroupList && this.paramsGroupList.length > 0) {
            this.selectedGroupIndex = 0
            // 控制数据源下拉框是否显示
            if (this.paramsGroupList[0].paramGroupName === 'datasource') {
              // 获取数据源可选项
              if (!this.options || this.options.length === 0) {
                this.getOptions()
              }
              this.showSelect = true
            } else {
              this.showSelect = false
            }
            document.querySelector('#a0').focus()
            this.paramsList = this.paramsGroupList[0].paramVos
            this.paramsGroupList.forEach((item) => {
              this.submitData.groups[`${item.paramGroupName}`] = false
              if (item.isRequired) {
                this.selectedRowKeys.push(item.paramGroupNameCn)
                this.requiredRowKeys.add(item.paramGroupNameCn)
              }
              if (settingParams) {
                if (item.paramVos && item.paramVos.length > 0) {
                  item.paramVos.forEach((param) => {
                    if (settingParams[`${item.paramGroupName}`]) {
                      this.selectedRowKeys.push(item.paramGroupNameCn)
                      param.defaultValue =
                        settingParams[`${item.paramGroupName}`][
                          `${param.paramName}`
                        ]
                    }
                  })
                }
              }
            })
          }
        })
      } else {
        this.$message.error(res.data.message)
      }
    },
    onSelectChange(selectedRowKeys) {
      const temp = new Set([])
      selectedRowKeys.forEach((item) => {
        if (!this.requiredRowKeys.has(item) && !temp.has(item)) {
          temp.add(item)
        }
      })
      this.selectedRowKeys = [
        ...Array.from(this.requiredRowKeys),
        ...Array.from(temp),
      ]
      this.handleResult()
    },
    // 数据源选择改变
    onSelect(value, option) {
      // console.log('this.paramsList', this.paramsList)
      const datasourceConnect = option.data.props
      // console.log('datasourceConnect', datasourceConnect)
      // console.log('value,option', value, option)
      const paramValues = this.getDatasourceParam(datasourceConnect)
      // console.log('paramValues', paramValues)
      if (paramValues) {
        if (this.paramsGroupList && this.paramsGroupList.length > 0) {
          this.paramsGroupList.forEach((item) => {
            if (item.paramGroupName === 'datasource') {
              if (item.paramVos && item.paramVos.length > 0) {
                item.paramVos.forEach((param) => {
                  if (param.paramName === 'spring_datasource_username') {
                    param.defaultValue = paramValues.springDatasourceUsername
                  }
                  if (param.paramName === 'spring_datasource_password') {
                    param.defaultValue = paramValues.springDatasourcePassword
                  }
                  if (
                    param.paramName === 'spring_datasource_driver_class_name'
                  ) {
                    param.defaultValue =
                      paramValues.springDatasourceDriverClassName
                  }
                  if (param.paramName === '"spring_datasource_url"') {
                    param.defaultValue = paramValues.springDatasourceUrl
                  }
                })
              }
            }
          })
        }
        if (this.paramsList && this.paramsList.length > 0) {
          this.paramsList.forEach((param) => {
            if (param.paramName === 'spring_datasource_username') {
              param.defaultValue = paramValues.springDatasourceUsername
            }
            if (param.paramName === 'spring_datasource_password') {
              param.defaultValue = paramValues.springDatasourcePassword
            }
            if (param.paramName === 'spring_datasource_driver_class_name') {
              param.defaultValue = paramValues.springDatasourceDriverClassName
            }
            if (param.paramName === 'spring_datasource_url') {
              param.defaultValue = paramValues.springDatasourceUrl
            }
          })
        }
      }
      // console.log('paramValues', paramValues)
    },
    getDatasourceParam(info) {
      if (
        info.typeCode !== 'POSTGRESQL' &&
        info.typeCode !== 'MYSQL' &&
        info.typeCode !== 'ORACLE' &&
        info.typeCode !== 'SQL_SERVER'
      ) {
        return null
      }
      let springDatasourceDriverClassName
      let springDatasourceUrl
      const springDatasourceUsername = `${info.userName}`
      const springDatasourcePassword = `${info.password}`
      const ipAndPort = info.addr.split(':')
      const ip = ipAndPort[0] ? ipAndPort[0] : '127.0.0.1'
      const port = ipAndPort[1]
      if (info.typeCode === 'POSTGRESQL') {
        // postgresql数据库
        springDatasourceDriverClassName = `org.postgresql.Driver`
        springDatasourceUrl = `jdbc:postgresql://${ip}:${port || 5432}/${
          info.dbName
        }`
      } else if (info.typeCode === 'MYSQL') {
        // mysql数据库 jdbc:mysql://localhost:3306/databasename 暂时不考虑mysql8.0时区等问题
        springDatasourceDriverClassName = `com.mysql.cj.jdbc.Driver`
        springDatasourceUrl = `jdbc:mysql://${ip}:${port || 3306}/${
          info.dbName
        }`
      } else if (info.typeCode === 'ORACLE') {
        // Oracle jdbc:oracle:thin:@localhost:1521:orcl
        springDatasourceDriverClassName = `oracle.jdbc.driver.OracleDriver`
        springDatasourceUrl = `jdbc:oracle:thin://${ip}:${port || 1521}:${
          info.dbName
        }`
      } else if (info.typeCode === 'SQL_SERVER') {
        // sqlserver
        springDatasourceDriverClassName = `com.microsoft.sqlserver.jdbc.SQLServerDriver`
        springDatasourceUrl = `jdbc:sqlserver://${ip}:${
          port || 1433
        };DatabaseName=${info.dbName}`
      }
      return {
        springDatasourceDriverClassName,
        springDatasourceUrl,
        springDatasourceUsername,
        springDatasourcePassword,
      }
    },

    handleResult() {
      if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
        this.selectedRowKeys.forEach((key) => {
          this.paramsGroupList
            .filter((item) => item.paramGroupNameCn === key)
            .forEach((paramGroup) => {
              if (paramGroup.paramVos && paramGroup.paramVos.length > 0) {
                this.submitData[`${paramGroup.paramGroupName}`] = {}
                paramGroup.paramVos.forEach((param) => {
                  this.submitData[`${paramGroup.paramGroupName}`][
                    `${param.paramName}`
                  ] = param.defaultValue
                })
              }
              this.submitData.groups[`${paramGroup.paramGroupName}`] = true
            })
        })
      }
      // 提取选择的数据源连接信息
      if (this.selectedDataSourceId && this.options.length > 0) {
        this.options.forEach((option) => {
          if (option.value === this.selectedDataSourceId) {
            let datasourceType = 'POSTGRESQL'
            if (option.typeCode === '1') {
              datasourceType = 'POSTGRESQL'
            } else if (option.typeCode === '3') {
              datasourceType = 'MYSQL'
            } else if (option.typeCode === '2') {
              datasourceType = 'ORACLE'
            } else if (option.typeCode === '4') {
              datasourceType = 'SQLSERVER'
            }
            this.submitData.dsInfo = {
              datasourceId: option.value,
              datasourceType,
              host: option.addr,
              port: option.port,
              databaseOrServiceName: option.dbName,
              username: option.userName,
              password: option.password,
            }
          }
        }, this)
      }
    },
    handleClick(record) {
      const index = this.paramsGroupList.findIndex(
        (item) => item.codeTemplateParamGrpId === record.codeTemplateParamGrpId
      )
      this.selectedGroupIndex = index
      this.paramsList = this.paramsGroupList[index].paramVos
      // 控制数据源下拉框是否显示
      if (record.paramGroupName === 'datasource') {
        // 获取数据源可选项
        if (!this.options || this.options.length === 0) {
          this.getOptions()
        }
        this.showSelect = true
      } else {
        this.showSelect = false
      }
    },
    inputChange(record, event) {
      this.inputError = ''
      if (event.target.value.trim() === '') {
        this.inputError = `请输入${record.paramNameCn}参数值！`
        this.$message.error(this.inputError, 1)
        return false
      }
      const paramsIndex = this.paramsList.findIndex(
        (item) =>
          item.projCodeTemplateParamId === record.projCodeTemplateParamId
      )
      if (paramsIndex !== -1) {
        this.$nextTick(() => {
          this.paramsList[paramsIndex].defaultValue = event.target.value
          const params = this.paramsGroupList[this.selectedGroupIndex].paramVos
          params[paramsIndex].defaultValue = event.target.value
        })
      }
    },
    handleSubmit() {
      const data = this.handleSubmitData()
      this.$emit('AddFormSubmit', { status: this.operation, data })
    },
    handleSubmitData() {
      if (this.inputError !== '') {
        this.$message.error(this.inputError, 1)
        return false
      }
      this.handleResult()
      const data = {
        ...this.projData,
        settingParams: JSON.stringify(this.submitData),
      }
      this.paramsGroupList = []
      this.paramsList = []
      this.submitData = {}
      this.selectedRowKeys = []
      return data
    },
    handleCreate() {
      const data = this.handleSubmitData()
      this.$emit('projCreate', { status: this.operation, data })
    },
    handleCancel() {
      this.paramsGroupList = []
      this.paramsList = []
      this.submitData = {}
      this.selectedRowKeys = []
      this.$emit('cancel')
    },
  },
}
</script>

<style scoped>
a {
  text-decoration: none;
  color: #262626;
}

a:focus:not(#back),
.click-a {
  color: #559df5;
  border-bottom: 3px solid #559df5;
  border-radius: 1px;
}

a:hover {
  color: #559df5;
}

.footer {
  text-align: center;
  margin: 0;
  padding: 0;
  width: 100%;
  display: flex;
  justify-content: center;
}

button {
  width: 80px;
  font-size: 12px;
  cursor: pointer;
  margin: 10px 5px;
}

.container {
  text-align: left;
  display: flex;
  justify-content: left;
  align-items: flex-start;
  height: 77%;
  width: 100%;
}

.monitor-title {
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  font-size: 16px;
  line-height: 22px;
  margin: 10px 0px 10px;
}

.monitor-title-back {
  text-align: right;
  font-size: 16px;
  padding-right: 30px;
  line-height: 22px;
  margin: 0px 0px 10px;
}

.paramsDiv {
  height: 490px;
  overflow-x: scroll;
  overflow-y: hidden;
  -webkit-overflow-scrolling: touch;
}

::-webkit-scrollbar {
  width: 0px;
}

.shortcutTip {
  width: 370px;
}

.description {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 260px;
}

.datasource-select {
  background-color: white;
  overflow: hidden;
  z-index: 9999;
  position: absolute;
  width: 500px;
  height: 35px;
  left: 239px;
  top: 65px;
}
</style>

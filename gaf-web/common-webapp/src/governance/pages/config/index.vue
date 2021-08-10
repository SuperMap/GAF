<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout v-show="!showConfigDialog && !showConfigServer">
        <!--<template #breadcrumb>
        <a-breadcrumb separator=">">
          <a-breadcrumb-item>
            <a @click="$emit('back')">配置管理列表</a>
          </a-breadcrumb-item>
        </a-breadcrumb>
      </template>-->
        <template #actions>
          <button
            @click="showConfigServerModel"
            type="primary"
            class="btn-fun blue btn-16"
          >
            <a-icon type="plus-circle" />
            <span>新增</span>
          </button>
          <a-popconfirm
            class="btn-fun blue"
            title="删除后无法恢复，确认是否继续?"
            ok-text="确认"
            cancel-text="取消"
            @confirm="() => batchDel()"
          >
            <button class="btn-fun blue">
              <span>批量删除</span>
            </button>
          </a-popconfirm>
        </template>
        <template #filter>
          <template v-if="showTenantList">
            <a-select
              v-model="selectedTenantId"
              @change="tenantChange"
              placeholder="选择租户过滤"
            >
              <a-select-option key="SYSTEM" value=""
                >所有服务配置</a-select-option
              >
              <a-select-option key="SYSTEM" value=""
                >所有服务配置</a-select-option
              >
              <a-select-option key="gaf" value="gaf"
                >平台基础服务</a-select-option
              >
              <a-select-option
                v-for="(t, idx) in tenantList"
                :key="idx"
                :value="t.id"
                >{{ t.name }}
              </a-select-option>
            </a-select>
          </template>
          <!-- <gaf-search @search="onSearch" /> -->
          <div class="search-position">
            <a-input-search
              @search="onSearch"
              placeholder="请输入名称查询"
              size="large"
            >
            </a-input-search>
          </div>
        </template>
        <template #default>
          <gaf-table-head :selectedRowKeys="selectedRowKeys" @clearOptions="clearOptions" />
          <gaf-table-with-page
            :scroll="{ y: 508 ,x: 1440}"
            :pagination="pagination"
            @change="tableChange"
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onSelect: rowSelect,
              onChange: onSelectChange,
            }"
            :columns="columns"
            :data-source="dataSource"
            :row-key="(record) => record.application"
            class="table-style"
            size="middle"
          >
            <span slot="action" slot-scope="text, record">
              <!--<a
              href="javascript:;"
              @click.stop="() => detailConfigProperties(record)"
            >
              <a-icon type="zoom-in" />详情
            </a>
            <a-divider type="vertical" />-->
              <a
                @click.stop="() => updateConfigProperties(record)"
                href="javascript:;"
                class="btn-margin"
              >
                <u>编辑</u>
              </a>
              <!--<a-divider type="vertical" />
            <a
              href="javascript:;"
              @click.stop="() => addConfigProperties(record)"
            >
              <a-icon type="plus" />新增
            </a>-->

              <a @click.stop="() => deployConfig(record)" href="javascript:;">
                <u>发布</u>
              </a>
            </span>
          </gaf-table-with-page>
        </template>
      </gaf-table-layout>
      <div v-show="showConfigDialog">
        <config-server-update-dialog
          v-model="showConfigDialog"
          :config-server-info="configServerInfo"
          :add-config-visable="addConfigVisable"
          :update-config-visable="updateConfigVisable"
          :show-form-remove-button="showDialogRemoveButton"
          :refresh-load-config-server-info-list="
            refreshLoadConfigServerInfoList
          "
          @back="showConfigDialog = false"
          @refresh="refreshLoadConfigServerInfoList"
        >
        </config-server-update-dialog>
      </div>
      <div v-show="showConfigServer">
        <add-new-config
          :tenant-list="tenantList"
          :show-tenant-list="showTenantList"
          @back="showConfigServer = false"
          @refresh="refreshLoadConfigServerInfoList"
        ></add-new-config>
      </div>
    </div>
  </div>
</template>
<script>
import AddNewConfig from "./addNewConfig";
import ConfigServerUpdateDialog from "../../views/config/ConfigServerUpdateDialog";

const columns = [
  {
    title: "微服务名称",
    dataIndex: "application",
    // align: "center",
    key: "application",
  },
  {
    title: "所属租户ID",
    dataIndex: "tenantId",
    // align: "center",
    key: "tenantId",
  },
  {
    title: "配置数",
    dataIndex: "propertiesCount",
    // align: "center",
    key: "propertiesCount",
  },
  {
    title: "操作",
    // align: "center",
    // fixed: 'right',
    key: "action",
    // width: 300,
    scopedSlots: { customRender: "action" },
  },
];
export default {
  components: { AddNewConfig, ConfigServerUpdateDialog },
  provide() {
    return {
      loadConfigServerInfo: this.loadConfigServerInfoList(
        this.$store.getters.tenant.id
      ),
    };
  },
  data() {
    return {
      deletePropertyModelText: `当前所选服务下面的所有配置项都会被删除，如果只想删除部分配置请点击修改按钮进行操作`,
      deletePropertyVisible: false,
      deletePropertyConfirmLoading: false,
      loading: false,
      showDialogRemoveButton: false,
      showConfigDialog: false,
      updateConfigVisable: false,
      addConfigVisable: false,
      loadingText: "请稍候..",
      selectedTenantId: "",
      columns,
      dataSource: [],
      tenantList: [],
      showTenantList: false,
      configurationList: [],
      configServerInfo: {},
      selectedRowKeys: [],
      selectedRows: [],
      selectedProperties: [],
      visible: false,
      showConfigServer: false,
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      sorter: {
        order: "",
        field: "",
      },
      searchText: "",
      searchInput: null,
      searchedColumn: "serviceName",
      searchKey: "",
      clearFilters: null,
    };
  },
  computed: {
    hasSelected() {
      return this.selectedRowKeys.length > 0;
    },
  },
  mounted: function () {
    this.loadConfigServerInfoList();
    // this.loadTenants()
  },
  methods: {
    async onSearch(value) {
      this.searchText = value;
      this.pagination.current = 1;
      await this.loadConfigServerInfoList();
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo, filters, sorter) {
      this.selectedRowKeys = [];
      if (pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      }
      if (sorter) {
        this.sorter.order = sorter.order === "descend" ? "DESC" : "ASC";
        this.sorter.field = sorter.columnKey;
      }
      this.loadConfigServerInfoList();
    },
    showDeletePropertyModel: function () {
      this.deletePropertyVisible = true;
    },
    showConfigServerModel: function () {
      this.showConfigServer = true;
    },
    refreshLoadConfigServerInfoList: function () {
      this.showConfigDialog = false;
      this.showConfigServer = false;
      this.loadConfigServerInfoList();
    },
    async loadConfigServerInfoList() {
      this.loadingText = "请稍候..";
      this.loading = true;
      // `/srv-governance/config/configurations?tenantId=${this.selectedTenantId}`
      let url = `/srv-governance/config/configurations?pageSize=${this.pagination.pageSize}&pageIndex=${this.pagination.current}`;
      if (this.searchText.trim() && this.searchedColumn) {
        url =
          // url +
          // '&searchFieldName=' +
          // this.searchedColumn +
          // '&searchFieldValue=' +
          // this.searchText.trim()
          url + "&" + this.searchedColumn + "=" + this.searchText.trim();
      }
      if (this.sorter.order && this.sorter.field) {
        url =
          url +
          "&orderFieldName=" +
          this.sorter.field +
          "&orderMethod=" +
          this.sorter.order;
      }
      const rst = await this.$axios.$get(url);
      // const data = await this.$configServerApi.getConfigServerInfoList(
      //   this.selectedTenantId
      // )
      if (rst.isSuccessed) {
        this.dataSource = rst.data.content;
        this.pagination.total = rst.data.total;
      } else {
        this.$message.error(`发布失败,原因:${rst.data.message}`);
      }
      // this.configurationList = data.map(d => {
      //   return {
      //     ...d,
      //     id: d.application
      //   }
      // })
      // let count = 1
      // const dataSourceTemp = []
      // this.configurationList.forEach(s => {
      //   const propertyInfos = []
      //   const properties = s.properties
      //   properties.map(property => {
      //     if (property.id != null) {
      //       propertyInfos.push({
      //         id: property.id,
      //         propertyKey: property.propertyKey,
      //         propertyValue: property.propertyValue
      //       })
      //     }
      //   })
      //   dataSourceTemp.push({
      //     key: count,
      //     id: s.application,
      //     application: s.application,
      //     profile: s.profile,
      //     label: s.label,
      //     tenantId: s.tenantId,
      //     propertynums: propertyInfos.length,
      //     innerData: [],
      //     propertyInfos
      //   })
      //   count++
      // })
      // this.loading = false
      // this.dataSource = dataSourceTemp
    },
    // 清空
    clearOptions() {
      this.selectedRowKeys = [];
      this.selectRowLength = 0;
    },
    rowSelect(record, selected, selectedRows) {
      this.selectedRows = selectedRows;
      this.selectedRowKeys = [];
      selectedRows.forEach((item) =>
        this.selectedRowKeys.push(item.application)
      );
    },
    async batchDel() {
      const url = "/srv-governance/config/configurations";
      let selectedRowKeys = this.selectedRowKeys;
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: this.selectedRows });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
        this.$nextTick(() => {
          if (
            this.pagination.current !== 1 &&
            selectedRowKeys.length === this.dataSource.length
          ) {
            this.pagination.current--;
          }
          this.loadConfigServerInfoList();
          this.selectedRowKeys = []
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
      this.selectedRowKeys = [];
    },
    cancelDeleteConfigServerInfo: function () {
      this.deletePropertyVisible = false;
    },
    deleteConfigServerInfo: function () {
      const selectedPropertiesTemp = this.selectedProperties;
      let propertyNumber = this.selectedProperties.length;
      this.deletePropertyModelText = `正在删除所选服务下所有配置，还剩${propertyNumber}个`;
      this.deletePropertyConfirmLoading = true;
      setTimeout(() => {
        this.deletePropertyVisible = false;
        this.deletePropertyConfirmLoading = false;
        this.loadConfigServerInfoList(this.$store.getters.tenant.id);
        this.selectedRowKeys = [];
      }, 2000);
      selectedPropertiesTemp.forEach((pr) => {
        if (propertyNumber) {
          this.deletePropertyModelText = `正在删除所选服务下所有配置，共${propertyNumber}个`;
          const { isSuccessed } = this.$configServerApi.delConfigServerInfo(
            pr.id
          );
          if (isSuccessed) {
            propertyNumber--;
          }
        }
      });
    },
    // onSearch: function(value) {
    //   if (!value) {
    //     this.dataSource = this.configurationList
    //   }
    //   this.dataSource = this.configurationList.filter(item =>
    //     item.application.includes(value)
    //   )
    // },
    // onSelectChange: function(selectedRowKeys) {
    //   let selectedRowKeysLength = selectedRowKeys.length
    //   if (selectedRowKeysLength === 0) {
    //     this.selectedProperties = []
    //   } else {
    //     this.dataSource.forEach(d => {
    //       if (selectedRowKeysLength > 0) {
    //         selectedRowKeys.forEach(s => {
    //           if (d.application === s) {
    //             d.propertyInfos.map(m => {
    //               this.selectedProperties.push(m)
    //             })
    //             selectedRowKeysLength--
    //           }
    //         })
    //       }
    //     })
    //   }
    //   this.selectedRowKeys = selectedRowKeys
    // },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRows = selectedRows;
      this.selectedRowKeys = selectedRowKeys;
      console.log("469", selectedRowKeys);
      this.selectRowLength = selectedRowKeys.length;
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false;
      } else {
        this.multiple = true;
      }
    },
    // async loadTenants() {
    //   const url = '/configcenter/manager/tenants'
    //   const list = await this.$axios.get(url)
    //   this.tenantList = list.data
    //   if (this.tenantList.length > 0) {
    //     this.showTenantList = true
    //   } else {
    //     this.showTenantList = false
    //   }
    // },
    tenantChange(v) {
      this.loadConfigServerInfoList(v);
    },
    expandedChange: function (expanded, record) {
      if (!expanded) {
        return;
      }
      const configProperties = [];
      this.dataSource.forEach((s) => {
        if (s.application === record.application) {
          s.propertyInfos.map((p) => {
            configProperties.push(p);
          });
        }
      });
      record.innerData = configProperties;
    },
    async updateConfigProperties(record) {
      let url = `/srv-governance/config/configurations/properties?tenantId=${record.tenantId}&serviceName=${record.application}&profile=${record.profile}&label=${record.label}`;
      const rst = await this.$axios.$get(url);
      record.configPropertiesVos = rst.data;
      this.configServerInfo = record;
      this.showConfigDialog = true;
      this.updateConfigVisable = true;
      this.addConfigVisable = false;
      this.showDialogRemoveButton = true;
    },
    detailConfigProperties: function (record) {
      this.showConfigDialog = true;
      this.configServerInfo = record;
      this.updateConfigVisable = false;
      this.addConfigVisable = false;
      this.showDialogRemoveButton = false;
    },
    addConfigProperties: function (record) {
      this.showConfigDialog = true;
      this.configServerInfo = record;
      this.addConfigVisable = true;
      this.updateConfigVisable = false;
      this.showDialogRemoveButton = true;
    },
    async deployConfig(record) {
      const { isSuccessed } = await this.$configServerApi.reloadConfiguration(
        record.application
      );
      if (isSuccessed) {
        await this.$message.success("操作成功");
      }
    },
  },
};
</script>
<style scoped>
.ant-select {
  width: 200px;
}
</style>

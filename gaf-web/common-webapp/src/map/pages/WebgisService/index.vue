<template>
  <div class="app-container">
    <div>
      <div class="page-left">
        <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          服务类型
        </div> -->
        <!-- <gaf-tree-transparent
          ref="myGafTreeTransparent"
          :data-of-tree="dataOfTree"
          :search-type="[0]"
          :expanded-node-keys.sync="expandedNodeKeys"
          :show-search="false"
          @select="onSelect"
          :show-line="true"
        >
        </gaf-tree-transparent> -->
        <a-menu
          :default-selected-keys="['-1']"
          mode="inline"
          theme="light"
          @select="onSelect"
        >
          <template v-for="item in dataOfTree">
            <a-menu-item v-if="!item.children" :key="item.key">
              <span>{{ item.title }}</span>
            </a-menu-item>
            <sub-menu v-else :key="item.key" :menu-info="item" />
          </template>
        </a-menu>
      </div>
      <div class="page-right">
        <gaf-table-layout>
          <template #actions>
            <button class="btn-fun blue btn-16" @click="handleAdd">
              <a-icon type="plus-circle" /><span>服务注册</span>
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
            <div class="search-position">
              <a-input-search
                @search="onSearch"
                placeholder="请输入服务名按回车查询"
                size="large"
              >
              </a-input-search>
            </div>
          </template>
          <template #default>
            <div class="choose-box">
              <a-icon type="exclamation-circle" class="exclamation" /><span
                >已选择</span
              >
              <b>{{ selectRowLength }}</b>
              <span>项</span>
              <a-popconfirm
                @confirm="() => clearOptions(record)"
                title="清空后无法恢复，确认是否继续?"
                ok-text="确认"
                cancel-text="取消"
              >
                <a href="javascript:;"><u>清空</u></a>
              </a-popconfirm>
            </div>
            <gaf-table-with-page
              :scroll="{ y: 508, x: 1440 }"
              :showXH="false"
              :pagination="pagination"
              :data-source="webgisServiceList"
              :loading="loading"
              :row-selection="{
                selectedRowKeys: selectedRowKeys,
                onChange: onSelectChange,
              }"
              :row-key="(r) => r.gisServiceId"
              :columns="
                columns.filter((item) => item.dataIndex !== 'gisServiceId')
              "
              class="table-style"
              size="middle"
              @change="tableChange"
            >
              <template slot="serviceType" slot-scope="text, record">
                {{
                  map.get(record.typeCode)[0] ? map.get(record.typeCode)[0] : ""
                }}
              </template>
              <template
                v-if="hasPKField"
                slot="operation"
                slot-scope="text, record"
              >
                <a
                  class="btn-margin"
                  href="javascript:;"
                  @click.stop="() => handleUpdate(record)"
                >
                  <u>编辑</u>
                </a>
                <a
                  v-if="
                    JSON.parse(map.get(record.typeCode)[1])
                      ? JSON.parse(map.get(record.typeCode)[1]).isDataService
                      : false
                  "
                  class="btn-margin"
                  @click.stop="configField(record)"
                >
                  <u>配置</u>
                </a>
                <a
                  v-if="
                    JSON.parse(map.get(record.typeCode)[1])
                      ? !JSON.parse(map.get(record.typeCode)[1]).isDataService
                      : true
                  "
                  class="btn-margin"
                  @click.stop="linkService(record)"
                >
                  <u>关联</u>
                </a>
                <a-popconfirm
                  title="删除后无法恢复，确认是否继续?"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="() => handleDelete(record)"
                >
                  <a href="javascript:;"> <u>删除</u></a>
                </a-popconfirm>
              </template>
              <template v-if="timeFormat" slot="timeRender" slot-scope="text">
                {{ timeFormat(text) }}
              </template>
              <template slot="address" slot-scope="text">
                <div class="url">
                  <a-tooltip placement="topLeft">
                    <template slot="title">
                      {{ text }}
                    </template>
                    <a :href="text" target="_blank">{{ text }}</a>
                  </a-tooltip>
                </div>
              </template>
            </gaf-table-with-page>
          </template>
        </gaf-table-layout>
      </div>
      <gaf-drawer
        :visible="open"
        :width="500"
        :footer="null"
        :centered="true"
        destroy-on-close
        @close="handleBack"
        placement="right"
        :closable="false"
      >
        <add-edit-form
          :title="title"
          :edit-data="editData"
          :operation="operation"
          :data-of-tree="dataOfTree"
          @submit="handleSubmit"
          @back="handleBack"
        >
        </add-edit-form>
      </gaf-drawer>
      <a-modal
        v-model="openconfigField"
        title="配置字段"
        width="40%"
        :centered="true"
        destroy-on-close
        @ok="handleOk"
        @cancel="handleCancel"
      >
        <config-field-list
          ref="configFieldList"
          :selected-service-id="selectedServiceId"
          @onOk="onOk"
          @onCancel="onCancel"
        ></config-field-list>
      </a-modal>
      <link-service
        v-model="linkedVisible"
        :webgis-service="webgisService"
      ></link-service>
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import "~/assets/css/common.css";
import AddEditForm from "../../views/WebgisService/AddOrEditForm.vue";
import ConfigFieldList from "./ConfigFieldList.vue";
import LinkService from "./LinkService.vue";

export default {
  components: {
    AddEditForm,
    LinkService,
    ConfigFieldList,
  },
  data() {
    return {
      open2: false,
      registrationResults: "",
      selectRowLength: 0,
      selectedServiceId: "",
      webgisService: null,
      // 关联模态框是否可见
      linkedVisible: false,
      map: new Map(),
      type: null,
      dataOfTree: [],
      expandedNodeKeys: [],
      // 搜索项
      searchKey: "",
      clearFilters: null,
      // 非多个禁用
      multiple: true,
      // 标题
      title: "",
      // 编辑记录
      editData: {},
      // 总条数
      total: 0,
      selectedRowKeys: [],
      // ${functionName}表格数据
      webgisServiceList: [],
      // 是否显示添加修改弹出层
      open: false,
      openconfigField: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: true,
      searchText: "",
      searchInput: null,
      searchedColumn: "name",
      sorter: {
        order: "",
        field: "",
      },
      // 新增：1，编辑：2
      operation: 0,
      // 有无主键
      hasPKField: true,
    };
  },
  computed: {
    columns() {
      const columns = [
        {
          title: "GIS服务id",
          dataIndex: "gisServiceId",
          key: "gis_service_id",
        },
        {
          title: "服务名称",
          scopedSlots: {
            filterDropdown: "filterDropdown",
            filterIcon: "filterIcon",
            customRender: "customRender",
          },
          width: "18%",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "服务类型",
          dataIndex: "typeCode",
          key: "type_code",
          width: "10%",
          scopedSlots: { customRender: "serviceType" },
        },
        {
          title: "服务地址",
          dataIndex: "address",
          key: "address",
          scopedSlots: { customRender: "address" },
          width: "33%",
        },
        {
          title: "时态",
          dataIndex: "timeAttribute",
          key: "time_attribute",
          width: "12%",
          scopedSlots: { customRender: "timeRender" },
        },
        {
          title: "操作",
          scopedSlots: { customRender: "operation" },
        },
      ];
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2);
    },
    timeFormat() {
      if (
        this.columns.filter(
          (item) =>
            item.scopedSlots && item.scopedSlots.customRender === "timeRender"
        ).length > 0
      ) {
        return function (str) {
          if (!str || str === "") {
            return "";
          }
          const dt = new Date(str);
          const year = dt.getFullYear();
          let month = dt.getMonth() + 1;
          let date = dt.getDate();
          let hour = dt.getHours();
          let minute = dt.getMinutes();
          let second = dt.getSeconds();
          month = month < 10 ? "0" + month : month;
          date = date < 10 ? "0" + date : date;
          hour = hour < 10 ? "0" + hour : hour;
          minute = minute < 10 ? "0" + minute : minute;
          second = second < 10 ? "0" + second : second;

          return `${year}-${month}-${date} ${hour}:${minute}:${second}`;
        };
      }
      return null;
    },
  },
  watch: {},
  mounted() {},
  async created() {
    await this.getNodesAndSetByType();
    this.getList();
  },
  methods: {
    handleOk() {
      this.$refs.configFieldList.onOk()
    },
    handleCancel() {
      this.$refs.configFieldList.onCancel()
    },
    onOk(openconfigField) {
      this.openconfigField = openconfigField;
    },
    onCancel(openconfigField) {
      this.openconfigField = openconfigField;
    },
    // 点击配置时
    configField(row) {
      // sd
      this.openconfigField = true;
      this.selectedServiceId = row.gisServiceId;
    },
    // 关联服务
    linkService(record) {
      this.webgisService = record;
      this.linkedVisible = true;
    },
    // 根据搜索文本拆分单元格文本内容
    splitCellWithSearchText(text) {
      const str = text === null ? "" : text;
      return str
        .toString()
        .split(
          new RegExp(`(?<=${this.searchText})|(?=${this.searchText})`, "i")
        );
    },
    handleSearchFieldChange(value) {
      this.searchedColumn = value;
    },
    async onSearch(value) {
      this.searchText = value;
      this.pagination.current = 1;
      await this.getList();
    },
    // 搜索查询
    handleSearch(selectedKeys, confirm, key, clearFilters) {
      if (this.searchedColumn !== key && this.clearFilters) this.clearFilters();
      confirm();
      this.searchText = selectedKeys[0];
      this.searchedColumn = key;
      this.clearFilters = clearFilters;
    },
    // 重置查询
    handleReset(clearFilters, key) {
      clearFilters();
      if (this.searchedColumn === key) {
        this.searchText = "";
        this.searchedColumn = "";
        this.clearFilters = null;
      }
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      }
      this.getList();
    },
    // 添加数据
    handleAdd() {
      this.operation = 2;
      this.open = true;
      this.title = "注册GIS服务";
    },
    // 添加修改提交后
    handleSubmit() {
      this.open = false;
      this.editData = {};
      this.getList();
    },
    // 添加修改返回后
    handleBack() {
      this.editData = {};
      this.open = false;
    },
    // 修改数据
    handleUpdate(row) {
      this.operation = 3;
      this.open = true;
      this.title = "修改GIS服务";
      this.editData = row;
    },
    /*   handleDetail(row) {
      this.operation = 1
      this.open = true
      this.title = '详情展示'
      this.editData = row
    }, */
    // 删除数据
    async handleDelete(row) {
      const url = `/map/webgis-services/` + row.gisServiceId;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        if (
          this.pagination.current !== 1 &&
          this.webgisServiceList.length === 1
        ) {
          this.pagination.current--;
        }
        this.getList();
      });
    },
    // 批量删除
    async batchDel() {
      const url = "/map/webgis-services/";
      const selectedRowKeys = this.selectedRowKeys;
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
        this.$nextTick(() => {
          if (
            this.pagination.current !== 1 &&
            selectedRowKeys.length === this.webgisServiceList.length
          ) {
            this.pagination.current--;
          }
          this.getList();
          this.selectedRowKeys = [];
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
    },
    // 清空
    clearOptions() {
      this.selectedRowKeys = [];
      this.selectRowLength = 0;
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectRowLength = selectedRowKeys.length;
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false;
      } else {
        this.multiple = true;
      }
    },
    // 获取地图应用 树结构数据
    async getNodesAndSetByType() {
      const url = `/sys-mgt/sys-dicts/ServiceType/tree`;
      const res = await this.$axios.$get(url);

      if (res.isSuccessed) {
        res.data.forEach((element) => {
          this.map.set(element.value, [element.label, element.extProperties]);
          element.key = element.value;
          element.title = element.label;
          delete element.value;
          delete element.label;
        });
        res.data.splice(0, 0, {
          key: "-1",
          title: "所有类型",
          children: null,
          type: 0,
          sortSn: 0,
          parentId: "0",
          scopedSlots: {
            title: "title",
          },
          style: "font-size: 18px;font-weight: bold",
        });
        this.dataOfTree = res.data;
      } else {
        this.$message.error("加载角色树失败,原因：" + res.message);
      }
    },
    //  树结构数据  搜索
    async getSelected() {
      /* const url = `/authority/auth-role-menu/list-by-role/${this.roleId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        if (res.data !== null && res.data.length > 0) {
          this.checkedNodeKeys = res.data.map(item => item.resourceMenuId)
        } else {
          this.checkedNodeKeys = []
        }
      } */
    },
    onSelect(item) {
      if (item.key === "-1") {
        this.type = null;
      } else {
        this.type = item.key;
      }
      this.getList();
    },
    // 树结构加载
    async getList() {
      this.loading = true;

      let url = `/map/webgis-services?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
      if (this.type) {
        url = url + "&typeCode=" + this.type;
      }
      if (this.searchText.trim() && this.searchedColumn) {
        url =
          url +
          "&searchFieldName=" +
          this.searchedColumn +
          "&searchFieldValue=" +
          this.searchText.trim();
      }
      if (this.sorter.order && this.sorter.field) {
        url =
          url +
          "&orderFieldName=" +
          this.sorter.field +
          "&orderMethod=" +
          this.sorter.order;
      }
      const res = await this.$axios.$get(url);

      this.loading = false;
      if (res.isSuccessed) {
        this.pagination.current = res.data.pageNum;
        this.pagination.pageSize = res.data.pageSize;
        this.pagination.total = res.data.totalCount;
        this.webgisServiceList = res.data.pageList;
      } else {
        this.$message.error(`查询失败,原因:${res.message}`);
      }
    },
  },
};
</script>

<style scoped>
.url {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 400px;
}

.url a {
  color: rgb(34, 34, 34);
  text-decoration: none;
}

.url a:link {
  color: rgb(34, 34, 34);
}

.url a:visited {
  color: rgb(153, 153, 153);
}

.url a:hover {
  color: #559df5;
}

.url a:active {
  color: rgb(153, 153, 153);
}
.ant-menu-item {
  height: 54px;
  line-height: 54px;
  font-size: 16px;
  margin-top: 0;
  margin-bottom: 0;
}
.ant-menu-item:hover {
  color: #077EEB;
}

.ant-menu .ant-menu-item-selected {
  background-color: #DEEFFF;
  color: #077EEB;
}

.ant-menu-item:after {
  border-right: 3px solid #1890ff;
}
.page-left {
  padding: 0;
}
</style>

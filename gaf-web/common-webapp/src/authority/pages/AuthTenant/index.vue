<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout v-show="!open">
        <template #actions>
          <button @click="handleAdd" class="btn-fun blue btn-16">
            <span><a-icon type="plus-circle" /> 新增</span>
          </button>
          <a-popconfirm
            class="btn-fun blue"
            title="确认是否继续?"
            ok-text="确认"
            cancel-text="取消"
            @confirm="() => batchDel()"
          >
            <button class="btn-fun blue">
              <span>批量禁用</span>
            </button>
          </a-popconfirm>
        </template>
        <template #filter>
          <!-- <a-select
          @change="handleSearchFieldChange"
          v-model="searchedColumn"
          style="width:150px"
        >
          <a-select-option
            v-for="i in searchOptions"
            :key="i.name"
            :value="i.name"
          >
            {{
              i.description === '状态'
                ? i.description + ': true/false'
                : i.description
            }}
          </a-select-option>
        </a-select>
        <gaf-search
          @search="keyword => handleFilterChange(keyword)"
          :placeholder="'请输入' + searchPlaceholder + '进行查询'"
          style="width:200px;"
        /> -->
          <div class="search-position">
            <a-input-search
              @search="onSearch"
              placeholder="请输入租户名称查询"
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
              <a href="javascript:;">清空</a>
            </a-popconfirm>
          </div>
          <gaf-table-with-page
            :scroll="{ y: 508, x: 1440 }"
            :pagination="pagination"
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
              onSelect: rowSelect,
              onSelectAll: rowSelectAll,
              getCheckboxProps: (record) => ({
                props: {
                  disabled: !record.status, // Column configuration not to be checked
                },
              }),
            }"
            :data-source="authTenantList"
            @change="tableChange"
            :row-key="(r, i) => r.tenantId"
            :columns="columns"
            class="table-style"
            size="middle"
          >
            <template slot="type" slot-scope="text, record">
              {{ getType(record.type) }}
            </template>
            <template slot="status" slot-scope="text, record">
              <a-switch
                :disabled="true"
                :checked="record.status"
                :style="{ background: record.status ? '#1890FF' : '#2F4F4F' }"
                checked-children="有效"
                un-checked-children="禁用"
              />
            </template>
            <template slot="operation" slot-scope="text, record">
              <a
                @click.stop="() => handleDetail(record)"
                href="javascript:;"
                class="btn-margin"
              >
                详情
              </a>
              <a-divider type="vertical" />
              <a
                @click.stop="() => handleUpdate(record)"
                href="javascript:;"
                class="btn-margin"
              >
                编辑
              </a>
              <a-divider type="vertical" />
              <a-popconfirm
                v-if="record.status"
                @confirm="() => setTenantStatus(record)"
                title="确认禁用该租户?"
                ok-text="确认"
                cancel-text="取消"
              >
                <a href="javascript:;" class="btn-margin">禁用</a>
              </a-popconfirm>
              <a-popconfirm
                v-if="!record.status"
                @confirm="() => setTenantStatus(record)"
                title="确认启用该租户?"
                ok-text="确认"
                cancel-text="取消"
              >
                <a href="javascript:;" class="btn-margin">启用</a>
              </a-popconfirm>
              <a-divider type="vertical" />
              <a
                @click.stop="() => setTenantSynchronization(record)"
                href="javascript:;"
                class="btn-margin"
              >
                同步
              </a>
              <a-divider type="vertical" />
              <a
                @click.stop="() => getAdministrators(record)"
                href="javascript:;"
              >
                全部管理员
              </a>
            </template>
            <template slot="timeRender" v-if="timeFormat" slot-scope="text">
              {{ timeFormat(text) }}
            </template>
          </gaf-table-with-page>
        </template>
      </gaf-table-layout>
      <add-edit-form
        :title="title"
        :editData="editData"
        @submit="handleSubmit"
        @back="handleBack"
        v-if="open"
        :operation="operation"
      >
      </add-edit-form>
      <gaf-modal
        v-model="mapList"
        :width="1000"
        :footer="null"
        title="第三方组件映射"
      >
        <mapping-process
          :tenantIdData="tenantIdData"
          :tenantRst="tenantRst"
        ></mapping-process>
      </gaf-modal>
      <gaf-modal
        v-model="adminList"
        :width="600"
        :footer="null"
        title="租户管理员"
      >
        <administrators :tenant-id="tenantId" style="height: 300px" />
      </gaf-modal>
    </div>
  </div>
</template>

<script>
import AddEditForm from "../../views/AuthTenant/AddOrEditForm";
import Administrators from "../../views/AuthTenant/Administrators";
import MappingProcess from "../../views/AuthTenant/MappingProcess";
import "~/assets/css/common.css";

export default {
  components: {
    AddEditForm,
    Administrators,
    MappingProcess,
  },
  data() {
    return {
      tenantRst: [],
      tenantIdData: "",
      tenantId: "",
      adminList: false,
      mapList: false,
      searchPlaceholder: "租户名称",
      searchOptions: [
        {
          name: "tenant_name",
          description: "租户名称",
        },
        {
          name: "admin_id",
          description: "管理员",
        },
        {
          name: "status",
          description: "状态",
        },
      ],
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
      authTenantList: [],
      // 是否显示添加修改弹出层
      open: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      searchText: "",
      searchInput: null,
      searchedColumn: "tenant_name",
      sorter: {
        order: "DESC",
        field: "status",
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      selectRowLength: 0,
      columns: [
        {
          title: "租户名称",
          dataIndex: "tenantName",
          key: "tenant_name",
        },
        {
          title: "租户类别",
          dataIndex: "type",
          key: "type",
          scopedSlots: {
            customRender: "type",
          },
        },
        // {
        //   title: '编码',
        //   dataIndex: 'code',
        //   key: 'code'
        // },
        {
          title: "状态",
          dataIndex: "status",
          key: "status",
          sorter: true,
          defaultSortOrder: "ascend",
          sortDirections: ["descend", "ascend"],
          scopedSlots: {
            customRender: "status",
          },
        },
        {
          title: "初始管理员",
          dataIndex: "adminName",
          key: "adminName",
        },
        {
          title: "操作",
          fixed: 'right',
          scopedSlots: { customRender: "operation" },
        },
      ],
    };
  },
  computed: {
    timeFormat: function () {
      if (
        this.columns.filter(
          (item) =>
            item.scopedSlots && item.scopedSlots.customRender === "timeRender"
        ).length > 0
      ) {
        return function (str) {
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

          return `${year}/${month}/${date} ${hour}:${minute}:${second}`;
        };
      }
      return null;
    },
  },
  created() {
    this.getList();
  },
  methods: {
    // 清空
    clearOptions() {
      this.selectedRowKeys = []
      this.selectRowLength = 0
    },
    async onSearch(val) {
      this.searchText = val;
      this.pagination.current = 1;
      await this.getList();
    },
    async batchDel() {
      const url = "/authority/auth-tenants/";
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
            selectedRowKeys.length === this.authTenantList.length
          ) {
            this.pagination.current--;
          }
          this.getList();
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectRowLength = selectedRowKeys.length
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false;
      } else {
        this.multiple = true;
      }
    },
    rowSelect(record, selected, selectedRows) {
      console.log(record, selected, selectedRows);
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      console.log(selected, selectedRows, changeRows);
    },
    async setTenantStatus(row) {
      const url =
        `/authority/auth-tenants/` + row.tenantId + `?status=${!row.status}`;
      const rst = await this.$axios.post(url);
      if (rst.data.isSuccessed) {
        this.$message.success("操作成功");
      } else {
        this.$message.error(`操作失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        this.getList();
      });
    },
    getAdministrators(row) {
      this.tenantId = row.tenantId;
      this.adminList = true;
    },
    // 同步
    async setTenantSynchronization(row) {
      this.mapList = true;
      this.mappingType = row.type;
      this.id = row.tenantId;
      this.tenantIdData = this.id;
      const url = `/authority/auth-p3-mapping-rule/list-by-type/`;
      const res = await this.$axios.get(url + this.mappingType + "/" + this.id);
      this.tenantRst = res.data.data;
    },
    getType(val) {
      switch (val) {
        case "1":
          return "开发运营类";
        case "2":
          return "运营类";
      }
    },
    handleSearchFieldChange(value) {
      this.searchedColumn = value;
      const res = this.searchOptions.filter((item) => {
        return item.name === value;
      });
      if (res.length > 0) {
        this.searchPlaceholder = res[0].description;
      }
    },
    async handleFilterChange(value) {
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
    tableChange(pageInfo, filters, sorter) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      }
      if (sorter) {
        this.sorter.order = sorter.order === "descend" ? "ASC" : "DESC";
        this.sorter.field = sorter.columnKey;
      } else {
        this.sorter.order = null;
        this.sorter.field = null;
      }
      this.getList();
    },
    // 添加数据
    handleAdd() {
      this.operation = 2;
      this.open = true;
      this.title = "添加租户";
      this.editData = {};
    },
    // 添加修改提交后
    handleSubmit() {
      this.open = false;
      this.editData = {};
      this.$nextTick(() => {
        this.getList();
      });
    },
    // 添加修改返回后
    handleBack() {
      // this.editData = {}
      this.open = false;
    },
    // 修改数据
    handleUpdate(row) {
      this.operation = 3;
      this.open = true;
      this.title = "修改租户";
      this.editData = row;
    },
    handleDetail(row) {
      this.operation = 1;
      this.open = true;
      this.title = "详情展示";
      this.editData = row;
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/authority/auth-tenants/` + row.tenantId;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        if (this.pagination.current !== 1 && this.authTenantList.length === 1) {
          this.pagination.current--;
        }
        this.getList();
      });
    },
    async getList() {
      let url = `/authority/auth-tenants?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
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
      if (res.isSuccessed) {
        this.authTenantList = res.data.pageList;
        this.pagination.total = res.data.totalCount;
      } else {
        this.$message.error(`查询失败,原因:${res.message}`);
      }
    },
  },
};
</script>

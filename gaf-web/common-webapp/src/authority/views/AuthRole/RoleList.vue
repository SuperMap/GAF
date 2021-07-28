<template>
  <div>
    <gaf-table-layout>
      <template #actions>
        <button @click="handleAdd" class="btn-fun blue btn-16">
          <a-icon type="plus-circle" /><span>添加角色</span>
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
            placeholder="请输入名称查询"
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
          :scroll="{ y: 508, x: 1400 }"
          :pagination="pagination"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange,
            onSelect: rowSelect,
            onSelectAll: rowSelectAll,
          }"
          :data-source="authResourceApiList"
          :loading="loading"
          @change="tableChange"
          :row-key="(r, i) => r.roleId"
          :columns="
            columns.filter((item) => item.dataIndex !== 'resourceApiId')
          "
          class="table-style"
          size="middle"
        >
          <template slot="type" slot-scope="text, record">
            {{ getType(record.type) }}
          </template>
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="hasPKField"
          >
            <a
              @click.stop="() => handleDetail(record)"
              href="javascript:;"
              class="btn-margin"
            >
              <u>详情</u>
            </a>

            <a
              @click.stop="() => handleUpdate(record)"
              href="javascript:;"
              class="btn-margin"
            >
              <u>编辑</u>
            </a>

            <a-popconfirm
              @confirm="() => handleDelete(record)"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
            >
              <a href="javascript:;" class="btn-margin"> <u>删除</u></a>
            </a-popconfirm>

            <a
              @click.stop="() => handleAssignMenu(record)"
              href="javascript:;"
              class="btn-margin"
            >
              <u>分配菜单</u>
            </a>
            <a @click.stop="() => handleAssignAPI(record)" href="javascript:;" class="btn-margin">
              <u>分配API</u>
            </a>
            <a @click.stop="() => handleRoleFile(record)" href="javascript:;">
              <u>分配文件</u>
            </a>
          </template>
          <template slot="timeRender" v-if="timeFormat" slot-scope="text">
            {{ timeFormat(text) }}
          </template>
        </gaf-table-with-page>
      </template>
    </gaf-table-layout>
    <gaf-drawer
      :visible="modalVisible"
      :width="500"
      :footer="null"
      :centered="true"
      @close="handleBack"
      :closable="false"
      placement="right"
      destroy-on-close
    >
      <add-edit-form
        :title="title"
        :editData="editData"
        @submit="handleSubmit"
        @back="handleBack"
        v-if="open"
        :operation="operation"
        :tenant-id="tenant"
        :role-grp-id="roleGroup"
        :role-group-path="roleGroupPath"
        :dataOfTree="dataOfTree"
      >
      </add-edit-form>
    </gaf-drawer>
    <gaf-drawer
      placement="right"
      :closable="false"
      :visible="drawerVisible2"
      :destroy-on-close="true"
      width="400"
      @close="drawerClose"
    >
      <role-menu :role="role"></role-menu>
    </gaf-drawer>
    <gaf-drawer
      :visible="RoleFileVisible"
      :width="500"
      :footer="null"
      :centered="true"
      @close="handleBackRoleFile"
      :closable="false"
      placement="right"
      destroy-on-close
    >
      <role-file
       :role="role"
       title="分配文件"
       @back="handleBackRoleFile"
      ></role-file>
    </gaf-drawer>
  </div>
</template>

<script>
import AddEditForm from "./RoleAddOrEditForm";
import roleMenu from "./RoleMenu";
import roleFile from "./RoleFile";
export default {
  components: {
    AddEditForm,
    roleMenu,
    roleFile
  },
  props: {
    tenant: {
      type: String,
      default: "",
    },
    roleGroup: {
      type: String,
      default: "",
    },
    roleGroupPath: {
      type: Array,
      default: () => [],
    },
    dataOfTree: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      drawerVisible2: false,
      RoleFileVisible: false,
      roleId: "",
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
      selectRowLength: 0,
      // ${functionName}表格数据
      authResourceApiList: [],
      searchTextApiList: [],
      // 是否显示添加修改弹出层
      open: false,
      modalVisible: false,
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
      searchedColumn: "roleName",
      sorter: {
        order: "",
        field: "",
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      // 有无主键
      hasPKField: true,
    };
  },
  computed: {
    columns: function () {
      const columns = [
        {
          title: "名称",
          dataIndex: "roleName",
          width: 180,
          key: "role_name",
        },
        // {
        //   title: '编码',
        //   dataIndex: 'code',
        //   key: 'code'
        // },
        {
          title: "类型",
          dataIndex: "type",
          width: 180,
          key: "type",
          scopedSlots: { customRender: "type" },
        },
        {
          title: "排序序号",
          width: 180,
          dataIndex: "sortSn",
          key: "sort_sn",
        },
        {
          title: "操作",
          fixed: "right",
          scopedSlots: { customRender: "operation" },
        },
      ];
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2);
    },
    timeFormat: function () {
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

          return `${year}/${month}/${date} ${hour}:${minute}:${second}`;
        };
      }
      return null;
    },
  },
  watch: {
    roleGroup(val) {
      this.pagination.current = 1;
      this.searchText = this.roleGroup;
      this.getList(val);
    },
    modalVisible(newValue) {
      this.open = newValue;
    },
    open(newValue) {
      this.modalVisible = newValue;
    },
  },
  created() {
    // this.getList()
    this.searchedColumn = "role_catalog_id";
    this.searchText = this.roleGroup;
  },
  methods: {
    async onSearch(val) {
      this.searchText = val;
      this.pagination.current = 1;
      // await this.getList()
      if (val === "") {
        this.searchedColumn = "role_catalog_id";
        this.searchText = this.roleGroup;
        await this.getList();
      } else {
        this.authResourceApiList = this.searchTextApiList.filter(
          (ltem) => ltem.roleName.includes(val) === true
        );
      }
    },
    async batchDel() {
      const url = `/authority/auth-roles/${this.roleGroup}/`;
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
            selectedRowKeys.length === this.authResourceApiList.length
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
    rowSelect(record, selected, selectedRows) {
      console.log(record, selected, selectedRows);
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      console.log(selected, selectedRows, changeRows);
    },
    getType(val) {
      switch (val) {
        case "1":
          return "组件内置";
        case "2":
          return "平台级";
        case "3":
          return "租户级";
      }
    },
    handleSearchFieldChange(value) {
      this.searchedColumn = value;
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
        this.sorter.order = sorter.order === "descend" ? "DESC" : "ASC";
        this.sorter.field = sorter.columnKey;
      }
      this.getList();
    },
    // 添加数据
    handleAdd() {
      this.operation = 2;
      this.open = true;
      this.title = "添加角色";
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
    handleBackRoleFile() {
      this.RoleFileVisible = false;
    },
    // 修改数据
    handleUpdate(row) {
      this.operation = 3;
      this.open = true;
      this.title = "修改角色";
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
      const url = `/authority/auth-roles/${this.roleGroup}/` + row.roleId;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        if (
          this.pagination.current !== 1 &&
          this.authResourceApiList.length === 1
        ) {
          this.pagination.current--;
        }
        this.getList();
      });
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
    async getList() {
      this.loading = true;
      let url = `/authority/auth-roles?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
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
        if (!res.data || !res.data.pageList) {
          this.authResourceApiList = [];
          this.pagination.total = 0;
        } else {
          this.searchTextApiList = res.data.pageList;
          if (this.searchTextApiList && this.searchTextApiList.length === 0) {
            this.$emit("changeOpenLeaf", true);
          } else {
            this.$emit("changeOpenLeaf", false);
          }
          this.authResourceApiList = res.data.pageList;
          this.pagination.total = res.data.totalCount;
        }
      } else {
        this.$message.error(`更新失败,原因:${res.message}`);
      }
    },
    handleAssignMenu(row) {
      this.drawerVisible2 = true;
      this.role = row;
    },
    handleRoleFile(row) {
      this.RoleFileVisible = true;
      this.role = row;
    },
    handleAssignAPI(row) {
      this.$emit("openRoleApi", row);
      console.log(row, "sss");
    },
    drawerClose() {
      this.drawerVisible2 = false;
    },
  },
};
</script>

<style scoped></style>

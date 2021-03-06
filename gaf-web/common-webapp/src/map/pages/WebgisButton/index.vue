<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout>
        <template #actions>
          <button class="btn-fun blue btn-16" @click="handleAdd">
            <a-icon type="plus-circle" /><span>新增按钮</span>
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
          <div class="box">
            <div class="select-box">
              <a-select
                v-model="searchedColumn"
                size="large"
                @change="handleSearchFieldChange"
              >
                <a-select-option value="name"> 名称 </a-select-option>
                <a-select-option value="type"> 类别 </a-select-option>
              </a-select>
              <a-select
                v-if="searchedColumn === 'type'"
                v-model="buttonType"
                size="large"
                @change="handleButtonTypeChange"
              >
                <a-select-option value="1"> 基础类 </a-select-option>
                <a-select-option value="2"> 业务类 </a-select-option>
              </a-select>
            </div>
            <div class="search-position">
              <a-input-search
                v-if="searchedColumn !== 'type'"
                @search="onSearch"
                placeholder="请输入条件按回车查询"
                size="large"
              >
              </a-input-search>
            </div>
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
            :pagination="pagination"
            :data-source="webgisButtonList"
            :loading="loading"
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
            }"
            :row-key="(r) => r.buttonId"
            @change="tableChange"
            :columns="columns.filter((item) => item.dataIndex !== 'buttonId')"
            class="table-style"
            size="middle"
          >
            <template slot="type" slot-scope="text, record">
              <div v-if="record.type === '1'">基础类</div>
              <div v-else-if="record.type === '2'">业务类</div>
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
                ><u>编辑</u>
              </a>

              <a-popconfirm
                @confirm="() => handleDelete(record)"
                title="删除后无法恢复，确认是否继续?"
                ok-text="确认"
                cancel-text="取消"
              >
                <a href="javascript:;"><u>删除</u></a>
              </a-popconfirm>
            </template>
            <template v-if="timeFormat" slot="timeRender" slot-scope="text">
              {{ timeFormat(text) }}
            </template>
          </gaf-table-with-page>
        </template>
      </gaf-table-layout>
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
          @submit="handleSubmit"
          @back="handleBack"
        >
        </add-edit-form>
      </gaf-drawer>
    </div>
  </div>
</template>

<script>
import "~/assets/css/common.css";
import AddEditForm from "../../views/WebgisButton/AddOrEditForm.vue";

export default {
  components: {
    AddEditForm,
  },
  data() {
    return {
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
      webgisButtonList: [],
      // 是否显示添加修改弹出层
      open: false,
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
      // 详情：1，新增：2，编辑：3
      operation: 0,
      // 有无主键
      hasPKField: true,
      buttonType: "1",
      selectRowLength: 0,
    };
  },
  computed: {
    columns() {
      const columns = [
        {
          title: "按钮id",
          dataIndex: "buttonId",
          key: "button_id",
        },
        {
          title: "按钮名称",
          width: '18%',
          scopedSlots: {
            filterDropdown: "filterDropdown",
            filterIcon: "filterIcon",
            customRender: "customRender",
          },
          dataIndex: "name",
          key: "name",
        },
        {
          title: "按钮类别",
          width: '18%',
          dataIndex: "type",
          scopedSlots: {
            customRender: "type",
          },
          key: "type",
        },
        {
          title: "方法",
          width: '22%',
          dataIndex: "method",
          key: "method",
        },
        {
          title: "描述",
          width: '18%',
          dataIndex: "description",
          key: "description",
        },
        {
          title: "操作",
          // fixed: 'right',
          scopedSlots: { customRender: "operation" },
        },
      ];
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2);
    },
    timeFormat() {
      if (
        // @ts-ignore
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

          // @ts-ignore
          month = month < 10 ? "0" + month : month;
          // @ts-ignore
          date = date < 10 ? "0" + date : date;

          return `${year}/${month}/${date}`;
        };
      }
      return null;
    },
  },
  created() {
    this.getList();
  },
  methods: {
    handleSearchFieldChange(value) {
      if (value === "type") {
        this.searchText = this.buttonType;
      } else {
        this.searchedColumn = value;
        this.searchText = "";
      }
      this.pagination.current = 1;
      this.getList();
    },
    handleButtonTypeChange(value) {
      this.searchText = value;
      this.pagination.current = 1;
      this.getList();
    },
    async onSearch(value) {
      // if (value === '基础类' && this.searchedColumn === 'type') {
      //   value = '1'
      // }
      // if (value === '业务类' && this.searchedColumn === 'type') {
      //   value = '2'
      // }
      this.searchText = value;
      this.pagination.current = 1;
      await this.getList();
    },
    // 搜索查询
    // async onSearch(value) {
    //   this.searchText = value
    //   this.pagination.current = 1
    //   await this.getList()
    // },
    /* handleSearch(selectedKeys, confirm, key, clearFilters) {
      if (this.searchedColumn !== key && this.clearFilters) this.clearFilters()
      confirm()
      this.searchText = selectedKeys[0]
      this.searchedColumn = key
      this.clearFilters = clearFilters
    }, */
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
      this.title = "添加地图按钮";
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
      this.title = "修改地图按钮";
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
      const url = `/map/webgis-buttons/` + row.buttonId;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        if (
          this.pagination.current !== 1 &&
          this.webgisButtonList.length === 1
        ) {
          this.pagination.current--;
        }
        this.getList();
      });
    },
    // 批量删除
    async batchDel() {
      const url = "/map/webgis-buttons/";
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
            selectedRowKeys.length === this.webgisButtonList.length
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
    async getList() {
      this.loading = true;
      let url = `/map/webgis-buttons?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
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
        this.webgisButtonList = res.data.pageList;
        // for (const i in this.webgisButtonList) {
        //   if (this.webgisButtonList[i].type === '1') {
        //     this.webgisButtonList[i].type = '基础类'
        //   }
        //   if (this.webgisButtonList[i].type === '2') {
        //     this.webgisButtonList[i].type = '业务类'
        //   }
        // }
      } else {
        this.$message.error(`查询失败,原因:${res.message}`);
      }
    },
  },
};
</script>

<style scoped>
.app-container {
  height: 100%;
}
.select-box {
  margin: 15px 0 0 65%;
  position: absolute;
}
</style>

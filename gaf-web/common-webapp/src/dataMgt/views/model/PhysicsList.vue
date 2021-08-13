<template>
  <div>
    <gaf-table-layout>
      <template #actions>
        <button @click="handlePhysicalization" class="btn-fun blue btn-16">
          <a-icon type="plus-circle" /><span>物理化</span>
        </button>
        <!-- <a-popconfirm
          class="btn-fun blue"
          title="删除后无法恢复，确认是否继续?"
          ok-text="确认"
          cancel-text="取消"
          @confirm="() => batchDel()"
        >
          <button class="btn-fun blue">
            <span>批量删除</span>
          </button>
        </a-popconfirm> -->
      </template>
      <!-- <template #filter>
        <div class="search-position">
          <a-input-search
            @search="onSearch"
            placeholder="searchedColumn === 'dict_name'? '请输入名称模糊查询': '请输入字典值查询'"
            size="large"
          >
          </a-input-search>
        </div>
      </template> -->
      <template #default>
        <!-- <gaf-table-head :selectedRowKeys="selectedRowKeys" @clearOptions="clearOptions" /> -->
        <gaf-table-with-page
          :showXH="true"
          :pagination="pagination"
          :data-source="sysDictList"
          :loading="loading"
          @change="tableChange"
          :row-key="(r) => r.key"
          :columns="columns"
          class="table-style"
          size="middle"
        >
          <template v-if="timeFormat" slot="timeRender" slot-scope="text">
            {{ timeFormat(text) }}
          </template>
          <template slot="visibility" slot-scope="text">
            {{ text ? "可见" : "不可见" }}
          </template>
        </gaf-table-with-page>
      </template>
    </gaf-table-layout>
	<gaf-drawer
    :visible="open"
    :width="500"
    :footer="null"
    :centered="true"
    @close="handleBackDrawer"
    :closable="false"
    placement="right"
    destroy-on-close
  >
    <physicalization-form
      :title="titlePhysicalization"
      :editData="editData"
      :tableId="tableId"
      @submit="handleSubmit"
      @back="handleBackDrawer"
      :operation="operation"
    >
    </physicalization-form>
  </gaf-drawer>
  </div>
</template>

<script>
import PhysicalizationForm from './PhysicalizationForm'

export default {
  components: {
    PhysicalizationForm
  },
  props: {
    tableId: {
      type: String,
      default: null,
    },
    modelId: {
      type: String,
      default: null,
    },
    dictCode: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      titlePhysicalization: '',
      selectRowLength: 0,
      // 标题
      title: "",
      // 编辑记录
      editData: {},
      selectedRowKeys: [],
      // ${functionName}表格数据
      sysDictList: [],
      // 是否显示添加修改弹出层
      open: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: false,
      searchText: "",
      searchedColumn: "dict_name",
      sorter: {
        order: "ASC",
        field: "seq",
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      columns: [
        {
          title: "数据源地址",
          dataIndex: "db_url",
          key: "db_url",
        },
        {
          title: "数据库",
          dataIndex: "db_name",
          key: "db_name",
        },
        {
          title: "物理表名称",
          dataIndex: "physics_name",
          key: "physics_name",
        },
      ],
    };
  },
  watch: {
    tableId: function () {
      this.pagination.current = 1;
      this.getList();
    },
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
  created() {
    if (this.tableId) {
      this.getList();
    }
  },
  methods: {
    clearOptions() {
      this.selectedRowKeys = []
      this.selectRowLength = 0
    },
    handleSearchFieldChange(value) {
      this.searchedColumn = value;
    },
    // 搜索查询
    async onSearch(val) {
      this.searchText = val;
      this.pagination.current = 1;
      await this.getList();
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo, filters, sorter) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      }
      if (sorter.order) {
        this.sorter.order = sorter.order === "descend" ? "DESC" : "ASC";
        this.sorter.field = sorter.columnKey;
      } else {
        this.sorter.order = null;
        this.sorter.field = null;
      }
      this.getList();
    },
    // onExpandedRowsChange(expandedRows) {
    //   if(!expandedRows || expandedRows.length == 0) {
    //     this.expandedRowKeys = []
    //   } else {
    //     this.expandedRowKeys = expandedRows.map(el=> el.dictId);
    //   }
    // },
    // 添加数据
    handleAdd() {
      this.operation = 2;
      this.open = true;
      this.editData = { pid: this.dicTypeId, dictCode: this.dictCode };
      this.title = "添加字段";
    },
    // 添加修改提交后
    handleSubmit() {
      this.open = false;
      this.getList();
    },
    // 添加修改返回后
    handleBack() {
      this.editData = {};
      this.open = false;
    },
    handlePhysicalization() {
      this.open = true;
      this.titlePhysicalization = "物理化";
    },
    // 修改数据
    handleUpdate(row) {
      this.operation = 3;
      this.open = true;
      this.title = "修改字段";
      this.editData = {
        dataDictId: row.key,
        pid: row.parentId,
        dictCode: this.dictCode,
        dictName: row.label,
        dictValue: row.value,
        seq: row.seq,
        visibility: row.visibility,
        dictDesc: row.dictDesc,
        createdTime: row.createdTime,
        createdBy: row.createdBy,
        updatedTime: row.updatedTime,
        updatedBy: row.updatedBy,
        extProperties: row.extProperties,
      };
    },
    handleAddChild(row) {
      this.operation = 2;
      this.open = true;
      this.editData = { dictCode: this.dictCode, pid: row.key };
      this.title = "添加字典数据";
    },
    handleDetail(row) {
      this.operation = 1;
      this.open = true;
      this.title = "详情展示";
      this.editData = {
        dataDictId: row.key,
        pid: row.parentId,
        dictCode: this.dictCode,
        dictName: row.label,
        dictValue: row.value,
        seq: row.seq,
        visibility: row.visibility,
        dictDesc: row.dictDesc,
        createdTime: row.createdTime,
        createdBy: row.createdBy,
        updatedTime: row.updatedTime,
        updatedBy: row.updatedBy,
        extProperties: row.extProperties,
      };
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/sys-mgt/sys-dicts/` + row.key;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        if (this.pagination.current !== 1 && this.sysDictList.length === 1) {
          this.pagination.current--;
        }
        this.selectedRowKeys = this.selectedRowKeys.filter(item => {
          return item !== row.key
        })
        this.getList();
      });
    },
    // 批量删除
    async batchDel() {
      const url = `/sys-mgt/sys-dicts/`;
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
            selectedRowKeys.length === this.sysDictList.length
          ) {
            this.pagination.current--;
          }
          this.getList();
          this.selectedRowKeys = []
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
    },
    // 复选框
    rowChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectRowLength = selectedRowKeys.length
    },
    rowSelect(record, selected, selectedRows) {
      // eslint-disable-next-line no-console
      console.log(record, selected, selectedRows);
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      // eslint-disable-next-line no-console
      console.log(selected, selectedRows, changeRows);
    },
    handleBackDrawer() {
      this.editData = {};
      this.open = false;
    },
    async getList() {
      this.loading = true;
      let url = `/data-mgt/model-manage/models/${this.modelId}/physics?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
      if (this.dicTypeId) {
        url += `&pid=${this.dicTypeId}`;
      }
      if (this.dictCode) {
        url += `&dictCode=${this.dictCode}`;
      }
      if (
        this.searchText.trim() &&
        this.searchedColumn &&
        this.searchedColumn === "dict_value"
      ) {
        url += `&dictValue=${this.searchText.trim()}`;
      }
      if (
        this.searchText.trim() &&
        this.searchedColumn &&
        this.searchedColumn === "dict_name"
      ) {
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
        // children空数组置为null
        // if(res.data.content && res.data.content.length > 0) {
        //   treeUtil.deepFirstTraverseTree(null, res.data.content, (parentNode, node) => {
        //     if(node.children && node.children.length == 0) {
        //       node.children = null
        //     }
        //   })
        // }
        this.sysDictList = res.data.content;
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
</style>

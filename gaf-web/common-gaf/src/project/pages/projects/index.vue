<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout>
        <template #actions>
          <button class="btn-fun blue btn-16" @click="createdTpl">
            <a-icon type="plus-circle" /><span>创建工程</span>
          </button>
        </template>
        <template #filter>
          <div class="select-box">
            <a-select
              v-model="statusSearch"
              size="large"
              @change="handleStatuslChange"
            >
              <a-select-option
                v-for="i in statusList"
                :key="i.name"
                :value="i.id"
              >
                {{ i.name }}
              </a-select-option>
            </a-select>
          </div>
          <div class="search-position">
            <a-input-search
              @search="onSearch"
              placeholder="请输入工程名按回车查询"
              size="large"
            >
            </a-input-search>
          </div>
        </template>
        <gaf-table-with-page
          :scroll="{ y: 508, X: 1440 }"
          :loading="loading"
          :columns="columns.filter((item) => item.dataIndex !== 'projId')"
          :data-source="projectList"
          :pagination="pagination"
          class="table-style"
          size="middle"
          :row-key="(r) => r.projId"
          @change="queryTableData"
        >
          <template slot="url" slot-scope="text">
            <div class="url">
              <a-tooltip class="shortcutTip">
                <template slot="title">
                  {{ text }}
                </template>
                <a :href="text" target="_blank">{{ text }}</a>
              </a-tooltip>
            </div>
          </template>
          <template slot="visibility" slot-scope="text">
            {{ text === "private" ? "私有" : "公开" }}
          </template>
          <span slot="status" slot-scope="text, record">
            <a-tag
              :color="record.status === 2 ? '#E3F2FD' : '#EEEEEE'"
              class="status"
            >
              <span style="color: #575757">
                {{ record.status === 2 ? "已创建" : "未创建" }}
              </span>
            </a-tag>
          </span>
          <span slot="action" slot-scope="text, record">
            <a
              v-if="record.status !== 2"
              href="javascript:;"
              @click="edit(record)"
              class="btn-margin"
            >
              <a-icon type="edit" />编辑
            </a>
            <!-- <a-divider v-if="record.status !== 2" type="vertical" /> -->
            <a
              v-if="record.status !== 2"
              href="javascript:;"
              @click="commit(record)"
              class="btn-margin"
            >
              创建工程
            </a>
            <!-- <a-divider v-if="record.status !== 2" type="vertical" /> -->
            <a
              @click.stop="() => handleDetail(record)"
              href="javascript:;"
              class="btn-margin"
            >
              详情
            </a>
            <a-divider type="vertical" />
            <a href="javascript:;">
              <a-popconfirm
                :title="'确定删除吗?删除后无法恢复'"
                ok-text="确定"
                cancel-text="取消"
                @confirm="deleteProjects(record)"
              >
                删除
              </a-popconfirm>
            </a>
          </span>
        </gaf-table-with-page>
      </gaf-table-layout>
      <a-modal
        v-model="generalVisible"
        :width="800"
        :footer="null"
        :title="title"
      >
        <add-edit-form
          ref="myModal"
          :edit-data="editData"
          :operation="operation"
          @cancel="handleCancel"
          @changeStep="handleStep"
        />
      </a-modal>

      <a-modal
        v-model="paramVisible"
        :width="1000"
        :footer="null"
        title=""
        :closable="false"
      >
        <add-params
          ref="addParams"
          :proj-data="projData"
          :title="title"
          :operation="operation"
          @cancel="handleCancel"
          @AddFormSubmit="saveItem"
          @projCreate="projCreate"
          @back="handlePreStep"
        />
      </a-modal>
    </div>
  </div>
</template>

<script>
//test
import AddEditForm from "../../views/projects/AddEditForm";
import AddParams from "../../views/projects/AddParams";
import { columns } from "./columns";

export default {
  components: {
    AddEditForm,
    AddParams,
  },
  data() {
    return {
      statusSearch: 0,
      loading: false,
      projectList: [],
      columns,
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      query: "",
      editData: {},
      statusList: [
        {
          id: 0,
          name: "查询全部工程",
        },
        {
          id: 1,
          name: "查询未创建工程",
        },
        {
          id: 2,
          name: "查询已创建工程",
        },
      ],
      title: "创建工程(1/2)-配置工程信息",
      generalVisible: false,
      paramVisible: false,
      status: 0,
      projData: {},
      // 1创建工程 2修改工程
      operation: 0,
    };
  },
  beforeMount() {
    this.loadProjectList();
  },
  methods: {
    handlePreStep() {
      if (this.operation === 1) this.title = "创建工程(1/2)-配置工程信息";
      else if (this.operation === 2) this.title = "编辑工程(1/2)-修改工程信息";
      this.projData = {};
      this.paramVisible = false;
      this.generalVisible = true;
    },
    handleStep(value) {
      this.generalVisible = false;
      this.projData = value;
      this.projData.projTemplateId = value.projCodeTemplateId;
      if (this.operation === 2) {
        this.projData.settingParams = this.editData.settingParams;
        this.projData.projId = this.editData.projId;
      }
      if (this.operation === 1) this.title = "创建工程(2/2)-配置参数组信息";
      else if (this.operation === 2)
        this.title = "编辑工程(2/2)-修改参数组信息";
      this.paramVisible = true;
      this.$nextTick(function () {
        this.$refs.addParams.resetSelectedDataSourceId();
      });
    },
    async handleStatuslChange(value) {
      this.statusSearch = value;
      await this.loadProjectList();
    },
    async commit(record) {
      this.loading = true;
      const url = `/proj/dev/project/git/${record.projId}`;
      const res = await this.$axios.$post(url);
      if (res.isSuccessed) {
        this.$message.success(res.message, 1);
        this.status = 2;
        await this.loadProjectList();
      } else {
        this.$message.error(res.message, 1);
      }
      this.loading = false;
    },
    createdTpl() {
      this.operation = 1;
      this.editData = null;
      this.title = `创建工程(1/2)-配置工程信息`;
      this.generalVisible = true;
      this.$forceUpdate();
    },
    handleDetail(row) {
      this.operation = 3;
      this.generalVisible = true;
      this.title = "详情展示";
      this.editData = row;
    },
    async edit(record) {
      const url = `/proj/dev/proj/${record.projId}`;
      const res = await this.$axios.get(url);
      if (res.data.successed) {
        this.operation = 2;
        this.editData = record;
        this.editData.settingParams = res.data.data.settingParams;
        this.title = "编辑工程(1/2)-修改工程信息";
        this.status = 1;
        this.generalVisible = true;
        this.$forceUpdate();
      } else {
        this.$message.error(res.data.message, 1);
      }
    },
    // 删除功能
    async deleteProjects(value) {
      // 单条删除
      const url = `/proj/dev/proj/${value.projId}`;
      const res = await this.$axios.$delete(url);
      if (res.successed) {
        this.$message.success("删除成功", 1);
        this.loadProjectList();
      } else {
        this.$message.error(`删除失败,原因:${res.message}`);
      }
    },
    queryTableData(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      }
      this.$nextTick(function () {
        this.loadProjectList();
      });
    },
    async loadProjectList() {
      this.loading = true;
      let url = `/proj/dev/project/list?pageNum=${this.pagination.current}&pageSize=${this.pagination.pageSize}`;
      if (this.query !== "" && this.query !== undefined) {
        url = `${url}&key=${this.query}`;
      }
      if (this.statusSearch !== 0 && this.statusSearch !== undefined) {
        url = `${url}&status=${this.statusSearch}`;
      }
      const res = await this.$axios.$get(url);
      this.loading = false;
      if (res.successed) {
        this.pagination.total = res.data.total;
        this.projectList = res.data.content;
      } else {
        this.pagination.total = 0;
        this.projectList = [];
        this.$message.error(`工程查询失败！,原因:${res.message}`);
      }
    },
    async handleFilterChange(value) {
      this.pagination.current = 1;
      this.query = value;
      await this.loadProjectList();
    },
    // 搜索
    async onSearch(val) {
      this.query = val;
      this.pagination.current = 1;
      await this.loadProjectList();
    },
    handleCancel() {
      this.editData = {};
      this.$refs.myModal.form.resetFields();
      this.paramVisible = false;
      this.generalVisible = false;
    },
    async projCreate(val) {
      this.$refs.myModal.form.resetFields();
      this.editData = {};
      this.paramVisible = false;
      this.generalVisible = false;
      const url = "/proj/dev/projwithcreating";
      let res = null;
      if (val.status === 1) res = await this.$axios.$post(url, val.data);
      else if (val.status === 2) res = await this.$axios.$put(url, val.data);
      if (res.isSuccessed)
        this.$message.success(
          res.message === "" || !res.message ? "创建工程成功！" : res.message,
          1
        );
      else this.$message.error(`创建工程失败,原因:${res.message}`);
      this.loadProjectList();
    },
    async saveItem(val) {
      // 根据emit 状态 判断是取消还是确认
      const url = `/proj/dev/proj`;
      let result = null;
      if (val.status === 1) {
        val.data.status = 1;
        result = await this.$axios.$post(url, val.data);
        if (result.isSuccessed) {
          this.$message.success(result.message, 1);
        } else {
          this.$message.error(result.message, 1);
        }
      } else if (val.status === 2) {
        val.data.status = 1;
        result = await this.$axios.put(url, val.data);
        if (result.data.isSuccessed) {
          this.$message.success(result.data.message, 1);
        } else {
          this.$message.error(result.data.message, 1);
        }
      }
      this.loadProjectList();
      this.$refs.myModal.form.resetFields();
      this.editData = {};
      this.paramVisible = false;
      this.generalVisible = false;
    },
  },
};
</script>

<style scoped>
.status {
  width: 100%;
  text-align: center;
}

i {
  font-size: 14px;
  display: inline-block;
}

.shortcutTip {
  width: 370px;
}

.url {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 300px;
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
</style>

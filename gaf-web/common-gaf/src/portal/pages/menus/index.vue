<template>
  <gaf-table-layout>
    <template #actions>
      <div>
        <a-button
          class="btn-fun blue btn-16"
          type="primary"
          size="default"
          icon="plus-circle"
          @click="handleItemAction(1, null)"
          >创建分组</a-button
        >
      </div>
    </template>
    <template #default>
      <div>
        <a-table
          :columns="columns"
          :data-source="data"
          :loading="loading"
          :scroll="{ y: 508 }"
          row-key="id"
          size="middle"
          class="table-style"
        >
          <span slot="action" slot-scope="record">
            <a
              href="javascript:;"
              @click="handleItemAction(2, { pid: record.id })"
            >
              创建菜单
            </a>
            <a href="javascript:;" @click="handleItemAction(3, { ...record })"
              >编辑</a
            >
            <a-popconfirm
              title="删除菜单将删除相关子菜单，确认删除？"
              ok-text="确定"
              cancel-text="取消"
              @confirm="deleteMenu(record)"
            >
              <a href="javascript:">删除</a>
            </a-popconfirm>
          </span>
        </a-table>
      </div>
      <gaf-modal v-model="navVisible" :title="title" @ok="saveItem('myModal')">
        <add-edit-form
          ref="myModal"
          :is-new="isNew"
          :menu-item="menuItem"
          :menu-list="data"
        />
      </gaf-modal>
      <gaf-modal
        v-model="groupVisible"
        :title="title"
        @ok="saveItem('myModal1')"
      >
        <group-menu-from
          ref="myModal1"
          :is-new="isNew"
          :menu-item="menuItem"
          :menu-list="data"
        />
      </gaf-modal>
    </template>
  </gaf-table-layout>
</template>

<script>
import AddEditForm from "../../views/menus/AddEditForm";
import GroupMenuFrom from "../../views/menus/GroupMenuForm";
const columns = [
  {
    title: "菜单名称",
    dataIndex: "title",
    key: "title",
  },
  {
    title: "图标",
    dataIndex: "icon",
    key: "icon",
  },
  {
    title: "路径",
    dataIndex: "path",
    key: "path",
  },
  {
    title: "排序",
    dataIndex: "order",
    key: "order",
  },
  {
    title: "操作",
    key: "action",
    width: 120,
    align: "center",
    scopedSlots: { customRender: "action" },
  },
];

const recursion = (array, id, pid, pidv) => {
  if (!Array.isArray(array)) {
    return array;
  }
  const data = array.map((a) => ({ ...a }));
  const tempArr = data.filter((a) => a[pid] === pidv);
  tempArr.forEach((a) => {
    a.children = recursion(data, id, pid, a[id]);
    a.children = a.children.length > 0 ? a.children : null;
    return a;
  });
  return tempArr.sort((a, b) => a.order - b.order);
};

export default {
  components: {
    AddEditForm,
    GroupMenuFrom,
  },
  data() {
    return {
      columns,
      data: [],
      loading: false,
      title: "",
      groupVisible: false,
      navVisible: false,
      isNew: true,
      menuItem: {},
    };
  },
  created() {
    this.queryTableData();
  },
  methods: {
    // action 1:创建分组，2：创建导航；3编辑
    handleItemAction(action, menuItem) {
      if (action === 1) {
        this.groupVisible = true;
        this.title = "创建分组";
      } else if (action === 2) {
        this.navVisible = true;
        this.title = "创建菜单";
      } else if (menuItem) {
        if (menuItem.path || menuItem.embed) {
          this.navVisible = true;
          this.title = "编辑菜单";
        } else {
          this.groupVisible = true;
          this.title = "编辑分组";
        }
      }
      this.menuItem = menuItem;
    },
    async queryTableData() {
      try {
        this.loading = true;
        const url = "/portal/manager/menus";
        const { success, data } = await this.$axios.$get(url);
        this.loading = false;
        if (success) {
          const lang = this.$store.getters.routes;
          for (let i = 0; i < data.length; i++) {
            const title = lang[data[i].name] || data[i].name;
            data[i].title = title;
            data[i].key = data[i].id;
            data[i].value = data[i].id;
          }
          this.data = recursion(data, "id", "pid", "");
        }
      } catch (e) {
        this.loading = false;
        this.$message.error("查询失败");
      }
    },
    async deleteMenu(scop) {
      if (scop !== null) {
        let res;
        try {
          res = await this.$axios.$delete(
            "/portal/manager/menus/" + scop.id + ""
          );
        } catch (e) {
          console.log(e);
        }
        if (res.success === true) {
          this.$message.success("删除成功");
          this.queryTableData();
        } else {
          this.$message.error(res.msg);
        }
      }
    },
    saveItem(frmRef) {
      const form = this.$refs[frmRef].form;
      form.validateFields(async (err, menuInfo) => {
        if (err) {
          return;
        }
        menuInfo.pid = Array.isArray(menuInfo.pid)
          ? menuInfo.pid.join(",")
          : menuInfo.pid;
        let url = `/portal/manager/menus`;
        let response;
        if (!this.menuItem || !this.menuItem.id) {
          response = await this.$axios.$post(url, menuInfo);
        } else {
          menuInfo.id = this.menuItem.id;
          url = `${url}/${menuInfo.id}`;
          response = await this.$axios.$put(url, menuInfo);
        }
        const { success, msg } = response;
        this.$message[success ? "success" : "error"](msg);
        if (success) {
          form.resetFields();
          this.groupVisible = false;
          this.navVisible = false;
          this.queryTableData();
        }
      });
    },
  },
};
</script>

<style scoped>
.longText {
  width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap; /**不换行**/
}
</style>

<template>
  <div>
    <gaf-detail style="padding-left: 15px">
      <template>
        <div style="margin: 7px 0">
          <a-breadcrumb separator=">">
            <a-breadcrumb-item class="text-bolder">
              <a @click="$emit('back')">工程组管理</a>
            </a-breadcrumb-item>
            <a-breadcrumb-item class="text-bolder">
              <span class="vertical-line">| </span
              >工程组项目管理</a-breadcrumb-item
            >
          </a-breadcrumb>
        </div>
      </template>
      <gaf-table-layout>
        <template #default>
          <gaf-table-with-page
            :pagination="pagination"
            :data-source="projGroupList"
            :loading="loading"
            :row-key="(r, i) => i.toString()"
            :columns="columns"
            style="width: 100%"
            bordered
            size="small"
            align="center"
            @change="tableChange"
          >
            <template slot="visibility" slot-scope="text">
              {{ text === 'private' ? '私有' : '公开' }}
            </template>
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
          </gaf-table-with-page>
        </template>
      </gaf-table-layout>
    </gaf-detail>
  </div>
</template>

<script>
export default {
  props: {
    recordItem: {
      type: Object,
      default: null,
    },
    isRequesting: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      // 总条数
      total: 0,
      // ${functionName}表格数据
      projGroupList: [],
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: true,
      columns: [
        { title: '工程名称', dataIndex: 'projName', key: 'projName' },
        {
          title: '可见性',
          dataIndex: 'visibility',
          key: 'visibility',
          scopedSlots: { customRender: 'visibility' },
        },
        {
          title: 'Git地址',
          dataIndex: 'gitUrl',
          key: 'gitUrl',
          scopedSlots: { customRender: 'url' },
        },
        {
          title: '描述',
          dataIndex: 'description',
          key: 'description',
        },
      ],
    }
  },
  watch: {
    recordItem(val) {
      this.getProjectList(val)
    },
  },
  methods: {
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      this.getProjectList(this.recordItem)
    },
    async getProjectList(item) {
      if (!this.isRequesting) return false
      this.loading = true
      const url = `/proj/dev/list/group?pageNum=1&pageSize=30&projGroupId=${item.projGroupId}`
      const res = await this.$axios.$get(url)
      this.loading = false
      if (res.successed) {
        this.projGroupList = res.data.content
      } else {
        this.$message.error(`工程查询失败,原因:${res.message}`)
      }
    },
  },
}
</script>

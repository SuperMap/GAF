<template>
  <div>
    <div>
      <gaf-table-layout>
        <template #actions>
          <button class="btn-fun blue" @click="handleAdd">
            <span><a-icon type="plus-circle" /> 新建目录</span>
          </button>
          <!-- <button @click="batchDel" class="btn-fun red">
          <span><a-icon type="delete" />
          批量删除</span>
        </button> -->
          <div class="AttachmentManagement">
            <gaf-upload
              text="上传"
              class="btn-upload"
              :show-upload-list="false"
              :max-upload="5"
              :dir="dir"
              :config-name="configName"
              minio-service-url="/storage/api/tenant-created/"
              @uploadComplate="uploadComplate"
            ></gaf-upload>
          </div>
        </template>
        <template #filter>
          <div class="search-position">
            <a-input-search
              placeholder="请输入名称查询"
              size="large"
              @search="onSearch"
            >
            </a-input-search>
          </div>
        </template>
        <!-- <template #filter>
        <div style="margin-top: 5px">
          <a-input-search
            @search="onSearch"
            style="width: 320px"
            :placeholder="searchedColumn === 'dict_name'? '请输入名称模糊查询': '请输入字典值查询'"
            size="large"
          >
            <button slot="enterButton" class="btn-search">
              搜索
            </button>
          </a-input-search>
        </div>
      </template> -->
        <template #default>
          <!-- <div class="choose-box">
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
          </div> -->
          <gaf-table-with-page
            :show-x-h="false"
            :scroll="{ y: 508, x: 1440 }"
            :data-source="dataList"
            :loading="loading"
            :expanded-row-keys="expandedRowKeys"
            :row-key="(r) => r.name"
            :columns="columns"
            class="table-style"
            size="middle"
            @change="tableChange"
            @expand="onExpand"
          >
            <template slot="name" slot-scope="text, record">
              <a
                v-if="record.objectType === 'commonPrefix'"
                href="javascript:;"
                class="btn-margin"
                @click.stop="() => handleConfig(record)"
                ><u>{{ getName(text) }}</u>
              </a>
              <span v-else>{{ getName(text) }}</span>
            </template>
            <template v-if="timeFormat" slot="timeRender" slot-scope="text">
              {{ timeFormat(text) }}
            </template>
            <template slot="size" slot-scope="text, record">
              {{ getSize(record) }}
            </template>
            <template slot="operation" slot-scope="text, record">
              <a
                v-if="isImg(record)"
                class="btn-margin"
                href="javascript:;"
                @click.stop="() => preview(record)"
              >
                <u>预览</u>
              </a>
              <a
                v-if="record.objectType === 'object'"
                class="btn-margin"
                href="javascript:;"
                @click.stop="() => share(record)"
              >
                <u>分享</u>
              </a>
              <!-- <a-divider
                type="vertical"
                v-if="record.type === 'commonPrefix'"
              /> -->
              <a
                v-if="record.objectType === 'object'"
                class="btn-margin"
                href="javascript:;"
                @click.stop="() => handleDownload(record)"
              >
                <u>下载</u>
              </a>
              <!-- <a-divider v-if="record.type === 'object'" type="vertical" /> -->
              <a-popconfirm
                title="删除后无法恢复，确认是否继续?"
                class="btn-margin"
                ok-text="确认"
                cancel-text="取消"
                @confirm="() => handleDelete(record)"
              >
                <a href="javascript:;"><u>删除</u></a>
              </a-popconfirm>
              <!-- <a-divider
                type="vertical"
                v-if="record.type === 'commonPrefix'"
              /> -->
            </template>
            <template v-if="timeFormat" slot="timeRender" slot-scope="text">
              {{ timeFormat(text) }}
            </template>
            <template slot="visibility" slot-scope="text">
              {{ text ? '可见' : '不可见' }}
            </template>
          </gaf-table-with-page>
        </template>
      </gaf-table-layout>
      <a-modal
        v-model="openImg"
        :width="800"
        :centered="true"
        destroy-on-close
        :footer="null"
        :closable="false"
        :body-style="{ padding: 0 }"
      >
        <img :src="urlImg" width="100%" height="100%" />
      </a-modal>
      <a-modal
        v-model="open"
        :width="800"
        :centered="true"
        destroy-on-close
        :title="title"
        @cancel="handleBack"
        @ok="handleOk"
      >
        <add-edit-form
          ref="addEditForm"
          :title="title"
          :edit-data="editData"
          :dir="dir"
          :config-name="configName"
          :operation="operation"
          :name="name"
          @submit="handleSubmit"
          @back="handleBack"
        >
        </add-edit-form>
      </a-modal>
      <a-modal
        v-model="openFileSharing"
        :width="800"
        :centered="true"
        destroy-on-close
        title="文件分享"
        ok-text="复制链接"
        @cancel="handleBackFileSharing"
        @ok="handleOkFileSharing"
      >
        <file-sharing
          ref="FileSharing"
          :title="title"
          :file-data="fileData"
          :operation="operation"
          :name="name"
          :config-name="configName"
          @back="handleBack"
        >
        </file-sharing>
      </a-modal>
    </div>
  </div>
</template>

<script>
import { gafDownloadUtil, GafStorage } from 'gaf-ui'
import AddEditForm from '../../views/AttachmentManagement/AddOrEditForm'
import FileSharing from '../../views/AttachmentManagement/FileSharing'

export default {
  components: {
    AddEditForm,
    FileSharing,
  },
  props: {
    dicTypeId: {
      type: String,
      default: null,
    },
    dictCode: {
      type: String,
      default: null,
    },
    configName: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      expandedRowKeys: [],
      name: '',
      expanded: true,
      uploadStyle:
        'height: auto,background-color: rgb(221, 171, 92);border: 1px solid transparent;padding: 2px 5px;border-radius: 5px;color: #fff;',
      // 标题
      title: '',
      // 编辑记录
      editData: [],
      selectedRowKeys: [],
      // ${functionName}表格数据
      dataList: [],
      searchTextDataList: [],
      // 是否显示添加修改弹出层
      open: false,
      openFileSharing: false,
      fileData: null,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: false,
      searchText: '',
      searchedColumn: 'dict_name',
      sorter: {
        order: 'ASC',
        field: 'seq',
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      columns: [
        {
          title: '名称',
          dataIndex: 'name',
          key: 'name',
          width: 600,
          align: 'left',
          scopedSlots: { customRender: 'name' },
        },
        {
          title: '大小',
          dataIndex: 'size',
          key: 'size',
          width: 125,
          align: 'center',
          scopedSlots: { customRender: 'size' },
        },
        {
          title: '时态',
          dataIndex: 'lastModified',
          key: 'lastModified',
          width: '250px',
          scopedSlots: { customRender: 'timeRender' },
        },
        {
          title: '操作',
          key: 'operation',
          scopedSlots: { customRender: 'operation' },
          fixed: 'right',
        },
      ],
      dir: '',
      urlImg: '',
      openImg: false,
    }
  },
  computed: {
    timeFormat() {
      if (
        this.columns.filter(
          (item) =>
            item.scopedSlots && item.scopedSlots.customRender === 'timeRender'
        ).length > 0
      ) {
        return function (str) {
          if (!str || str === '') {
            return ''
          }
          const dt = new Date(str)
          const year = dt.getFullYear()
          let month = dt.getMonth() + 1
          let date = dt.getDate()
          let hour = dt.getHours()
          let minute = dt.getMinutes()
          let second = dt.getSeconds()

          month = month < 10 ? '0' + month : month
          date = date < 10 ? '0' + date : date
          hour = hour < 10 ? '0' + hour : hour
          minute = minute < 10 ? '0' + minute : minute
          second = second < 10 ? '0' + second : second

          return `${year}/${month}/${date} ${hour}:${minute}:${second}`
        }
      }
      return null
    },
  },
  watch: {
    dicTypeId() {
      this.pagination.current = 1
      this.getList()
    },
  },
  created() {
    this.getList()
  },
  methods: {
    handleOk() {
      this.$refs.addEditForm.submitForm()
    },
    handleOkFileSharing() {
      this.$refs.FileSharing.submitForm()
    },
    handleSearchFieldChange(value) {
      this.searchedColumn = value
    },
    // 搜索查询
    async onSearch(val) {
      this.pagination.current = 1
      if (val === '') {
        await this.getList(this.dir)
      } else {
        this.dataList = this.searchTextDataList.filter(
          (ltem) => ltem.name.includes(val) === true
        )
      }
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo, filters, sorter) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      if (sorter.order) {
        this.sorter.order = sorter.order === 'descend' ? 'DESC' : 'ASC'
        this.sorter.field = sorter.columnKey
      } else {
        this.sorter.order = null
        this.sorter.field = null
      }
      // this.getList()
    },
    async onExpand(expanded, record) {
      if (expanded && !record.expanded) {
        this.loading = true
        const url = `/storage/file-storage/list-objects?prefix=${record.name}`
        const res = await this.$axios.$get(url)
        if (res.isSuccessed) {
          this.expanded = false
          this.loading = false
          res.data.forEach((element) => {
            if (element.objectType === 'commonPrefix') {
              element.children = []
            }
          })
          record.expanded = true
          record.children.push(...res.data)
          // this.dataList.forEach(item => {
          //   if (item.name === record.name){
          //     item.children.push(...res.data)
          //   }
          // })
        } else {
          this.$message.error(`查询失败,原因:${res.message}`)
        }
      }
      if (expanded) {
        this.expandedRowKeys.push(record.name)
      } else {
        this.expandedRowKeys.splice(
          this.expandedRowKeys.indexOf(record.name),
          1
        )
      }
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
      this.operation = 2
      this.open = true
      this.editData = this.dataList
      this.title = '新建目录'
    },
    share(row) {
      this.fileData = row
      this.openFileSharing = true
    },
    // 添加修改提交后
    handleSubmit(prefix) {
      this.open = false
      setTimeout(() => {
        this.getList(prefix)
      }, 200)
    },
    // 添加修改返回后
    handleBack() {
      this.editData = []
      this.open = false
    },
    handleBackFileSharing() {
      this.openFileSharing = false
    },
    // 下载
    handleDownload(row) {
      gafDownloadUtil(
        this.$axios,
        `/storage/api/tenant-created/`,
        this.configName,
        row.name
      )
    },
    // 预览
    preview(row) {
      const gafstorage = new GafStorage(this.$axios, '/storage/api/tenant-created/')
      gafstorage.setConfigName(this.configName)
      gafstorage.getDownloadSignUrl(row.name, true).then((data) => {
        this.urlImg = data.data.data
        this.openImg = true
      })
    },
    // 是否是图片
    isImg(row) {
      if (row.objectType === 'object') {
        const imgAraay = [
          'bmp',
          'jpg',
          'png',
          'tif',
          'gif',
          'pcx',
          'tga',
          'exif',
          'fpx',
          'svg',
          'psd',
          'cdr',
          'pcd',
          'dxf',
          'ufo',
          'eps',
          'ai',
          'raw',
          'WMF',
          'webp',
        ]
        const endName = row.name.split('.').pop().toLowerCase()
        return imgAraay.includes(endName)
      } else {
        return false
      }
    },
    handleAddChild(row) {
      this.operation = 2
      this.open = true
      this.editData = row.children
      this.name = row.name
      this.title = '新建子目录'
    },
    handleDetail(row) {
      this.operation = 1
      this.open = true
      this.title = '详情展示'
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
      }
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/storage/api/tenant-created/${this.configName}/` + row.name
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
      this.$nextTick(() => {
        // if (this.dataList.length === 1) {
        //   this.$emit('popRoutes')
        // }
        this.getList(this.dir)
        this.expandedRowKeys = []
      })
    },
    // 批量删除
    async batchDel() {
      const url = `/storage/file-storage/`
      const selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys })
        if (rst.data.isSuccessed) {
          this.$message.success('删除成功')
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`)
        }
        this.$nextTick(() => {
          this.pagination.current = 1
          this.getList()
        })
      } else {
        this.$message.warn('请选择您要删除的内容')
      }
    },
    // 复选框
    rowChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    rowSelect(record, selected, selectedRows) {
      // eslint-disable-next-line no-console
      console.log(record, selected, selectedRows)
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      // eslint-disable-next-line no-console
      console.log(selected, selectedRows, changeRows)
    },
    async getList(prefix) {
      this.loading = true
      let url = `/storage/api/tenant-created/${this.configName}/list-objects/`
      if (this.searchText.trim() && this.searchedColumn) {
        url =
          url +
          '&searchFieldName=' +
          this.searchedColumn +
          '&searchFieldValue=' +
          this.searchText.trim()
      }
      if (prefix) {
        url = url + prefix
      }
      // if (this.sorter.order && this.sorter.field) {
      //   url =
      //     url +
      //     '&orderFieldName=' +
      //     this.sorter.field +
      //     '&orderMethod=' +
      //     this.sorter.order
      // }
      const res = await this.$axios.$get(url)
      this.loading = false
      if (res.isSuccessed) {
        this.pagination.current = res.data.pageIndex
        this.pagination.pageSize = res.data.pageSize
        this.pagination.total = res.data.total
        // children空数组置为null
        // if(res.data.content && res.data.content.length > 0) {
        //   treeUtil.deepFirstTraverseTree(null, res.data.content, (parentNode, node) => {
        //     if(node.children && node.children.length == 0) {
        //       node.children = null
        //     }
        //   })
        // }
        this.dataList = res.data
        this.searchTextDataList = res.data
        // this.dataList.forEach((element) => {
        //   if (element.type === "commonPrefix") {
        //     element.children = [];
        //   }
        // });
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    uploadComplate() {
      this.getList(this.dir)
      // this.expandedRowKeys = [];
    },
    getName(data) {
      const name = data.split('/')
      if (name[name.length - 1] === '') {
        return name[name.length - 2] + '/'
      } else {
        return name[name.length - 1]
      }
    },
    getSize(row) {
      if (row.objectType === 'object') {
        const Units = [' KB', ' MB', ' GB', ' TB']
        let size = row.size
        let index = 0
        for (let i = 0; size >= 1024; i++) {
          size = size / 1024
          index = i
        }
        return size.toFixed(2) + Units[index]
      }
    },
    handleConfig(record) {
      this.dir = record.name
      this.$emit('addbreadcrumb', record)
      this.getList(record.name)
    },
    setDir(path) {
      if (path) {
        this.dir = path
      } else {
        this.dir = ''
      }
    },
  },
}
</script>

<style scoped lang="less">
.app-container {
  height: 100%;
}
.btn-m10 {
  margin-left: 29px;
}
.AttachmentManagement {
  display: inline-block;
  margin-left: 10px;
}
</style>

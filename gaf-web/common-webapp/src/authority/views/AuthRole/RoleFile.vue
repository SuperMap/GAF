<template>
  <div class="page-container">
    <div class="grid-container">
      <div class="drawer-header">
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    </div>
    <div class="drawer-content">
      <gaf-table-no-page
        :showXH="false"
        :data-source="dataList"
        :loading="loading1"
        :row-key="(r) => r.name"
        @change="tableChange"
        @expand="onExpand"
        :columns="columns.filter((item) => item.dataIndex !== 'buttonId')"
        class="table-style"
        size="middle"
      >
        <template slot="name" slot-scope="text, record">
          {{ getName(record)}}
        </template>
        <!-- <template
          slot="operation"
          slot-scope="text, record"
        >
        </template> -->
        <template slot="upload" slot-scope="text,record">
          <a-checkbox-group v-model="record.permissions" @change="onChange(record)">
            <a-checkbox @change="onChange2($event,record)" :disabled="record.upload && record.permissions.indexOf('upload') === -1" value="upload"   />
            <div v-show="false">
              <a-checkbox value="delete" />
              <a-checkbox value="share" />
              <a-checkbox value="download" />
              <a-checkbox value="query" />
            </div>
          </a-checkbox-group>
        </template>
        <template slot="delete" slot-scope="text,record">
          <a-checkbox-group v-model="record.permissions" @click.stop @change="onChange(record)">
            <a-checkbox @change="onChange2($event,record)" :disabled="record.delete && record.permissions.indexOf('delete') === -1" value="delete" />
            <div v-show="false">
              <a-checkbox value="upload" />
              <a-checkbox value="share" />
              <a-checkbox value="download" />
              <a-checkbox value="query" />
            </div>
          </a-checkbox-group>
        </template>
        <template slot="share" slot-scope="text,record">
          <a-checkbox-group v-model="record.permissions" @change="onChange(record)">
            <a-checkbox @change="onChange2($event,record)" :disabled="record.share && record.permissions.indexOf('share') === -1" value="share" />
            <div v-show="false">
              <a-checkbox value="delete" />
              <a-checkbox value="upload" />
              <a-checkbox value="download" />
              <a-checkbox value="query" />
            </div>
          </a-checkbox-group>
        </template>
        <template slot="download" slot-scope="text,record">
          <a-checkbox-group v-model="record.permissions" @change="onChange(record)">
            <a-checkbox @change="onChange2($event,record)" :disabled="record.download && record.permissions.indexOf('download') === -1" value="download" />
            <div v-show="false">
              <a-checkbox value="delete" />
              <a-checkbox value="share" />
              <a-checkbox value="upload" />
              <a-checkbox value="query" />
            </div>
          </a-checkbox-group>
        </template>
        <template slot="query" slot-scope="text,record">
          <a-checkbox-group v-model="record.permissions" @change="onChange(record)">
            <a-checkbox @change="onChange2($event,record)" :disabled="record.query && record.permissions.indexOf('query') === -1" value="query" />
            <div v-show="false">
              <a-checkbox value="delete" />
              <a-checkbox value="share" />
              <a-checkbox value="download" />
              <a-checkbox value="upload" />
            </div>
          </a-checkbox-group>
        </template>
      </gaf-table-no-page>
    </div>
    <div class="drawer-footer">
      <div class="drawer-footer-div">
      <button @click="backToList" class="cancel-modal">取消</button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
        确定
      </a-button>
      </div>
      </div>
    </div>
  </div>
</template>

<script>
import '../../../common/css/common.css'

export default {
  components: {},
  props: {
    role: {
      type: Object,
      default: () => {}
    },
    title: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      roleId: this.role.roleId,
      profile: 'profile',
      menu: 'menu',
      tag: 'tag',
      tags: 'tags',
      checkedNodeKeys: [],
      dataOfTree: [],
      dataOfTree2: [],
      expandedNodeKeys: [],
      expandedNodeKeys2: [],
      searchPlaceholder: '请输入角色名称查询',
      searchPlaceholder2: '请输入菜单组名称查询',
      // roleId: '',
      nodeType: 12,
      menuTree: [],
      dataList: [],
      loading: false,
      loading1: false,
      configName: '',
      targetValue: '',
      targetChecked: false,
      resultData: [],
      resultConfigeName: '',
      map: new Map()
    }
  },
  computed: {
    columns() {
      const columns = [
        {
          title: "名称",
          dataIndex: "name",
          key: "name",
          scopedSlots: {
            customRender: "name",
          },
        },
        {
          title: "上传",
          scopedSlots: {
            customRender: "upload",
          },
          align: 'center'
        },
        {
          title: "删除",
          scopedSlots: {
            customRender: "delete",
          },
          align: 'center'
        },
        {
          title: "查询",
          scopedSlots: {
            customRender: "query",
          },
          align: 'center'
        },
        {
          title: "下载",
          scopedSlots: {
            customRender: "download",
          },
          align: 'center'
        },
        {
          title: "分享",
          // fixed: 'right',
          scopedSlots: { customRender: "share" },
          align: 'center'
        },
      ];
      return columns
    }
  },
  watch: {},
  created() {
    this.getList()
  },
  mounted() {
  },
  methods: {
    async getSelected() {
      const url = `/authority/auth-role-menu/list-by-role/${this.roleId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        if (res.data !== null && res.data.length > 0) {
          this.checkedNodeKeys = res.data.map(item => item.resourceMenuId)
        } else {
          this.checkedNodeKeys = []
        }
      }
    },
    async handleAdd() {
      const checkedMenus = this.menuTree
        .filter(
          item =>
            this.checkedNodeKeys.indexOf(item.key) !== -1 && item.type === 13
        )
        .map(i => i.key)
      const url = `/authority/auth-role-menu/handle`
      const data = {
        roleId: this.roleId,
        menuList: checkedMenus
      }
      const res = await this.$axios.$post(url, data)
      if (res.isSuccessed) {
        this.$message.success('添加成功')
      } else {
        this.$message.error(`添加失败,原因:${res.message}`)
      }
    },
    onSelect(selectedKeys, e) {
      this.roleId = e.node.dataRef.key
      this.nodeType = e.node.dataRef.type
      if (this.nodeType !== 5) {
        this.checkedNodeKeys = []
      } else {
        this.getSelected()
      }
    },
    backToList() {
      this.$emit('back')
    },
    tableChange() {
      console.log('eee')
    },
    getName(data) {
      if (data.objectType) {
        let name = data.name.split("/");
        if (name[name.length - 1] === "") {
          return name[name.length - 2] + "/";
        } else {
          return name[name.length - 1];
        }
      } else {
        return data.name
      }
      
    },
    async getList() {
      this.loading1 = true;
      let url = `/storage/tenant-server-configs/all-names?ower=${this.role.roleId}`;
      const res = await this.$axios.$get(url);
      this.loading1 = false;
      if (res.isSuccessed) {
        this.dataList = res.data;
        this.dataList.forEach(element => {
          
          element.children = []
          element.change = false
          if(!element.permissions) {
            element.permissions = []
          }
          this.map.set(element.name, element.permissions)
        });
      } else {
        this.$message.error(`查询失败,原因:${res.message}`);
      }
    },
    async onExpand(expanded, record) {
      let url
      if (!record.expand1) {
        if (record.objectType) {
          url = `/permissions/ower/${this.role.roleId}/${this.configName}/list-objects/${record.name}?hasPermissions=true`;
        } else {
          this.configName = record.name
          url = `/permissions/ower/${this.role.roleId}/${this.configName}/list-objects/?hasPermissions=true`;
        }
        this.loading1 = true
        const res = await this.$axios.$get(url);
        if (res.isSuccessed) {
        record.children = res.data
        record.expand1 = true
        record.children.forEach(element => {
          element.change = false
          if (element.objectType === 'commonPrefix') {
            element.children = []
          }
          if(!element.permissions) {
            element.permissions = []
          }
          this.map.set(element.name, element.permissions)
        });
        this.setChildren(record)
        } else {
          this.$message.error(`查询失败,原因:${res.message}`);
        }
        this.loading1 = false
      }
      
    },
    async submitForm() {
      this.loading = true
      let url = `/storage/permissions/ower/${this.role.roleId}`
      this.getCorrectData(this.dataList)
      const res = await this.$axios.$put(url, this.resultData)
      this.loading = false
      if (res.isSuccessed) {
        this.$message.success('设置成功')
      } else {
        this.$message.error(`设置失败,原因:${res.message}`)
      }
      this.resultData = []
    },
    getCorrectData(data) {
      data.forEach(item => {
        if (!item.objectType) {
          this.resultConfigeName = item.name
        }
        if (item.change) {
          if (item.objectType) {
            this.resultData.push({
              resource: item.name,
              scope: item.permissions.join(),
              ower: this.role.roleId,
              configName: this.resultConfigeName,
            })
          } else {
            this.resultData.push({
              resource: "",
              scope: item.permissions.join(),
              ower: this.role.roleId,
              configName: item.name
            })
          }
        }
        if (item.children) {
          this.getCorrectData(item.children)
        }
      })
    },
    onChange(record) {
      if (this.map.get(record.name).length !== record.permissions.length) {
        record.change = true
      } else {
        record.permissions.forEach(item => {
          if (this.map.get(record.name).indexOf(item) === -1) {
            record.change = true
          } else {
            record.change = false
          }
        })
      }
      this.loading1 = true
      if (record.objectType !== 'object') {
        if (this.targetChecked){
          this.setChildren(record)
        } else {
          this.setChildren(record, this.targetValue)
        }
      }
      this.loading1 = false
    },
    onChange2(e) {
      this.targetValue = e.target.value
      this.targetChecked = e.target.checked
    },
    setChildren(data, value) {
      const allArray = ['upload','delete','download','share','query']
      data.children.forEach(item => {
        // item.permissions = Array.from(new Set([...data.permissions, ...item.permissions]))
        if (value) {
          if (item.permissions.indexOf(value) > -1) {
            item.permissions.splice(item.permissions.indexOf(value), 1)
          }
        }
        allArray.forEach(item2 => {
          if ((data.permissions.indexOf(item2) > -1) || data[item2]) {
            item[item2] = true
          } else {
            item[item2] = false
          }
        })
        
        if(item.children) {
          this.setChildren(item, value)
        }
      })
    }
  }
}
</script>

<style scoped>
.bottom {
  height: 100%;
  width: 100%;
}
.left {
  width: 20%;
  height: 100%;
  float: left;
  margin: 1% 0 1% 1%;
  padding: 0 1% 5% 0;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
}
.main-top-left {
  height: 87%;
  width: 28%;
  float: left;
  margin: 1% 0 1% 1%;
  padding: 0 1% 5% 0;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
}
.main-top-right {
  width: 70%;
  height: 87%;
  float: left;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
  border-left: 1px solid rgba(204, 204, 204, 0.5);
}
.role-btn {
  margin-left: 1%;
  background-color: rgba(84, 92, 100, 0.75);
  color: rgba(255, 255, 255, 0.7);
  font-weight: bold;
  border: none;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.55);
  transition: linear 0.2s;
}
.role-btn:hover {
  color: white;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.25);
}
.main {
  height: 100%;
  width: 79%;
  float: left;
  overflow: hidden;
}
.main-top {
  line-height: 6vh;
  width: 100%;
  border-bottom: 1px solid #ccc;
}
</style>

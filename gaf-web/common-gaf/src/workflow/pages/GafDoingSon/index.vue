<template>
  <div class="app-container">
    <div class="page-single">
      <div class="func-top">
        <div class="func-box">
          <span>{{ dataAll.ProInst_Name }}</span>
        </div>
        <div class="btn-box">
          <button
            :disabled="isdisabled"
            class="btn-fun blue"
            @click="showModalRoll"
          >
            转出
          </button>
          <a-modal
            v-model="visible"
            :modal-visible="addModalVisible"
            :footer="null"
            title="转出列表"
          >
            <gaf-tree-transparent
              ref="GafTreeTransparent"
              v-model="checkedKeys"
              :search-type="[0]"
              :data-of-tree="dataOfTree"
              :expanded-node-keys.sync="expandedNodeKeys"
              :selected-keys.sync="selectedNodeKeys"
              :checkable="true"
              :check-node-strictly="false"
              class="tree-height"
              show-line
              @select="onSelect"
            ></gaf-tree-transparent>
            <div id="zc-modal-txt">
              <div class="zc-btn">
                <div>
                  <div>填写意见</div>
                </div>
                <div>
                  <div>
                    <span>消息通知</span>
                    <a-switch
                      checked-children="是"
                      un-checked-children="否"
                      default-checked
                    />
                  </div>
                </div>
              </div>
              <div class="transfer-input">
                <a-textarea :rows="6" />
              </div>
            </div>
            <div class="yesAndno">
              <button class="submit-gray" type="submit" @click="acceptSubmit">
                确认
              </button>
              <button class="cancel-modal" @click="acceptCancel">取消</button>
            </div>
          </a-modal>
          <button
            :disabled="isdisabled"
            class="btn-fun blue"
            @click="showModalTransfer"
          >
            转办
          </button>
          <a-modal v-model="visible1" :footer="null" title="转办列表">
            <gaf-tree-transparent
              ref="GafTreeTransparent"
              v-model="checkedKeys"
              :search-type="[0]"
              :data-of-tree="dataOfTreeTransfer"
              :check-node-strictly="false"
              :expanded-node-keys.sync="expandedNodeKeys"
              :selected-keys.sync="selectedNodeKeys"
              show-line
              checkable
              class="tree-height"
              @currentChecked="currentChecked"
              @select="onSelect"
            ></gaf-tree-transparent>
            <div id="zc-modal-txt">
              <div class="zc-btn">
                <div>
                  <div>填写意见</div>
                </div>
                <div>
                  <div>
                    <span>消息通知</span>
                    <a-switch
                      checked-children="是"
                      un-checked-children="否"
                      default-checked
                    />
                  </div>
                </div>
              </div>
              <div class="transfer-input">
                <a-textarea :rows="6" />
              </div>
            </div>
            <div class="yesAndno">
              <button class="submit-gray" @click="acceptSubmitTransfer">
                确认
              </button>
              <button class="cancel-modal" @click="acceptCancel">取消</button>
            </div>
          </a-modal>
          <button
            :disabled="isdisabled"
            class="btn-fun red"
            @click="showModalRollback"
          >
            驳回
          </button>
          <a-modal v-model="visible2" :footer="null" title="驳回列表">
            <div id="contentBox">
              <gaf-tree-transparent
                ref="GafTreeTransparent"
                v-model="checkedKeys"
                :search-type="[0]"
                :data-of-tree="dataOfTreeReject"
                :check-node-strictly="false"
                :expanded-node-keys.sync="expandedNodeKeys"
                :selected-keys.sync="selectedNodeKeys"
                show-line
                checkable
                class="tree-height"
                @currentChecked="currentChecked"
                @select="onSelect"
              ></gaf-tree-transparent>
            </div>
            <div id="zc-modal-txt">
              <div class="zc-btn">
                <div>
                  <div>填写意见</div>
                </div>
                <div>
                  <div>
                    <span>消息通知</span>
                    <a-switch
                      checked-children="是"
                      un-checked-children="否"
                      default-checked
                    />
                  </div>
                </div>
              </div>
              <div class="transfer-input">
                <a-textarea :rows="6" />
              </div>
            </div>
            <div class="yesAndno">
              <button class="submit-gray" @click="acceptSubmitReject">
                确认
              </button>
              <button class="cancel-modal" @click="acceptCancel">取消</button>
            </div>
          </a-modal>
          <a-dropdown :disabled="isdisabled" class="btn-menu">
            <a-menu slot="overlay" class="btn-menu" @click="handleMenuClick">
              <a-menu-item key="1">全部保存</a-menu-item>
              <a-menu-item key="2" @click="hangUp">
                <span @click="hangUp">挂起</span>
                <a-modal
                  id="model-style"
                  v-model="visible3"
                  :footer="null"
                  :mask="false"
                  title="您确认要挂起该业务吗？"
                >
                  <a-textarea
                    v-model="value"
                    :auto-size="{ minRows: 7, maxRows: 6 }"
                    style="margin-bottom: 10px"
                    placeholder=""
                  />
                  <button class="submit-gray ycbtn" @click="hangUpOk()">
                    确认
                  </button>
                  <button class="cancel-modal ycbtn" @click="acceptCancel">
                    取消
                  </button>
                </a-modal>
              </a-menu-item>
              <a-menu-item key="3">
                <a @click="closeSkip">关闭</a>
              </a-menu-item>
            </a-menu>
            <button style="margin-left: 8px">
              其他按钮
              <a-icon type="down" />
            </button>
          </a-dropdown>
        </div>
      </div>
      <div class="all-top">
        <a-menu mode="horizontal">
          <a-menu-item class="menu-title">流程信息</a-menu-item>
        </a-menu>
      </div>
      <div class="all-box">
        <div class="box-left">
          <div class="left-l">
            <div class="title-top">{{ dataAll.ProInst_Name }}</div>
            <ul>
              <li>
                <span>流程分类：</span>
                {{ dataAll.ProDef_TypeName }}
              </li>
              <li>
                <span>活动环节：</span>
                {{ dataSecond.ActInst_Name }}
              </li>
              <li>
                <span>业务类别：</span>
                {{ dataZ.OperationTypeEnum }}
              </li>
              <li>
                <span>活动开始：</span>
                {{ dataSecond.ActInst_StartDate }}
              </li>
              <li>
                <span>预计结束：</span>
                {{ dataSecond.ActInst_StartDate }}
              </li>
              <li>
                <span>流程状态：</span>
                <b class="tags tags_proinststate">
                  <i name="Normal" class="layout-hide Normal">正常</i>
                  <i name="ProLong" class="layout-hide ProLong">延期</i>
                  <i name="LogOut" class="layout-hide LogOut">注销</i>
                  <i name="Complete" class="layout-hide Complete">办结</i>
                  <i name="HangingUp" class="layout-hide HangingUp">挂起</i>
                  <i name="Locked" class="layout-hide Locked">锁定</i>
                  <i name="Supervised" class="layout-hide Supervised">督办</i>
                  <i name="OverDue" class="layout-hide OverDue">超期</i>
                </b>
              </li>
              <li>
                <span>提示信息：</span>
                {{ dataSecond.Actinst_Msg }}
              </li>
              <li>
                <span>项目备注：</span>
                {{ dataSecond.ActInst_Desc }}
              </li>
            </ul>
          </div>
          <div class="left-r">
            <ul>
              <li class="red-color">
                <span>流程编号：</span>
                <span>{{ dataAll.ProInst_Code }}</span>
              </li>
              <li>
                <span>流程名称：</span>
                {{ dataAll.ProInst_Name }}
              </li>
              <li>
                <span>流程开始：</span>
                {{ dataAll.ProInst_StartDate }}
              </li>
              <li>
                <span>预计办结：</span>
                {{ dataAll.ProInst_EndDate }}
              </li>
              <li>
                <span>紧急程度：</span>
                <b class="tags tags_proinsttype">
                  <i name="Normal" class="layout-hide Normal">正常</i>
                  <i name="Urgent" class="layout-hide Urgent">加急</i>
                  <i name="Assigned" class="layout-hide Assigned">交办</i>
                  <i name="GreenChannel" class="layout-hide GreenChannel"
                    >绿色</i
                  >
                </b>
              </li>
            </ul>
          </div>
        </div>
        <div class="box-right">
          <ul
            v-for="(item, hangupId) in dataSeconds"
            :key="hangupId"
            :class="handlerType.get(item.ActInst_Type).bgName"
            class="hanging-right"
          >
            <div class="box-rightTxt">{{ DefType[hangupId] }}</div>
            <li class="start-txt">{{ item.ActInst_Name }}</li>
            <li>
              <span>办理类型：</span>
              <b class="tags tags_proinsttype">
                <i :class="handlerType.get(item.ActInst_Type).className">{{
                  handlerType.get(item.ActInst_Type).name
                }}</i>
              </b>
            </li>
            <li>
              <span>办理人员：</span>
              {{ item.Staff_Name }}
            </li>
            <li>
              <span>开始：</span>
              {{ item.ActInst_StartDate }}
            </li>
            <li>
              <span>办结：</span>
              {{ item.ActInst_FinishDate }}
            </li>
          </ul>
        </div>
      </div>
      <div class="layout-vertical" style="position: relative">
        <div
          class="layout-fixed"
          style="
            display: flex;
            justify-content: space-between;
            user-select: none;
          "
        >
          <span style="color: #019688; line-height: 35px; padding-left: 15px"
            >流程图：</span
          >
        </div>
        <div
          style="
            position: absolute;
            color: #999;
            line-height: 35px;
            font-size: 12px;
            right: 70px;
            top: 40px;
          "
        >
          <!--                                        <span style="display:inline-block;color:#A1D6FF;height: 20px;line-height: 20px;padding: 0;font-size: 12px;">在办</span>：-->
          <span
            id="progressIn"
            style="
              display: inline-block;
              background-color: #a1d6ff;
              color: #333333;
              border: none;
              border-radius: 5px;
              height: 25px;
              line-height: 25px;
              padding: 0px 10px;
              font-size: 11px;
            "
            >在办</span
          >
        </div>
        <div
          style="
            position: absolute;
            color: #999;
            line-height: 35px;
            font-size: 12px;
            right: 70px;
            top: 70px;
          "
        >
          <!--                                        <span style="display:inline-block;color:#B9EEC2;height: 20px;line-height: 20px;padding: 0;font-size: 12px;">已办</span>：-->
          <span
            id="progressDone"
            style="
              display: inline-block;
              background-color: #b9eec2;
              color: #333333;
              border: none;
              border-radius: 5px;
              height: 25px;
              line-height: 25px;
              padding: 0px 10px;
              font-size: 11px;
            "
            >已办</span
          >
        </div>
        <div
          style="
            position: absolute;
            color: #999;
            line-height: 35px;
            font-size: 12px;
            right: 70px;
            top: 100px;
          "
        >
          <!--                                        <span style="display:inline-block;color:#CBD7E2;height: 20px;line-height: 20px;padding: 0;font-size: 12px;">待办</span>：-->
          <span
            id="progressWait"
            style="
              display: inline-block;
              background-color: #cbd7e2;
              color: #333333;
              border: none;
              border-radius: 5px;
              height: 25px;
              line-height: 25px;
              padding: 0px 10px;
              font-size: 11px;
            "
            >待办</span
          >
        </div>
        <div
          style="
            position: absolute;
            text-align: right;
            padding-right: 10px;
            right: 0;
            top: 40px;
            z-index: 99;
          "
        >
          <button
            name="zoomIn"
            class="primaryBtnL row"
            title="放大"
            data-readonly="false"
          >
            <a-icon type="zoom-in" />
          </button>
          <button
            name="zoomOut"
            class="primaryBtnS row"
            title="缩小"
            data-readonly="false"
          >
            <a-icon type="zoom-out" />
          </button>
        </div>
        <div class="bg-color-4">
          <div id data-type="readonly" class="layout-horizontal">
            <canvas data-readonly="true"
              >你的浏览器不支持canvas,请升级你的浏览器</canvas
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import GafTreeTransparent from '../../views/GafWorkflowCommon/GafTreeTransparent'
export default {
  name: 'GafDoingSon',
  components: {
    GafTreeTransparent,
  },
  data() {
    return {
      inputSearchValue: '',
      checkedKeys: [],
      autoExpandParent: false,
      addModalVisible: false,
      visible: false,
      visible1: false,
      visible2: false,
      visible3: false,
      isdisabled: '',
      dataAll: [],
      dataZ: [],
      dataSecond: [],
      dataSeconds: [],
      dataOfTree: [],
      dataOfTreeTransfer: [],
      dataOfTreeReject: [],
      transferOutInfo: [],
      acceptReject: [],
      expandedNodeKeys: [],
      selectedNodeKeys: [],
      handlerType: undefined,
      DefType: [],
    }
  },
  computed: {
    showPaneContent() {
      return this.selectedNodeKeys && this.selectedNodeKeys.length > 0
    },
  },
  created() {
    this.getTable()
    this.handlerType = new Map()
    this.handlerType.set(1020, {
      name: '驳回转出',
      className: 'RollBackOut',
      bgName: 'RollBackOutBg',
    })
    this.handlerType.set(2010, {
      name: '被驳回',
      className: 'RollBack',
      bgName: 'RollBackBg',
    })
    this.handlerType.set(3010, {
      name: '挂起',
      className: 'HangUp',
      bgName: 'HangUpBg',
    })
    this.handlerType.set(4010, {
      name: '转交',
      className: 'Transfer',
      bgName: 'TransferBg',
    })
    this.handlerType.set(2020, {
      name: '跳跃驳回',
      className: 'RollJumpBack',
      bgName: 'RollJumpBackBg',
    })
    this.handlerType.set(4010, {
      name: '抄送',
      className: 'Copy',
      bgName: 'CopyBg',
    })
    this.handlerType.set(1010, {
      name: '受理',
      className: 'Accept',
      bgName: 'AcceptBg',
    })
  },
  methods: {
    /* 关闭 */
    closeSkip() {
      this.$router.push({
        path: '../../../pages/workflow/GafDoingBox',
      })
    },
    /* 挂起 */
    hangUp() {
      this.visible3 = true
    },
    async hangUpOk() {
      const url = `/workflow/execute/process/hangup`
      const proInstDesc = this.value
      const nowActInstId = this.$route.query.nowActInstId
      const proInstId = this.$route.query.proInstId
      const actInstId = this.$route.query.actInstId
      const hangRes = await this.$axios.post(url, {
        nowActInstId,
        proInstDesc,
        proInstId,
        actInstId,
      })
      if (hangRes.data.isSuccessed) {
        this.dataOfTree = hangRes.data.data
        this.$message.success(`挂起成功`)
        this.$router.push({
          path: '../../../pages/workflow/GafDoingBox',
        })
      } else {
        this.$message.error(`获取部门用户树失败，原因：${hangRes.data.message}`)
      }
      this.visible = true
    },
    async acceptSubmit() {
      this.visible = false
      const url = `/workflow/execute/process/roll-out`
      const proInstId = this.$route.query.proInstId
      const actInstId = this.$route.query.actInstId
      const nowActInstId = this.$route.query.nowActInstId
      const processVo = { proInstId, actInstId, nowActInstId }
      const treeInfos = []
      this.deepFirstTraverseTree({}, this.dataOfTree, (parentNode, node) => {
        if (this.checkedKeys.includes(node.Id)) {
          const selectNode = { ...node }
          delete selectNode.children
          delete selectNode.key
          delete selectNode.title
          delete selectNode.scopedSlots
          treeInfos.push(selectNode)
        }
      })
      const processDealInfo = { nowActInstId, treeInfos }
      const acceptTransfer = await this.$axios.post(url, {
        processVo,
        processDealInfo,
      })
      this.acceptTransfer = acceptTransfer
      location.reload()
    },
    async acceptSubmitTransfer() {
      this.visible1 = false
      const url = `/workflow/execute/process/transfer`
      const proInstId = this.$route.query.proInstId
      const actInstId = this.$route.query.actInstId
      const nowActInstId = this.$route.query.nowActInstId
      const processVo = { proInstId, actInstId, nowActInstId }
      const treeInfos = []
      this.deepFirstTraverseTree({}, this.dataOfTree, (parentNode, node) => {
        if (this.checkedKeys.includes(node.Id)) {
          const selectNode = { ...node }
          delete selectNode.children
          treeInfos.push(selectNode)
        }
      })
      const processDealInfo = { nowActInstId, treeInfos }
      const transferOutInfo = await this.$axios.post(url, {
        processVo,
        processDealInfo,
      })
      this.transferOutInfo = transferOutInfo
      location.reload()
    },

    async acceptSubmitReject() {
      this.visible2 = false
      const url = `/workflow/execute/process/rollback`
      const proInstId = this.$route.query.proInstId
      const actInstId = this.$route.query.actInstId
      const nowActInstId = this.$route.query.nowActInstId
      const processVo = { proInstId, actInstId, nowActInstId }
      const processDealInfo = { nowActInstId }
      const acceptReject = await this.$axios.post(url, {
        processVo,
        processDealInfo,
      })
      this.acceptReject = acceptReject
    },
    /* 功能键 模态框 树组件遍历 */
    deepFirstTraverseTree(parentNode, data, callback) {
      if (data) {
        for (let i = 0; i < data.length; i++) {
          const node = data[i]
          callback(parentNode, node)
          if (node.children) {
            this.deepFirstTraverseTree(node, node.children, callback)
          }
        }
      }
    },
    acceptCancel() {
      this.visible = false
      this.visible1 = false
      this.visible2 = false
    },
    /* 转出 模态框 树组件接口 */
    async showModalRoll() {
      this.visible = true
      const url = `/workflow/query/roll-out/user/tree`
      const proInstId = this.$route.query.proInstId
      const actInstId = this.$route.query.actInstId
      const nowActInstId = this.$route.query.nowActInstId
      const res = await this.$axios.post(url, {
        proInstId,
        actInstId,
        nowActInstId,
      })
      // 上级页面 表单中获取的值 proInstId, actInstId 传给后台
      if (res.data.isSuccessed) {
        this.deepFirstTraverseTree({}, res.data.data, (parentNode, node) => {
          node.key = node.Id
          node.title = node.Name
          node.scopedSlots = { title: 'title' }
          this.expandedNodeKeys.push(node.Id)
        })
        this.dataOfTree = res.data.data
      } else {
        this.$message.error(`获取部门用户树失败，原因：${res.data.message}`)
      }
    },
    /* 转办 模态框 树组件接口 */
    async showModalTransfer() {
      this.visible1 = true
      const url = `/workflow/query/transfer-out/user/tree`
      const proInstId = this.$route.query.proInstId
      const actInstId = this.$route.query.actInstId
      const nowActInstId = this.$route.query.nowActInstId
      const resTransfer = await this.$axios.post(url, {
        proInstId,
        actInstId,
        nowActInstId,
      })
      // console.log(proInstId, actInstId, nowActInstId)
      // 上级页面 表单中获取的值 proInstId, actInstId 传给后台
      if (resTransfer.data.isSuccessed) {
        this.deepFirstTraverseTree(
          {},
          resTransfer.data.data,
          (parentNode, node) => {
            node.key = node.Id
            node.title = node.Name
            node.scopedSlots = { title: 'title' }
            this.expandedNodeKeys.push(node.Id)
          }
        )
        this.dataOfTreeTransfer = resTransfer.data.data
      } else {
        this.$message.error(
          `获取部门用户树失败，原因：${resTransfer.data.message}`
        )
      }
    },
    /* 驳回 模态框 树组件接口 */
    async showModalRollback() {
      this.visible2 = true
      const url = `/workflow/query/rollback-out/user/tree`
      const proInstId = this.$route.query.proInstId
      const actInstId = this.$route.query.actInstId
      const nowActInstId = this.$route.query.nowActInstId
      const resReject = await this.$axios.post(url, {
        proInstId,
        actInstId,
        nowActInstId,
      })
      // console.log(proInstId, actInstId, nowActInstId)
      // 上级页面 表单中获取的值 proInstId, actInstId 传给后台
      if (resReject.data.isSuccessed) {
        this.deepFirstTraverseTree(
          {},
          resReject.data.data,
          (parentNode, node) => {
            node.key = node.Id
            node.title = node.Name
            node.scopedSlots = { title: 'title' }
            this.expandedNodeKeys.push(node.Id)
          }
        )
        this.dataOfTreeReject = resReject.data.data
      } else {
        this.$message.error(
          `获取部门用户树失败，原因：${resReject.data.message}`
        )
      }
    },
    handleMenuClick() {},
    /* 键值对 获取后台的数据 接口 */
    async getTable() {
      const proInstId = this.$route.query.proInstId
      const actInstId = this.$route.query.actInstId
      // const nowActInstId = this.$route.query.nowActInstId
      const url = `/workflow/query/process-detail`
      // 上级页面 表单中获取的值 proInstId, actInstId 传给后台
      const rst = await this.$axios.post(url, { proInstId, actInstId })
      if (rst.data.data.nowActInsts == null) {
        this.isdisabled = true
      } else {
        this.isdisabled = false
      }
      if (rst.data.isSuccessed) {
        // this.$message.success('成功')
      } else {
        this.$message.error('更新失败')
      }
      this.dataZ = rst.data.data
      this.dataAll = rst.data.data.proInst
      this.dataSecond = rst.data.data.actInst
      this.dataSeconds = rst.data.data.actInsts
      this.dataStateCode = this.dataAll.ProInst_State
      this.dataTypeCode = this.dataAll.ProInst_Type
      /* 右侧状态数据 */
      for (let i = 0; i < this.dataSeconds.length; i++) {
        if (
          this.dataSeconds[i].ActInst_Type === 2010 &&
          this.dataSeconds[i].ActDef_Type < 9000
        ) {
          if (this.dataSeconds[i].Staff_ID != null) {
            this.DefType[i] = '在办'
          } else {
            this.DefType[i] = '待办'
          }
        } else if (this.dataSeconds[i].ActInst_State === 3010) {
          this.DefType[i] = '正在待办'
        } else if (this.dataSeconds[i].ActInst_State === 4010) {
          this.DefType[i] = '转出'
        } else if (this.dataSeconds[i].ActDef_Type >= 9000) {
          this.DefType[i] = '办结'
        } else {
          this.DefType[i] = '受理'
        }
      }
      const i = {
        ProInst_Type: {
          Normal: 1010,
          Urgent: 2010,
          Assigned: 3010,
          GreenChannel: 4010,
        },
        ProInst_State: {
          Normal: 1010,
          HangingUp: 2010,
          Supervised: 2020,
          Locked: 2030,
          OverDue: 3010,
          ProLong: 3020,
          LogOut: 4010,
          Complete: 9010,
        },
      }
      const stateObj = i.ProInst_State
      const typeObj = i.ProInst_Type
      for (const key in stateObj) {
        if (stateObj[key] === this.dataStateCode) {
          document
            .querySelector(`.tags_proinststate i[name=${key}]`)
            .classList.remove('layout-hide')
        }
      }
      for (const key in typeObj) {
        if (typeObj[key] === this.dataTypeCode) {
          document
            .querySelector(`.tags_proinsttype i[name=${key}]`)
            .classList.remove('layout-hide')
          return
        }
      }
    },
  },
}
</script>
<style scoped>
.func-top {
  height: 50px;
  border-bottom: 1px solid rgb(243, 243, 243);
  background-color: rgb(243, 243, 243);
  position: relative;
}
.func-box span {
  font-size: 18px;
  color: black;
  font-weight: bolder;
  margin-left: 2%;
  line-height: 50px;
}

.func-box {
  position: absolute;
  margin-left: 2%;
  width: 600px;
}
.btn-box {
  line-height: 32px;
  float: right;
  margin-right: 3%;
}

.btn-blue {
  color: white;
  background-color: rgb(109, 138, 204);
}

.btn-deepblue {
  color: white;
  background-color: rgb(24, 113, 186);
}
.all-top {
  border-bottom: 1px solid gray;
}

.all-box {
  position: relative;
  width: 100%;
  border: none;
}

.all-box li {
  list-style: none;
  padding: 3px;
}

.layout-vertical {
  margin-top: 30%;
  margin: 390px 2% 0 2%;
  border-radius: 15px;
  font-size: 18px;
  width: 65%;
  position: absolute;
  z-index: 99;
}
.left-l span {
  font-weight: bolder;
  font-size: 19px;
  color: rgb(96, 108, 136);
}

.left-r span {
  font-weight: bolder;
  font-size: 19px;
  color: rgb(96, 108, 136);
}

.left-l {
  width: 40%;
  float: left;
  margin: 1% 8% 0 12%;
}

.title-top {
  color: black;
  font-size: 25px;
  font-weight: bolder;
}
.box-left {
  margin: 1% 2%;
  border-radius: 15px;
  position: absolute;
  font-size: 18px;
  height: 370px;
  width: 65%;
  background-color: rgb(245, 247, 250);
}

.box-right {
  float: right;
  width: 30%;
}
.hanging-right {
  background-color: rgb(221, 244, 234);
  color: black;
  height: 30%;
  border-radius: 5px;
  margin: 3% 5% 0 0;
  padding: 3% 15%;
}
.box-rightTxt {
  float: right;
  color: white;
  font-size: 20px;
  font-weight: bolder;
  background-color: rgba(36, 158, 158, 0.4);
  padding: 3px 15px;
  border-radius: 15px;
}
.ycbtn {
  line-height: 32px;
  border-radius: 4px;
}
.Normal {
  background-color: rgb(30, 144, 255);
  padding: 3px 8px 3px 4px;
}

.ProLong {
  background-color: rgb(226, 174, 63);
  padding: 3px 8px 3px 4px;
}

.LogOut {
  background-color: rgb(230, 53, 53);
  padding: 3px 8px 3px 4px;
}

.Complete {
  background-color: rgb(151, 216, 66);
  padding: 3px 8px 3px 4px;
}

.HangingUp {
  background-color: rgb(255, 215, 0);
  padding: 3px 8px 3px 4px;
}

.Locked {
  background-color: rgb(190, 188, 188);
  padding: 3px 8px 3px 4px;
}

.Supervised {
  background-color: rgb(34, 156, 140);
  padding: 3px 8px 3px 4px;
}

.OverDue {
  background-color: rgb(245, 232, 58);
  padding: 3px 8px 3px 4px;
}

.Urgent {
  background-color: rgb(221, 68, 114);
  padding: 3px 8px 3px 4px;
}

.Assigned {
  background-color: rgb(183, 53, 206);
  padding: 3px 8px 3px 4px;
}

.GreenChannel {
  background-color: rgb(85, 206, 55);
  padding: 3px 8px 3px 4px;
}
.layout-hide {
  display: none;
}
.red-color span:nth-child(2) {
  color: red;
}
</style>

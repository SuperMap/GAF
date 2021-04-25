<template v-else-if="record.type === 'table'">
  <!-- <div
    class="table-box"
    @click.stop="handleSelectItem(record)"
  > -->
  <div>
    <div>
      <sortable-control-item-header
        :title="data.title"
        :isShowMore="data.isShowMore"
        :href="data.href"
        @change="toggleCollapsed"
        @delete="$emit('delete')"
      />
    </div>
    <table
      class="table-layout div-table"
      :class="{
        bright: record.options.bright,
        small: record.options.small,
        bordered: record.options.bordered
      }"
      :style="record.options.customStyle"
      v-show="!collapsed"
    >
      <tr v-for="(trItem, trIndex) in record.trs" :key="trIndex">
        <td
          class="table-td"
          v-for="(tdItem, tdIndex) in trItem.tds"
          :key="tdIndex"
          :colspan="tdItem.colspan"
          :rowspan="tdItem.rowspan"
          @contextmenu.prevent="handleShowRightMenu($event, record, trIndex, tdIndex)"
        >
    <draggable
      v-model="tdItem.list"
      v-bind="dragOptions"
      sort="true"
      @start="drag = true"
      @end="drag = false"
    >
      <transition-group
        :name="!drag ? 'flip-list' : null"
        tag="div"
        type="transition"
        class="list-main"
        :key="tdIndex"
        @click.native.stop="onInnerItemClickHandle(trIndex,tdIndex)"
        :class="getItemClass(tdItem)"
        :style="setWidthHeight(tdItem)"
      >
        <template v-for="(l, idx) in tdItem.list" >
          <sortable-control-item
            :key="idx"
            :data="l"
            @delete="tdItem.list.splice(idx, 1)"
          />
        </template>
      </transition-group>
    </draggable>
        </td>
      </tr>
    </table>
    <!-- 右键菜单 start -->
    <div
      v-show="showRightMenu"
      :style="{ top: menuTop + 'px', left: menuLeft + 'px' }"
      class="right-menu"
    >
      <ul>
        <li @click="handleDownMerge"><a-icon type="caret-down" />向下合并</li>
        <li @click="handleRightMerge"><a-icon type="caret-right" />向右合并</li>
        <li @click="handleAddCol">
          <a-icon type="border-horizontal" />增加一列
        </li>
        <li @click="handleAddRow"><a-icon type="border-verticle" />增加一行</li>
      </ul>
    </div>
  </div>
</template>
<script>
import draggable from 'vuedraggable'
import SortableControlItem from './SortableControlItem'
import sortableControlItemHeader from './SortableControlItemHeader'
export default {
  name: "SortableGridTableControlItem",
  inject: ['updateProperties'],
  data() {
    return {
      showRightMenu: false,
      collapsed: false,
      drag: false,
      dragOptions: {
        animation: 150,
        group: {
          name: 'controls',
          pull: true,
          put(to) {
            return to.el.children.length < 1
          },
        },
        ghostClass: 'ghost',
        sort: false,
      },
      rightMenuSelectValue: {},
      menuTop: 0,
      menuLeft: 0,
      trIndex: 0,
      tdIndex: 0,
      customStyle: null,
    }
  },
  components: {
    SortableControlItem,
    draggable,
    sortableControlItemHeader
  },
  props:{
    data: {
      type: Object,
      required: true
    },
    selectInnerItem: {
      type: Object,
      required: true,
    },
  },
  computed: {
    record() {
      return this.data || {}
    },
    columns() {
      return this.attr.columns.filter((c) => c.visible) || []
    },
  },
  mounted(){
    // 添加监听取消右键菜单
    document.addEventListener("click", this.handleRemoveRightMenu, true);
    document.addEventListener("contextmenu", this.handleRemoveRightMenu, true);
  },
  destroyed() {
    // 移除监听
    document.removeEventListener("click", this.handleRemoveRightMenu, true);
    document.removeEventListener(
      "contextmenu",
      this.handleRemoveRightMenu,
      true
    );
  },
  methods: {
    setWidthHeight(data) {
      const item = data.list[this.tdIndex]
      let style = null
      let width = null
      let height = null
      if(item && item.width && item.width !== '') {
         width = item.width
         style += `;width:${width}px`
      }
      if(item && item.height && item.height !== '') {
         height = item.height
         style += `;height:${height}px;overflow-y: auto;`
      }
      return style
    },
    deleteItem(index) {
      this.data.list.splice(index, 1)
    },
    toggleCollapsed(collapsed) {
      this.collapsed = collapsed
    },
    getItemClass(item) {
      let active = false
      if (this.selectInnerItem && item) {
        active = item.id === this.selectInnerItem.id
      }
      return {
        'control-item': true,
        active,
      }
    },
    onInnerItemClickHandle(trIndex, tdIndex) {
      const selectItem = this.data.trs[trIndex].tds[tdIndex]
      this.$emit('selectitem', selectItem)
      this.updateProperties(selectItem.list[0])
    },
    handleSelectItem(record) {
      this.$emit("handleSelectItem", record);
    },
        handleDownMerge() {
      // 向下合并
      if (
        this.rightMenuSelectValue.trs.length -
          this.rightMenuSelectValue.trs[this.trIndex].tds[this.tdIndex]
            .rowspan <=
        this.trIndex
      ) {
        this.$message.error("当前是最后一行，无法向下合并");
        return false;
      }

      // 计算rowspan超过自身的td
      let rows = 0;
      this.rightMenuSelectValue.trs[this.trIndex].tds.forEach(
        (element, index) => {
          if (index >= this.tdIndex) {
            return false;
          }
          if (
            element.rowspan >
            this.rightMenuSelectValue.trs[this.trIndex].tds[this.tdIndex]
              .rowspan
          ) {
            rows += 1;
          }
        }
      );
      if (
        this.rightMenuSelectValue.trs[this.trIndex].tds[this.tdIndex]
          .colspan !==
        this.rightMenuSelectValue.trs[this.trIndex + 1].tds[this.tdIndex - rows]
          .colspan
      ) {
        this.$message.error("当前表格无法向下合并");
        return false;
      }

      this.rightMenuSelectValue.trs[this.trIndex].tds[
        this.tdIndex
      ].rowspan += 1;
      this.rightMenuSelectValue.trs[
        this.trIndex + 1
      ].tds = this.rightMenuSelectValue.trs[this.trIndex + 1].tds.filter(
        (item, index) => index !== this.tdIndex - rows
      );

      // }
    },
    handleRightMerge() {
      // 向右合并
      const sumCols = this.rightMenuSelectValue.trs[this.trIndex].tds
        .map(item => item.colspan)
        .reduce(function(partial, value) {
          return partial + value;
        });
      if (
        sumCols -
          this.rightMenuSelectValue.trs[this.trIndex].tds[this.tdIndex]
            .colspan <=
        this.tdIndex
      ) {
        this.$message.error("当前是最后一列，无法向右合并");
        return false;
      }
      if (
        this.rightMenuSelectValue.trs[this.trIndex].tds[this.tdIndex]
          .rowspan !==
        this.rightMenuSelectValue.trs[this.trIndex].tds[this.tdIndex + 1]
          .rowspan
      ) {
        this.$message.error("当前表格无法向右合并");
        return false;
      }

      this.rightMenuSelectValue.trs[this.trIndex].tds[
        this.tdIndex
      ].colspan += this.rightMenuSelectValue.trs[this.trIndex].tds[
        this.tdIndex + 1
      ].colspan;

      this.rightMenuSelectValue.trs[
        this.trIndex
      ].tds = this.rightMenuSelectValue.trs[this.trIndex].tds.filter(
        (item, index) => {
          return index !== this.tdIndex + 1;
        }
      );
      // }
    },
    handleAddCol() {
      // 增加列
      this.rightMenuSelectValue.trs.forEach(item => {
        item.tds.splice(this.tdIndex + 1, 0, {
          colspan: 1,
          rowspan: 1,
          list: [],
          id: Math.random()
        });
      });
    },
    handleAddRow() {
      // 增加行
      // 获取总col值
      const sumCols = this.rightMenuSelectValue.trs[0].tds
        .map(item => item.colspan)
        .reduce(function(partial, value) {
          return partial + value;
        });
      const rowJson = { tds: [] };
      for (let i = 0; i < sumCols; i++) {
        rowJson.tds.push({
          colspan: 1,
          rowspan: 1,
          list: [],
          id: Math.random()
        });
      }
      this.rightMenuSelectValue.trs.splice(this.trIndex + 1, 0, rowJson);
    },
    handleShowRightMenu(e, val, trIndex, tdIndex) {
      // 显示右键菜单
      e.stopPropagation();
      // this.fileItem = item
      // 显示
      this.showRightMenu = true;
      // 定位
      this.menuTop = e.clientY;
      this.menuLeft = e.clientX;
      // this.rightMenuType = type
      // this.rightId = id
      this.activeArr = [val];
      this.rightMenuSelectValue = val;
      this.trIndex = trIndex;
      this.tdIndex = tdIndex;
      return false;
    },
    handleRemoveRightMenu() {
      // 取消右键菜单
      this.showRightMenu = false;
    }
  }
  
}
</script>
<style lang="less" scoped>
.div-table{
  width: 100%;
  -webkit-box-sizing:border-box;
  box-sizing:border-box
}
.div-table,.div-table tr,.div-table tr td{
  -webkit-transition:all .3s;
  transition:all .3s;
  border-collapse:collapse
  }
.div-table tr td{
  -webkit-box-sizing:border-box;box-sizing:border-box;
  padding:12px 12px;vertical-align:top
  }
.div-table.bordered tr td{
  border:1px solid #e8e8e8!important
  }
.div-table.bright tr:hover>td{
  background:#e6f7ff
  }
.div-table.small tr td{
  padding:8px 8px
  }
.div-table .ant-row.ant-form-item{
  margin:0!important
}
.list-main {
  min-height: 60px;
  position: relative;
}
.control-item {
  border: solid 1px rgb(37, 214, 185);
  margin-bottom: 6px;
  padding: 6px;
}
.active {
  border: dashed 2px #0e67ee;
}
.table-td {
  .draggable-box {
    min-height: 60px;
    min-width : 50px;
    border    : 1px #ccc dashed;
    background: #fff;

    .list-main {
      min-height: 60px;
      position  : relative;
      border    : 1px #ccc dashed;
    }
  }
}
.right-menu {
  width     : 190px;
  background: #fff;
  border    : 1px solid #ccc;
  position  : fixed;
  transition: all 0s;
  box-shadow: 3px 3px 5px #999;
  padding   : 8px 0;
  z-index   : 999;
  ul {
    padding   : 0;
    margin    : 0;
    list-style: none;

    li {
      cursor     : pointer;
      user-select: none;
      padding    : 0 15px;
      height     : 30px;
      line-height: 30px;
      font-size  : 14px;

      &:hover {
        background: #eee;
      }
    }
  }
}
</style>
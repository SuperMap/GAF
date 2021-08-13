<template>
  <div class="app-container">
    <div class="page-left">
        
    </div>
    <div class="page-right">
      <svg
        id='mmCavasSvg'
        :width='width'
        :height='height'
        style='border:1px dashed #000;background-color: #efefef;'
      ></svg>
    </div>
  </div>
</template>
<script>
import "~/assets/css/common.css";
import * as d3 from 'd3'
export default {
  data() {
    return {
      modelId: null,
      nodes: [],
      links: [],
      nodeLink: new Map(),
      width: 1200,
      height: 600,
      colWidth: 200,
      offLine: 20,
      fieldsHeight: 25,
      headHeight: 35,
      preWidth: 20,
      forceSimulation: null,
      deletedNodes: new Map(),
      hightColor: '#19be6b',
      headColor: '#fff',
      headTextColor: '#000'
    }
  },
  async mounted() {
    const re = await this.getData()
    if (!re) {
      return
    }
    const that = this
    const svg = d3.select('#mmCavasSvg')
    svg.append("defs").selectAll("marker")
      .data(["suit", "licensing", "resolved"])
      .enter().append("marker")
      .attr("id", function (d) { return d; })
      .attr("viewBox", "0 -5 10 10")
      .attr("refX", 9)
      .attr("refY", 0)
      .attr("markerWidth", 6)
      .attr("markerHeight", 6)
      .attr("orient", "auto")
      .append("path")
      .attr("d", "M0,-5L10,0L0,5");
    const gRoot = svg.append('g')
    gRoot.append("g").attr('class', 'links')
    // 初始化力导向参数
    that.forceSimulation = d3.forceSimulation()
      .force('charge', d3.forceManyBody().strength(-100))//电荷力
      .force('forceCollide', d3.forceCollide().radius(that.colWidth / 2 + 50))//检测碰撞
      .force('link', d3.forceLink().id(function (table) {
        return table.tableId
      }))//link
      .force('x', d3.forceX(this.width / 2))
      .force('y', d3.forceY(this.height / 2))
    const zoom = d3.zoom()
      .scaleExtent([0.1, 10])
      .on("zoom", function () {
        gRoot.attr("transform", function () {//初始化
          return `translate(${d3.event.transform.x},${d3.event.transform.y}) scale(${d3.event.transform.k})`
        })
      })
    svg.call(zoom).on('dblclick.zoom', null);
    this.update()
  },
  methods: {
    async getData() {
      const that = this
      that.modelId = this.$route.query.modelId
      that.modelId = 'eda861df-592c-4856-9ef3-df5bc4004d69'
      const url = `/data-mgt/model-manage/models/${that.modelId}/layout`;
      const res = await this.$axios.get(url);
      if (res.data.successed) {
        that.nodes = res.data.data.nodes
        that.links = res.data.data.links

        that.links.forEach(item => {
          if (that.nodeLink.has(item.source)) {
            that.nodeLink.get(item.source).add(item)
          } else {
            const linksSet = new Set()
            linksSet.add(item)
            that.nodeLink.set(item.source, linksSet)
          }
          if (that.nodeLink.has(item.target)) {
            that.nodeLink.get(item.target).add(item)
          } else {
            const linksSet = new Set()
            linksSet.add(item)
            that.nodeLink.set(item.target, linksSet)
          }
        })
        return true
      } else {
        that.$message.error(res.data.message, 1);
        return false
      }
    },
    async update(reLoadData) {
      const that = this;
      if (reLoadData) {
        await that.getData()
      }
      //迭代函数
      that.forceSimulation.nodes(that.nodes)
        .on('end', function () { })
        .on('tick', function () {
          that.gRoot().selectAll(".node")
            // .data(that.nodes, function (node, index) { return node.id })
            .data(that.nodes, function (table) {
              return table.tableId
            }).attr("transform", function (d) {
              return `translate(${d.x},${d.y})`
            })
          that.gRoot().select('.links').selectAll(".link-polyline")
            .data(that.links, function (d) {
              return `${d.source.tableId}-${d.sourceFieldIndex}:${d.target.tableId}-${d.targetFieldIndex}`
            }).attr('points', function (d) {
              return that.linkFn(d)
            })
        });
      that.forceSimulation.force('link')
        .links(that.links)
        .distance(function () {
          return that.colWidth + 100
        })
      //初始化节点
      let nodeData = that.gRoot().selectAll(".node")
        // .data(that.nodes, function (node, index) { return node.id })
        .data(that.nodes, function (table) {
          return table.tableId
        })
      nodeData.exit().remove()
      // 添加节点
      let node = nodeData.enter()
        .append('g')
        .attr('style', 'cursor: move;')
        .attr('class', 'node')
        .attr('id', function (table) { return table.tableId })


      //添加表头
      const tableHeader = node.append('g')
        .attr('class', 'table-header')
      tableHeader.append('rect')
        .attr('width', that.colWidth)
        .attr('height', that.headHeight)
        .attr('fill', that.headColor)
        .attr('stroke', 'black')

      // 添加删除按钮
      tableHeader.append('text')
        .attr('dx', that.colWidth - 19)
        .attr('dy', 10)
        .text('🗙')
        .attr('font-size', "1.2  em")
        .attr('dominant-baseline', 'middle')
        .attr('fill', 'red')
        .attr('style', 'cursor: pointer')
        .on('click', function (table) {
          that.nodes = that.nodes.filter(item => {
            if (item.tableId === table.tableId) {
              that.deletedNodes.set(item.tableId, item)
              return false
            } else {
              return true
            }
          })
          that.gRoot().selectAll(".node").data(that.nodes, function (tableI) {
            return tableI.tableId
          }).exit().remove()
          that.links = that.links.filter(item => {
            return table !== item.source && table !== item.target
          })
          that.gRoot().selectAll(".link-polyline").data(that.links, function (d) {
            return `${d.source.tableId}-${d.sourceFieldIndex}:${d.target.tableId}-${d.targetFieldIndex}`
          }).exit().remove()
        })


      tableHeader.append('g')
        .attr('width', that.colWidth - that.preWidth)
        .append('text')
        .attr('dx', that.colWidth / 2)
        .attr('dy', that.headHeight / 2)
        .text(function (table) {
          return table.tableName
        })
        .attr('text-anchor', 'middle')
        .attr('dominant-baseline', 'middle')
        .attr('fill', that.headTextColor)
        .attr('font-size', that.headHeight / 2+2)




      //添加表body
      node.append('g')
        .attr('class', 'table-body')
        .attr('font-size', 'smaller')
        .attr('fill', '#fff')


      // 表body添加字段
      const tableField = node.select('.table-body')
        .selectAll('g')
        .data(function (table) {
          table.fields.forEach(item => {
            item.tableId = table.tableId
          })
          return table.fields
        })
        .enter()
        .append('g')
        .attr('class', 'table-field')
        .attr("transform", (field, i) => `translate(0,${that.headHeight + i * that.fieldsHeight})`)
        .attr('id', function (field) { return 'I-'+field.fieldId })

      tableField.append('rect')
        .attr('width', that.colWidth)
        .attr('height', that.fieldsHeight)
        .attr('stroke', 'black')

      tableField.filter(field => {
        return field.fieldPrimaryKey === true
      })
        .append('text')
        .attr('dx', 0)
        .attr('dy', that.fieldsHeight / 2)
        .text('🔑')
        .attr('dominant-baseline', 'middle')
        .attr('font-size', that.fieldsHeight / 2)

      // .attr('fill', '#000')

      tableField.append('text')
        .attr('dx', that.preWidth)
        .attr('dy', that.fieldsHeight / 2)
        .text(function (field) {
          return field.fieldName
        })
        .attr('dominant-baseline', 'middle')
        .attr('fill', '#000')
        .attr('font-size', that.fieldsHeight / 2+2)

      tableField.append('text')
        .attr('dx', that.colWidth - 15)
        .attr('dy', that.fieldsHeight / 2)
        .text('➥')
        .attr('dominant-baseline', 'middle')
        .attr('fill', 'black')
        .attr('font-size', that.fieldsHeight / 2+4)
        .attr('style', 'cursor: pointer;')
        .on('click', function (field) {
          console.log(field)
        })
      // 节点高亮
      tableField.on('mouseenter', function () {
        that.hightField(this)
      })
      tableField.on('mouseleave', function () {
        d3.selectAll('.table-field')
          .select('rect')
          .transition()
          // .delay(50)
          .attr('fill', '#fff')
        d3.selectAll('.link-polyline').attr('stroke', '#80848f')
      })
      // 添加连线
      const linkData = that.gRoot().select('.links').selectAll(".link-polyline")
        .data(that.links, function (d) {
          return `${d.source.tableId}-${d.sourceFieldIndex}:${d.target.tableId}-${d.targetFieldIndex}`
        })
      linkData.enter()
        .append("polyline")
        .attr('id', function (d) {
          return `${d.source.fields[d.sourceFieldIndex - 1].fieldId}:${d.target.fields[d.targetFieldIndex - 1].fieldId}`
        })
        .attr("fill", 'none')
        .attr("stroke", '#80848f')
        .attr("stroke-width", 2)
        .attr('class', 'link-polyline')
        // .attr("marker-end", "url(#suit)")
        .attr('stroke-dasharray', "3,3")
      // 添加拖拽
      that.gRoot().selectAll(".node")
        // .data(that.nodes, function (node, index) { return node.id })
        .data(that.nodes, function (table) {
          return table.tableId
        }).call(d3.drag()
          .on("start", function (table) {//拖动开始
            if (!d3.event.active) {
              that.forceSimulation.alphaTarget(0.9).restart()//[0,1]
            }
            table.fx = table.x
            table.fy = table.y
          })
          .on("drag", (table) => {//拖动中
            table.fx = d3.event.x
            table.fy = d3.event.y
          })
          .on("end", () => {//拖动结束
            if (!d3.event.active) {
              that.forceSimulation.alphaTarget(0)
            }
          })
        )
    },
    hightField(fieldDom) {
      const that = this
      const rect = d3.select(fieldDom).select('rect')
      if (rect._groups[0][0].getAttribute('fille') === that.hightColor) {
        return
      }
      rect.transition()
        .attr('fill', that.hightColor)
      let anotherFields = []
      const fieldId = fieldDom.id.substring(2)
      d3.selectAll('.link-polyline')
        .filter(function () {
          if (this.getAttribute('stroke') === that.hightColor || this.getAttribute('id').indexOf(fieldId) === -1) {
            return false
          }
          const fieldIds = this.getAttribute('id').split(':')
          anotherFields.push(fieldIds[0] !== fieldId ? fieldIds[0] : fieldIds[1])
          return true
        })
        .attr('stroke', that.hightColor)
      anotherFields.forEach(item => {
        that.hightField(d3.select('#I-'+item)._groups[0][0])
      })
    },
    linkFn(d) {
      let res = []
      const that = this
      if (d.source.x < d.target.x) {
        res[2] = [d.target.x - this.offLine, d.target.y]
        res[3] = [d.target.x, d.target.y]
        if ((d.source.x + that.colWidth + this.offLine * 0) < d.target.x) {
          res[0] = [d.source.x + that.colWidth, d.source.y]
          res[1] = [d.source.x + that.colWidth + this.offLine, d.source.y]
        } else {
          res[0] = [d.source.x, d.source.y]
          res[1] = [d.source.x - this.offLine, d.source.y]
        }
      } else {
        res[1] = [d.source.x - this.offLine, d.source.y]
        res[0] = [d.source.x, d.source.y]
        if ((d.target.x + that.colWidth + this.offLine * 0) < d.source.x) {
          res[3] = [d.target.x + that.colWidth, d.target.y]
          res[2] = [d.target.x + that.colWidth + this.offLine, d.target.y]
        } else {
          res[3] = [d.target.x, d.target.y]
          res[2] = [d.target.x - this.offLine, d.target.y]
        }
      }
      res[0][1] += this.headHeight + (d.sourceFieldIndex - 1) * this.fieldsHeight + this.fieldsHeight / 2
      res[1][1] += this.headHeight + (d.sourceFieldIndex - 1) * this.fieldsHeight + this.fieldsHeight / 2
      res[2][1] += this.headHeight + (d.targetFieldIndex - 1) * this.fieldsHeight + this.fieldsHeight / 2
      res[3][1] += this.headHeight + (d.targetFieldIndex - 1) * this.fieldsHeight + this.fieldsHeight / 2
      return res.map(v => v.join(',')).join(' ')
    },
    gRoot() {
      return d3.select('#mmCavasSvg').select('g')
    },
    backNode(tableId) {
      const that = this
      if (that.deletedNodes.has(tableId)) {
        const node = that.deletedNodes.get(tableId)
        delete node.fx
        delete node.fy
        that.nodes.push(node)
        that.deletedNodes.delete(tableId)
        if (that.nodeLink.has(tableId)) {
          that.nodeLink.get(tableId).forEach(item => {
            if (!that.deletedNodes.has(item.source.tableId) && !that.deletedNodes.has(item.target.tableId)) {
              that.links.push(item)
            }
          })

        }
        that.update()
      }
    },
  },
}
</script>

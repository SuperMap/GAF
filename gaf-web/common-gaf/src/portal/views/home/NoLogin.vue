<template>
  <div>
    <a-carousel autoplay>
      <div
        v-for="item in items"
        :key="item"
        :style="bg(item)"
        class="img-item"
      />
    </a-carousel>
    <div id="anchorLink" class="anchor-link">
      <a @click="() => navPos('feature')">产品特性</a>
      <a @click="() => navPos('case')">产品案例</a>
      <a @click="() => navPos('help')">产品手册</a>
    </div>
    <div class="gaf-container">
      <div id="feature" class="big-itle">产品特性</div>
      <a-row class="row-gap">
        <a-col :span="12">
          <div class="flex-feature">
            <img
              :src="relolveImgPath('/img/devops.png')"
              class="img-feature"
              alt="图片"
            />
            <div>
              <span class="title">一站式企业协同研发</span>
              <div class="sub-title">
                沉淀敏捷项目研发、自动化持续集成等理念并落地实施，自动识别发布异常并采取合理措施。提供从“需求->开发->测试->发布->运维->运营”端到端的协同服务和研发工具支撑。
              </div>
            </div>
          </div>
        </a-col>
        <a-col :span="12">
          <div class="flex-feature">
            <img
              :src="relolveImgPath('/img/jiankong.png')"
              class="img-feature"
              alt="图片"
            />
            <div>
              <span class="title">完善的微服务治理功能</span>
              <div class="sub-title">
                支持灰度发布、熔断、流量监测、流量管控、限流、链路追踪、智能路由等完善的微服务治理功能
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
      <a-row class="row-gap">
        <a-col :span="12">
          <div class="flex-feature">
            <img
              :src="relolveImgPath('/img/jikaijiyong.png')"
              class="img-feature"
              alt="图片"
            />
            <div>
              <span class="title">即存即用、省心运维</span>
              <div class="sub-title">
                二维/三维数据（超图工作空间、Excel、OSGB、S3M等）安全上云，可通过应用组件实现数据的浏览、查询、编辑、分析，系统自带GIS集群、健康监控与自恢复，用户无需关心服务器部署与运维。
              </div>
            </div>
          </div>
        </a-col>
        <a-col :span="12">
          <div class="flex-feature">
            <img
              :src="relolveImgPath('/img/safe.png')"
              class="img-feature"
              alt="图片"
            />
            <div>
              <span class="title">完善的账户权限管理</span>
              <div class="sub-title">
                多级账户权限，多层级数据可控管理，详实操作日志记录，数据管理更放心
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
      <a-row class="row-gap">
        <a-col :span="12">
          <div class="flex-feature">
            <img
              :src="relolveImgPath('/img/keshihua.png')"
              class="img-feature"
              alt="图片"
            />
            <div>
              <span class="title"
                >丰富、专业的GIS分析工具和更直观的空间展示</span
              >
              <div class="sub-title">
                包括空间大数据的存储管理、空间分析、流数据处理与可视化等技术，致力于提供全面支持大数据的GIS基础软件与服务；基于IT大数据技术重构传统GIS，支持海量经典空间数据的分布式存储、处理与分析，实现数量级的性能提升。
              </div>
            </div>
          </div>
        </a-col>
        <a-col :span="12">
          <div class="flex-feature">
            <img
              :src="relolveImgPath('/img/kuaisufenxi.png')"
              class="img-feature"
              alt="图片"
            />
            <div>
              <span class="title"
                >高效便捷的数据统计、分析，一键式成果下载</span
              >
              <div class="sub-title">
                对数据进行在线空间分析，包括缓冲区、插值、叠加、泰森多边形、提取等值线/面等分析。支持对各类成果的下载，通过二维码或链接进行分享。
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
      <div id="case" class="big-itle">产品案例</div>
      <div class="flex">
        <div
          v-for="(item, index) in cases"
          :key="index"
          class="flex-vertical"
          @click="() => updateActiveCaseIndex(index)"
        >
          <img :src="item.img1" class="item img-case" alt="图片" />
          <span class="item">{{ item.h1 }}</span>
        </div>
      </div>
      <div class="flex" style="justify-content: center">
        <img :src="activeCase.img2" style="width: 860px; height: 460px" />
        <div style="margin-left: 15px">
          <h3 style="margin: 15px 0">{{ activeCase.h2 }}</h3>
          <h4 style="margin: 15px 0 30px">{{ activeCase.h3 }}</h4>
          <div style="width: 400px; margin-bottom: 60px">
            {{ activeCase.desc }}
          </div>
          <div style="text-align: right">
            <a :href="activeCase.link">查看详细</a>
          </div>
        </div>
      </div>
      <help-link />
    </div>
  </div>
</template>
<script>
import HelpLink from './components/HelpLink'
export default {
  components: {
    HelpLink,
  },
  data() {
    return {
      items: [
        this.relolveImgPath('/img/ScrollChart.png'),
        this.relolveImgPath('/img/servicemanager.png'),
        this.relolveImgPath('/img/bigdata.png'),
        this.relolveImgPath('/img/scroll_safe.png'),
      ],
      activeCaseIndex: 0,
      cases: [
        {
          h1: 'GAF应用案例一',
          img1: this.relolveImgPath('/img/land_menu.png'),
          h2: 'GAF应用案例一',
          img2: this.relolveImgPath('/img/landmanager.png'),
          h3: '超图国土调查数据库管理系统',
          desc:
            '系统基于超图云服务器和云端一体化体系架构，实现了云GIS服务与各种智能终端的一体化协同。共享分析软件作为数据的提供者，把土地利用现状数据实时共享给协同部门。便于协同部门实时获取数据，实现国土资源部门与协同部门之间数据的实时互通、以及与住建、农业、林业等部门的信息共享和业务联动。极大的提高信息资源利用率，避免在信息采集、存储和管理上的重复与浪费。',
          link: '/',
        },
        {
          h1: 'GAF应用案例二',
          img1: this.relolveImgPath('/img/smartcity_meun.png'),
          h2: 'GAF应用案例二',
          img2: this.relolveImgPath('/img/landmanager.png'),
          h3: '超图国土调查数据库管理系统',
          desc:
            '系统基于超图云服务器和云端一体化体系架构，实现了云GIS服务与各种智能终端的一体化协同。共享分析软件作为数据的提供者，把土地利用现状数据实时共享给协同部门。便于协同部门实时获取数据，实现国土资源部门与协同部门之间数据的实时互通、以及与住建、农业、林业等部门的信息共享和业务联动。极大的提高信息资源利用率，避免在信息采集、存储和管理上的重复与浪费。',
          link: '/',
        },
        {
          h1: 'GAF应用案例三',
          img1: this.relolveImgPath('/img/release_menu.png'),
          h2: 'GAF应用案例三',
          img2: this.relolveImgPath('/img/landmanager.png'),
          h3: '超图国土调查数据库管理系统',
          desc:
            '系统基于超图云服务器和云端一体化体系架构，实现了云GIS服务与各种智能终端的一体化协同。共享分析软件作为数据的提供者，把土地利用现状数据实时共享给协同部门。便于协同部门实时获取数据，实现国土资源部门与协同部门之间数据的实时互通、以及与住建、农业、林业等部门的信息共享和业务联动。极大的提高信息资源利用率，避免在信息采集、存储和管理上的重复与浪费。',
          link: '/',
        },
        {
          h1: 'GAF应用案例四',
          img1: this.relolveImgPath('/img/zhihuiyuanqu_menu.png'),
          h2: 'GAF应用案例四',
          img2: this.relolveImgPath('/img/landmanager.png'),
          h3: '超图国土调查数据库管理系统',
          desc:
            '系统基于超图云服务器和云端一体化体系架构，实现了云GIS服务与各种智能终端的一体化协同。共享分析软件作为数据的提供者，把土地利用现状数据实时共享给协同部门。便于协同部门实时获取数据，实现国土资源部门与协同部门之间数据的实时互通、以及与住建、农业、林业等部门的信息共享和业务联动。极大的提高信息资源利用率，避免在信息采集、存储和管理上的重复与浪费。',
          link: '/',
        },
      ],
    }
  },
  computed: {
    activeCase() {
      return this.cases[this.activeCaseIndex]
    },
  },
  mounted() {
    anchorLinkFixed('anchorLink')
  },
  methods: {
    relolveImgPath(img) {
      const isDev = this.$store.state.isDev
      return isDev ? img : `.${img}`
    },
    bg(item) {
      return `background-image: url(${item})`
    },
    updateActiveCaseIndex(index) {
      this.activeCaseIndex = index
    },
    navPos(id) {
      document.getElementById(id).scrollIntoView()
    },
  },
}
function anchorLinkFixed(id) {
  const obj = document.getElementById(id)
  const _getHeight = obj.offsetTop
  window.onscroll = function () {
    changePos(id, _getHeight)
  }
}

function changePos(id, height) {
  const obj = document.getElementById(id)
  const scrollTop =
    document.documentElement.scrollTop || document.body.scrollTop
  if (scrollTop < height) {
    obj.style.position = 'relative'
    obj.classList.remove('anchor-link-bg')
  } else {
    obj.style.position = 'fixed'
    obj.classList.add('anchor-link-bg')
  }
}
</script>
<style scoped>
.gaf-container {
  width: 90%;
  margin: 0 auto;
}
.row-gap {
  margin-bottom: 60px;
}
.img-item {
  background-size: cover;
  height: 300px;
  width: 100%;
}
.anchor-link {
  background-color: #eeeeee;
  display: flex;
  justify-content: center;
  left: 0;
  top: 0;
  right: 0;
  width: 100%;
  z-index: 1;
  line-height: 50px;
  height: 50px;
}
.anchor-link > a {
  text-decoration: none;
  color: inherit;
  width: 80px;
  text-align: center;
  cursor: pointer;
}
.anchor-link > a.active {
  color: #3498db;
  border-bottom: 1px solid #3498db;
}
.img-feature {
  width: 64px;
  height: 64px;
  margin: 0 15px;
  box-sizing: border-box;
}
.img-case {
  width: 64px;
  height: 64px;
}
.big-itle {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin: 80px 0 40px;
}
.flex-feature {
  display: flex;
  margin-bottom: 45px;
}
.flex {
  display: flex;
  justify-content: space-around;
  margin-bottom: 45px;
}
.flex-vertical {
  display: flex;
  flex-direction: column;
  width: 22.5%;
  justify-content: center;
  align-items: center;
}
.item {
  margin-bottom: 15px;
}
.divider:not(:last-child) {
  border-right: 1px solid #dddddd;
  padding-right: 80px;
}
.title {
  margin-bottom: 10px;
  font-size: 15px;
  font-weight: bold;
  display: block;
}
.sub-title {
  font-size: 13px;
  margin-right: 200px;
  display: block;
}
</style>

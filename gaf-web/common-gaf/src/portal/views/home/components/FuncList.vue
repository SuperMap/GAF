<template>
  <div>
    <a-row v-for="(group, gIndex) in groups" :key="gIndex">
      <a-col :span="24">
        <a-row :gutter="50">
          <a-col v-for="n in group" :key="n.name" :span="6">
            <div
              class="func-item"
              @click="(e) => updateName(e, n.name, gIndex)"
            >
              <span :class="n.icon" />
              {{ n.title }}
            </div>
          </a-col>
        </a-row>
        <div class="group-item">
          <a-row v-for="(routes, idx) in cRoutes" :key="idx" class="row-gap">
            <a-col v-for="r in routes" :key="r.name" :span="10" :offset="1">
              <a
                v-if="r.target"
                :href="r.embedUrl"
                class="flex"
                target="_blank"
              >
                <div class="flex-vertical">
                  <span class="title">{{ r.title }}</span>
                  <span>{{ r.desc }}</span>
                </div>
                <span class="el-icon-arrow-right link"></span>
              </a>
              <nuxt-link v-else :to="r.path" class="flex">
                <div class="flex-vertical">
                  <span class="title">{{ r.title }}</span>
                  <span>{{ r.desc }}</span>
                </div>
                <span class="el-icon-arrow-right link"></span>
              </nuxt-link>
            </a-col>
          </a-row>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
export default {
  name: 'FuncList',
  data() {
    return {
      name: '',
    }
  },
  computed: {
    groups() {
      let start = 0
      let end = 4
      const ns = this.$store.getters.groupNavs
      const groups = []
      while (start < ns.length) {
        groups.push(ns.slice(start, end))
        start += 4
        end += 4
      }
      return groups
    },
    cRoutes() {
      const navs = this.$store.getters.groupNavs
      let ns = navs[0].children
      if (this.name) {
        ns = navs.find((n) => n.name === this.name).children
      }
      let start = 0
      let end = 2
      const routes = []
      while (start < ns.length) {
        routes.push(ns.slice(start, end))
        start += 2
        end += 2
      }
      return routes
    },
  },
  mounted() {
    this.initialize(0)
    const funcItem = document.querySelector('.func-item')
    if (funcItem) {
      funcItem.classList.add('active')
    }
  },
  methods: {
    updateName(e, name, gIndex) {
      this.name = name
      document
        .querySelectorAll('.func-item')
        .forEach((item) => item.classList.remove('active'))
      this.initialize(gIndex)
      e.currentTarget.classList.add('active')
    },
    initialize(gIndex) {
      document.querySelectorAll('.group-item').forEach((item, idx) => {
        if (gIndex === idx) {
          item.classList.remove('hidden')
        } else {
          item.classList.add('hidden')
        }
      })
    },
  },
}
</script>

<style scoped>
.row-gap {
  margin-top: 30px;
  margin-bottom: 45px;
}
.hidden {
  display: none;
  height: 0;
}
.group-item {
  transition: max-height 1s linear;
  background-color: #efefef;
  padding: 5px 0;
}
.func-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  padding: 5px;
}
.active {
  color: #00c1de;
}
.func-item > span {
  width: 48px;
  height: 48px;
  font-size: 48px;
  margin-bottom: 15px;
}
.flex {
  display: flex;
  flex-direction: row;
  padding: 5px;
  cursor: pointer;
  color: #000;
  text-decoration: none;
}
.flex:hover {
  background-color: #ffffff;
}
.flex:hover .link {
  display: block;
}
.link {
  height: 44px;
  line-height: 44px;
  display: none;
}
.flex-vertical {
  display: flex;
  flex-direction: column;
  flex: 1;
}
.title {
  font-size: 15px;
  font-weight: bold;
  margin-bottom: 5px;
}
</style>

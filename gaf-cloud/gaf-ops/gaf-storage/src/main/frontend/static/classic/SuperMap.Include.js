;(function() {
  const r = new RegExp('(^|(.*?\\/))(SuperMap.Include.js)(\\?|$)')
  const s = document.getElementsByTagName('script')
  let src
  let baseurl = ''
  for (let i = 0, len = s.length; i < len; i++) {
    src = s[i].getAttribute('src')
    if (src) {
      const m = src.match(r)
      if (m) {
        baseurl = m[1]
        break
      }
    }
  }
  function inputScript(inc) {
    const script =
      '<' +
      'script type="text/javascript" src="' +
      inc +
      '"' +
      '><' +
      '/script>'
    document.writeln(script)
  }
  function inputCSS(style) {
    const css =
      '<' +
      'link rel="stylesheet" href="' +
      baseurl +
      '../theme/default/' +
      style +
      '"' +
      '><' +
      '/>'
    document.writeln(css)
  }
  // 加载类库资源文件
  function loadSMLibs() {
    inputScript(baseurl + 'SuperMap-8.1.1-17226.js')
    if (!window.excludePlot) {
      inputScript(baseurl + 'SuperMap_Plot-8.1.1-17226.js')
    }
    loadLocalization()
    inputCSS('style.css')
    inputCSS('google.css')
  }
  // 引入汉化资源文件
  function loadLocalization() {
    let userLang
    // 针对不通浏览器做语言浏览器做判断
    if (navigator.userLanguage) {
      // 针对IE浏览器
      userLang = navigator.userLanguage
    } else if (navigator.languages) {
      // 针对Chrome
      userLang = navigator.languages[0]
    } else {
      // 其他
      userLang = navigator.language
    }
    if (userLang.indexOf('zh') > -1) {
      inputScript(baseurl + 'Lang/zh-CN.js')
    } else {
      inputScript(baseurl + 'Lang/en.js')
    }
  }
  loadSMLibs()
  loadLocalization()
})()

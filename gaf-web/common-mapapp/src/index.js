// @ts-nocheck
/**
 * Created by hyt on 2021/04/09
 */
import pkg from "../package";

// 地图应用
import WebgisBasicApp from './mapapp/pages/WebgisBasicApp'

// 叠加分析
import OverlayAnalysis from './mapapp/pages/OverlayAnalysis'


const components = [
    // 地图应用
    WebgisBasicApp,

    // 叠加分析
    OverlayAnalysis,
];

const install = function(Vue) {
    components.forEach((component) => {
        Vue.component(component.name, component);
    });
};

/* istanbul ignore if */
if (typeof window !== "undefined" && window.Vue) {
    install(window.Vue);
}

export {
    // 地图应用
    WebgisBasicApp,

    // 叠加分析
    OverlayAnalysis,
};

export default {
    version: pkg.version,
    install,
};
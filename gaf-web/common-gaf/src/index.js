// @ts-nocheck
/**
 * Created by huanl on 2020/12/01
 */
import pkg from "../package";

// project 工程
import CodeGenerate from './project/pages/CodeGenerate'
import codeTemplate from './project/pages/codeTemplate'
import projects from './project/pages/projects'
import projGroup from './project/pages/projGroup'

// portal 权限
import portalCustom from './portal/pages/customization'
import menus from './portal/pages/menus'
import portalLogin from './portal/pages/portallogin'

// workflow 工作流
import GafAcceptBox from './workflow/pages/GafAcceptBox'
import GafAcceptSon from './workflow/pages/GafAcceptSon'
import GafAllBusiness from './workflow/pages/GafAllBusiness'
import GafAssistanceSon from './workflow/pages/GafAssistanceSon'
import GafAssistantBox from './workflow/pages/GafAssistantBox'
import GafDoingBox from './workflow/pages/GafDoingBox'
import GafDoingSon from './workflow/pages/GafDoingSon'
import GafDoneBox from './workflow/pages/GafDoneBox'
import GafHandleBox from './workflow/pages/GafHandleBox'
import GafHandleSon from './workflow/pages/GafHandleSon'
import GafHangingBox from './workflow/pages/GafHangingBox'
import GafHangingSon from './workflow/pages/GafHangingSon'
import GafNeedDealt from './workflow/pages/GafNeedDealtBox'
import GafNeedSon from './workflow/pages/GafNeedSon'


const components = [
    // project 工程
    CodeGenerate, codeTemplate, projects, projGroup,
    
    //  portal 权限
    portalCustom, menus ,portalLogin,

    // workflow 工作流
    GafAcceptBox, GafAcceptSon, GafAllBusiness, GafAssistanceSon, GafAssistantBox, GafDoingBox, 
    GafDoingSon, GafDoneBox, GafHandleBox, GafHandleSon, GafHangingBox, GafHangingSon, GafNeedDealt,
    GafNeedSon,
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
    // project 工程
    CodeGenerate, codeTemplate, projects, projGroup,
    
    //portal 权限
    portalCustom,  menus, portalLogin,

    // workflow 工作流
    GafAcceptBox, GafAcceptSon, GafAllBusiness, GafAssistanceSon, GafAssistantBox, GafDoingBox, 
    GafDoingSon, GafDoneBox, GafHandleBox, GafHandleSon, GafHangingBox, GafHangingSon, GafNeedDealt,
    GafNeedSon,
};

export default {
    version: pkg.version,
    install,
};
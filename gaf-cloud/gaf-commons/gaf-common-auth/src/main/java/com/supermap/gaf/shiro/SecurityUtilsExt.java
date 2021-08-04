package com.supermap.gaf.shiro;

import java.util.ArrayList;
import java.util.List;

import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.commontype.AuthResourceModule;
import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.commontype.AuthUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.pac4j.core.profile.CommonProfile;

import com.supermap.gaf.shiro.commontypes.ShiroUser;

import io.buji.pac4j.subject.Pac4jPrincipal;

/**
 * <p>
 * shiro扩展，直接获取当前登录用户信息
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @since 1.0.0
 */
public abstract class SecurityUtilsExt {
//    @Autowired(required = false)
//    private static ITenantService tenantService;

    public static String SHIRO_USER = "shiroUserKey";

    public static ShiroUser getUser() {
        Subject subject = ThreadContext.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            //不快速获取shiro的session
            try {
                if (getSessionQuietly(subject) != null) {
                    return (ShiroUser) subject.getSession().getAttribute(SHIRO_USER);
                }
            } catch (InvalidSessionException e) {
                subject.logout();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    return (ShiroUser) ThreadContext.get(SecurityUtilsExt.SHIRO_USER);
                } catch (Exception e1) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            if (getSessionQuietly(subject) != null) {
                subject.getSession().removeAttribute(SHIRO_USER);
                subject.logout();
            }
            ThreadContext.remove(SHIRO_USER);
        }
        return null;
    }


    public static String getJWTToken() {
        Subject subject = ThreadContext.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            if (subject.getPrincipal() instanceof Pac4jPrincipal) {
                Pac4jPrincipal pri = (Pac4jPrincipal) subject.getPrincipal();
                if (pri.getProfile().getAttribute("access_token") != null) {
                    return (String) pri.getProfile().getAttribute("access_token").toString();
                }
            }
        }
        return StringUtils.EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static void recordKeycloakUser(CommonProfile profile) {
        if (profile != null) {
            ShiroUser user = new ShiroUser();
            user.setUsername(profile.getUsername());
            user.setRealName((String) profile.getAttribute("realName"));
            if (StringUtils.isEmpty(user.getRealName())) {
                user.setRealName(user.getUsername());
            }
            user.setRoles(new ArrayList<>());
            if (CollectionUtils.isNotEmpty(profile.getRoles())) {
                user.getRoles().addAll(profile.getRoles());
            }
            user.setPermerssions(new ArrayList<>());
            if (CollectionUtils.isNotEmpty(profile.getPermissions())) {
                user.getPermerssions().addAll(profile.getPermissions());
            }
            user.setGroups(new ArrayList<>());
            if (CollectionUtils.isNotEmpty((List<String>) profile.getAttribute(GroupGenerator.CURRENT_GROUP_KEY))) {
                user.getGroups().addAll((List<String>) profile.getAttribute(GroupGenerator.CURRENT_GROUP_KEY));
            }
            user.setSysRoles(new ArrayList<>());
            if (CollectionUtils.isNotEmpty((List<String>) profile.getAttribute(RoleGenerator.CURRENT_SYS_ROLES_KEY))) {
                user.getSysRoles().addAll((List<String>) profile.getAttribute(RoleGenerator.CURRENT_SYS_ROLES_KEY));
            }
            user.setId(profile.getId());

            if (getSessionQuietly(SecurityUtils.getSubject()) != null) {
                SecurityUtils.getSubject().getSession().setAttribute(SecurityUtilsExt.SHIRO_USER, user);
            }
            ThreadContext.put(SecurityUtilsExt.SHIRO_USER, user);
        }
    }

    @SuppressWarnings("unchecked")
    public static void recordKeycloakUser(CommonProfile profile, AuthUser authUser, List<AuthResourceApi> authResourceApis, List<AuthResourceModule> authResourceModules, List<AuthRole> authRoles) {
        if (profile != null) {
            ShiroUser user = new ShiroUser();
            user.setUsername(authUser.getName());
            user.setRealName(authUser.getRealName());
            user.setRoles(new ArrayList<>());
            user.setPermerssions(new ArrayList<>());
            user.setGroups(new ArrayList<>());
            user.setSysRoles(new ArrayList<>());
            user.setId(authUser.getUserId());

            //添加新权限内容
            user.setAuthUser(authUser);
            user.setAuthResourceApis(authResourceApis);
            user.setAuthResourceModules(authResourceModules);
            user.setAuthRoles(authRoles);
            user.setTenantId(authUser.getTenantId());

            if (getSessionQuietly(SecurityUtils.getSubject()) != null) {
                SecurityUtils.getSubject().getSession().setAttribute(SecurityUtilsExt.SHIRO_USER, user);
            }
            ThreadContext.put(SecurityUtilsExt.SHIRO_USER, user);
        }
    }

    private static Session getSessionQuietly(Subject subject) {
        try {
            return subject != null ? subject.getSession() : null;
        } catch (Exception e) {
        }
        return null;
    }
}

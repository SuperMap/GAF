package com.supermap.gaf.storage.filter;

import com.supermap.gaf.storage.entity.Permission;
import com.supermap.gaf.storage.config.StorageCustomConfig;
import com.supermap.gaf.storage.dao.StoragePermissionMapper;
import com.supermap.gaf.storage.enums.PermissionType;
import com.supermap.gaf.storage.utils.StorageCommonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoragePermissionFilter implements Filter {
    private static final String TEMPLATE = "%s %s";
    private static final Pattern UPLOAD_PATTERN = Pattern.compile("^(get|post|put) /api/([^/]+)/([^/]+)/(metadata|complete-multi-upload|multi-upload|upload|create-empty-dir)/(.+)");
    private static final Pattern QUERY_PATTERN = Pattern.compile("^(get) /api/([^/]+)/([^/]+)/(list-objects)/(.*)");
    private static final Pattern SHARE_PATTERN = Pattern.compile("^(get) /api/([^/]+)/([^/]+)/(metadata|share)/(.+)");
    private static final Pattern DOWNLOAD_PATTERN = Pattern.compile("^(get) /api/([^/]+)/([^/]+)/(download)/(.+)");
    private static final Pattern DELETE_PATTERN = Pattern.compile("^(delete) /api/([^/]+)/([^/]+)(/)(.+)");

    private static final Pattern METADATA_PATTERN = Pattern.compile("^(get) /api/([^/]+)/([^/]+)/(metadata)/(.+)");

    private static final Pattern VOLUME_PATH_PATTERN = Pattern.compile("^(get) /api/([^/]+)/([^/]+)/(volume-path)/(.*)");
    private static final int CONFIGNAME_GROUP_INDEX = 3;
    private static final int PATH_GROUP_INDEX = 5;

    private StoragePermissionMapper storagePermissionMapper;

    public StoragePermissionFilter(StoragePermissionMapper storagePermissionMapper) {
        this.storagePermissionMapper = storagePermissionMapper;
    }
   
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String owersString = request.getHeader(StorageCustomConfig.PERMISSION_HEADER);
        String relativePath = request.getPathInfo();
        String value = String.format(TEMPLATE,request.getMethod().toLowerCase(),relativePath);
        if(VOLUME_PATH_PATTERN.matcher(value).matches()){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        if(StringUtils.isEmpty(owersString)){
            response.setStatus(403);
            return;
        }
        Set<String> owers = StorageCommonUtils.split(owersString,true,true);
        if(owers.isEmpty()){
            response.setStatus(403);
            return;
        }else if(owers.contains(StorageCustomConfig.SUPER_OWER)){
            // 超级用户放行
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }


        List<PermissionType> needPermission = new ArrayList<>();
        String configName;
        String path;
        Matcher m;
        if((m = UPLOAD_PATTERN.matcher(value)).matches()){
            needPermission.add(PermissionType.upload);
            if(METADATA_PATTERN.matcher(value).matches()){
                needPermission.add(PermissionType.share);
            }
        }else if((m = QUERY_PATTERN.matcher(value)).matches()){
            needPermission.add(PermissionType.query);
        }else if((m = SHARE_PATTERN.matcher(value)).matches()){
            needPermission.add(PermissionType.share);
        }else if((m = DOWNLOAD_PATTERN.matcher(value)).matches()){
            needPermission.add(PermissionType.download);
        }else if((m = DELETE_PATTERN.matcher(value)).matches()){
            needPermission.add(PermissionType.delete);
        }else{
            response.setStatus(403);
            return;
        }
        configName = m.group(CONFIGNAME_GROUP_INDEX);
        path = m.group(PATH_GROUP_INDEX)==null?"":m.group(PATH_GROUP_INDEX);
        List<Permission> permissions = storagePermissionMapper.selectByOwersAndConfigName(configName,owers);
        Set<PermissionType> hasPermissions = new HashSet<>();
        for(Permission item:permissions){
            if(path.startsWith(item.getResource())){
                for(String scopeItem:item.getScope().split(",")){
                    try{
                        hasPermissions.add(PermissionType.valueOf(scopeItem));
                    }catch (Exception e){}
                }
            }
        }
        if(hasPermissions.isEmpty()){
            response.setStatus(403);
            return;
        }
        boolean passable = false;
        for(PermissionType permissionType:needPermission){
            if(hasPermissions.contains(permissionType)){
                passable = true;
                break;
            }
        }
        if(passable){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }else{
            response.setStatus(403);
            return;
        }
    }



}

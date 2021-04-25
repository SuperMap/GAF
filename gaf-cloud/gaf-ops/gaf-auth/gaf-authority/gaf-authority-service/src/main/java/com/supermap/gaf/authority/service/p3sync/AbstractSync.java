package com.supermap.gaf.authority.service.p3sync;

import com.supermap.gaf.authority.commontype.AuthP3MappingRule;
import com.supermap.gaf.authority.commontype.P3SyncResult;
import com.supermap.gaf.authority.service.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 同步数据抽象方法，pull\push
 * @author yd
 * @since : 2020-11-26
 *
 */
public abstract class AbstractSync {

    public AuthP3TenantMappingService authP3TenantMappingService;
    public AuthP3DepartmentMappingService authP3DepartmentMappingService;
    public AuthP3UserMappingService authP3UserMappingService;

    public AuthTenantService authTenantService;
    public AuthDepartmentService authDepartmentService;
    public AuthUserService authUserService;

    public RestTemplate restTemplate;

    public AbstractSync(AuthP3TenantMappingService authP3TenantMappingService, AuthP3DepartmentMappingService authP3DepartmentMappingService, AuthP3UserMappingService authP3UserMappingService, AuthTenantService authTenantService, AuthDepartmentService authDepartmentService, AuthUserService authUserService, RestTemplate restTemplate) {
        this.authP3TenantMappingService = authP3TenantMappingService;
        this.authP3DepartmentMappingService = authP3DepartmentMappingService;
        this.authP3UserMappingService = authP3UserMappingService;
        this.authTenantService = authTenantService;
        this.authDepartmentService = authDepartmentService;
        this.authUserService = authUserService;
        this.restTemplate = restTemplate;
    }

    /**
     * 从第三方获取资源
     *
     * @param authP3MappingRule 规则信息
     * @param search            模糊查询值
     * @return 返回查询结果
     */
    public abstract List<P3SyncResult> pull(AuthP3MappingRule authP3MappingRule, String search);

    /**
     * 向第三方推送资源
     * 方法内部得到推送结果后直接把映射关系入库
     *
     * @param authP3MappingRule 规则信息
     * @param gafResourceId     gaf原始资源id
     * @return 返回推送结果
     */
    public abstract boolean push(AuthP3MappingRule authP3MappingRule, String gafResourceId);

}

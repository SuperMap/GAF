/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.service.AuthTenantService;
import com.supermap.gaf.authority.vo.TenantInitVo;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.ShiroUser;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.storage.service.MinioConfigHandlerI;
import com.supermap.gaf.storage.service.S3ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 租户接口
 * @date:2021/3/25
 * @author zhm
 */
@Component
@Api(value = "租户接口")
public class AuthTenantResource{
    @Autowired
    private MinioConfigHandlerI minioConfigHandlerI;
    @Autowired
    private S3ClientService s3ClientService;

    private final AuthTenantService authTenantService;

    public AuthTenantResource(AuthTenantService authTenantService) {
        this.authTenantService = authTenantService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/s3-config")
    public MessageResult<String> getVolumeConfig() throws AuthenticationException {
        String configIni = minioConfigHandlerI.getConfigIni();
        if(StringUtils.isEmpty(configIni)){
            return MessageResult.failed(String.class).status(200).message("未找到有关配置").build();
        }
        s3ClientService.initBucket();
        return MessageResult.data(configIni).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "查询当前用户所属租户")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/current-user")
    public MessageResult<AuthTenant> getTenantsWithCurrentUser() {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        if (shiroUser == null){
            throw new GafException("查询租户时，此用户不存在");
        }
        String tenantId = shiroUser.getTenantId();
        if (StringUtils.isEmpty(tenantId)){
            throw new GafException("查询不到此用户的租户信息");
        }
        return MessageResult.data(authTenantService.getById(tenantId)).build();
    }

    @ApiOperation(value = "查询租户", notes = "根据id查询租户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "path", dataType = "string", required = true)
    })
	@GET
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{tenantId}")
    public MessageResult<AuthTenant> getById(@PathParam("tenantId")String tenantId){
		return MessageResult.data(authTenantService.getById(tenantId)).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "查询租户下的所有管理员", notes = "根据租户id查询租户下的所有管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("all-admin/{tenantId}")
    public MessageResult<List<AuthUser>> getAllAdmin(@PathParam("tenantId")String tenantId){
        return MessageResult.data(authTenantService.getAllAdmin(tenantId)).message("查询成功").build();
    }

    @ApiOperation(value = "条件查询租户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchFieldName", value = "模糊查询字段名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "searchFieldValue", value = "模糊查询字段值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderFieldName", value = "排序字段值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderMethod", value = "排序方式。升序为ASC,降序为DESC。默认不排序",allowableValues="ASC,DESC", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[1,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range(0,infinity]", paramType = "query", dataType = "integer"),
    })
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Map<String,Object>> pageList(@QueryParam("searchFieldName")String searchFieldName,
										@QueryParam("searchFieldValue")String searchFieldValue,
										@QueryParam("orderFieldName")String orderFieldName,
										@QueryParam("orderMethod")String orderMethod,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Map<String,Object> result = authTenantService.tenantSearch(searchFieldName,searchFieldValue,orderFieldName,orderMethod,pageNum,pageSize);
		return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "初始化租户", notes = "初始化租户即新增租户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantInitVo", value = "租户初始化信息", dataTypeClass = TenantInitVo.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/init")
    public MessageResult<Void> initAuthTenant(TenantInitVo tenantInitVo){
	    authTenantService.initAuthTenant(tenantInitVo);
        return MessageResult.successe(Void.class).status(200).message("初始化租户成功").build();
    }

    @ApiOperation(value = "删除租户", notes = "根据id删除租户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{tenantId}")
    public MessageResult<Void> deleteAuthTenant(@PathParam("tenantId")String tenantId){
        authTenantService.deleteAuthTenant(tenantId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "禁用或启用租户", notes = "查询参数status表示启用禁用状态, true表示启动, false表示禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "status", value = "启用禁用状态", allowableValues = "true,false",paramType = "query", dataType = "string", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{tenantId}")
    public MessageResult<Void> setAuthTenantStatus(@PathParam("tenantId")String tenantId,@QueryParam("status") String status){
        authTenantService.setAuthTenantStatus(tenantId,Boolean.parseBoolean(status));
        return MessageResult.successe(Void.class).status(200).message("操作成功").build();
    }

    @ApiOperation(value = "批量删除租户", notes = "根据id集合批量删除租户")
	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> tenantIds){
        authTenantService.batchDelete(tenantIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新租户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authTenant", value = "租户", dataTypeClass = AuthTenant.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{tenantId}")
    public MessageResult<Void> updateAuthTenant(AuthTenant authTenant,@PathParam("tenantId")String tenantId){
        authTenant.setTenantId(tenantId);
        authTenantService.updateAuthTenant(authTenant);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }
}

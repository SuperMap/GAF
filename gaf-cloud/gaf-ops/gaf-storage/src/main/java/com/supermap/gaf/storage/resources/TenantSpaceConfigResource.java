package com.supermap.gaf.storage.resources;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.storage.entity.ConfigName;
import com.supermap.gaf.storage.entity.SpaceConfig;
import com.supermap.gaf.storage.entity.vo.SpaceConfigSelectVo;
import com.supermap.gaf.storage.service.TenantSpaceConfigService;
import com.supermap.gaf.storage.utils.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

/**
 * 接口
 *
 * @author zrc
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "租戶私有存储配置接口")
public class TenantSpaceConfigResource {
    @Autowired
    private TenantSpaceConfigService tenantSpaceConfigService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @Path("/{id}")
    public MessageResult<SpaceConfig> getById(@PathParam("id") String id) {
        return MessageResult.data(tenantSpaceConfigService.getById(id)).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询", notes = "分页条件查询")
    public MessageResult<Page> pageList(@BeanParam SpaceConfigSelectVo spaceConfigSelectVo,
                                        @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                        @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        Page<SpaceConfig> page = tenantSpaceConfigService.listByPageCondition(spaceConfigSelectVo, pageNum, pageSize);
        return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "获取租户所有可用存储名称（包括平台赋予的）", notes = "获取租户所有可用存储名称（包括平台赋予的）")
    @Path("/all-names")
    public MessageResult<Collection<ConfigName>> getAllNames(@QueryParam("ower") String ower) {
        Collection<ConfigName> re = tenantSpaceConfigService.getAllNames(ower);
        return MessageResult.data(re).status(200).message("查询成功").build();
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增", notes = "新增")
    public MessageResult<Void> insertTenantServerConf(SpaceConfig spaceConfig) {
        tenantSpaceConfigService.insertTenantServerConf(spaceConfig);
        return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    @Path("/{id}")
    public MessageResult<Void> deleteTenantServerConf(@PathParam("id") String id) {
        tenantSpaceConfigService.deleteTenantServerConf(id);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除", notes = "批量删除")
    public MessageResult<Void> batchDelete(List<String> ids) {
        tenantSpaceConfigService.batchDelete(ids);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }


    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新", notes = "根据id更新")
    @Path("/{id}")
    public MessageResult<Void> updateTenantServerConf(SpaceConfig spaceConfig, @PathParam("id") String id) {
        spaceConfig.setId(id);
        tenantSpaceConfigService.updateTenantServerConf(spaceConfig);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }


}

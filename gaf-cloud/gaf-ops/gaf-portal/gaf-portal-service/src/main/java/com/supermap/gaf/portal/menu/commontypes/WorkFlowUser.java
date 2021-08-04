package com.supermap.gaf.portal.menu.commontypes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * @author:yw
 * @Date 2021-3-12
 * 工作流用户
 **/

public final class WorkFlowUser implements Serializable {

    public WorkFlowUser() {
    }

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String staffId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String staffName;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("")
    private String areacode;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private String userType;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

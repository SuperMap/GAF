package com.supermap.gaf.data.mgt.entity.vo;

import com.supermap.gaf.data.mgt.entity.MmPhysics;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物理表详情
 * @author wxl
 * @since 2021/8/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("物理表详情")
public class MmPhysicsVO  extends MmPhysics {

    @ApiModelProperty("数据源名称")
    private String dsName;

    @ApiModelProperty("地址。数据库类型格式应该是ip(或域名):端口,文件类型应该是文件路径")
    private String addr;

    @ApiModelProperty("数据库名称")
    private String dbName;

    @ApiModelProperty(value = "数据源类型。选自数据字典", example = "udb",
            allowableValues = "1.文件型-1:udb,2:udbx…\n2.数据库型-1.PostgreSQL,2.MySQL,3.Hbase,4.MongoDB…")
    private String typeCode;

    @ApiModelProperty("是否空间数据库。true:是，false:否")
    private Boolean isSdx;
    @ApiModelProperty("是否空间数据库模板。true:是，false:否")
    private Boolean isTemplate;


}

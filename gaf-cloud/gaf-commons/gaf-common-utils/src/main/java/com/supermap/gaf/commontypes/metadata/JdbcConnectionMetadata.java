package com.supermap.gaf.commontypes.metadata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author:yw
 * @Date 2021/3/17
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("JDBC连接元数据信息")
public class JdbcConnectionMetadata {
    @ApiModelProperty(value = "database")
    private String database;
    @ApiModelProperty(value = "schema")
    private String schema;
    @ApiModelProperty(value = "databaseType")
    private String databaseType;
    @ApiModelProperty(value = "databaseVersion")
    private String databaseVersion;
}

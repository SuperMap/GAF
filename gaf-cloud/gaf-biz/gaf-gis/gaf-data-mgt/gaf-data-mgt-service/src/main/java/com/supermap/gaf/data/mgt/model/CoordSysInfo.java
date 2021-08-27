package com.supermap.gaf.data.mgt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 坐标系信息
 * 含编码、名称、类型(平面、地理、投影)、epsg code
 *
 * @author wxl
 * @since 2021/8/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("坐标系信息.含编码、名称、类型(平面、地理、投影)、epsg code.其中编码值代表某坐标系，可用于其他需要传入坐标系参数的接口")
public class CoordSysInfo {
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("类型.参考com.supermap.data.GeoSpatialRefType类实例名,SPATIALREF_NONEARTH(平面坐标系) SPATIALREF_EARTH_LONGITUDE_LATITUDE(地理坐标系) SPATIALREF_EARTH_PROJECTION(投影坐标系)")
    private String coordSysInfoType;
    @ApiModelProperty("坐标系的epsgCode.部分坐标系,可能未找到epsg code配置,即为空,用0表示")
    private Integer epsgCode;

}

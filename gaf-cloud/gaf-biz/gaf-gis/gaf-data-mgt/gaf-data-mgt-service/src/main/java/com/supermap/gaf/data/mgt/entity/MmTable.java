/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.entity;

import com.supermap.gaf.annotation.ParentIdField;
import com.supermap.gaf.annotation.SortSnField;
import com.supermap.gaf.annotation.UpdatedTimeField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 逻辑
 * @author wxl 
 * @since yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("逻辑")
public class MmTable implements Serializable{
    @Id
    @NotNull
    @ApiModelProperty("主键")
    private String tableId;
    @NotNull
    @ApiModelProperty("逻辑表名称")
    private String tableName;
    @ParentIdField
    @NotNull
    @ApiModelProperty("模型id")
    private String modelId;
    @ApiModelProperty(value = "空间数据源sdx数据集信息。\n" +
            "建空间模型的逻辑表对应空间数据源的数据集。在新增空间模型的新增逻辑表时需要填写对应数据集的额外参数设置信息。\n" +
            "该信息以JSON字符串格式传入.具体可以设置参数分为几个大类：DatasetGridInfo(栅格数据集信息) DatasetImageInfo(影像数据集信息) \n" +
            "DatasetVectorInfo(矢量数据集信息) DatasetTopologyInfo(拓扑数据集信息) DatasetVolumeInfo (体数据集信息) 镶嵌数据集信息(可以设置name名字和空间坐标系epsg编码)\n" +
            "以DatasetVectorInfo(矢量数据集信息) 为例，JSON字符串的可设置参数如下:\n" +
            "// iobjects for java 在线帮助文档(以下简称iobjects帮助文档) 地址: http://support.supermap.com.cn/DataWarehouse/WebDocHelp/iObjectsJava/index.html\n" +
            "{\n" +
            "\t\"type\": \"REGION\", // 数据集类型 无论哪个大类都必须设置 具体值参考iobjects帮助文档搜索DatasetType自定义枚举类实例的名字 \n" +
            "\t// 主要有POINT(点)、LINE(线)、REGION(面)、POINT3D(点)、LINE3D(线)、REGION3D(面)、TEXT(文本)、CAD(复合)、TABULAR（纯属性）MODEL(模型)等属于矢量\n" +
            "\t\"encodeType\": \"NONE\", // 编码类型(可选)。 具体值参考iobjects帮助文档搜索EncodeType自定义枚举类实例的名字\n" +
            "\t\"fileCache\": false, // 是否使用文件形式的缓存(可选)\n" +
            "\t\"name\": \"xxx\" ,// 数据集的名字(可选)\n" +
            "\t\"prjCoordSys\": \"PCS_WGS_1984_UTM_1N\" // 指定格式的字符串(可选) \n" +
            "}\n" +
            "该JSON里的可设置项是通过参考iobjects帮助文档DatasetVectorInfo类的属性(将以set开头的方法名去掉set转为小驼峰)，再根据如下几条规则转换得到：\n" +
            "1. 遇到自定义枚举类型,取枚举实例的名字;\n" +
            "2. 遇到属性是对象,则传入该对象对应的json对象; \n" +
            "例如：\n" +
            "1. DatasetVectorInfo的属性encodeType (对应的setEncodeType(EncodeType value)方法) ,该属性为自定义枚举EncodeType,\n" +
            "则取EncodeType枚举实例的名字，转换后\"encodeType\": \"NONE\"。\n" +
            "2. DatasetGridInfo的属性bounds(地理范围) ，该属性是Rectangle2D对象类型，Rectangle2D对象类型有bottom left right top  属性，\n" +
            "则转换后 \"bounds\": {\"bottom\": -200 ,\"left\": -200, \"right\":200, \"top\":200}\n" +
            "除此之外坐标系比较特殊，通过指定格式的字符串来代表坐标系,例如: \"prjCoordSys\": \"PCS_WGS_1984_UTM_1N\"\n" +
            "其他的DatasetGridInfo DatasetImageInfo DatasetVolumeInfo DatasetTopologyInfo 可设置项类似。注意数据集类型type必须有, 其中GRID属于栅格， IMAGE属于影像 ，TOPOLOGY 属于拓扑 ,MOSAIC属于镶嵌，VOLUME属于体数据集，剩余的暂时归为矢量  \n" +
            "特殊的是，镶嵌数据集信息可设置参数： { \"type\": \"MOSAIC\",\"prjCoordSys\": 指定格式的坐标系字符串, \"name\": \"xxx\"(可选) }\n" +
            "\n" +
            "关于坐标系的特殊说明：\n" +
            "用指定格式的坐标系字符串来代表坐标系. 暂时不支持自定义坐标系\n" +
            "坐标系分为平面坐标系 地理坐标系 投影坐标系\n" +
            "坐标系字符串格式是\n" +
            "1. 平面坐标系：\n" +
            "PCS_NON_EARTH_${坐标系单位}, 坐标系单位有CENTIMETER(厘米) DECIMETER分米 FOOT英尺 INCH英寸 KILOMETER千米\n" +
            "METER米 MILE英里 MILIMETER毫米 ,具体可查看iobjects for java 在线帮助文档搜索Unit枚举类(com.supermap.data.Unit)\n" +
            "例如：PCS_NON_EARTH_METER 或者 PCS_NON_EARTH_MILE\n" +
            "2. 地理坐标系\n" +
            "PCS_EARTH_LONGITUDE_LATITUDE_${地理坐标系类型名}\n" +
            "地理坐标系类型名可参考iobjects for java 在线帮助文档 GeoCoordSysType 枚举类(com.supermap.data.GeoCoordSysType)\n" +
            "例如：GeoCoordSysType枚举类中的实例GCS_CHINA_2000,则得到地理坐标系字符串为PCS_EARTH_LONGITUDE_LATITUDE_GCS_CHINA_2000\n" +
            "3. 投影坐标系\n" +
            "可参考iobjects for java 在线帮助文档, 搜索PrjCoordSysType枚举类(com.supermap.data.PrjCoordSysType)\n" +
            "每个枚举实例名就代表投影坐标系,除了PCS_NON_EARTH  PCS_EARTH_LONGITUDE_LATITUDE PCS_USER_DEFINED\n" +
            "例如：PrjCoordSysType枚举类实例 PCS_WGS_1984_UTM_1N,则得到投影坐标系字符串为 PCS_WGS_1984_UTM_1N" +
            "" )
    private String sdxInfo;
    @SortSnField
    @ApiModelProperty("排序")
    private Integer sortSn;
    @ApiModelProperty("描述")
    private String description;
    /**
    * 默认值1：now()
    */
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @UpdatedTimeField
    @ApiModelProperty("更新时间")
    private Date updatedTime;
    @ApiModelProperty("更新人")
    private String updatedBy;
}
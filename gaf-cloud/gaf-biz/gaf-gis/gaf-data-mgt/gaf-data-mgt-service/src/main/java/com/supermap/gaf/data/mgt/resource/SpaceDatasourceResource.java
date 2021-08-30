/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.data.mgt.service.S3MCacheService;
import com.supermap.gaf.data.mgt.service.SpaceDatasourceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

/**
 * 空间数据源接口
 * @author wxl
 * @since 2021/7/27
 */
@Component
@Api(value = "空间数据源接口")
public class SpaceDatasourceResource {

    @Autowired
    private SpaceDatasourceService spaceDatasourceService;

    @Autowired
    private S3MCacheService s3mCacheService;

    @Path("/{datasourceId}/datasets/{datasetName}")
    public Class<DatasetResource> datasetResourceClass(){
        return DatasetResource.class;
    }

//    @ApiOperation(value = "获取所有所有已预定义坐标系信息", notes = "获取的坐标系数据为树形结构,分为两层,第一层为坐标系分组节点,分组名固定为常用坐标系、平面坐标系、地理坐标系、投影坐标系,第二层就是具体的坐标系信息节点。" +
//            "坐标系信息包含编码、名称、类型(平面、地理、投影)、epsg code" +
//            "")
//    @GET
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path("/list-coordsys")
//    public MessageResult<List<DefaultTreeNode>> listPrjCoordSys() {
//        List<DefaultTreeNode> treeList = new LinkedList<>();
//
//        DefaultTreeNode frequentlyUsedGroup = new DefaultTreeNode();
//        frequentlyUsedGroup.setLeaf(false);
//        frequentlyUsedGroup.setTitle("常用坐标系");
//        frequentlyUsedGroup.setKey("1");
//        frequentlyUsedGroup.setSortSn(1);
//        frequentlyUsedGroup.setParentId("0");
//        treeList.add(frequentlyUsedGroup);
//
//        DefaultTreeNode planerGroup = new DefaultTreeNode();
//        planerGroup.setLeaf(false);
//        planerGroup.setTitle("平面坐标系");
//        planerGroup.setKey("2");
//        planerGroup.setSortSn(2);
//        planerGroup.setParentId("0");
//        treeList.add(planerGroup);
//
//        DefaultTreeNode geoGroup = new DefaultTreeNode();
//        geoGroup.setLeaf(false);
//        geoGroup.setTitle("地理坐标系");
//        geoGroup.setKey("3");
//        geoGroup.setSortSn(3);
//        geoGroup.setParentId("0");
//        treeList.add(geoGroup);
//
//        DefaultTreeNode prjGroup = new DefaultTreeNode();
//        prjGroup.setLeaf(false);
//        prjGroup.setTitle("投影坐标系");
//        prjGroup.setKey("4");
//        prjGroup.setSortSn(4);
//        prjGroup.setParentId("0");
//        treeList.add(prjGroup);
//
//        List<CoordSysInfo> frequentlyUsed = PrjCoordSysUtil.listFrequentlyUsed();
//        TempSerialNumberGenerator tempSerialNumberGenerator = new TempSerialNumberGenerator(4,1);
//
//        List<DefaultTreeNode> frequentlyUsedNodes = new LinkedList<>();
//        for (int i = 0; i < frequentlyUsed.size(); i++) {
//            DefaultTreeNode treeNode = new DefaultTreeNode();
//            treeNode.setKey(String.valueOf(tempSerialNumberGenerator.nextInt()));
//            treeNode.setParentId("1");
//            treeNode.setSortSn(i+1);
//            treeNode.setLeaf(true);
//            CoordSysInfo coordSysInfo = frequentlyUsed.get(i);
//            treeNode.setTitle(coordSysInfo.getName());
//            treeNode.setUserObject(coordSysInfo);
//            frequentlyUsedNodes.add(treeNode);
//        }
//        frequentlyUsedGroup.setChildren(frequentlyUsedNodes);
//
//        List<CoordSysInfo> all = PrjCoordSysUtil.listPrjCoordSysInfo();
//        Map<String, List<CoordSysInfo>> groupMap = all.stream().collect(Collectors.groupingBy(CoordSysInfo::getCoordSysInfoType));
//
//        List<CoordSysInfo> planerCoordSysInfos = groupMap.get(GeoSpatialRefType.SPATIALREF_NONEARTH.name());
//        List<DefaultTreeNode> planerNodes = new LinkedList<>();
//        for (int i = 0; i < planerCoordSysInfos.size(); i++) {
//            CoordSysInfo planerInfo = planerCoordSysInfos.get(i);
//            DefaultTreeNode treeNode = new DefaultTreeNode();
//            treeNode.setKey(String.valueOf(tempSerialNumberGenerator.nextInt()));
//            treeNode.setParentId("2");
//            treeNode.setSortSn(i+1);
//            treeNode.setLeaf(true);
//            treeNode.setTitle(planerInfo.getName());
//            treeNode.setUserObject(planerInfo);
//            planerNodes.add(treeNode);
//        }
//        planerGroup.setChildren(planerNodes);
//
//        List<CoordSysInfo> geoCoordSysInfos = groupMap.get(GeoSpatialRefType.SPATIALREF_EARTH_LONGITUDE_LATITUDE.name());
//        List<DefaultTreeNode> geoNodes = new LinkedList<>();
//        for (int i = 0; i < geoCoordSysInfos.size(); i++) {
//            CoordSysInfo geoInfo = geoCoordSysInfos.get(i);
//            DefaultTreeNode treeNode = new DefaultTreeNode();
//            treeNode.setKey(String.valueOf(tempSerialNumberGenerator.nextInt()));
//            treeNode.setParentId("3");
//            treeNode.setSortSn(i+1);
//            treeNode.setLeaf(true);
//            treeNode.setTitle(geoInfo.getName());
//            treeNode.setUserObject(geoInfo);
//            geoNodes.add(treeNode);
//        }
//        geoGroup.setChildren(geoNodes);
//
//        List<CoordSysInfo> coordSysInfos = groupMap.get(GeoSpatialRefType.SPATIALREF_EARTH_PROJECTION.name());
//        List<DefaultTreeNode> prjNodes = new LinkedList<>();
//        for (int i = 0; i < coordSysInfos.size(); i++) {
//            CoordSysInfo prjInfo = coordSysInfos.get(i);
//            DefaultTreeNode treeNode = new DefaultTreeNode();
//            treeNode.setKey(String.valueOf(tempSerialNumberGenerator.nextInt()));
//            treeNode.setParentId("4");
//            treeNode.setSortSn(i+1);
//            treeNode.setLeaf(true);
//            treeNode.setTitle(prjInfo.getName());
//            treeNode.setUserObject(prjInfo);
//            prjNodes.add(treeNode);
//        }
//        prjGroup.setChildren(prjNodes);
//
//        MessageResult<List<DefaultTreeNode>> mr = new MessageResult<>();
//        mr.setSuccessed(true);
//        mr.setData(treeList);
//        mr.setMessage("查询坐标系成功");
//        return mr;
//    }
//
//
//
//    @ApiOperation(value = "新建空的空间数据源", notes = "新建空的空间数据源")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "sysResourceDatasource", value = "数据源", dataTypeClass = SysResourceDatasource.class, paramType = "body",required = true)
//    })
//    @POST
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path(value = "/empty")
//    public MessageResult<SysResourceDatasource> createEmptySpaceDatasource(SysResourceDatasource sysResourceDatasource) {
//        if (spaceDatasourceService.checkNameRepeat(sysResourceDatasource.getDsName())) {
//            throw new IllegalArgumentException("数据源别名重复");
//        }
//        SysResourceDatasource datasource = spaceDatasourceService.createEmptySpaceDatasource(sysResourceDatasource);
//        return MessageResult.data(datasource).build();
//
//    }
//
//    @ApiOperation(value = "根据空间数据源模板创建空间数据源", notes = "根据空间数据源模板创建空间数据源，空间数据源也是空间数据源模板")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "templateToTargetVO", value = "数据源模板或数据源id 和目标数据源id", dataTypeClass = TemplateToTargetVO.class, paramType = "body",required = true),
//    })
//    @POST
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path(value = "/template/target")
//    public MessageResult<Void> createSpaceDatasourceByTemplate(TemplateToTargetVO templateToTargetVO) {
//        String templateId = templateToTargetVO.getTemplateId();
//        String targetId = templateToTargetVO.getTargetId();
//        spaceDatasourceService.createSpaceDatasourceByTemplate(templateId,targetId);
//        return MessageResult.successe(Void.class).build();
//
//    }
//
//
//    @ApiOperation(value = "数据导入", notes = "导入矢量、栅格、海图、三维、电信、COGO.\n" +
//            "具体能够导入的类型参考 iobjects 的在线帮助文档,http://support.supermap.com.cn/DataWarehouse/WebDocHelp/iObjectsJava/index.html\n" +
//            "找到程序员参考->概述,找到com.supermap.data.conversion包下的 ImportSetting 类,查看其直接子类,其子类则是可以导入的类型,\n" +
//            "某些导入类型只支持windows,所以这些导入类型该接口不支持。\n" +
//            "而需要传入的参数是json数组,具体参数详情需要参考iobjects的ImportSetting子类,并结合自定义的参数规则，具体详情查看参数说明\n"
//    )
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "importSettingJsonArray",
//                    value = "数据导入设置信息json数组.\n" +
//                            "json数组中的每个json对象都是对数据导入的设置.\n" +
//                            "每个json对象的内容，例如:\n" +
//                            "{\n" +
//                            "\t\"commonPart\": { // 公共部分(必要)\n" +
//                            "\t\t\"importSettingType\": \"VCT\", // 数据导入类型 (必要)\n" +
//                            "\t\t\"sourceFilePath\": \"标准格式数据/2001H2021513225.vct\", // 导入文件在gaf文件存储系统中的路径 (必要)\n" +
//                            "\t\t\"targetDatasourceId\": \"9e36ae94-8817-4d28-a710-f73de5e3a605\" // 目标数据源id  (必要,但可以被其他两种参数替换)\n" +
//                            "\t\t// 可以是targetDatasourceConnectionInfo 可以被可以是targetDatasourceConnectionInfo: {engineType:\"POSTGRESQL\",server:\"192.168.xx.xx:xxx\",database:\"space\",username:\"xxx\",password:\"xxx\"}\n" +
//                            "\t\t// 或者targetSysResourceDatasource:{typeCode: \"POSTGRESQL\",addr:\"192.168.11.xx:xx\",dbName:\"space\",userName:\"xxx\",password:\"xxx\"} 替换\n" +
//                            "\t},\n" +
//                            "\t\"basePart\": { // 基本部分(可选)\n" +
//                            "\t\t\"importMode\": \"NONE\", // 设置当同名数据集存在时导入的模式\n" +
//                            "\t\t\"scalingFactor\": {\"ratioX\":0.9 , \"ratioY\":0.9, \"ratioZ\":0.9}, // 设置缩放因子\n" +
//                            "\t\t\"targetEncodeType\": \"NONE\", // 设置要生成的数据集的编码类型\n" +
//                            "\t\t\"targetPrjCoordSys\": \"PCS_WGS_1984_UTM_1N\", // 设置导入后目标数据的坐标系 注意:该项的值参考最后面关于坐标系的特殊说明\n" +
//                            "\t\t\"importEmptyDataset\": true  // 是否导入空数据集 注意:该设置项非每种导入类型都有\n" +
//                            "\t},\n" +
//                            "\t\"dataInfosPart\": [{// 导入数据信息部分，即对源数据集到目标数据集的映射的设置(可选)\n" +
//                            "\t\t\"targetName\": \"mytesttargetname\" // 设置目标数据集的名称\n" +
//                            "\t}, {\n" +
//                            "\t\t// 注意:该设置项非每种导入类型都有\n" +
//                            "\t\t\"exchangeFieldOrder\": { // 用于交换字段信息集中两个指定元素的索引位置\n" +
//                            "\t\t\t\"fieldName1\":\"字段名1\",\n" +
//                            "\t\t\t\"fieldName2\": \"字段名2\"\n" +
//                            "\t\t}\n" +
//                            "\t}, {\n" +
//                            "\t\t// 注意:该设置项非每种导入类型都有\n" +
//                            "\t\t\"changeFieldName\": { // 修改指定字段的字段名称\n" +
//                            "\t\t\t\"oldFieldName\": \"BZ\",\n" +
//                            "\t\t\t\"newFieldName\": \"BZ2\"\n" +
//                            "\t\t}\n" +
//                            "\t}, {\n" +
//                            "\t\t// 注意:该设置项非每种导入类型都有\n" +
//                            "\t\t\"importFieldState\": { //  设置是否导入指定字段\n" +
//                            "\t\t\t\"oldFieldName\": \"HDMC\",\n" +
//                            "\t\t\t\"excludeField\": true\n" +
//                            "\t\t}\n" +
//                            "\t}, {\n" +
//                            "\t\t// 注意:该设置项非每种导入类型都有\n" +
//                            "\t\t\"targetFieldInfos\": [ // 设置目标数据中的字段信息 目标数据的字段定义必须与需要导入的源VCT文件中的实际字段值相符。 \n" +
//                            "\t\t\t{\"caption\":\"别名\",\"defaultValue\":\"默认值\",\"maxLength\": 255,\"name\":\"字段名\",\"required\":false,\"type\":\"TEXT\",\"zeroLengthAllowed\": true}\n" +
//                            "\t\t]\n" +
//                            "\t}\n" +
//                            "\t\n" +
//                            "\t]\n" +
//                            "}\n" +
//                            "其包含三个部分:\n" +
//                            "一. commonPart公共部分.  importSettingType数据导入类型,具体可以查看gaf的数据字典,该类型对应ImportSetting类的子类\n" +
//                            "二. basePart基本部分. 该部分参考iobjects在线文档的ImportSetting及其子类的属性(即以set开头的单个参数的方法,方法名去掉set,转换为小驼峰,就是属性)\n" +
//                            "再根据如下几条规则,可以转换得到json参数\n" +
//                            "规则如下:\n" +
//                            "1. 遇到自定义枚举类型,取枚举实例的名字;\n" +
//                            "2. 遇到可以对数据导入进行设置的包含多个参数的方法，将多个参数视为一个json对象传入;\n" +
//                            "3. 遇到属性是对象,则传入该对象对应的json对象; \n" +
//                            "4. 遇到属性是路径,该路径是通过gaf文件系统上传后返回的路径.\n" +
//                            "例如:\n" +
//                            "1. ImportSetting类设置属性importMode的方法setImportMode(ImportMode importMode),其方法参数是自定义枚举类型,取枚举实例的名字,转换为json参数-> basePart: {\"importMode\": \"NONE\"}\n" +
//                            "2. ImportSetting类设置缩放因子方法setScalingFactor(double ratioX, double ratioY, double ratioZ),虽然不是设置属性的方法,\n" +
//                            "也可以将方法的多个参数视为一个json对象,转换为json参数-> basePart: {\"scalingFactor\": {\"ratioX\":0.9 , \"ratioY\":0.9, \"ratioZ\":0.9}}\n" +
//                            "3. ImportSetting类设置属性targetPrjCoordSys的方法setTargetPrjCoordSys(PrjCoordSys prjCoordSys), 这里的方法参数是坐标系,稍微特殊,参考最后的关于坐标系的特殊说明,\n" +
//                            "转换为json参数-> basePart: {\"targetPrjCoordSys\": \"PCS_WGS_1984_UTM_1N\"}, 其中PCS_WGS_1984_UTM_1N是指定格式的字符串，代表某坐标系\n" +
//                            "这几条规则也适用于下面的dataInfosPart\n" +
//                            "\n" +
//                            "三 dataInfosPart导入数据信息部分，即对源数据集到目标数据集的映射的设置\n" +
//                            "该部分参考iobjects在线文档的ImportDataInfo及其子类的可设置方法,再根据上面的几条规则,转换得到json参数\n" +
//                            "例如:\n" +
//                            "1. ImportDataInfoVCT类的修改指定字段的字段名称的方法changeFieldName(String oldName,String newName),\n" +
//                            "转换为json参数时,将方法名作为设置项名,将方法的多个参数视为一个json对象,转换结果为 \"changeFieldName\": { \"oldFieldName\": \"BZ\", \"newFieldName\": \"BZ2\"}\n" +
//                            "这里, 旧字段名统一为oldFieldName 新字段名统一为newFieldName\n" +
//                            "注意: dataInfosPart JSON数组内的JSON对象顺序很重要,可以先调用查看数据导入时所有源数据集到目标数据集映的接口,得到顺序和映射详情,再做参数设置\n" +
//                            "\n" +
//                            "关于坐标系的特殊说明：\n" +
//                            "用指定格式的坐标系字符串来代表坐标系. 暂时不支持自定义坐标系\n" +
//                            "坐标系分为平面坐标系 地理坐标系 投影坐标系\n" +
//                            "坐标系字符串格式是\n" +
//                            "1. 平面坐标系：\n" +
//                            "PCS_NON_EARTH_${坐标系单位}, 坐标系单位有CENTIMETER(厘米) DECIMETER分米 FOOT英尺 INCH英寸 KILOMETER千米\n" +
//                            "METER米 MILE英里 MILIMETER毫米 ,具体可查看iobjects for java 在线帮助文档搜索Unit枚举类(com.supermap.data.Unit)\n" +
//                            "例如：PCS_NON_EARTH_METER 或者 PCS_NON_EARTH_MILE\n" +
//                            "2. 地理坐标系\n" +
//                            "PCS_EARTH_LONGITUDE_LATITUDE_${地理坐标系类型名}\n" +
//                            "地理坐标系类型名可参考iobjects for java 在线帮助文档 GeoCoordSysType 枚举类(com.supermap.data.GeoCoordSysType)\n" +
//                            "例如：GeoCoordSysType枚举类中的实例GCS_CHINA_2000,则得到地理坐标系字符串为PCS_EARTH_LONGITUDE_LATITUDE_GCS_CHINA_2000\n" +
//                            "3. 投影坐标系\n" +
//                            "可参考iobjects for java 在线帮助文档, 搜索PrjCoordSysType枚举类(com.supermap.data.PrjCoordSysType)\n" +
//                            "每个枚举实例名就代表投影坐标系,除了PCS_NON_EARTH  PCS_EARTH_LONGITUDE_LATITUDE PCS_USER_DEFINED\n" +
//                            "例如：PrjCoordSysType枚举类实例 PCS_WGS_1984_UTM_1N,则得到投影坐标系字符串为 PCS_WGS_1984_UTM_1N" +
//                            ""
//                    , dataTypeClass = String.class, paramType = "body",required = true),
//    })
//    @POST
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path(value = "/import")
//    public MessageResult<DataImportResult> importData(String importSettingJsonArray) {
//        DataImportResult dataImportResult = spaceDatasourceService.importData(importSettingJsonArray);
//        if (dataImportResult.getFailed().size() <= 0) {
//            return MessageResult.successe(DataImportResult.class).data(dataImportResult).message("全部导入成功").build();
//        } else {
//            return MessageResult.failed(DataImportResult.class).data(dataImportResult).message("全部导入失败").build();
//        }
//    }
//
//    @ApiOperation(value = "数据导出", notes = "批量导出矢量、栅格、海图、三维、电信、COGO\n" +
//            "具体能够导入的类型参考 iobjects 的在线帮助文档,http://support.supermap.com.cn/DataWarehouse/WebDocHelp/iObjectsJava/index.html\n" +
//            "找到程序员参考->概述,找到com.supermap.data.conversion包下的 ExportSetting 类,查看其直接子类,其子类则是可以导入的类型,\n" +
//            "某些导入类型只支持windows,所以这些导入类型该接口不支持。并且数据导出接口返回的是已导出文件的有时间限制的下载链接.\n" +
//            "而需要传入的参数是json数组,具体参数详情需要参考iobjects的ExportSetting子类,并结合自定义的参数规则，具体详情查看参数说明")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "exportSettingJsonArray", value = "数据导出设置信息数组\n" +
//                    "json数组中的每个json对象都是对数据导出的设置.\n" +
//                    "每个json对象的内容,参考下例:\n" +
//                    "{\n" +
//                    "\t\"commonPart\": { //公共部分(必要)\n" +
//                    "\t\t\"exportSettingType\": \"CSV\", // 数据导出类型(必要)\n" +
//                    "\t\t\"sourceDataset\": \"XZQ\", // 要导出的源数据集名 (必要)\n" +
//                    "\t\t\"sourceDatasourceId\": \"9e36ae94-8817-4d28-a710-f73de5e3a605\" // 要导出的源数据源id (必要,但可选择另外两种参数)\n" +
//                    "\t\t// sourceDatasourceId 可以被sourceDatasourceConnectionInfo: {engineType:\"POSTGRESQL\",server:\"192.168.xx.xx:xxx\",database:\"space\",username:\"xxx\",password:\"xxx\"}\n" +
//                    "\t\t// 或者sourceSysResourceDatasource:{typeCode: \"POSTGRESQL\",addr:\"192.168.11.xx:xx\",dbName:\"space\",userName:\"xxx\",password:\"xxx\"} 替换\n" +
//                    "\t},\n" +
//                    "\t\"basePart\": { // 基本部分\n" +
//                    "\t\t\"targetFilePath\": \"Desktop/xzq.csv\", // 目标文件路径, 该路径是gaf文件存储系统的路径 (必要)\n" +
//                    "\t\t\"targetFileType\": \"CSV\", // 目标文件类型 \n" +
//                    "\t\t\"overwrite\": false, // 返回导出目录中存在同名文件时，是否强制覆盖, 默认false\n" +
//                    "\t\t\"filter\":  \"smid > 1000\",// 设置导出目标文件的过滤信息。\n" +
//                    "\t\t\"ignoreFieldNames\": [\"field1\",\"field2\"] , // 设置导出目标文件的过滤信息\n" +
//                    "\t\t\"modifyFieldInfos\": [{\"name\":\"xxx\",\"precision\":2,\"scale\":5000,\"type\":\"DOUBLE\"}], // 设置导出目标字段信息\n" +
//                    "\t\t\"targetFileCharset\": \"UTF8\" // 设置需要导出的文件的字符集类型\n" +
//                    "\t\t// ... 不同数据导出类型可以设置的其他参数\n" +
//                    "\t}\n" +
//                    "}\n" +
//                    "其包含两个部分:\n" +
//                    "commonPart 公共部分. 提供源数据源、源数据集名、数据导出类型信息\n" +
//                    "basePart 基本部分. 通过参考iobjects在线帮助文档的ExportSetting类及其子类的属性,再根据如下几条规则,可以转换得到json参数.\n" +
//                    "规则如下:\n" +
//                    "1. 遇到自定义枚举类型,取枚举实例的名字;\n" +
//                    "2. 遇到可以对数据导入设置的多个参数的方法，将多个参数视为一个json对象传入;\n" +
//                    "3. 遇到属性是对象,则传入该对象对应的json对象; \n" +
//                    "4. 遇到属性是路径,该路径是通过gaf文件系统上传后返回的路径.\n" +
//                    "例如,\n" +
//                    "1. ExportSetting类的设置属性的方法setTargetFileType(FileType type),转换为json参数-> basePart: {\"targetFileType\": \"UTF8\"}, 其中UTF8是FileType枚举类型的一个实例的名字\n" +
//                    "2. ExportSetting类的设置属性的方法setTargetFilePath(java.lang.String string),该方法涉及到路径,该路径是通过gaf文件系统上传后返回的路径,转换为json参数-> basePart: {\"targetFilePath\": \"Desktop/xzq.csv\"}\n" +
//                    "3. ExportSetting类的设置属性的方法setModifyFieldInfos(ExportFieldModifyInfo[] fieldInfos),该方法的参数是对象数组,转换为json参数-> basePart: {\"modifyFieldInfos\": [{\"name\":\"xxx\",\"precision\":2,\"scale\":5000,\"type\":\"DOUBLE\"}]}\n",
//                    dataTypeClass = String.class, paramType = "body",required = true),
//    })
//    @POST
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path(value = "/export")
//    public MessageResult<DataExportResult> exportData(String exportSettingJsonArray) {
//        DataExportResult dataExportResult = spaceDatasourceService.exportData(exportSettingJsonArray);
//        if (dataExportResult.getFailed().size() <= 0) {
//            return MessageResult.successe(DataExportResult.class).data(dataExportResult).message("全部导出成功").build();
//        } else {
//            return MessageResult.failed(DataExportResult.class).data(dataExportResult).message("全部导出失败").build();
//        }
//    }
//
//
//    @ApiOperation(value = "查找数据源下的数据集列表", notes = "根据数据源信息查找数据集列表")
//    @ApiImplicitParam(name = "dataSourceInfo",value = "数据源实体类",paramType = "body",dataTypeClass = DataSourceInfo.class)
//    @GET
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path("/list-dataset")
//    public MessageResult<List<GDataset>> listDatasets(DataSourceInfo dataSourceInfo) {
//        List<GDataset> result = spaceDatasourceService.listDataset(dataSourceInfo);
//        return MessageResult.data(result).message("查询成功").build();
//    }
//
//    @ApiOperation(value = "根据数据源id查找数据源下的数据集列表",notes = "数据源应该是空间数据源，若未非空间数据源则返回空集合")
//    @ApiImplicitParam(name = "datasourceId",value = "数据源id",paramType = "path",dataTypeClass = String.class)
//    @GET
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path("/{datasourceId}/datasets")
//    public MessageResult<List<GDataset>> listDatasetsById(@PathParam("datasourceId") String datasourceId) {
//        List<GDataset> result = spaceDatasourceService.listDataset(datasourceId);
//        return MessageResult.data(result).message("查询成功").build();
//    }
//
//
//
//    @ApiOperation(value = "生成s3m切片", notes = "生成s3m切片")
//    @ApiImplicitParam(name = "buildS3MParameter",value = "构建s3m实体类",paramType = "body",dataTypeClass = BuildS3MParameter.class)
//    @POST
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path("/s3m")
//    public MessageResult<String> buildS3m(BuildS3MParameter buildS3mParameter) {
//        return s3mCacheService.buildS3M(buildS3mParameter.getDataSourceInfo(), buildS3mParameter.getDatasetNames(), buildS3mParameter.isOverWrite(), buildS3mParameter.getOutputFolder());
//    }
//


}

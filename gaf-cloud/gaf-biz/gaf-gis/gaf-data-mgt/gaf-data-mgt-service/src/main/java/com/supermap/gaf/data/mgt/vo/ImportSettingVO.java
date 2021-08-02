package com.supermap.gaf.data.mgt.vo;

import lombok.Data;

/**
 * 数据导入设置VO
 * @author wxl
 * @since 2021/8/1
 */
@Data
public class ImportSettingVO {

    /**
     * 数据导入设置基本部分 为json格式的字符串，包含数据导入类型importSettingType 上传后的源文件路径sourceFilePath 目标数据源id targetDatasourceId(已在空间数据源管理中新增的数据源连接信息id)或目标数据源连接信息targetDatasourceConnectionInfo
     * 例如{"importSettingType":"FileGDBVector","sourceFilePath":"/data/test.gdb","targetDatasourceConnectionInfo": "{engineType:"POSTGRESQL",server:"192.168.11.118:32222",database:"space",username:"xxx",password:"xxx"}"}
     * 或者{"importSettingType":"FileGDBVector","sourceFilePath":"/data/test.gdb","targetDatasourceId": "xxx-xxx-xxx-xxx"}
     * 或者{"importSettingType":"FileGDBVector","sourceFilePath":"/data/test.gdb","targetSysResourceDatasource": "{typeCode: "POSTGRESQL",addr:"192.168.11.118:32222",dbName:"space",userName:"xxx",password:"xxx"}"}
     *
     */
    String basePart;
    //缩放因子ScalingFactor
    /**
     * 数据导入设置的其他部分 为json格式的字符串,
     * 包括导入模式importMode 导入文件的原始字符集类型sourceFileCharset  要生成的数据集的编码类型TargetEncodeType 导入后目标数据的坐标系(使用ESPG代表坐标系)TargetPrjCoordSys 是否使用 FME 导入方式useFME
     * 以及不同数据导入类型个别的属性参数设置 例如数据导入类型是 CSV 则可以设置是否导入空数据集 importEmptyDataset
     * 不同数据导入类型个别的属性参数设置可以参考iobjects ImportSetting的子类
     *
     * 例如
     * {"targetEncodeType": "NONE","targetPrjCoordSys":4512,"importMode": "NONE","sourceFileCharset": "UTF8"}
     * {"targetEncodeType": "NONE","importMode": "NONE","sourceFileCharset": "UTF8"}
     *
     *
     */
    String otherPart;
    /**
     * 导入数据信息集合
     * 实际上是对源数据源到目标数据源 要导入的数据集的映射的设置
     * 该设置是json格式的字符串
     *
     *
     *
     *
     */
    String dataInfosPart;
}

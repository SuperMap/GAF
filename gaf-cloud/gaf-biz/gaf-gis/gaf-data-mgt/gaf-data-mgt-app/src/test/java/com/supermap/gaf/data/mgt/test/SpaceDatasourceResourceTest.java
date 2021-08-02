package com.supermap.gaf.data.mgt.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.supermap.data.Charset;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EncodeType;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.conversion.ImportDataInfos;
import com.supermap.data.conversion.ImportMode;
import com.supermap.data.conversion.ImportSettingFileGDBVector;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;


public class SpaceDatasourceResourceTest {

    @Test
    public void testImport() {
        // String jsonStr = "{\"targetEncodeType\": \"NONE\",\"targetPrjCoordSys\":4512,\"importMode\": \"NONE\",\"sourceFileCharset\": \"UTF8\",\"targetDataInfos\":[]}";
        String jsonStr = "{\"targetEncodeType\": \"NONE\",\"targetPrjCoordSys\":4512,\"importMode\": \"NONE\",\"sourceFileCharset\": \"UTF8\"}";
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.getDeserializers().put(EncodeType.class,EnumDeserializer.instance);
        parserConfig.getDeserializers().put(ImportMode.class,EnumDeserializer.instance);
        parserConfig.getDeserializers().put(Charset.class,EnumDeserializer.instance);
        parserConfig.getDeserializers().put(PrjCoordSys.class,PrjCoordSysDeserializer.instance);
        //parserConfig.getDeserializers().put(ImportDataInfos.class,ImportDataInfosDeserializer.instance);
        ImportSettingFileGDBVector i = JSON.parseObject(jsonStr, ImportSettingFileGDBVector.class,parserConfig);
        i.setSourceFilePath("C:\\Users\\wxl\\Desktop\\udb\\阿坝县基础数据包\\基础数据包\\原格式数据\\513231阿坝县.gdb");
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo("192.168.11.118:32222","abaxian2018xianzhuang", UUID.randomUUID().toString(),"admin","123456");
        i.setTargetDatasourceConnectionInfo(datasourceConnectionInfo);
        Assert.assertEquals(EncodeType.NONE,i.getTargetEncodeType());
        Assert.assertEquals(ImportMode.NONE,i.getImportMode());
        Assert.assertEquals(Charset.UTF8,i.getSourceFileCharset());
        Assert.assertEquals(4512,i.getTargetPrjCoordSys().getEPSGCode());
        System.out.println("getTargetEncodeType = " + i.getTargetEncodeType());
        System.out.println("getImportMode = " + i.getImportMode());
        System.out.println("getSourceFileCharset = " + i.getSourceFileCharset());
        System.out.println("getTargetPrjCoordSys EPSGCode = " + i.getTargetPrjCoordSys().getEPSGCode());
        System.out.println("getTargetPrjCoordSys Name = " + i.getTargetPrjCoordSys().getName());
        ImportDataInfos targetDataInfos = i.getTargetDataInfos(null);
        System.out.println("targetDataInfos = " + targetDataInfos);


    }


}
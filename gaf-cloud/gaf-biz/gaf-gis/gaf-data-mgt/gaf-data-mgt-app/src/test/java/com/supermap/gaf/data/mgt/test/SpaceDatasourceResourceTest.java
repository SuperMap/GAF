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
import com.supermap.gaf.data.mgt.Application;
import com.supermap.gaf.data.mgt.resource.SpaceDatasourceResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;


@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class SpaceDatasourceResourceTest {

    @Autowired
    private SpaceDatasourceResource spaceDatasourceResource;

    @Test
    public void testExport() {
        String s = "[{\"commonPart\":{\"exportSettingType\":\"CSV\",\"sourceDataset\":\"XZQ\",\"sourceDatasourceId\":\"9e36ae94-8817-4d28-a710-f73de5e3a605\"},\"basePart\":{\"targetFilePath\":\"C:/Users/wxl/Desktop/xzq.csv\",\"targetFileType\":\"CSV\"}}]";
        spaceDatasourceResource.exportData(s);
    }
    
    @Test
    public void testImport2() {
        String s = "[{\"commonPart\":{\"importSettingType\":\"VCT\",\"sourceFilePath\":\"E:\\chromedownload\\九寨沟县(513225)第三次国土调查成果\\九寨沟县(513225)第三次国土调查成果\\基础数据包\\标准格式数据\\2001H2021513225.vct\",\"targetDatasourceId\":\"9e36ae94-8817-4d28-a710-f73de5e3a605\"},\"basePart\":{\"importEmptyDataset\":true},\"dataInfosPart\":[{\"targetName\":\"mytesttargetname\"},{\"changeFieldName\":{\"oldFieldName\":\"BZ\",\"newFieldName\":\"BZ2\"}},{\"importFieldState\":{\"oldFieldName\":\"HDMC\",\"excludeField\":true}}]}]";
        spaceDatasourceResource.importData(s);
    }

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
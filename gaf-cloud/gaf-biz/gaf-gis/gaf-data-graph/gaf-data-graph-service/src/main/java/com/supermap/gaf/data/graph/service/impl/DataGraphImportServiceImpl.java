package com.supermap.gaf.data.graph.service.impl;

import com.supermap.gaf.data.graph.config.Neo4jProperties;
import com.supermap.gaf.data.graph.dao.DataGraphSysDictMapper;
import com.supermap.gaf.data.graph.service.DataGraphImportService;
import com.supermap.gaf.data.graph.utils.Neo4jAutoCloseable;
import com.supermap.gaf.storage.entity.PresignUploadRequest;
import com.supermap.gaf.storage.service.S3ClientService;
import com.supermap.gaf.storage.utils.CommonUtils;
import com.supermap.gaf.sys.mgt.commontype.SysDict;
import com.supermap.gaf.utils.ConvertToFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author : duke
 * @since 2021/5/6 5:27 PM
 */
@Slf4j
@EnableConfigurationProperties(Neo4jProperties.class)
@Service
public class DataGraphImportServiceImpl implements DataGraphImportService {
    private static final String ADMINTYPE_COUNTY_LIKE = "%\"adminType\":\"county\"%";
    private static final String ADMINTYPE_CITY_LIKE = "%\"adminType\":\"city\"%";
    private static final String ADMINTYPE_PROVINCE_LIKE = "%\"adminType\":\"province\"%";
    private static final String XZQH_DICT_CODE = "XZQH";

    private static final String COUNTYCSV_FILEUPLOADPATH = "public/county.csv";
    private static final String CITYCSV_FILEUPLOADPATH = "public/city.csv";
    private static final String PROVINCECSV_FILEUPLOADPATH = "public/province.csv";

    @Autowired
    private DataGraphSysDictMapper dataGraphSysDictMapper;
    @Autowired
    private S3ClientService s3ClientService;
    @Autowired
    private Neo4jProperties neo4jProperties;

    @Override
    public void importXzqh() {
        //查字典
        List<SysDict> countySysDict = dataGraphSysDictMapper.listSysDictViaExtPropertiesLike(XZQH_DICT_CODE,ADMINTYPE_COUNTY_LIKE);
        List<SysDict> citySysDict = dataGraphSysDictMapper.listSysDictViaExtPropertiesLike(XZQH_DICT_CODE,ADMINTYPE_CITY_LIKE);
        List<SysDict> provinceSysDict = dataGraphSysDictMapper.listSysDictViaExtPropertiesLike(XZQH_DICT_CODE,ADMINTYPE_PROVINCE_LIKE);
        //转为string list
        List<String[]> county = sysDictConvertToXzqhStringArrList(countySysDict);
        List<String[]> city = sysDictConvertToXzqhStringArrList(citySysDict);
        List<String[]> province = sysDictConvertToXzqhStringArrList(provinceSysDict);
        //转为csv
        File countyCsv = null;
        File cityCsv = null;
        File provinceCsv = null;
        try {
            countyCsv = ConvertToFileUtil.stringArrListToTempCsv(county);
            cityCsv = ConvertToFileUtil.stringArrListToTempCsv(city);
            provinceCsv = ConvertToFileUtil.stringArrListToTempCsv(province);
        }catch (Exception e){
            log.error("xzqh字典数据转为csv文件失败");
        }
        //上传csv
        String countyCsvUrl = "";
        String cityCsvUrl = "";
        String provinceCsvUrl = "";
        try {
            countyCsvUrl = uploadTempFile(COUNTYCSV_FILEUPLOADPATH,countyCsv);
            cityCsvUrl = uploadTempFile(CITYCSV_FILEUPLOADPATH,cityCsv);
            provinceCsvUrl = uploadTempFile(PROVINCECSV_FILEUPLOADPATH,provinceCsv);
        }catch (Exception e){
            log.error("xzqh-csv文件上传到minio失败");
        }finally {
            if (cityCsv != null){
                countyCsv.delete();
            }
            if (cityCsv != null){
                cityCsv.delete();
            }
            if (provinceCsv != null){
                provinceCsv.delete();
            }
        }
        //导入neo4j
        try (Neo4jAutoCloseable neo4j = new Neo4jAutoCloseable(neo4jProperties)){
            importXzqhToNeo4j(neo4j,provinceCsvUrl,cityCsvUrl,countyCsvUrl);
        }
    }

    @Override
    public void importZrzysjtx() {

    }

    /**
     * 导入XZQH到neo4j
     * @param neo4j
     * @param provinceCsvUrl
     * @param cityCsvUrl
     * @param countyCsvUrl
     */
    private void importXzqhToNeo4j(Neo4jAutoCloseable neo4j,String provinceCsvUrl,String cityCsvUrl,String countyCsvUrl){
        //导入根节点
        neo4j.execute("MERGE (l0:中华人民共和国行政区划 {name: \"中华人民共和国行政区划\"})");
        //导入省级,建立根节点到省级的联系
        neo4j.execute( String.format(
                        "LOAD CSV WITH HEADERS FROM '%s' AS row\n" +
                        "MATCH (l0:中华人民共和国行政区划)\n" +
                        "MERGE (l1:省级 {name: row.name, code: row.code, id:row.id, pid:row.pid})\n" +
                        "MERGE (l0)-[r1:省级]->(l1)"
                        , provinceCsvUrl));
        //导入地级，建立省级节点到地级的联系
        neo4j.execute( String.format(
                        "LOAD CSV WITH HEADERS FROM '%s' AS row\n" +
                        "MATCH (l1:省级 {id: row.pid})\n" +
                        "MERGE (l2:地级 {name: row.name, code: row.code, id:row.id, pid:row.pid})\n" +
                        "MERGE (l1)-[r2:地级]->(l2)"
                , cityCsvUrl));
        //导入县级，建立县级父节点到县级的联系
        neo4j.execute( String.format(
                        "LOAD CSV WITH HEADERS FROM '%s' AS row\n" +
                        "MATCH (lx)  where lx:省级 or lx:地级 MATCH (lx) where lx.id=row.pid\n" +
                        "MERGE (l3:县级 {name: row.name, code: row.code, id:row.id, pid:row.pid})\n" +
                        "MERGE (lx)-[r3:县级]->(l3)"
                , countyCsvUrl));
    }

    /**
     * 系统字典列表转为XZQH字符串数组列表
     */
    private List<String[]> sysDictConvertToXzqhStringArrList(List<SysDict> sysDictList){
        List<String[]> res = new ArrayList<>();
        int itemCount = 4;
        String[] head = new String[itemCount];
        head[0] = "id";
        head[1] = "name";
        head[2] = "pid";
        head[3] = "code";
        res.add(head);
        sysDictList.forEach(item -> {
            String[] itemStringArr = new String[itemCount];
            itemStringArr[0] = item.getDataDictId();
            itemStringArr[1] = item.getDictName();
            itemStringArr[2] = item.getPid();
            itemStringArr[3] = item.getDictValue();
            res.add(itemStringArr);
        });
        return res;
    }

    /**
     * 上传文件到minio,并返回可访问的url
     * @param fileUploadPath
     * @param file
     * @return
     * @throws Exception
     */
    private String uploadTempFile(String fileUploadPath,File file) throws Exception{
        String uploadSignUrl = s3ClientService.getUploadSignUrl(fileUploadPath,null);
        CommonUtils.uploadByPreSignedUrl(new PresignUploadRequest(uploadSignUrl),new FileInputStream(file));
        return s3ClientService.getUrl(fileUploadPath).toString();
    }

}

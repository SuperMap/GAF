/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.util;

import com.nimbusds.jose.util.StandardCharset;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.utils.ClassLoaderUtil;
import com.supermap.gaf.utils.LogUtil;
import com.supermap.mapping.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * @program: gaf-datacenter-modules
 * @description: 模板地图解析器
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/05/21
 */
@Service("templaetMapParser")
public class TemplateMapParser {
    private static Logger logger = LogUtil.getLocLogger(TemplateMapParser.class);

    @Deprecated
    public String getMapTemplatePath(String mapName) {
        String mapTemplatePath = StringUtils.EMPTY;
        try {
            File file = ResourceUtils.getFile("classpath:templates/maptemplate");
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File childFile : files) {
                        if (StringUtils.equals(FilenameUtils.getBaseName(childFile.getName()), mapName)) {
                            mapTemplatePath = childFile.getAbsolutePath();
                            break;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return mapTemplatePath;
    }

    /**
     * 获取地图from mapXml
     *
     * @param dsAlias
     * @param mapContent
     * @return
     */
    public MessageResult<Map> getTemplateMap(String dsAlias, String datasetName, String mapContent, Workspace tempWorkspace) {
        MessageResult<Map> mapResult = new MessageResult<>();
//        Workspace tempWorkspace = new Workspace();
        Map templateMap = new Map(tempWorkspace);
        String xmlContent = formatMapTemplateContent(dsAlias, datasetName, mapContent);
        templateMap.fromXML(xmlContent);
        mapResult.setSuccessed(true);
        mapResult.setMessage("获取模板地图成功!");
        mapResult.setData(templateMap);
        return mapResult;
    }

    /**
     * 读取mapXml内容并替换数据集、数据源
     *
     * @param datasource
     * @param templateXmlPath
     * @return
     */
    public String readTemplateMap(Datasource datasource, String templateXmlPath) {
        String strMapXml = "";
        String fileEncode = StandardCharset.UTF_8.toString();
        if (templateXmlPath != null) {
            try {
                File file = new File(templateXmlPath);
                strMapXml = FileUtils.readFileToString(file, fileEncode);
                // strMapXML = GetStreamReadeString(templateXMLPath, fileEncode);
                System.out.println("数据源是否为空：" + datasource.getAlias());
                String dsAlias = datasource.getAlias();
                // 更新地图文件中的数据源名(?<=sml:DataSourceAlias>)(.*?)(?=</sml:DataSourceAlias>)
                // String pattern = "(?<=@)(.*?)(?=#\\d{1,}</sml:Name>)"; // 先替换带#
                // String pattern2 = "(?<=@)((?!.*#).*)(?=</sml:Name>)"; //再替换不带#部分
                strMapXml = strMapXml.replaceAll("(?<=sml:DataSourceAlias>)(.*?)(?=</sml:DataSourceAlias>)", dsAlias);
                strMapXml = strMapXml.replaceAll("(?<=@)(.*?)(?=#\\d{1,}</sml:Name>)", dsAlias);
                strMapXml = strMapXml.replaceAll("(?<=@)((?!.*#).*)(?=</sml:Name>)", dsAlias);
                strMapXml = strMapXml.replaceAll("(?<=@)(.*?)(?=#\\d{1,}</sml:Caption>)", dsAlias);
                strMapXml = strMapXml.replaceAll("(?<=@)((?!.*#).*)(?=</sml:Caption>)", dsAlias);
                // 替换数据集
                // ToDo
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return strMapXml;
    }

    /**
     * 替换地图模板的数据集、数据源
     *
     * @param dsAlias
     * @param mapContent
     * @return
     */
    public String formatMapTemplateContent(String dsAlias, String datasetName, String mapContent) {
        String content = "";
        if (StringUtils.isNotEmpty(mapContent) && StringUtils.isNotEmpty(dsAlias)) {
            try (InputStream inputStream = IOUtils.toInputStream(mapContent, "UTF-8");) {
                if (inputStream != null && inputStream.available() > 0) {
                    Reader reader = new InputStreamReader(inputStream, "utf-8");
                    int ch = 0;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = reader.read()) != -1) {
                        sb.append((char) ch);
                    }
                    content = sb.toString();
                    content = replaceDsAilas(dsAlias, datasetName, content);
                    // 替换数据集
                    // ToDo
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return content;
    }

    /**
     * 读取mapXml内容并替换数据集、数据源
     *
     * @param datasource
     * @param mapTemplateName
     * @return
     */
    public String readTemplateMapFromName(Datasource datasource, String mapTemplateName) {
        String strMapXml = "";
        if (StringUtils.isNotEmpty(mapTemplateName)) {
            try (InputStream inputStream = getMapTemplateContent(mapTemplateName);) {
                if (inputStream != null && inputStream.available() > 0) {
                    Reader reader = new InputStreamReader(inputStream, StandardCharset.UTF_8.toString());
                    int ch = 0;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = reader.read()) != -1) {
                        sb.append((char) ch);
                    }
                    strMapXml = sb.toString();
                    String dsAlias = datasource.getAlias();
                    // 更新地图文件中的数据源名(?<=sml:DataSourceAlias>)(.*?)(?=</sml:DataSourceAlias>)
                    strMapXml = replaceDsAilas(dsAlias, "", strMapXml);
                    // 替换数据集
                    // ToDo
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage());
            }
        }
        return strMapXml;
    }

    protected InputStream getMapTemplateContent(String mapName) {
        ClassLoader cl = ClassLoaderUtil.contextLoader();
        InputStream inputStream = cl.getResourceAsStream("templates/maptemplate/" + mapName + ".xml");
        return inputStream;
    }

    private static String replaceDsAilas(String dsAlias, String daatsetName, String str) {
        str = str.replaceAll("(?<=sml:DataSourceAlias>)(.*?)(?=</sml:DataSourceAlias>)", dsAlias);
        str = str.replaceAll("(?<=@)(.*?)(?=#\\d{1,}</sml:Name>)", dsAlias);
        str = str.replaceAll("(?<=@)((?!.*#).*)(?=</sml:Name>)", dsAlias);
        str = str.replaceAll("(?<=@)(.*?)(?=#\\d{1,}</sml:Caption>)", dsAlias);
        str = str.replaceAll("(?<=@)((?!.*#).*)(?=</sml:Caption>)", dsAlias);
        //替换数据集
        str = str.replaceAll("(?<=sml:Caption>)(.*?)(?=@.*?</sml:Caption>)", daatsetName);
        str = str.replaceAll("(?<=sml:DatasetName>)(.*?)(?=</sml:DatasetName>)", daatsetName);
        return str;
    }

}

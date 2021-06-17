/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop;

import com.supermap.data.Workspace;
import com.supermap.desktop.core.AbstractPlugin;
import com.supermap.desktop.core.PluginInfo;
import com.supermap.desktop.core.license.LicenseException;
import com.supermap.desktop.develop.entity.GafGlobalEnvironments;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;
import com.supermap.iobjects.processes.process3d.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author SuperMap
 * @date:2021/3/25
 * @time 2018/4/16 0016 上午 10:05
 */
public class GAFPlugin extends AbstractPlugin {
    public GAFPlugin(String name, PluginInfo pluginInfo) throws LicenseException {
        super(name, pluginInfo);
        Workspace workspace = ApplicationContextUtils.getWorkspace();
        workspace.addOpenedListener(event -> {
            CommonUtils.checkGafPopMenuStatus();
        });
        GafGlobalEnvironments.initResource();
        String nowAlias = GAFProperties.getString("String_Alias");
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
                GafGlobalEnvironments.initResource();
                String newAlias = GafGlobalEnvironments.getAlias();
                if(!nowAlias.equals(newAlias)){
                    String jarPath = GafGlobalEnvironments.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                    try (ByteArrayOutputStream bArrOS = new ByteArrayOutputStream()) {
                        try (
                                JarFile jarFile = new JarFile(jarPath);
                                JarOutputStream jos = new JarOutputStream(bArrOS)
                        ) {
                            jarFile.stream().forEach(entry -> {
                                try (InputStream is = jarFile.getInputStream(entry)) {
                                    jos.putNextEntry(new JarEntry(entry.getName()));
                                    if ("GAF_zh_CN.properties".equals(entry.getName()) || "GAF_en_US.properties".equals(entry.getName()) || "GAF.properties".equals(entry.getName())) {
                                        Properties properties = new Properties();
                                        properties.load(is);
                                        properties.setProperty("String_Alias", newAlias);
                                        properties.store(jos, null);
                                    }else if("SuperMap.Desktop.GAF.config".equals(entry.getName())){
                                        Document document = XmlUtilities.getDocument(is);
                                        changeConfig(document.getDocumentElement(),nowAlias,newAlias);
                                        writeXml(document,jos);
                                    }else {
                                        byte[] buffer = new byte[1024];
                                        int len;
                                        while ((len = is.read(buffer)) > -1) {
                                            jos.write(buffer, 0, len);
                                        }
                                    }
                                    jos.closeEntry();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }

                        bArrOS.writeTo(new FileOutputStream(jarPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                System.out.println("我被关闭了");
            }
        ));
    }
    private void writeXml(Node node, OutputStream out) throws TransformerException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("encoding", "UTF-8");
        DOMSource source = new DOMSource();
        source.setNode(node);
        StreamResult result = new StreamResult();
        result.setOutputStream(out);
        transformer.transform(source, result);
    }
    private void changeConfig(Node node, String oldAlias, String newAlias) {

        NodeList nodeList = node.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println(currentNode.getNodeName());
                NamedNodeMap attributes = currentNode.getAttributes();
                for(int j = 0,length = attributes.getLength();j<length;++j){
                    Node attr = attributes.item(j);
                    if(attr.getNodeName().equals("label") || attr.getNodeName().equals("title")){
                        attr.setTextContent(attr.getTextContent().replace(oldAlias,newAlias));
                    }
                }
                changeConfig(currentNode,oldAlias, newAlias);
            }
        }
    }
    @Override
    public boolean isGranted() {
        return true;
    }

    @Override
    public String getPluginTitle() {
        return "GAF";
    }

    @Override
    public String getPluginName() {
        return "SuperMap.Desktop.GAF";
    }
}


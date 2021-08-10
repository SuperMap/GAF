/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>
 * 工具类，提供xml文档解析的简单封装
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @date:2021/3/25
 * @since 1.0.0
 */
public class XMLTool {

    /**
     * 文档解析器
     */
    private static final DocumentBuilderFactory builderFactoryInstance = DocumentBuilderFactory.newInstance();
    private static final TransformerFactory transformerFactoryInstance = TransformerFactory.newInstance();

    /**
     * <p>
     * 解析xml文档
     * </p>
     *
     * @param filePath
     * @return
     * @since 1.0.0
     */
    public static Document parseFile(String filePath) {
        return parse(new File(filePath));
    }

    /**
     * <p>
     * 解析文件
     * </p>
     *
     * @param xmlFile
     * @return
     * @since 1.0.0
     */
    public static Document parse(File xmlFile) {
        Document resultDocument = null;
        if (xmlFile != null && xmlFile.exists() && xmlFile.length() != 0L) {
            try (FileInputStream fin = new FileInputStream(xmlFile)) {
                resultDocument = parse((InputStream) fin);
            } catch (FileNotFoundException arg6) {
//                throw new IllegalArgumentException(
//                        b.getMessage("XMLTool.parse.FileNotFoundException", xmlFile.getAbsolutePath()), arg6);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultDocument;
    }

    public static Document parse(InputStream inputStream) {
        Document resultDocument = null;
        try {
            if (inputStream != null) {
                resultDocument = (Document) builderFactoryInstance.newDocumentBuilder().parse(inputStream);
            }
        } catch (IOException arg7) {
            ;
        } catch (SAXException arg8) {
            ;
        } catch (Exception arg9) {
            ;
        }
        return resultDocument;
    }

    /**
     * <p>
     * 获取xml子节点
     * </p>
     *
     * @param parentNode
     * @param name
     * @return
     * @since 1.0.0
     */
    public static Node getChildNode(Node parentNode, String name) {
        Node childNode = null;
        if (parentNode != null && parentNode.hasChildNodes()) {
            NodeList nodeList = parentNode.getChildNodes();
            int iLength = nodeList.getLength();
            for (int i = 0; i < iLength; i++) {
                Node node = nodeList.item(i);
                if (isMatchByLocalNameOrNodeName(node, name)) {
                    childNode = node;
                    break;
                }
            }
        }
        return childNode;
    }

    /**
     * <p>
     * 创建子节点
     * </p>
     *
     * @param node
     * @param nodeName
     * @return
     * @since 1.0.0
     */
    public static Node createNode(Node node, String nodeName) {
        Document document = node instanceof Document ? (Document) node : node.getOwnerDocument();
        Node current = node;
        Node child = XMLTool.getChildNode(current, nodeName);
        //不存在，则创建
        if (child == null) {
            child = document.createElement(nodeName);
            current.appendChild(child);
        }
        current = child;
        return current;
    }

    /**
     * <p>
     * 使用newNode替换oldNode的内容
     * </p>
     *
     * @param oldNode
     * @param newNode
     * @return
     * @since 1.0.0
     */
    public static boolean replaceNode(Node oldNode, Node newNode) {
        if (oldNode == null || newNode == null || oldNode.getParentNode() == null) {
            return false;
        }
        Node parentNode = oldNode.getParentNode();
        Node newNode1 = oldNode.getOwnerDocument().importNode(newNode, true);
        parentNode.insertBefore(newNode1, oldNode);
        parentNode.removeChild(oldNode);
        return true;
    }

    /**
     * <p>
     * 保存xml到指定文件中
     * </p>
     *
     * @param doc
     * @param file
     * @return
     * @since 1.0.0
     */
    public static boolean save(Document doc, File file) {
        String docStr = XMLTool.xmlToString(doc);
        try (FileOutputStream outs = new FileOutputStream(file)) {
            IOUtils.write(docStr, outs, "UTF-8");
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean isMatchByLocalNameOrNodeName(Node node, String name) {
        String localPart = node.getLocalName();
        String nodeName = node.getNodeName();
        return localPart == null ? nodeName.equalsIgnoreCase(name) : localPart.equalsIgnoreCase(name) || nodeName.equalsIgnoreCase(name);
    }

    /**
     * <p>
     * 将xml节点转换为对应的字符串
     * </p>
     *
     * @param doc
     * @return
     * @since 1.0.0
     */
    public static String xmlToString(Node node) {
        try (StringWriter writer = new StringWriter();) {
            Transformer transformer = transformerFactoryInstance.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerConfigurationException e) {
            return "";
        } catch (TransformerException e) {
            return "";
        } catch (TransformerFactoryConfigurationError e) {
            return "";
        } catch (IOException e1) {
            return "";
        }
    }

    /**
     * @param xml形状的str串
     * @return Document 对象
     */
    public static Document stringToXml(String xmlString) {
        Document doc = null;
        try {
            InputStream is = new ByteArrayInputStream(xmlString.getBytes("utf-8"));
            doc = builderFactoryInstance.newDocumentBuilder().parse(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
}

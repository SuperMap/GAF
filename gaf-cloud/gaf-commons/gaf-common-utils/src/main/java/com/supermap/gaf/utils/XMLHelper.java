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
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.w3c.dom.Node;

/**
 * 工具类，提供xml与实体对象的序列化转换
 *
 * @author:yj
 * @date:2021/3/25 Created by Administrator on 2018/5/16.
 */
public class XMLHelper {

    private static final Logger logger = LogUtil.getLocLogger(XMLHelper.class);

    /**
     * <p>
     * 将xml文件解析为对应的类型实体
     * </p>
     *
     * @param xmlFilePath 待解析文件路径，可以是文件相对路径/绝对路径，也可以是文件相对jar包的内部路径
     * @param clazz       实体类型
     * @return
     * @throws JAXBException
     * @throws IOException
     * @since 1.0.0
     */
    public static <T> T deserialize(String xmlFilePath, Class<T> clazz) {
        T result = null;
        File f = new File(xmlFilePath);
        if (f.exists()) {
            try (FileInputStream fis = new FileInputStream(xmlFilePath)) {
                result = deserializeXML(fis, clazz);
            } catch (Exception e) {
                logger.debug(e.getMessage());
            }
        } else {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = loader.getResourceAsStream(xmlFilePath)) {
                result = deserializeXML(inputStream, clazz);
            } catch (Exception e) {
                logger.debug(e.getMessage());
            }
        }
        return result;
    }

    /**
     * <p>
     * 将xml字符串解析为对应的类型实体
     * </p>
     *
     * @param instanceXML
     * @param clazz
     * @return
     * @throws JAXBException
     * @throws IOException
     * @since 1.0.0
     */
    public static <T> T deserializeXML(String instanceXML, Class<T> clazz) {
        T result = null;
        try (ByteArrayInputStream ins = new ByteArrayInputStream(instanceXML.getBytes("UTF-8"))) {
            result = deserializeXML(ins, clazz);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return result;
    }


    /**
     * <p>
     * 将xml字符串解析为对应的类型实体
     * </p>
     *
     * @param xmlNode
     * @param clazz
     * @return
     * @since 1.0.0
     */
    public static <T> T deserializeXML(Node xmlNode, Class<T> clazz) {
        return deserializeXML(XMLTool.xmlToString(xmlNode), clazz);
    }

    /**
     * <p>
     * 将xml流解析为对应的类型实体
     * </p>
     *
     * @param xmlInputStream
     * @param clazz
     * @return
     * @throws JAXBException
     * @throws UnsupportedEncodingException
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserializeXML(InputStream xmlInputStream, Class<T> clazz) {
        if (xmlInputStream == null) {
            return null;
        }
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshal = context.createUnmarshaller();
            return (T) unmarshal.unmarshal(xmlInputStream);
        } catch (JAXBException e) {
            logger.debug(e.getMessage(), e);
        }
        return null;
    }

    /**
     * <p>
     * 将对象序列化为xml字符串并返回
     * </p>
     *
     * @param obj
     * @return
     * @throws UnsupportedEncodingException
     * @throws JAXBException
     * @since 1.0.0
     */
    public static <T> String serialize(T obj) {
        StringBuilder builder = new StringBuilder();
        boolean isSuccessed = false;
        try (StringBuilderWriter writer = new StringBuilderWriter(builder)) {
            serialize(obj, writer);
            isSuccessed = true;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            System.out.println("22");
        } catch (JAXBException e) {
            System.out.println("11");
            // TODO Auto-generated catch block
        }
        return isSuccessed ? builder.toString() : StringUtils.EMPTY;
    }

    /**
     * <p>
     * 将对象序列化为xml字符串，并存储到数据文件中
     * </p>
     *
     * @param obj
     * @param xmlFilePath
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws JAXBException
     * @since 1.0.0
     */
    public static <T> boolean serialize(T obj, String xmlFilePath) {
        boolean isSuccessed = false;
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(xmlFilePath), "UTF-8");) {
            serialize(obj, writer);
            isSuccessed = true;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isSuccessed;
    }

    /**
     * <p>
     * 将xml对象携入对应的字节流
     * </p>
     *
     * @param obj
     * @param writer
     * @throws JAXBException
     * @throws UnsupportedEncodingException
     * @since 1.0.0
     */
    private static <T> void serialize(T obj, Writer writer)
            throws JAXBException, UnsupportedEncodingException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshal = context.createMarshaller();
        marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshal.marshal(obj, writer);
    }
}

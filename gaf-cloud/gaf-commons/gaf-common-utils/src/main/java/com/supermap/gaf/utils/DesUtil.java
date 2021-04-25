/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.cal10n.LocLogger;

import com.supermap.gaf.services.resource.CommonResource;

/**
 * @author:yj
 * @date:2021/3/25
 * DES加密 解密算法
 */
public class DesUtil {

    private final static String DES = "DES";
    private final static String ENCODE = "UTF-8";
    private final static String DEFAULT_KEY = "SuperMap_Land";
    private static ResourceManager rm = new ResourceManager(CommonResource.class);
    private static final LocLogger logger = LogUtil.getLocLogger(DesUtil.class, rm);
    public static void main(String[] args) throws Exception {
        String data = "ED5wLgc3Mnw=";
        System.out.println(decrypt(data));
        System.out.println(encrypt("123"));
    }

    /**
     * 使用 默认key 加密
     * @return String
     */
    public static String encrypt(String data) {
        String encryStr = StringUtils.EMPTY;
        try {
            byte[] bt = encrypt(data.getBytes(ENCODE), DEFAULT_KEY.getBytes(ENCODE));
            encryStr = Base64.getEncoder().encodeToString(bt);
        } catch (Exception ex) {
            logger.warn(CommonResource.DESUTIL_ENCRYPTION_FAILED);
        }
        return encryStr;
    }

    /**
     * 使用 默认key 解密
     * @return String
     */
    public static String decrypt(String data) {
        String decryStr = StringUtils.EMPTY;
        try {
            byte[] buf = Base64.getDecoder().decode(data);
            byte[] bt = decrypt(buf, DEFAULT_KEY.getBytes(ENCODE));
            decryStr = new String(bt, ENCODE);
        } catch (Exception ex) {
            logger.warn(CommonResource.DESUTIL_DECRYPTION_FAILED);
        }
        return decryStr;

    }

    /**
     * Description 根据键值进行加密
     * 
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), DEFAULT_KEY.getBytes(ENCODE));
        return Base64.getEncoder().encodeToString(bt);
    }

    /**
     * Description 根据键值进行解密
     * 
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException, Exception {
        if (data == null)
            return null;
        byte[] buf = Base64.getDecoder().decode(data);
        byte[] bt = decrypt(buf, key.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }

    /**
     * Description 根据键值进行加密
     * 
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     * 
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }
}

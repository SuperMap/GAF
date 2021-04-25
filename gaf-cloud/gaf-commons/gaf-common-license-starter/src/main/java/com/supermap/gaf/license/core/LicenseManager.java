/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.license.core;

import com.supermap.License;
import com.supermap.gaf.license.autoconfigure.LicenseItem;
import com.supermap.gaf.license.autoconfigure.LicenseProperties;
import com.supermap.gaf.license.util.MessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author:yj
 * @date:2021/3/25
*/

public class LicenseManager {

    private static final Logger logger = LoggerFactory.getLogger(LicenseManager.class);
    public static final int COMMON_TRIAL_LICNESE = 65400;

    private LicenseProperties licenseProperties;

    private Map<Integer, AtomicBoolean> cache;

    public MessageResult<Void> licenseVerify(String uri) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean isPublicPath = false;
        if(licenseProperties.getPublicPaths() != null && licenseProperties.getPublicPaths().length > 0) {
            for (String  publicPath: licenseProperties.getPublicPaths()) {
                if(antPathMatcher.match(publicPath, uri)) {
                    isPublicPath = true;
                    break;
                }
            }
        }
        if(isPublicPath) {
            return MessageResult.successe(Void.class).build();
        } else {
            LicenseItem[] licenseItemArray = licenseProperties.getLicenseItemArray();
            for (LicenseItem licenseItem : licenseItemArray) {
                String[] paths = licenseItem.getPaths();
                int featureId = licenseItem.getFeatureId();
                for (String path: paths) {
                    boolean match = antPathMatcher.match(path, uri);
                    if(match) {
                        logger.debug("进行许可校验");
                        // 进行许可校验
                        // 从缓存中取出是否已经校验过
                        AtomicBoolean isVerifyPass = this.cache.get(featureId);
                        logger.debug("模块id:{}从缓存中取出,是否已经校验过:{}", featureId,isVerifyPass);
                        AtomicBoolean isTrailVerifyPass = this.cache.get(COMMON_TRIAL_LICNESE);
                        logger.debug("模块id:{}从缓存中取出,是否已经校验过:{}", COMMON_TRIAL_LICNESE,isTrailVerifyPass);
                        if(!isVerifyPass.get() && !isTrailVerifyPass.get()) {
                            // 进行正式许可校验
                            logger.debug("进行正式许可校验, 模块id:{}", featureId);
                            String verifyInfo = licenseVerify(featureId);
                            if(StringUtils.isEmpty(verifyInfo)) {
                                // 放入缓存
                                logger.debug("校验成功，将成功标志放入缓存, 模块id:{}", featureId);
                                this.cache.get(featureId).set(true);
                                return MessageResult.successe(Void.class).build();
                            }
                            // 校验试用许可
                            logger.debug("校验试用许可, 模块id:{}", COMMON_TRIAL_LICNESE);
                            String verifyTrailInfo = licenseVerify(COMMON_TRIAL_LICNESE);
                            if(StringUtils.isEmpty(verifyTrailInfo)) {
                                // 放入缓存
                                logger.debug("校验成功，将成功标志放入缓存, 模块id:{}", COMMON_TRIAL_LICNESE);
                                this.cache.get(COMMON_TRIAL_LICNESE).set(true);
                                return MessageResult.successe(Void.class).build();
                            }
                            logger.error(verifyInfo);
                            return MessageResult.failed(Void.class).message(verifyInfo).build();
                        }
                        return MessageResult.successe(Void.class).build();
                    }
                }
            }
            return MessageResult.successe(Void.class).build();
        }

    }

    /**
     * 许可校验
     * @param featureId 许可模块id
     * @return 若校验成功则返回空字符串，否则返回失败信息
     */
    private String licenseVerify(int featureId) {
        String result = "";
        License license = null;
        try {
            logger.debug("创建许可对象");
            license = new License();
            logger.debug("连接模块，模块id:{}", featureId);
            int i = license.connect(featureId);
            if (i == 0) {
                logger.debug("连接成功,开始校验");
                int j = license.verify();
                // 许可有效时，返回许可相关的一些信息 0为有效
                if (j != 0) {
                    logger.debug("校验失败");
                    throw new RuntimeException("SuperMap许可验证失效！");
                }
                logger.debug("校验成功");
            }else {
                logger.debug("连接失败");
                throw new RuntimeException("SuperMap许可不可用："+License.getErrorMessage(i));
            }
        }catch(Throwable e){
            result = "featureId: " + featureId + " license content error:"+e.getMessage();
        }finally {
            // 断开连接
            if(license!=null) {
                try {
                    logger.debug("断开连接");
                    license.disconnect();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return result;
    }


    public LicenseManager(LicenseProperties licenseProperties) {
        this.licenseProperties = licenseProperties;
        Map<Integer, AtomicBoolean> cache = new HashMap<>();
        cache.putIfAbsent(COMMON_TRIAL_LICNESE, new AtomicBoolean(false));
        for (LicenseItem licenseItem : licenseProperties.getLicenseItemArray()) {
            cache.putIfAbsent(licenseItem.getFeatureId(), new AtomicBoolean(false));
        }
        this.cache = cache;
        startSeheduledVerify();
    }

    private void startSeheduledVerify() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, r -> {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        );
        final Map<Integer, AtomicBoolean>  cahceCpoy = this.cache;
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            cahceCpoy.forEach((id, oldVerify) -> {
                String verifyInfo = licenseVerify(id);
                if( StringUtils.isEmpty(verifyInfo) && !oldVerify.get()) {
                    oldVerify.set(true);
                } else if(!StringUtils.isEmpty(verifyInfo) && oldVerify.get()) {
                    oldVerify.set(false);
                }
            });
        },60, licenseProperties.getPeriod(), TimeUnit.SECONDS);
    }

    public LicenseProperties getLicenseProperties() {
        return licenseProperties;
    }

    public void setLicenseProperties(LicenseProperties licenseProperties) {
        this.licenseProperties = licenseProperties;
    }

    public Map<Integer, AtomicBoolean> getCache() {
        return cache;
    }

    public void setCache(Map<Integer, AtomicBoolean> cache) {
        this.cache = cache;
    }
}

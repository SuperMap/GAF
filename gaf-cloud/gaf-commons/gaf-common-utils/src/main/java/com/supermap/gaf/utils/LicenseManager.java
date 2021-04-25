/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections4.MapUtils;

import com.supermap.data.License;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class LicenseManager {
    
    private static LicenseManager instance = new LicenseManager();

    private Map<Integer,Boolean> isVerifiedMaps = new HashMap<>();
//    private boolean isVerified = false;
    private boolean isTrial = false;
    private Timer checkTimer;
    
    private LicenseManager() {
        doVerified();
    }

    public static LicenseManager getInstance() {
        return instance;
    }

    public boolean isVerified(int licenseModuleType) {
        //如果验证成功，直接返回
        if(this.isVerifiedMaps.containsKey(licenseModuleType)) {
            return this.isVerifiedMaps.get(licenseModuleType);
        }
        
        //初次验证，先设置为false
        this.isVerifiedMaps.put(licenseModuleType, false);
        this.doVerified();
        return this.isVerifiedMaps.get(licenseModuleType);
    }

    public boolean isTrial() {
        return isTrial;
    }

    public void setTrial(boolean isTrial) {
        this.isTrial = isTrial;
    }

    public void doVerified() {
        
        boolean isTrial = true;
        License license = new License();
        //未包含待验证模块，直接返回
        if(MapUtils.isEmpty(this.isVerifiedMaps)) {
            return;
        }
        //如果有一个正式许可，则视为已经启用了正式许可，忽略试用许可
        for(int type : this.isVerifiedMaps.keySet()) {
            // 验证正式许可
            int i = license.connect(type);
            if (i == 0) {
                int j = license.verify();
                // 许可有效时，返回许可相关的一些信息
                if (j == 0) {
                    this.isVerifiedMaps.put(type, true);
                    isTrial = false;
                }
            }            
        }
       
        // 测试模块试用许可检查
        if(isTrial) {
            int i = -1;
            boolean result = false;
            for (int licenseId = 65400; licenseId <= 65410; licenseId++) {
                i = license.connect(licenseId);
                if (i == 0) {
                    result = true;
                    break;
                }
            }
            if(result) {
                for(int type : this.isVerifiedMaps.keySet()) {
                    this.isVerifiedMaps.put(type, true);
                }
            }
        }
        this.setTrial(isTrial);
    }

    public void startLicenseCheckTask() {
        if(checkTimer == null){
            synchronized (LicenseManager.class) {
                if(checkTimer == null){
                    checkTimer = new Timer();
                    checkTimer.schedule(new LicenseVerifyTask(), 3600000L, 10800000L);
                }
            }
        }
    }
    
    static class LicenseVerifyTask extends TimerTask{
        @Override
        public void run() {
            LicenseManager.instance.doVerified();
        }
    }
    
}

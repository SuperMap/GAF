/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyorException;
import ch.qos.cal10n.MessageParameterObj;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.supermap.gaf.commontypes.LogType;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class ResourceManager implements IMessageConveyor {
    private ResourceBundle resourceBundle = null;
    private static final String SEP = " -";
    private LogType logtype;

    public ResourceManager(Class<? extends Enum<?>> clz) {
        resourceBundle(((BaseName) clz.getAnnotation(BaseName.class)).value());
    }

    public ResourceManager(Class<? extends Enum<?>> clz, LogType logtype) {
        resourceBundle(((BaseName) clz.getAnnotation(BaseName.class)).value());
        this.logtype = logtype;
    }

    private void resourceBundle(String paramString) {
        try {
            this.resourceBundle = ResourceBundle.getBundle(paramString);
        } catch (Exception localException) {
        }
    }

    public String getMessage(String key, String param) {
        if (param != null) {
            return getMessage(key, new Object[]{param});
        }
        return getMessage(key);
    }

    public <T> String getMessage(String key, T... params) {
        String str = null;
        try {
            str = this.resourceBundle.getString(key);
            str = MessageFormat.format(str, params);
        } catch (Exception localException) {
            //找不到value或是其他异常，都返回key
            str = key;
        }
        return logtype == null ? str : logtype + SEP + str;
    }

    public String getMessage(String key) {
        String str = null;
        try {
            str = this.resourceBundle.getString(key);
        } catch (Exception localException) {
            //找不到value或是其他异常，都返回key
            str = key;
        }
        return logtype == null ? str : logtype + SEP + str;
    }

    public String getMessage(MessageParameterObj messageParameterObj) throws MessageConveyorException {
        if (messageParameterObj == null) {
            throw new IllegalArgumentException("MessageParameterObj argumument cannot be null");
        }
        return getMessage(messageParameterObj.getKey().name(), messageParameterObj.getArgs());
    }

    public <E extends Enum<?>> String getMessage(E key, Object... arrayOfObject) throws MessageConveyorException {
        return getMessage(key.name(), arrayOfObject);
    }
}

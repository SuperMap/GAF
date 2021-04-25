/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.utils;

import java.time.OffsetDateTime;
import java.util.Date;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/1 10:20 AM
 */
public class TimestampUtils {
    public static String timestampToCalendar(Long time){
        OffsetDateTime odt = OffsetDateTime.now();
        Date date = new Date(time);
        return date.toInstant().atOffset(odt.getOffset()).toString();
    }




}

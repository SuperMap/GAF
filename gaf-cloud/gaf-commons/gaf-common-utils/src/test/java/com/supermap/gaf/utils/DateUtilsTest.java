/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author:yj
 * @date:2021/3/25
*/

public class DateUtilsTest {

    @Test
    public void testIsPearYear() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(1988, 0, 1);
        Assert.assertTrue(DateUtils.isLeapYEAR(calendar.getTime()));
        
        calendar.set(1987, 0, 1);
        Assert.assertFalse(DateUtils.isLeapYEAR(calendar.getTime()));

        calendar.set(1600, 0, 1);
        Assert.assertTrue(DateUtils.isLeapYEAR(calendar.getTime()));
        
        calendar.set(1900, 0, 1);
        Assert.assertFalse(DateUtils.isLeapYEAR(calendar.getTime()));
    }
}

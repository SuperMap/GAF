/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2021/2/2 5:05 PM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
public class AuthRoleApiServiceImplTest {
    @Autowired
    AuthRoleApiServiceImpl authRoleApiService;

    @Test
    public void test() {
        authRoleApiService.listByRole("role_000000");
        System.out.println("ok");
    }

}

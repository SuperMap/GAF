/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.util;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @date:2021/3/25
 * @author C
 */
public class JdbcUtils {
    private static final Logger logger = LogUtil.getLocLogger(JdbcUtils.class);

    public static Connection openConnection(String url,String username,String password) throws SQLException {
        Properties props =new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("remarks", "true");
        return DriverManager.getConnection(url,props);
    }

    public static MessageResult<Void> checkConnectionInfo(String url, String username, String password){
        Connection connection = null;
        try{
            connection = openConnection(url,username,password);
            return MessageResult.successe(Void.class).message("测试连接成功").build();
        } catch (SQLException e) {
            logger.info("打开数据库连接失败", e);
            return  MessageResult.failed(Void.class).message("测试连接失败" + e.getMessage()).build();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("关闭数据库连接失败", e);
            }
        }
    }


}

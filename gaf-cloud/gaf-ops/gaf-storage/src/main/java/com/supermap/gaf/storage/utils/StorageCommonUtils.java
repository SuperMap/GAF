package com.supermap.gaf.storage.utils;

import com.github.pagehelper.PageException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class StorageCommonUtils {

    public static Set<String> split(String source, boolean trim, boolean ignoreEmpty) {
        return splitAndConvert(source, item -> item, trim, ignoreEmpty);
    }

    public static <T> Set<T> splitAndConvert(String source, Function<String, T> convert, boolean trim, boolean ignoreEmpty) {
        Set<T> owers = new HashSet<>();
        for (String item : source.split(",")) {
            if (trim) {
                item = item.trim();

            }
            if (ignoreEmpty && item.equals("")) {
                continue;
            }
            owers.add(convert.apply(item));
        }
        return owers;
    }

    /**
     * 获取url
     *
     * @param dataSource
     * @return
     */
    public static String getUrl(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return conn.getMetaData().getURL();
        } catch (SQLException e) {
            throw new PageException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }

    public static String getDriverName(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return conn.getMetaData().getDriverName();
        } catch (SQLException e) {
            throw new PageException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }
}

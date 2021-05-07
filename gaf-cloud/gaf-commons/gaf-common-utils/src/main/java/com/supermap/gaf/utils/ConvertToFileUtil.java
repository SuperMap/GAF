/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * @author : duke
 * @since 2021/5/7 8:57 AM
 */
public class ConvertToFileUtil {
    private static final String CSV_FILE_SUFFIX = ".csv";
    private static final String COMMA = ",";
    private static final String ESCAPE_DOUBLE_QUOTES = "\"";
    private static final String ESCAPE_TWO_DOUBLE_QUOTES = "\"\"";
    private static final String SPACE = " ";
    private static final String APOSTROPHE = "'";
    private static final String TEMP_FILE_NAME = "CSV_TEMP_";

    private static String convertToCsv(String[] data) {
        return Stream.of(data)
                .map(ConvertToFileUtil::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public static File stringArrListToTempCsv(List<String[]> dataLines) throws IOException {
        File csvOutputFile = File.createTempFile(TEMP_FILE_NAME ,CSV_FILE_SUFFIX);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(ConvertToFileUtil::convertToCsv)
                    .forEach(pw::println);
        }
        assertTrue(csvOutputFile.exists());
        return csvOutputFile;
    }
    private static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", SPACE);
        if (data.contains(COMMA) || data.contains(ESCAPE_DOUBLE_QUOTES) || data.contains(APOSTROPHE)) {
            data = data.replace(ESCAPE_DOUBLE_QUOTES, ESCAPE_TWO_DOUBLE_QUOTES);
            escapedData = ESCAPE_DOUBLE_QUOTES + data + ESCAPE_DOUBLE_QUOTES;
        }
        return escapedData;
    }


    public static void main(String[] args) throws Exception{
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[]
                { "John", "Doe", "38", "Comment Data\nAnother line of comment data" });
        dataLines.add(new String[]
                { "Jane", "Doe, Jr.", "19", "She said \"I'm being quoted\"" });
        ConvertToFileUtil.stringArrListToTempCsv(dataLines);
    }
}

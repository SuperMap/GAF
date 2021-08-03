package com.supermap.gaf.authority.service.impl;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

/**
 * @author wxl
 * @since 2021/6/22
 */
public class StringRangeValidatorTest {
    private static Pattern orderFileNamePattern = Pattern.compile("private String orderFieldName;");
    private static String orderReplaceTemplate = "@StringRange(entityClass = %s.class,message = \"不在指定的字段名范围内\")\n    private String orderFieldName;";
    private static Pattern searchFieldNamePattern = Pattern.compile("private String searchFieldName;");
    private static String searchReplaceTemplate = "@StringRange(entityClass = %s.class,message = \"不在指定的字段名范围内\")\n    private String searchFieldName;";
    private static Pattern equalFieldNamePattern = Pattern.compile("private String equalFieldName;");
    private static String equalReplaceTemplate = "@StringRange(entityClass = %s.class,message = \"不在指定的字段名范围内\")\n    private String equalFieldName;";
    private static Pattern orderMethodPattern = Pattern.compile("private String orderMethod;");
    private static String orderMethodReplace = "@StringRange(value = {\"asc\",\"desc\"},message = \"不在指定的范围[asc,desc]内\")\n    private String orderMethod;";


    private static String asString(File file) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    private static void stringToFile(String s, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(s);
            fileWriter.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        final Path start = Paths.get("C:\\work\\development\\workspace\\GAF\\gaf-cloud");
        replace(start);
    }

    private static void replace(Path start) throws IOException {
        Files.walkFileTree(start, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                File file1 = file.toFile();
                String name = file1.getName();
                if (name.endsWith("SelectVo.java")) {
                    String srcSelectVo = asString(file1);
                    if (srcSelectVo.contains("@StringRange")) {
                        return FileVisitResult.CONTINUE;
                    }
                    String entityClassName = name.substring(0, name.indexOf("SelectVo.java"));
                    ;
                    String s = orderFileNamePattern.matcher(srcSelectVo).replaceFirst(String.format(orderReplaceTemplate, entityClassName));
                    String s1 = searchFieldNamePattern.matcher(s).replaceFirst(String.format(searchReplaceTemplate, entityClassName));
                    String s2 = equalFieldNamePattern.matcher(s1).replaceFirst(String.format(equalReplaceTemplate, entityClassName));
                    String s3 = orderMethodPattern.matcher(s2).replaceFirst(orderMethodReplace);
                    stringToFile(s3, file1);
                } else if (name.endsWith("Resource.java")) {
                    String srcResource = asString(file1);
                    String entityClassName = name.substring(0, name.indexOf("Resource.java"));
                    ;
                    final String s = Pattern.compile("(@Valid )*@BeanParam ?" + entityClassName + "SelectVo").matcher(srcResource).replaceFirst("@Valid @BeanParam " + entityClassName + "SelectVo");
                    final String s1 = Pattern.compile("(@StringRange\\(entityClass = " + entityClassName + ".class\\) )*@QueryParam\\(\"searchFieldName\"\\)").matcher(s).replaceFirst("@StringRange(entityClass = " + entityClassName + ".class) @QueryParam(\"searchFieldName\")");
                    final String s2 = Pattern.compile("(@StringRange\\(entityClass = " + entityClassName + ".class\\) )*@QueryParam\\(\"orderFieldName\"\\)").matcher(s1).replaceFirst("@StringRange(entityClass = " + entityClassName + ".class) @QueryParam(\"orderFieldName\")");
                    final String s3 = Pattern.compile("(@StringRange\\(\\{\"asc\",\"desc\"}\\) )*@QueryParam\\(\"orderMethod\"\\)").matcher(s2).replaceFirst("@StringRange({\"asc\",\"desc\"}) @QueryParam(\"orderMethod\")");
                    stringToFile(s3, file1);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }
}

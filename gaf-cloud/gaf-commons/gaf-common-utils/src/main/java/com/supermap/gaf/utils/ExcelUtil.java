/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @program: app-landuse
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2018/12/03
 */
public class ExcelUtil {

    /**
     * 获根据文件路径获取Excel工作簿
     * @param filePath
     * @return
     */
    public static Workbook getWorkbook(String filePath) {
        Workbook workbook = null;
//        Sheet sheet = null;
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(filePath));) {
            if (filePath.endsWith(".xlsx")) // 2007版本
            {
                workbook = new XSSFWorkbook(fileStream);
            } else if (filePath.endsWith(".xls")) // 2003版本
            {
                // 打开HSSFWorkbook
                workbook = new HSSFWorkbook(fileStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * 获取当前sheet中活动的单元格所在的最大列数
     * @param sheet
     * @return
     */
    public static int getActiveCellCnt(Sheet sheet) {
        int cellCnt = 0; // 活动单元格总的列数
//        int rowCnt = sheet.getLastRowNum() + 1; // 总行数
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {// 迭代所有行
            Row row = sheet.getRow(i);
            if (row != null && row.getPhysicalNumberOfCells() > cellCnt) {
                cellCnt = row.getPhysicalNumberOfCells();
            }
        }
        return cellCnt;
    }

    /**
     * 根据sheet,rowIndex,cellIndex获取(创建)单元格
     * @param sheet
     * @param rowNum 行
     * @param columnNum 列
     * @return
     */
    public static Cell getCell(Sheet sheet, int rowNum, int columnNum) {
        Cell cell = null;
        // String value = "";
        if (sheet != null) {
            Row row = sheet.getRow(rowNum);
            if(row == null) {
                row = sheet.createRow(rowNum);
            }
            if (row != null) {
                cell = row.getCell(columnNum);
                
                if(cell == null) {
                    cell = row.createCell(columnNum);
                }
            }
        }
        return cell;
    }
    
    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        if (cell == null)
            return "";
        if (cell.getCellTypeEnum().equals(CellType.STRING)) {
            return cell.getStringCellValue();
        } else if (cell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
            return cell.getCellFormula();
        } else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }

    /**
     * 复制行
     * @param sheet
     * @param fromRow
     * @param startRow
     * @param copyRowNum
     */
    public static void copyRow(Sheet sheet, Row fromRow, int startRow, int copyRowNum) {
        if (sheet != null) {
            int currentRowIndex = 1;
            while (currentRowIndex < copyRowNum) {
                Row newRow = sheet.createRow(startRow + currentRowIndex);
                if (fromRow != null && newRow != null) {
                    copyCell(fromRow, newRow, true);
                }
                currentRowIndex++;
            }
        }
    }

    /**
     * 复制行
     * @param fromRow
     * @param toRow
     * @param copyValueFlag 是否copy值
     */
    public static void copyCell(Row fromRow, Row toRow, boolean copyValueFlag) {
        if (fromRow != null && toRow != null) {
            Cell sourceCell = null;
            Cell targetCell = null;
            for (int i = fromRow.getFirstCellNum(); i < fromRow.getLastCellNum(); i++) {
                sourceCell = fromRow.getCell(i);
                targetCell = toRow.createCell(i);
                if (copyValueFlag) {
                    targetCell.setCellType(sourceCell.getCellTypeEnum());
                    setCellValue(sourceCell, targetCell);
                }
            }
        }
    }

    /**
     * 单元格设值
     * @param srcCell
     * @param distCell
     */
    public static void setCellValue(Cell srcCell, Cell distCell) {
        distCell.setCellStyle(srcCell.getCellStyle());
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        CellType srcCellType = srcCell.getCellTypeEnum();
        distCell.setCellType(srcCellType);
        switch (srcCellType) {
        case STRING:
            distCell.setCellValue(srcCell.getRichStringCellValue());
            break;
        case BOOLEAN:
            distCell.setCellValue(srcCell.getBooleanCellValue());
            break;
        case FORMULA:
            distCell.setCellFormula(srcCell.getCellFormula());
            break;
        case NUMERIC:
            if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                distCell.setCellValue(srcCell.getDateCellValue());
            } else {
                distCell.setCellValue(srcCell.getNumericCellValue());
            }
            break;
        case ERROR:
            distCell.setCellErrorValue(srcCell.getErrorCellValue());
            break;
        case _NONE:
        case BLANK:
        }
    }

    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Cell fCell = getCell(sheet, firstRow, firstColumn);
                    return getCellValue(fCell);
                }
            }
        }
        return null;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /** 
     * 创建新excel. 
     * @param fileDir  excel的路径 
     * @param sheetName 要创建的表格索引 
     * @param titleRow excel的第一行即表格头 
     */  
    public static void createExcel(String fileDir, String sheetName, String titleRow[]) throws Exception{   
        //新建文件  
        try (XSSFWorkbook workbook = new XSSFWorkbook();
                FileOutputStream out = new FileOutputStream(fileDir);){
            workbook.createSheet(sheetName);
            //添加表头  
            XSSFRow row = workbook.getSheet(sheetName).createRow(0);    //创建第一行    
            for(short i = 0;i < titleRow.length;i++){
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(titleRow[i]);
            }
            workbook.write(out);
        } catch (Exception e) {
            throw e;
        } 
    }

}

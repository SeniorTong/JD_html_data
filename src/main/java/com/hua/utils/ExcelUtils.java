package com.hua.utils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SeniorTong丶
 * @Date: 2020/11/5 10:30
 * @Version: 1.0
 */
public class ExcelUtils {


    /**
     * 读取excel表格中特定的列
     *
     * @param file  文件
     * @param index 第index列（0开始）
     * @throws Exception
     */
    public static List<String> readColumn(File file, int index) throws Exception {

        List<String> list = new ArrayList<>();
        InputStream inputStream = new FileInputStream(file.getAbsoluteFile());
        //新建并获取工作薄
        Workbook workbook = Workbook.getWorkbook(inputStream);
        //读取工作表 index从0开始，0对应Sheet1
        Sheet sheet = workbook.getSheet(0);
        //获取总行数
        int rows = sheet.getRows();
//        int columns = sheet.getColumns();
        for (int i = 1; i < rows; i++) {
            //获取单元格
            Cell cell = sheet.getCell(index, i);
            //读取单元格内容  并装载到list
            list.add(cell.getContents());
        }

        return list;
    }


    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeightInPoints(20);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);


        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式


        // 声明列对象
        HSSFCell cell = null;

        // 创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        // 创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                // 将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }

        return wb;
    }

}

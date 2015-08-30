package com.sunmeng.educationaladministration.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sunmeng.educationaladministration.utils.JsonUtil;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class JXLUtil {
    //顶部标题 宋体 18
    public static WritableFont arial18font = null;
    public static WritableCellFormat arial14format = null;

    public static WritableFont arial12font = null;
    public static WritableCellFormat arial12format = null;

    public static int index = 1;// 写入序号
    public static int row = 3;// 具体字段写入从第二行开始

    /**
     * 格式定义
     */
    public static void format() {
        try {
            arial18font = new WritableFont(WritableFont.ARIAL, 18,
                    WritableFont.BOLD);
            arial18font.setColour(jxl.format.Colour.BLACK);
            arial14format = new WritableCellFormat(arial18font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.MEDIUM);

            arial12font = new WritableFont(WritableFont.ARIAL,15,WritableFont.BOLD);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.MEDIUM);
            arial12format.setAlignment(jxl.format.Alignment.CENTRE);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入sheet工作簿
     */
    public static void initExcel(String fileName) {
        JXLUtil.index = 0;// 设置为初始值。不然static的index会一直递增
        JXLUtil.row = 3;
        format();// 先设置格式
        WritableWorkbook workbook = null;
        try {
            // WorkbookSettings setEncode = new WorkbookSettings(); // 设置读文件编码
            // setEncode.setEncoding(UTF8_ENCODING);
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }

            workbook = Workbook.createWorkbook(file);

            WritableSheet sheet = workbook.createSheet("中心教室机房占用表", 0);// 建立sheet

            sheet.mergeCells(0, 0, 14, 0);

            sheet.addCell((WritableCell) new Label(0, 0, "南校区教室机房占用表",
                    arial14format));// 表头设置完成

            sheet.mergeCells(0, 1, 0, 2);
            sheet.addCell((WritableCell) new Label(0, 1, "星期", arial12format));// 表头设置完成

            sheet.mergeCells(1, 1, 1, 2);
            sheet.addCell((WritableCell) new Label(1, 1, "日期", arial12format));// 表头设置完成

            sheet.mergeCells(2, 1, 2, 2);
            sheet.addCell((WritableCell) new Label(2, 1, "教室机房", arial12format));// 表头设置完成
            sheet.mergeCells(3, 1, 5, 1);

            sheet.addCell((WritableCell) new Label(3, 1, "上午 8：30-10：20",
                    arial12format));// 表头设置完成
            sheet.mergeCells(6, 1, 8, 1);
            sheet.addCell((WritableCell) new Label(6, 1, "上午 10：30-12：20",
                    arial12format));// 表头设置完成

            sheet.mergeCells(9, 1, 11, 1);
            sheet.addCell((WritableCell) new Label(9, 1, "下午 13：30-15：20",
                    arial12format));// 表头设置完成

            sheet.mergeCells(12, 1, 14, 1);
            sheet.addCell((WritableCell) new Label(12, 1, "下午 15：30-17：20",
                    arial12format));// 表头设置完成

            sheet.addCell((WritableCell) new Label(3, 2, "课程",
                    arial12format));// 表头设置完成
            sheet.addCell((WritableCell) new Label(4, 2, "班级编号",
                    arial12format));// 表头设置完成
            sheet.addCell((WritableCell) new Label(5, 2, "教师姓名",
                    arial12format));// 表头设置完成

            sheet.addCell((WritableCell) new Label(6, 2, "课程",
                    arial12format));// 表头设置完成
            sheet.addCell((WritableCell) new Label(7, 2, "班级编号",
                    arial12format));// 表头设置完成
            sheet.addCell((WritableCell) new Label(8, 2, "教师姓名",
                    arial12format));// 表头设置完成

            sheet.addCell((WritableCell) new Label(9, 2, "课程",
                    arial12format));// 表头设置完成
            sheet.addCell((WritableCell) new Label(10, 2, "班级编号",
                    arial12format));// 表头设置完成
            sheet.addCell((WritableCell) new Label(11, 2, "教师姓名",
                    arial12format));// 表头设置完成

            sheet.addCell((WritableCell) new Label(12, 2, "课程",
                    arial12format));// 表头设置完成
            sheet.addCell((WritableCell) new Label(13, 2, "班级编号",
                    arial12format));// 表头设置完成
            sheet.addCell((WritableCell) new Label(14, 2, "教师姓名",
                    arial12format));// 表头设置完成

            workbook.write();// 写入数据
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static <T> void writeObjListToExcel(String jsonStr, String fileName, Context c) {
        WritableWorkbook writebook = null;
        InputStream in = null;
        List<Map<String, Object>> temoList = null;
        try {
            /**
             * 读取原来写入的文件
             */
            // WorkbookSettings setEncode = new WorkbookSettings();
            // //设置读文件编码
            // setEncode.setEncoding(UTF8_ENCODING);
            in = new FileInputStream(new File(fileName));
            Workbook workbook = Workbook.getWorkbook(in);
            writebook = Workbook.createWorkbook(new File(fileName),
                    workbook);
            WritableSheet sheet = writebook.getSheet(0);
            int cellCount = 0;
            for (int i = 0; i < 7; i++) {
                cellCount = i * 12;
                temoList = JsonUtil.getJsonToListMap(jsonStr, i + 1);
                /**
                 * 合并单元格：星期，日期，教室机房
                 * */
                sheet.mergeCells(0, 3 + cellCount, 0, 14 + cellCount);
                sheet.mergeCells(1, 3 + cellCount, 1, 14 + cellCount);

                sheet.addCell((WritableCell) new Label(0, 3 + i, temoList.get(0).get("wename").toString(), arial12format));
                sheet.addCell((WritableCell) new Label(1, 3 + i, temoList.get(0).get("totime").toString(), arial12format));

                sheet.addCell((WritableCell) new Label(2, 3 + cellCount, "教室一", arial12format));
                sheet.addCell((WritableCell) new Label(2, 4 + cellCount, "教室二", arial12format));
                sheet.addCell((WritableCell) new Label(2, 5 + cellCount, "教室三", arial12format));
                sheet.addCell((WritableCell) new Label(2, 6 + cellCount, "教室四", arial12format));
                sheet.addCell((WritableCell) new Label(2, 7 + cellCount, "教室五", arial12format));
                sheet.addCell((WritableCell) new Label(2, 8 + cellCount, "教室六", arial12format));
                sheet.addCell((WritableCell) new Label(2, 9 + cellCount, "教室七", arial12format));
                sheet.addCell((WritableCell) new Label(2, 10 + cellCount, "教室八", arial12format));
                sheet.addCell((WritableCell) new Label(2, 11 + cellCount, "实训一", arial12format));
                sheet.addCell((WritableCell) new Label(2, 12 + cellCount, "实训二", arial12format));
                sheet.addCell((WritableCell) new Label(2, 13 + cellCount, "实训三", arial12format));
                sheet.addCell((WritableCell) new Label(2, 14 + cellCount, "阅览室", arial12format));
                for (Map map : temoList) {
                    int location=Integer.valueOf(map.get("location").toString());
                    int heng = (location + 1) / 4 + 1;//教室ID
                    int shu = (location + 1) % 4;//时间分类
                    if (shu == 0) {
                        shu = 4;
                        heng = heng - 1;
                    }
                    sheet.addCell(new Label(3*shu, 2+heng+cellCount, map.get("toname").toString(), arial12format));
                    sheet.addCell(new Label(3*shu+1,2+heng+cellCount, map.get("cnname").toString(), arial12format));
                    sheet.addCell(new Label(3*shu+2,2+heng+cellCount, map.get("tename").toString(), arial12format));
                }
            }
            writebook.write();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writebook != null) {
                try {
                    writebook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

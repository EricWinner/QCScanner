package com.example.ubuntu.qc_scanner.util;


import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.mode.ExcelData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ubuntu on 17-8-15.
 */

public class ExcelUtil {

    private static final String TAG = "ExcelUtil";
    private static Context mContext;
    //内存地址
    public static String root = Environment.getExternalStorageDirectory()
            .getPath();

    public static void writeExcel(Context context, List<ExcelData> exportOrder,
                                  String fileName) throws Exception {
        mContext = context;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && getAvailableStorage() > 1000000) {
            Toast.makeText(context, "SD卡不可用", Toast.LENGTH_LONG).show();
            return;
        }
        File dir = new File(context.getExternalFilesDir(null).getPath());
        Log.d(TAG, "writeExcel fileName = " + fileName);
        File file = new File(dir, fileName + ".xls");
        if (!dir.exists()) {
            Log.d(TAG, "create dir !");
            dir.mkdirs();
        }
        // 创建Excel工作表
        WritableWorkbook writeWorkbook;
        OutputStream os = new FileOutputStream(file);
        writeWorkbook = Workbook.createWorkbook(os);
        // 添加第一个工作表并设置第一个Sheet的名字
        WritableSheet sheet = writeWorkbook.createSheet(mContext.getString(R.string.qrdata_table), 0);
        setExcelTitle(sheet);
        setExcelData(exportOrder, sheet);
        writeWorkbook.write();
        writeWorkbook.close();
        Toast.makeText(context, "写入成功", Toast.LENGTH_LONG).show();
    }

    private static void setExcelData(List<ExcelData> exportOrder, WritableSheet sheet) throws WriteException {
        for (int i = 0; i < exportOrder.size(); i++) {
            ExcelData order = exportOrder.get(i);
            Label idLabel = new Label(0, i + 1, order.id);
            Label groupIDLabel = new Label(1, i + 1, order.groupID);
            Label nameLabel = new Label(2, i + 1, order.numberID);
            Label dateLabel = new Label(3, i + 1, order.dateTime);
            Label valleyLabel = new Label(4, i + 1, order.valleyValue);
            Label peakLabel = new Label(5, i + 1, order.peakValue);
            Label totalLabel = new Label(6, i + 1, order.totalValue);
            sheet.addCell(idLabel);
            sheet.addCell(groupIDLabel);
            sheet.addCell(nameLabel);
            sheet.addCell(dateLabel);
            sheet.addCell(valleyLabel);
            sheet.addCell(peakLabel);
            sheet.addCell(totalLabel);
        }
    }

    private static void setExcelTitle (WritableSheet sheet) {
        String columnFirst = mContext.getString(R.string.qrdata_id);
        String columnSecond = mContext.getString(R.string.qrdata_group_name);
        String columnThird = mContext.getString(R.string.qrdata_number);
        String columnFourth = mContext.getString(R.string.qrdata_date);
        String columnFifth = mContext.getString(R.string.qrdata_valley_value);
        String columnSixth = mContext.getString(R.string.qrdata_peak_value);
        String columnSeventh = mContext.getString(R.string.qrdata_total_value);

        Label label;
        String[] title = {columnFirst, columnSecond, columnThird, columnFourth, columnFifth, columnSixth, columnSeventh};
        for (int i = 0; i < title.length; i++) {
            // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
            // 在Label对象的子对象中指明单元格的位置和内容
            label = new Label(i, 0, title[i], getHeader());
            // 将定义好的单元格添加到工作表中
            try {
                sheet.addCell(label);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }


    public static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);// 定义字体
        try {
            font.setColour(Colour.BLUE);// 蓝色字体
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            // format.setBorder(Border.ALL, BorderLineStyle.THIN,
            // Colour.BLACK);// 黑色边框
            // format.setBackground(Colour.YELLOW);// 黄色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 获取SD可用容量
     */
    private static long getAvailableStorage() {
        StatFs statFs = new StatFs(root);
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        long availableSize = blockSize * availableBlocks;
        // Formatter.formatFileSize(context, availableSize);
        return availableSize;
    }
}

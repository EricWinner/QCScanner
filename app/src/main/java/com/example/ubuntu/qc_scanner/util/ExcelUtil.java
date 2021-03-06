package com.example.ubuntu.qc_scanner.util;


import com.example.ubuntu.qc_scanner.R;
import com.example.ubuntu.qc_scanner.mode.ExcelDataItem;
import com.example.ubuntu.qc_scanner.mode.IQRDataItem;

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

    public static final String OWNER_CREATE_EXCEL_PATH = Environment.getExternalStorageDirectory().toString() + "/QRData";

    private static Context mContext;

    private static String mUserName;
    private static String mDateTime;
    //内存地址
    public static String root = Environment.getExternalStorageDirectory()
            .getPath();

    public static void writeExcel(Context context, List<IQRDataItem> exportOrder,
                                  String fileName) throws Exception {
        mContext = context;
        File file = createQRDataPath(context, fileName);
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
    }

    private static File createQRDataPath(Context context, String fileName) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SD卡不可用", Toast.LENGTH_LONG).show();
            return null;
        }
        File file = null;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            String path = OWNER_CREATE_EXCEL_PATH;
            File destDir = new File(path);
            if (!destDir.exists()) {
                boolean result = destDir.mkdir();
                Log.d(TAG, "createQRDataPath mkdirs , destDir = " + destDir.getPath() + ",success = " + result);
                if (!result) {
                    Log.d(TAG, "create folder :" + path + ", failed.");
                }
            }
            file = new File(destDir, fileName + ".xls");
        }
        return file;
    }


    private static void setExcelData(List<IQRDataItem> exportOrder, WritableSheet sheet) throws WriteException {
        int exportOrderSize = exportOrder.size();
        for (int i = 0; i < exportOrderSize; i++) {
            IQRDataItem order = exportOrder.get(i);
            Label idLabel       = new Label(0, i + 1, order.getId());
            Label groupIDLabel  = new Label(1, i + 1, order.getGroupID());
            Label nameLabel     = new Label(2, i + 1, order.getNumberID());
            Label totalLabel    = new Label(3, i + 1, order.getTotalValue());
            Label valleyLabel   = new Label(4, i + 1, order.getValleyValue());
            Label caseNameLabel = new Label(5, i + 1, order.getCaseName());
            Label caseTypeLabel = new Label(6, i + 1, order.getCaseType());

            Log.d(TAG, "order.getId() = " + order.getId() + ",order.getGroupID() = " + order.getGroupID() + ",order.getNumberID() = " + order.getNumberID());
            Log.d(TAG, "order.getTotalValue() = " + order.getTotalValue() + ",order.getValleyValue() = " + order.getValleyValue() + ",order.getNumberID() = " + order.getCaseName() + ",order.getCaseType() = " + order.getCaseType());
            sheet.addCell(idLabel);
            sheet.addCell(groupIDLabel);
            sheet.addCell(nameLabel);
            sheet.addCell(totalLabel);
            sheet.addCell(valleyLabel);
            sheet.addCell(caseNameLabel);
            sheet.addCell(caseTypeLabel);
        }

        addExcelUserInfo(sheet, exportOrderSize);
    }

    private static void addExcelUserInfo(WritableSheet sheet, int exportOrderSize) throws WriteException {
        UserLoginInfoUtil.UserInfo userinfo = UserLoginInfoUtil.getInstance().getUserInfo(mContext);
        mUserName = userinfo.getUsername();
        mDateTime = userinfo.getDateTime();
        Log.d(TAG, "setExcelData mUserName = " + mUserName + ",mDateTime = " + mDateTime);
        Label idLabel            = new Label(0, exportOrderSize + 5, "");
        Label groupIDLabel       = new Label(1, exportOrderSize + 5, "");
        Label nameLabel          = new Label(2, exportOrderSize + 5, "");
        Label totalLabel         = new Label(3, exportOrderSize + 5, "");
        Label valleyLabel        = new Label(4, exportOrderSize + 5, "");
        Label usernameLabelTitle = new Label(5, exportOrderSize + 5, mContext.getString(R.string.qrdata_excel_username));
        Label usernameLabelName  = new Label(6, exportOrderSize + 5, mUserName);

        sheet.addCell(idLabel);
        sheet.addCell(groupIDLabel);
        sheet.addCell(nameLabel);
        sheet.addCell(totalLabel);
        sheet.addCell(valleyLabel);
        sheet.addCell(usernameLabelTitle);
        sheet.addCell(usernameLabelName);

        Label idLabel2           = new Label(0, exportOrderSize + 6, "");
        Label groupIDLabel2      = new Label(1, exportOrderSize + 6, "");
        Label nameLabel2         = new Label(2, exportOrderSize + 6, "");
        Label totalLabel2        = new Label(3, exportOrderSize + 6, "");
        Label valleyLabel2       = new Label(4, exportOrderSize + 6, "");
        Label datetimeLabelTitle = new Label(5, exportOrderSize + 6, mContext.getString(R.string.qrdata_excel_datetime));
        Label datetimeLabelName  = new Label(6, exportOrderSize + 6, mDateTime);

        sheet.addCell(idLabel2);
        sheet.addCell(groupIDLabel2);
        sheet.addCell(nameLabel2);
        sheet.addCell(totalLabel2);
        sheet.addCell(valleyLabel2);
        sheet.addCell(datetimeLabelTitle);
        sheet.addCell(datetimeLabelName);
    }

    private static void setExcelTitle(WritableSheet sheet) {
        String columnFirst = mContext.getString(R.string.qrdata_id);
        String columnSecond = mContext.getString(R.string.qrdata_group_name);
        String columnThird = mContext.getString(R.string.qrdata_number);
        String columnFourth = mContext.getString(R.string.qrdata_total_value);
        String columnFifth = mContext.getString(R.string.qrdata_valley_value);
        String columnSixth = mContext.getString(R.string.qrdata_case_name);
        String columnSeventh = mContext.getString(R.string.qrdata_case_type);

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
            font.setColour(Colour.BLACK);// 黑色字体
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

package com.lyloou.lou.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016/8/5 14:31
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2016/8/5 14:31        Lou                 1.0             1.0
 * Why & What is modified:
 */
public class Uimg {

    private static final String TAG = "Uimg";

    /**
     * 功能：通过view获取bitmap
     * 参考资料：http://stackoverflow.com/questions/2339429/android-view-getdrawingcache-returns-null-only-null?noredirect=1&lq=1
     *
     * @param v
     * @return 以view形式存在的bitmap
     */
    public static Bitmap getBitmap(View v) {
        // 获取bgBitmap
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap bgBitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache
        return bgBitmap;
    }

    // 保存到本地，并将名称添加到数据库中；
    // 注意文件名称不要包括「/」符号，通过openFileOutput保存的只能是“文件”不能是“文件夹/文件名”；
    public static String saveBitmapToLocal(Context context, Bitmap bitmap, String fileName) {
        // 获取时间作为文件名称后缀；
        // fileName = fileName + Udate.format(new Date(), "yyyy_MM_dd_HH_mm_ss_SS", Locale.US) + "_" + new Random().nextInt(100) + ".png";
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    Log.d(TAG, "saveBitmapToLocal: saved to -->" + fileName);
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }

    // 从本地获取Bitmap对象；
    public static Bitmap loadBitmapFromLocal(Context context, String fileName) {
        Bitmap bitmap = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

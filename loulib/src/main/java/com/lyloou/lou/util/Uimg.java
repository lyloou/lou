package com.lyloou.lou.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    /**
     * 功能：获得圆角bitmap
     * 参考资料：http://blog.sina.com.cn/s/blog_5a6f39cf0101aqsw.html
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void deleteDirContent(Context context, String dirPath) {
        File dir = new File(Environment.getExternalStorageDirectory(), dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                if (file.delete()) {
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://" + file.getAbsolutePath())));
                }
            } else if (file.isDirectory()) {
                deleteDirContent(context, dirPath); // 递规的方式删除文件夹
            }
        }
        dir.delete();// 删除目录本身
    }

    public static void saveImageToGallery(Context context, String imageUrl, String dir) throws IOException {
        Bitmap bmp = BitmapFactory.decodeStream(getImageStream(imageUrl));
        if (bmp == null) {
            throw new IOException("translate imageUrl to bmp failed");
        }
        saveImageToGallery(context, bmp, dir);
    }

    public static void saveImageToGallery(Context context, Bitmap bmp, String dir) throws IOException {
        File file = toFile(bmp, dir, System.currentTimeMillis() + ".jpg");
        if (file == null) {
            return;
        }

        String path = file.getAbsolutePath();

        // 把文件插入到系统图库
        MediaStore.Images.Media.insertImage(context.getContentResolver(), path, "", null);
        // 通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }


    public static File toFile(@NonNull Bitmap bmp, String dir, String fileName) throws IOException {
        File appDir = new File(Environment.getExternalStorageDirectory(), dir);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File file = new File(appDir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return file;
    }

    public static File toFile(String imageUrl, String dir, String fileName) throws IOException {
        Bitmap bmp = BitmapFactory.decodeStream(getImageStream(imageUrl));
        if (bmp == null) {
            return null;
        }
        return toFile(bmp, dir, fileName);
    }

    public static InputStream getImageStream(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    private static final int CAMERA_REQUEST_CODE = 1;

    /**
     * request permission before call runnable
     * Note: need add permission
     * i.e.
     * <pre>
     * @<code>
     *     Uimage.doImageTaskInCompatible(activity, () -> {
     *         try {
     *             String imageUrl = args.getString(0);
     *             Uimage.saveImageToGallery(activity, imageUrl, IMAGE_PATH);
     *         } catch (Exception e) {
     *             // do error
     *         }
     *     });
     * </code>
     * </pre>
     *
     * @param activity
     * @param runnable
     */
    public static void doImageTaskInCompatible(Activity activity, Runnable runnable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            runnable.run();
            return;
        }

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            runnable.run();
        } else {
            //申请STORAGE权限
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    CAMERA_REQUEST_CODE);
        }

    }
}


## 微信多图片分享功能

- [微信分享多张图片 - 简书](http://www.jianshu.com/p/15c50ccf376a)
```java
Intent intent = new Intent();

// intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")); // 分享给朋友
intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI")); // 分享到朋友圈
// intent.setAction("android.intent.action.SEND"); // 分享单张
intent.setAction("android.intent.action.SEND_MULTIPLE"); // 分享多张
intent.setType("image/*");
intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(pictruePath))); //图片数据（支持本地图片的Uri形式）
intent.putExtra("Kdescription", description); // 微信分享页面，图片上边的描述（当为`ShareImgUI`的时候，该属性被忽略）

context.startActivity(intent);
```


- [Glide加载图片并保存到本地相册 - w372426096的博客 - CSDN博客](http://blog.csdn.net/w372426096/article/details/52472984)
```java
Bitmap bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

FileOutputStream fos = null;
try {
    fos = new FileOutputStream(currentFile);
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
    fos.flush();
} catch (FileNotFoundException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    try {
        if (fos != null) {
            fos.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```


- [【android】getCacheDir()、getFilesDir()、getExternalFilesDir()、getExternalCacheDir()的作用，getfilesdir_Android教程 | 帮客之家](http://www.bkjia.com/Androidjc/887665.html)
```java
public String getDiskCacheDir(Context context) {
    String cachePath = null;
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            || !Environment.isExternalStorageRemovable()) {
        cachePath = context.getExternalCacheDir().getPath();
    } else {
        cachePath = context.getCacheDir().getPath();
    }
    return cachePath;
}
```


- [Android 删除SD卡文件和文件及创建文件夹和文件](http://www.2cto.com/kf/201408/327957.html)
```java
package com.jiub.client.mobile.addphoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtils {

    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/formats/";//获取文件夹
    //保存图片
    public static void saveBitmap(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        Log.d("text", SDPATH);
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            File f = new File(SDPATH, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.e("", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }
    //删除文件
    public static void delFile(String fileName){
        File file = new File(SDPATH + fileName);
        if(file.isFile()){
            file.delete();
        }
        file.exists();
    }
    //删除文件夹和文件夹里面的文件
    public static void deleteDir() {
        File dir = new File(SDPATH);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

}
```
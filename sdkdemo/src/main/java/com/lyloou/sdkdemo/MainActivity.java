/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.sdkdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String getImageFilePathFromImageUrl(Context context, String imageUrl) {
        int lastIndexOf = imageUrl.lastIndexOf('/');
        int length = imageUrl.length();
        if (lastIndexOf + 1 == length) {
            return null;
        }
        String imgName = imageUrl.substring(lastIndexOf + 1, length);
        String[] split = imgName.split("\\.");
        String suffix = split[split.length - 1];


        String url = imageUrl;
        String fileName = imgName;
        String postfix = suffix;
        FileOutputStream fileOutputStream = null;
        try {
            // 下载图片
            Bitmap bitmap = Glide.with(context.getApplicationContext())
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

            // 保存图片
            File imgDir = new File(getDiskCacheDir(context), "image_caches");
            if (!imgDir.exists()) {
                imgDir.mkdirs();
            }
            File imgFile = new File(imgDir, fileName);
            fileOutputStream = new FileOutputStream(imgFile);
            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            if (postfix.equalsIgnoreCase("png")) {
                compressFormat = Bitmap.CompressFormat.PNG;
            } else if (postfix.equalsIgnoreCase("jpg")) {
                compressFormat = Bitmap.CompressFormat.JPEG;
            }
            bitmap.compress(compressFormat, 100, fileOutputStream);
            fileOutputStream.flush();
            return imgFile.getAbsolutePath();

        } catch (InterruptedException | IOException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void sharePicsToWechatMoments(Context context, String description, List<String> paths) {

        Intent intent = new Intent();
        String SHARE_TYPE_TIMELINE = "com.tencent.mm.ui.tools.ShareToTimeLineUI"; // 发送多张图片到朋友圈
        String SHARE_TYPE_FRIEND = "com.tencent.mm.ui.tools.ShareImgUI"; // 发送多张图片给朋友

        intent.setComponent(new ComponentName("com.tencent.mm", SHARE_TYPE_TIMELINE));
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        ArrayList<Uri> imageList = new ArrayList();
        for (String picPath : paths) {
            File f = new File(picPath);
            if (f.exists()) {
                imageList.add(Uri.fromFile(f));
            }
        }
        if (imageList.size() == 0) {
            Toast.makeText(context, "图片不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageList); //图片数据（支持本地图片的Uri形式）
        intent.putExtra("Kdescription", description); //微信分享页面，图片上边的描述
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String imageFilePathFromImageUrl = getImageFilePathFromImageUrl(MainActivity.this, "http://avatar.csdn.net/A/E/9/1_w372426096.jpg");
                String imageFilePathFromImageUrl2 = getImageFilePathFromImageUrl(MainActivity.this, "http://cdn2.jianshu.io/assets/default_avatar/1-04bbeead395d74921af6a4e8214b4f61.jpg");
                String imageFilePathFromImageUrl3 = getImageFilePathFromImageUrl(MainActivity.this, "http://upload-images.jianshu.io/upload_images/1835526-496467cd0d0847a2.jpg");
                String imageFilePathFromImageUrl4 = getImageFilePathFromImageUrl(MainActivity.this, "http://upload-images.jianshu.io/upload_images/1835526-f3764c1a4b8af50d.jpg");
                List<String> paths = new ArrayList<String>();
                paths.add(imageFilePathFromImageUrl);
                paths.add(imageFilePathFromImageUrl2);
                paths.add(imageFilePathFromImageUrl3);
                paths.add(imageFilePathFromImageUrl4);
                sharePicsToWechatMoments(MainActivity.this, "你是我的玛尼玛尼哄", paths);
            }
        }).start();
    }
}

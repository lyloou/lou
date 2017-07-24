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

package com.lyloou.test.gank;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.lyloou.test.common.NetWork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class Ushare {
    public static final String SHARE_TYPE_TIMELINE = "com.tencent.mm.ui.tools.ShareToTimeLineUI"; // 发送多张图片到朋友圈
    public static final String SHARE_TYPE_FRIEND = "com.tencent.mm.ui.tools.ShareImgUI"; // 发送多张图片给朋友

    public static String loadWelfareUrl(String activeDay) {
        String[] split = activeDay.split("-");
        if (split.length == 3) {
            String year = split[0];
            String month = split[1];
            String day = split[2];

            Call<ResponseBody> gankData = NetWork.getGankApi().getGankData(year, month, day);
            Response<ResponseBody> response = null;
            try {
                response = gankData.execute();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body == null) {
                        return null;
                    }
                    String string = body.string();
                    JSONObject jsonObject = new JSONObject(string);
                    JSONObject results = jsonObject.getJSONObject("results");
                    JSONArray welfares = results.getJSONArray("福利");
                    JSONObject welfare = welfares.getJSONObject(0);
                    System.out.println(welfare);
                    String welfareUrl = welfare.getString("url");
                    return welfareUrl;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

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
        Context applicationContext = context.getApplicationContext();
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
            Bitmap bitmap = Glide.with(applicationContext)
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

            // 保存图片
            File imgDir = new File(getDiskCacheDir(applicationContext), "image_caches");
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

    public static void sharePicsToWechatMoments(Context context, String description, List<String> paths, String shareTypeTimeline) {

        Intent intent = new Intent();

        intent.setComponent(new ComponentName("com.tencent.mm", shareTypeTimeline));
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        ArrayList<Uri> imageList = new ArrayList();
        for (String picPath : paths) {
            if (TextUtils.isEmpty(picPath)) {
                continue;
            }
            File f = new File(picPath);
            if (f.exists()) {
                imageList.add(Uri.fromFile(f));
            }
        }
        if (imageList.size() == 0) {
            System.out.println("图片不存在");
            return;
        }
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageList); //图片数据（支持本地图片的Uri形式）
        intent.putExtra("Kdescription", description); //微信分享页面，图片上边的描述
        context.startActivity(intent);
    }
}

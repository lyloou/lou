
- [okHttp3](http://square.github.io/okhttp/)
```groovy
compile 'com.squareup.okhttp3:okhttp:3.11.0'
```

```java
package com.lyloou.lou.util.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UokHttp {
    private static UokHttp instance;
    private OkHttpClient client = new OkHttpClient();

    public static UokHttp init() {
        if (instance == null) {
            synchronized (UokHttp.class) {
                if (instance == null) {
                    instance = new UokHttp();
                }
            }
        }
        return instance;
    }

    public void post(final ICallBack callBack, String json) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(callBack.getUrl())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.doError("请求失败:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    callBack.doError("返回数据为空");
                    return;
                }
                try {
                    callBack.doResponse(new JSONObject(responseBody.string()));

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.doError(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.doError("返回数据异常:" + e.getMessage());
                }
            }
        });
    }


    public void get(final ICallBack callBack) {
        Request request = new Request.Builder()
                .url(callBack.getUrl())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.doError("请求失败:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody responseBody = response.body();
                    if (responseBody == null) {
                        callBack.doError("返回数据为空");
                        return;
                    }
                    callBack.doResponse(new JSONObject(responseBody.string()));
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.doError(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.doError("返回数据异常");
                }
            }
        });
    }

    // https://stackoverflow.com/questions/35622676/image-upload-using-okhttp
    public void postFile(final ICallBack callBack, String filePath) {
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "image",
                        filename,
                        RequestBody.create(MEDIA_TYPE_PNG, new File(filePath)))
                .build();

        Request request = new Request.Builder()
                .url(callBack.getUrl())
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.doError("请求失败:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    callBack.doError("返回数据为空");
                    return;
                }
                try {
                    callBack.doResponse(new JSONObject(responseBody.string()));

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.doError(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.doError("返回数据异常:" + e.getMessage());
                }
            }
        });
    }
}

```

- [okHttp3](http://square.github.io/okhttp/)
```groovy
implementation 'com.squareup.okhttp3:okhttp:3.11.0'
implementation 'com.squareup.okhttp3:logging-interceptor:3.5.0'
```

```java
import android.util.Log;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Log.d("HttpLogInfo", message);
    }
}
```

```java
import android.webkit.CookieManager;

import com.renrenyoupin.activity.util.Ustring;

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
import okhttp3.logging.HttpLoggingInterceptor;

// https://www.jianshu.com/p/1873287eed87
public class UokHttp {
    private static final String ERR_IO = "IO异常";
    private static final String ERR_BACK_NULL = "返回数据为空";
    private static final String ERR_BACK_DATA = "返回数据异常";
    public static final String DELIMITER = ", ";

    private static UokHttp instance;
    private OkHttpClient client;

    private UokHttp() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(logInterceptor)
                .build();
    }

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
                callBack.doError(Ustring.join(DELIMITER, ERR_IO, e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    callBack.doError(ERR_BACK_NULL);
                    return;
                }
                try {
                    callBack.doResponse(new JSONObject(responseBody.string()));

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.doError(Ustring.join(DELIMITER, ERR_IO, e.getMessage()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.doError(Ustring.join(DELIMITER, ERR_BACK_DATA, e.getMessage()));
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
                callBack.doError(Ustring.join(DELIMITER, ERR_IO, e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody responseBody = response.body();
                    if (responseBody == null) {
                        callBack.doError(ERR_BACK_NULL);
                        return;
                    }
                    callBack.doResponse(new JSONObject(responseBody.string()));
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.doError(Ustring.join(DELIMITER, ERR_IO, e.getMessage()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.doError(Ustring.join(DELIMITER, ERR_BACK_DATA, e.getMessage()));
                }
            }
        });
    }

    public String getCookie(String siteName, String cookieName) {
        String cookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        if (cookies != null) {
            String[] temp = cookies.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(cookieName)) {
                    String[] temp1 = ar1.split("=");
                    cookieValue = temp1[1];
                }
            }
        }
        return cookieValue;
    }

    // https://stackoverflow.com/questions/35622676/image-upload-using-okhttp
    public void postImageFile(final ICallBack callBack, String filePath) {
        File file = new File(filePath);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",
                        file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        Request request = new Request.Builder()
                .url(callBack.getUrl())
                .post(requestBody)
                .addHeader("Accept", "*/*")
                .addHeader("cookie", "ci_session=" + getCookie(APP_URL, "ci_session"))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.doError(Ustring.join(DELIMITER, ERR_IO, e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    callBack.doError(ERR_BACK_NULL);
                    return;
                }
                try {
                    callBack.doResponse(new JSONObject(responseBody.string()));
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.doError(Ustring.join(DELIMITER, ERR_IO, e.getMessage()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.doError(Ustring.join(DELIMITER, ERR_BACK_DATA, e.getMessage()));
                }
            }
        });
    }
}


```
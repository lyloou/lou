- [Volley overview](https://developer.android.com/training/volley/)
```groovy
compile 'com.android.volley:volley:1.1.1'
```

```java
package com.lyloou.lou.util.http;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：Volley工具类
 * 创建人： Lou
 * 创建时间： 2016/8/1 17:38
 * 修改人： Lou
 * 修改时间：2016/8/1 17:38
 * 修改备注：
 * 注意： 需要权限支持（<uses-permission android:name="android.permission.INTERNET" />）；
 * 参考资料：http://bxbxbai.github.io/2014/09/14/android-working-with-volley/
 * 应用场合：这里主要针对，用户模块的网络封装（获取验证码，忘记密码，用户登陆，用户注册等）；其他场合使用时请具体问题具体对待；
 */

public class Uvolley {

    private static final String TAG = "Uvolley";
    private static Uvolley INSTANCE;
    private final RequestQueue mRequestQueue;

    private Uvolley(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static Uvolley init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Uvolley(context);
        }
        return INSTANCE;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        judgeInitialized();
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(30000, 2, 1f));
        mRequestQueue.add(req);
    }

    private void judgeInitialized() {
        if (INSTANCE == null) {
            throw new NullPointerException("请先调用init(context)进行初始化");
        }
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG);
    }

    public void cancelPendingRequests(Object tag) {
        judgeInitialized();
        mRequestQueue.cancelAll(tag);
    }


    public void post(final ICallBack callBack, String jsonObjectString) {
        JsonRequest<JSONObject> jsonRequest2 = new JsonRequest<JSONObject>(
                Request.Method.POST,
                callBack.getUrl(),
                jsonObjectString,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callBack.doResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        JSONObject errJson = new JSONObject();
                        try {
                            if (error != null) {
                                errJson.put("errMsg", error.getMessage());

                                NetworkResponse networkResponse = error.networkResponse;
                                if (networkResponse != null) {
                                    String dataStr = new String(networkResponse.data);
                                    int code = networkResponse.statusCode;
                                    Map<String, String> headers = networkResponse.headers;
                                    errJson.put("code", code);
                                    errJson.put("header", headers);
                                    errJson.put("data", dataStr);
                                } else {
                                    errJson.put("result", "NetworkResponse=null");
                                }
                            } else {
                                errJson.put("result", "VolleyError=null");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callBack.doError(errJson.toString());
                    }
                }
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        addToRequestQueue(jsonRequest2);
    }

    public void get(final ICallBack callBack) {
        final JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(
                        Request.Method.GET,
                        callBack.getUrl(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                callBack.doResponse(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                callBack.doError(error.getMessage());
                            }
                        }
                );
        addToRequestQueue(jsonObjReq);
    }

}
```
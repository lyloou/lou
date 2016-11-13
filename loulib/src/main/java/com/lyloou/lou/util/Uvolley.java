package com.lyloou.lou.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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
    private final RequestQueue mRequestQueue;
    private static Uvolley INSTANCE;

    private Uvolley(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }


    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static Uvolley init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Uvolley(context);
        }
        return INSTANCE;
    }

    public static Uvolley getInstance() {
        if (INSTANCE == null) {
            throw new NullPointerException("请先调用init(context)进行初始化");
        }
        return INSTANCE;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG);
    }

    public void cancelPendingRequests(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public static interface ICallBackForPost {
        String getUrl();

        void doResponse(String responseInfo);

        void doError(String errorMsg);

        void assignParams(Map<String, String> params);
    }

    public void doPostForStringRequest(final ICallBackForPost callBack) {
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                callBack.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        callBack.doResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        callBack.doError(error.getMessage());
                    }

                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                callBack.assignParams(params);
                return params;
            }
        };
        addToRequestQueue(sr);
    }

    public void doPostForJsonObjectRequest(final ICallBackForPost callBack, JSONObject jsonObject) {
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                callBack.getUrl(),
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callBack.doResponse(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.doError(error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        addToRequestQueue(jsonRequest);
    }

    public interface ICallBackForGet {
        String getUrl();

        void doResponse(String responseInfo);

        void doError(String errorMsg);
    }

    public void doGetForJsonObjectRequest(final ICallBackForGet callBack) {
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                callBack.getUrl(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        callBack.doResponse(response.toString());
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        callBack.doError(error.getMessage());
                    }
                }
        );
        addToRequestQueue(jsonObjReq);
    }

    public void doGetForStringRequest(final ICallBackForGet callBack) {
        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                callBack.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        callBack.doResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        callBack.doError(error.getMessage());
                    }
                }
        );
        addToRequestQueue(request);
    }
}

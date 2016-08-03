package com.lyloou.lou.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
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

    public static Uvolley init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Uvolley(context);
        }
        return INSTANCE;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        if (INSTANCE == null) {
            throw new NullPointerException("请先调用init(context)进行初始化");
        }
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG);
    }

    public void cancelPendingRequests(Object tag) {
        if (INSTANCE == null) {
            throw new NullPointerException("请先调用init(context)进行初始化");
        }
        mRequestQueue.cancelAll(tag);
    }

    ///////////////////////////////////////////////////////////////////////////
    // post
    ///////////////////////////////////////////////////////////////////////////
    public interface ICallBackForPost {
        String getUrl();

        void doResponse(ResponseInfo responseInfo);

        void doError();

        void assignParams(Map<String, String> params);
    }

    public void post(final ICallBackForPost callBack) {
        StringRequest sr = new StringRequest(Request.Method.POST, callBack.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                ResponseInfo info = ResponseInfo.valueOf(response);
                if (info != null) {
                    callBack.doResponse(info);
                } else {
                    callBack.doError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                callBack.doError();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                callBack.assignParams(params);
                return params;
            }
        };
        addToRequestQueue(sr);
    }

    ///////////////////////////////////////////////////////////////////////////
    // get
    ///////////////////////////////////////////////////////////////////////////
    public interface ICallBackForGet {
        String getUrl();

        void doResponse(ResponseInfo responseInfo);

        void doError();
    }

    public void get(final ICallBackForGet callBack) {
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, callBack.getUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        ResponseInfo info = ResponseInfo.valueOf(response);
                        if (info != null) {
                            callBack.doResponse(info);
                        } else {
                            callBack.doError();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                callBack.doError();
            }
        });
        addToRequestQueue(jsonObjReq);
    }

    ///////////////////////////////////////////////////////////////////////////
    // response info 返回的信息
    // 格式1：{"status":4,"message":"手机号已注册","data":[]} // 其中，data是jsonObject字符串；
    // 格式2：{"status":0,"message":"操作成功","data":[]}
    ///////////////////////////////////////////////////////////////////////////
    public static class ResponseInfo {
        public final int status;
        public final String message;
        public final String data;

        public ResponseInfo(int status, String message, String data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }


        public static ResponseInfo valueOf(JSONObject jsonObject) {
            ResponseInfo info = null;
            try {
                info = new ResponseInfo
                        (
                                jsonObject.getInt("status"),
                                jsonObject.getString("message"),
                                jsonObject.getString("data")
                        );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return info;
        }

        public static ResponseInfo valueOf(String jsonString) {
            ResponseInfo info = null;
            try {
                info = ResponseInfo.valueOf(new JSONObject(jsonString));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return info;
        }


        @Override
        public String toString() {
            return "\n\nstatus: " + status + " \nmessage:" + message + " \ndata:" + data;
        }
    }

}

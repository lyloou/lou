package com.lyloou.demo;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uvolley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LouActivity {


    @BindView(R.id.textView)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }


    public static final String URL_JSON_OBJECT_REQUEST = "http://api.androidhive.info/volley/person_object.json";
    public static final String URL_STRING_REQUEST = "http://api.androidhive.info/volley/string_response.html";

    private void initView() {
        mTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uvolley.getInstance().doGetForStringRequest(new Uvolley.ICallBackForGet() {
                    @Override
                    public String getUrl() {
                        return URL_STRING_REQUEST;
                    }

                    @Override
                    public void doResponse(final String responseInfo) {
                        showResult("\ndoGetForStringRequest:\n" + responseInfo);

                    }

                    @Override
                    public void doError(String errorMsg) {
                        showResult("\n" + "doGetForStringRequest:\n error");
                    }
                });
            }
        }, 1000);

        mTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uvolley.getInstance().doGetForJsonObjectRequest(new Uvolley.ICallBackForGet() {
                    @Override
                    public String getUrl() {
                        return URL_JSON_OBJECT_REQUEST;
                    }

                    @Override
                    public void doResponse(final String responseInfo) {
                        showResult("\ndoGetForJsonObjectRequest:\n" + responseInfo);

                    }

                    @Override
                    public void doError(String errorMsg) {
                        showResult("\n" + "doGetForJsonObjectRequest:\n error");
                    }
                });
            }
        }, 2000);

        mTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uvolley.getInstance().doPostForJsonObjectRequest(new Uvolley.ICallBackForPost() {
                    @Override
                    public String getUrl() {
                        return URL_STRING_REQUEST;
                    }

                    @Override
                    public void doResponse(String responseInfo) {
                        showResult("\ndoPostForJsonObjectRequest:\n" + responseInfo);
                    }

                    @Override
                    public void doError(String errorMsg) {
                        showResult("\n" + "doPostForJsonObjectRequest:\n error");
                    }

                    @Override
                    public void assignParams(Map<String, String> params) {
                        params.put("name", "Lyloou");
                        params.put("email", "lyloou6@gmail.com");
                        params.put("password", "lllooouuu");
                    }
                }, null);
            }
        }, 3000);

        mTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uvolley.getInstance().doPostForStringRequest(new Uvolley.ICallBackForPost() {
                    @Override
                    public String getUrl() {
                        return URL_STRING_REQUEST;
                    }

                    @Override
                    public void doResponse(String responseInfo) {
                        showResult("\ndoPostForStringRequest:\n" + responseInfo);
                    }

                    @Override
                    public void doError(String errorMsg) {
                        showResult("\n" + "doPostForStringRequest:\n error");

                    }

                    @Override
                    public void assignParams(Map<String, String> params) {
                    }
                });
            }
        }, 4000);
    }

    private void showResult(final String responseInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String text = mTextView.getText().toString() + "\n\n==========\n" + responseInfo;
                mTextView.setText(text);
            }
        });
    }

}

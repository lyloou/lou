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

package com.lyloou.test.common.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lyloou.test.R;
import com.lyloou.test.util.Unet;
import com.lyloou.test.util.Usp;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_URL = "EXTRA_DATA_URL";
    private static String sKey;
    private String mUrl;
    private WebView mWvContent;
    private Activity mContext;

    public static void newInstance(Context context, String url, String tag) {
        sKey = context.getClass().getSimpleName().toUpperCase() + "_" + tag;

        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_DATA_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mContext = this;

        Usp.init(mContext);
        initData();
        initView();
    }

    private void initData() {
        if (!Unet.isAvailable(mContext)) {
            Toast.makeText(mContext, "没网络了", Toast.LENGTH_LONG).show();
            return;
        }

        if (!TextUtils.isEmpty(sKey)) {
            mUrl = Usp.getInstance().getString(sKey, null);
        }

        if (TextUtils.isEmpty(mUrl)) {
            mUrl = getIntent().getStringExtra(EXTRA_DATA_URL);
            if (TextUtils.isEmpty(mUrl)) {
                mUrl = "https://lyloou.github.io";
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWvContent = findViewById(R.id.wv_content);
        mWvContent.setScrollbarFadingEnabled(true);
        mWvContent.getSettings().setJavaScriptEnabled(true);
        mWvContent.getSettings().setBuiltInZoomControls(false);
        mWvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWvContent.getSettings().setDomStorageEnabled(true);
        mWvContent.getSettings().setAppCacheEnabled(false);
        mWvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWvContent.getSettings().setBlockNetworkImage(false);

        mWvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 50) {
                    int lastPosition = Usp.getInstance().getInt(sKey + "position", 0);
                    view.scrollTo(0, lastPosition);
                }
            }
        });

        mWvContent.loadUrl(mUrl);

        findViewById(R.id.fab).setOnClickListener(v -> mContext.finish());
    }

    @Override
    public void onBackPressed() {
        if (mWvContent.canGoBack()) {
            mWvContent.goBack();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Usp.getInstance()
                .putString(sKey, mWvContent.getUrl())
                .putInt(sKey + "position", mWvContent.getScrollY())
                .putString(sKey, mWvContent.getUrl())
                .commit();
        sKey = null;
    }
}

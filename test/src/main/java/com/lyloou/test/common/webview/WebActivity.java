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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_URL = "extra_data_url";
    private String mUrl;

    private WebView mWvContent;

    public static void newInstance(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_DATA_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWvContent = new WebView(this);
        setContentView(mWvContent);


        mUrl = getIntent().getStringExtra(EXTRA_DATA_URL);
        if (mUrl == null) {
            mUrl = "http://www.lyloou.com";
        }

        initView();
    }

    private void initView() {
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

        mWvContent.loadUrl(mUrl);
    }

    @Override
    public void onBackPressed() {
        if (mWvContent.canGoBack()) {
            mWvContent.goBack();
            return;
        }

        super.onBackPressed();
    }
}

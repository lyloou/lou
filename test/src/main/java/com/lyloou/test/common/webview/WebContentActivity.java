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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

public class WebContentActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_CONTENT = "extra_data_content";
    private static final String TAG = "WebContentActivity";
    private String mContent;
    private WebView mWvContent;

    // 通过mUrls来管理在页面点击的链接（为了好控制返回事件：onBackPressed）。
    private List<String> mUrls = new ArrayList<>();

    public static void newInstance(Context context, String content) {
        Intent intent = new Intent(context, WebContentActivity.class);
        intent.putExtra(EXTRA_DATA_CONTENT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWvContent = new WebView(this);
        setContentView(mWvContent);


        mContent = getIntent().getStringExtra(EXTRA_DATA_CONTENT);
        if (mContent == null) {
            mContent = "hello, world";
        }

        initView();
    }

    // https://jiandanxinli.github.io/2016-08-31.html#
    private void initView() {

        mWvContent.setScrollbarFadingEnabled(true);

        WebSettings settings = mWvContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setBuiltInZoomControls(false);

//        settings.supportMultipleWindows();
//        settings.setSupportMultipleWindows(true); // 这个操作会导致链接不可点击

        settings.setBlockNetworkImage(false);

        //设置适应屏幕
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);

        // 设置字体
//        settings.setMinimumFontSize(16);
//        settings.setDefaultFontSize(16);
//        settings.setMinimumLogicalFontSize(16);

        //设置缓存模式
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(mWvContent.getContext().getCacheDir().getAbsolutePath());

        mWvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mUrls.add(url);
                view.loadUrl(url);
                // 浏览器窗口打开
                // startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(request.toString())));
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });

        mWvContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        showContent();
    }

    public void showContent() {

        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/render.css\" type=\"text/css\">";

        String result = "<!DOCTYPE html>\n"
                + "<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head>\n<meta charset=\"utf-8\" />\n"
                + css
                + "\n</head>\n<body>\n"
                + "<div class=\"container bs-docs-container\">\n"
                + "<div class=\"post-container\">\n"
                + mContent
                + "</div>\n</div>\n</body>\n</html>";

        mWvContent.loadDataWithBaseURL("x-data://base", result, "text/html", "utf-8", null);
    }

    @Override
    public void onBackPressed() {
        if (mWvContent.canGoBack()) {
            if (mUrls.size() == 0) {
                super.onBackPressed();
                return;
            }

            if (mUrls.size() == 1) {
                mUrls.remove(0);
                showContent();
                return;
            }

            mUrls.remove(mUrls.size() - 1);
            mWvContent.goBack();
            return;
        }

        super.onBackPressed();
    }
}

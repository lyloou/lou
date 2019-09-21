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

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lyloou.test.R;
import com.lyloou.test.util.Unet;
import com.lyloou.test.util.Usp;
import com.lyloou.test.util.Utoast;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_URL = "EXTRA_DATA_URL";
    private static String sKey;
    private String mUrl;
    private WebView mWvContent;
    private Activity mContext;
    private boolean isScrolled;
    private boolean isShowFab;

    public static void newInstance(Context context, String url, String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (!TextUtils.isEmpty(tag)) {
            sKey = context.getClass().getSimpleName().toUpperCase() + "_" + tag;
        }

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
                mUrl = "http://lyloou.com";
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWvContent = findViewById(R.id.wv_content);
        mWvContent.setScrollbarFadingEnabled(true);
        WebSettings webSettings = mWvContent.getSettings();
        // https://www.jianshu.com/p/14ca454ab3d1
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBlockNetworkImage(false);

        mWvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
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
                Utoast.show(mContext, "progress:" + newProgress);
                if (!isScrolled && newProgress > 50) {
                    int lastPosition = Usp.getInstance().getInt(sKey + "position", 0);
                    view.scrollTo(0, lastPosition);
                    isScrolled = true;
                }
            }
        });

        mWvContent.loadUrl(mUrl);

        if (!TextUtils.isEmpty(sKey)) {
            View fabWrap = findViewById(R.id.fab_wrap);
            FloatingActionButton fab = findViewById(R.id.fab);
            FloatingActionButton fab1 = findViewById(R.id.fab_1);
            FloatingActionButton fab2 = findViewById(R.id.fab_2);
            FloatingActionButton fab3 = findViewById(R.id.fab_3);
            fab.setVisibility(View.VISIBLE);
            fabWrap.setOnClickListener(v -> {
                showFab(isShowFab = false, fab1, fab2, fab3);
                fabWrap.setClickable(false);
            });
            fabWrap.setClickable(false);

            fab.setOnClickListener(v -> {
                showFab(isShowFab = !isShowFab, fab1, fab2, fab3);
                fabWrap.setClickable(isShowFab);
            });
            fab1.setOnClickListener(v -> mContext.finish());
            fab2.setOnClickListener(v -> {
                String title = mWvContent.getTitle();
                String url = mWvContent.getUrl();
                String text = "- [" + title + "]" + "(" + url + ")";
                String tips = "- [标题](链接)已复制到剪切板";
                copyToClipboard(text, tips);
            });
            fab2.setOnLongClickListener(v -> {
                String text = mWvContent.getUrl();
                String tips = "(链接)已复制到剪切板";
                copyToClipboard(text, tips);
                return true;
            });
            fab3.setOnClickListener(v -> {
                ObjectAnimator anim = ObjectAnimator.ofInt(mWvContent, "scrollY", mWvContent.getScrollY(), 0);
                anim.setDuration(500);
                anim.start();
            });

        }
    }

    private void copyToClipboard(String text, String tips) {
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData label = ClipData.newPlainText("label", text);
            clipboardManager.setPrimaryClip(label);
            Snackbar.make(mWvContent, tips, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showFab(boolean isShowFab, View... views) {
        for (View view : views) {
            view.setVisibility(isShowFab ? View.VISIBLE : View.GONE);
        }
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
        saveUrlAndPositionToHistory();
    }

    private void saveUrlAndPositionToHistory() {
        if (!TextUtils.isEmpty(sKey)) {
            Usp.getInstance()
                    .putString(sKey, mWvContent.getUrl())
                    .putInt(sKey + "position", mWvContent.getScrollY())
                    .putString(sKey, mWvContent.getUrl())
                    .commit();
            sKey = null;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

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

package com.lyloou.test.man;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.test.R;
import com.lyloou.test.util.Uanimator;
import com.lyloou.test.util.Unet;
import com.lyloou.test.util.Usp;

import java.util.List;

public class WebActivity extends AppCompatActivity {

    private Data mData;
    private WebView mWvContent;
    private Activity mContext;
    private boolean isScrolled;
    private TextView mTvTitle;
    private SwipeRefreshLayout mRefreshLayout;

    public static void newInstance(Context context, Data data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Const.Extra.WEB_DATA.str(), data);
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

        mData = (Data) getIntent().getSerializableExtra(Const.Extra.WEB_DATA.str());
        if (mData == null) {
            String webTitle = getIntent().getStringExtra(Const.Extra.WEB_TITLE.str());
            if (!TextUtils.isEmpty(webTitle)) {
                Data data = queryDataByTitle(webTitle);
                if (data != null) {
                    mData = data;
                    return;
                }
            }

            mData = new Data()
                    .setTitle("木子楼")
                    .setUrl("http://lyloou.com");
        }
    }

    private Data queryDataByTitle(String title) {
        List<Data> data = DataRepositoryHelper.newInstance(this).readData();
        for (Data d : data) {
            if (title.equals(d.getTitle())) {
                return d;
            }
        }
        return null;
    }

    private void initView() {
        initTopView();
        initLoadingView();
        initWebView();
    }

    private void initLoadingView() {
        mRefreshLayout = findViewById(R.id.srl_man);
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(() -> mWvContent.reload());
    }


    private void initTopView() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.iv_more).setOnClickListener(v -> onMorePressed());
        findViewById(R.id.iv_more).setOnLongClickListener(v -> minimize(false));
        findViewById(R.id.iv_close).setOnClickListener(v -> finish());
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(mData.getTitle());
        mTvTitle.setOnClickListener(v -> tapTwoTimesQuicklyScrollToTop());
        minimize(true);
    }

    private long lastClickedTime;

    private void tapTwoTimesQuicklyScrollToTop() {
        if (System.currentTimeMillis() - lastClickedTime < 800) {
            ObjectAnimator anim = ObjectAnimator.ofInt(mWvContent, "scrollY", mWvContent.getScrollY(), 0);
            anim.setDuration(360);
            anim.start();
            return;
        }
        lastClickedTime = System.currentTimeMillis();
    }

    enum OperateType {
        REFRESH("刷新"),
        COPY_LINK("复制链接"),
        COPY_MD_LINK("复制md链接"),
        OPEN_WITH_BROWSER("在浏览器中打开"),
        ;
        String title;

        OperateType(String title) {
            this.title = title;
        }

        public static OperateType indexOf(int index) {
            return OperateType.values()[index];
        }

        public static String[] toStrArray() {
            OperateType[] values = OperateType.values();
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = values[i].title;
            }
            return result;
        }
    }

    private void onMorePressed() {
        new AlertDialog.Builder(mContext)
                .setTitle("操作")
                .setItems(OperateType.toStrArray(), (dialog, which) -> {
                    switch (OperateType.indexOf(which)) {
                        case REFRESH:
                            mWvContent.reload();
                            break;
                        case COPY_LINK:
                            copyToClipboard(mWvContent.getUrl(), "已复制到剪切板");
                            break;
                        case COPY_MD_LINK:
                            String title = mWvContent.getTitle();
                            String url = mWvContent.getUrl();
                            copyToClipboard("- [" + title + "]" + "(" + url + ")", "已复制到剪切板");
                            break;
                        case OPEN_WITH_BROWSER:
                            Uri uri = Uri.parse(mWvContent.getUrl());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            break;
                        default:
                    }
                })
                .create().show();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWvContent = findViewById(R.id.wv_content);
//        mWvContent.setScrollbarFadingEnabled(true);
        initWebSettings();
        mWvContent.setWebViewClient(getWebViewClient());
        mWvContent.setWebChromeClient(getWebChromeClient());
        mWvContent.loadUrl(getUrl());
    }

    private void initWebSettings() {
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
    }

    private WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 80) {
                    if (!isScrolled) {
                        int lastPosition = mData.getPosition();
                        view.setScrollY(lastPosition);
                        isScrolled = true;
                    }
                    mRefreshLayout.setRefreshing(false);
                }
            }
        };
    }

    private WebViewClient getWebViewClient() {
        return new WebViewClient() {
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
                mTvTitle.setText(view.getTitle());
            }
        };
    }


    private boolean minimize(boolean showTopBar) {
        View topView = findViewById(R.id.rylt);
        if (showTopBar && topView.getVisibility() == View.VISIBLE) {
            return true;
        }
        Uanimator.animHeightToView(this, topView, showTopBar, 300);
        View mini = findViewById(R.id.iv_minimize);
        mini.setOnClickListener(v -> minimize(!showTopBar));
        mini.setVisibility(showTopBar ? View.GONE : View.VISIBLE);
        return true;
    }


    private String getUrl() {
        if (!TextUtils.isEmpty(mData.getLastUrl())) {
            return mData.getLastUrl();
        }
        return mData.getUrl();
    }

    private void copyToClipboard(String text, String tips) {
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData label = ClipData.newPlainText("label", text);
            clipboardManager.setPrimaryClip(label);
            Snackbar.make(mWvContent, tips, Snackbar.LENGTH_SHORT).show();
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
    protected void onPause() {
        super.onPause();
        saveUrlAndPositionToHistory();
    }

    private void saveUrlAndPositionToHistory() {
        mData.setPosition(mWvContent.getScrollY())
                .setLastUrl(mWvContent.getUrl());
        Intent intent = new Intent();
        intent.setAction(Const.Action.POSITION.str());
        intent.putExtra(Const.Extra.WEB_DATA.str(), mData);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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

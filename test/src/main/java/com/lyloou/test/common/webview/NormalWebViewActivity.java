package com.lyloou.test.common.webview;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.lyloou.test.R;
import com.lyloou.test.util.Usp;
import com.lyloou.test.util.Uview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by lyloou on 2019-04-19 14:24:23
 * https://blog.csdn.net/chu_cheng/article/details/78084728
 * https://www.jianshu.com/p/2adaa6a5f85f
 */

public class NormalWebViewActivity extends AppCompatActivity {
    public static final String TEST_AUTHORITY = "com.lyloou.test";
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE2 = 1;
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_IS_DOWNLOAD = "isDownload";
    public static final String EXTRA_DESC = "desc";

    private AgentWeb mAgentWeb;
    private WebView mWebView;
    private Activity mContext;
    private DownloadManager downloadManager;
    private long downloadId;

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    public static void newInstance(Context context, String url) {
        newInstance(context, new JSONObject() {{
            try {
                putOpt("url", url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }});
    }

    public static void newInstance(Context context, JSONObject jsonObject) {
        Intent intent = new Intent(context, NormalWebViewActivity.class);
        if (jsonObject != null) {
            intent.putExtra(NormalWebViewActivity.EXTRA_URL, jsonObject.optString(EXTRA_URL));
            intent.putExtra(EXTRA_IS_DOWNLOAD, jsonObject.optBoolean(EXTRA_IS_DOWNLOAD));
            intent.putExtra(EXTRA_DESC, jsonObject.optString(EXTRA_DESC));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private boolean isScrolled;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_web);

        initData();
        initViewForTop();
        initViewForDownload();
        initViewForWeb();

        // 竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void initData() {
        Usp.init(this);
        Intent data = getIntent();
        mUrl = data.getStringExtra(EXTRA_URL);
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "http://lyloou.com";
        }
    }

    private void initViewForWeb() {
        View view = findViewById(R.id.llyt_container);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) view, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(getResources().getColor(R.color.colorAccent), 2)
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        mToolbar.setTitle(title);
                    }

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);

                        // 记录历史
                        if (!isScrolled && newProgress > 90) {
                            int lastPosition = Usp.init(mContext).getInt(mUrl, 0);
                            view.scrollTo(0, lastPosition);
                            isScrolled = true;
                        }
                    }
                })
                .createAgentWeb()
                .ready()
                .go(mUrl);
        mWebView = mAgentWeb.getWebCreator().getWebView();

        // https://www.jianshu.com/p/14ca454ab3d1
        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        mAgentWeb.getWebCreator()
                .getWebView()
                .setDownloadListener((url1, userAgent, contentDisposition, mimeType, contentLength) ->
                        runOnUiThread(() -> {
                            //使用前先判断是否有读取、写入内存卡权限
                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE2);
                            } else {
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url1));
                                request.setMimeType(mimeType);
                                // https://stackoverflow.com/questions/33434532/android-webview-download-files-like-browsers-do/33501835
                                //------------------------COOKIE!!------------------------
                                String cookies = CookieManager.getInstance().getCookie(url1);
                                request.addRequestHeader("cookie", cookies);
                                //------------------------COOKIE!!------------------------
                                request.addRequestHeader("User-Agent", userAgent);
                                request.setDescription("Downloading file...");
                                request.setTitle(URLUtil.guessFileName(url1, contentDisposition, mimeType));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url1, contentDisposition, mimeType));
                                downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                downloadId = downloadManager.enqueue(request);
                                Toast.makeText(getApplicationContext(), "正在下载文件", Toast.LENGTH_LONG).show();
                            }
                        }));
    }

    private int count = 0;
    private Handler handler = new Handler();
    private Runnable resetCount = () -> count = 0;

    // 双击 View 回到顶部
    private void doubleClickToolbar(View view, Runnable task) {
        view.setOnClickListener(v -> {
            if (++count >= 2) {
                task.run();
            }
            handler.postDelayed(resetCount, 500);
        });
    }

    private void initViewForTop() {

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        // doubleClickToolbar scrollToTop
        doubleClickToolbar(mToolbar, () -> mWebView.scrollTo(0, 0));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uview.initStatusBar(this, R.color.colorAccent);
    }

    private void initViewForDownload() {
        Intent data = getIntent();
        boolean isDownload = data.getBooleanExtra(EXTRA_IS_DOWNLOAD, false);
        String desc = data.getStringExtra(EXTRA_DESC);
        if (mUrl.endsWith(".apk") && isDownload && !TextUtils.isEmpty(desc)) {
            TextView tvDesc = findViewById(R.id.tv_desc);
            tvDesc.setText(desc);
            tvDesc.setVisibility(View.VISIBLE);
        }
    }

    //检查下载状态
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    installAPK(mContext, TEST_AUTHORITY);
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //下载到本地后执行安装
    private void installAPK(Activity activity, String authority) {
        Intent intent = new Intent();
        File apkFile = queryDownloadedFile();
        if (!apkFile.getName().endsWith(".apk")) { // 非apk文件不做处理
            return;
        }

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0启动姿势<pre name="code" class="html">
            // com.xxx.xxx.fileprovider为上述manifest中provider所配置相同；apkFile为问题1中的外部存储apk文件</pre>
            uri = FileProvider.getUriForFile(activity, authority, apkFile);
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，次过程没有用户交互
        } else {//7.0以下启动姿势
            uri = Uri.fromFile(apkFile);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    public File queryDownloadedFile() {
        File file = null;
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloadManager.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!uriString.isEmpty()) {
                        file = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return file;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                } else {
                    // Permission denied
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mAgentWeb.back()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        unregisterReceiver(receiver);
        super.onDestroy();
    }


    public static void copyString(Context context, String content) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("test", content));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_normal_webview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_with_browser:
                String url = mAgentWeb.getWebCreator().getWebView().getUrl();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.menu_refresh:
                mAgentWeb.getUrlLoader().reload();
                break;
            case R.id.menu_copy_link:
                url = mAgentWeb.getWebCreator().getWebView().getUrl();
                copyString(mContext, url);
                Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePosition();
    }

    private void savePosition() {
        int scrollY = mWebView.getScrollY();
        Usp.init(mContext).putInt(mUrl, scrollY).commit();
    }
}

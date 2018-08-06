/*
 * Copyright  (c) 2018 Lyloou
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

package com.lyloou.weex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.IWXDebugProxy;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements IWXRenderListener {

    WXSDKInstance mWXSDKInstance;
    protected BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createWeexInstance();
        renderPage();
        registerBroadcastReceiver(mBroadcastReceiver, null);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        setContentView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }


    @Override
    public void onStart() {
        super.onStart();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityDestroy();
        }
        unregisterBroadcastReceiver();
    }

    public interface WxReloadListener {
        void onReload();
    }

    public interface WxRefreshListener {
        void onRefresh();
    }


    protected void destoryWeexInstance() {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.registerRenderListener(null);
            mWXSDKInstance.destroy();
            mWXSDKInstance = null;
        }
    }


    private WxReloadListener mReloadListener;
    private WxRefreshListener mRefreshListener;

    public void registerBroadcastReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        mBroadcastReceiver = receiver != null ? receiver : new DefaultBroadcastReceiver();
        if (filter == null) {
            filter = new IntentFilter();
        }
        filter.addAction(IWXDebugProxy.ACTION_DEBUG_INSTANCE_REFRESH);
        filter.addAction(WXSDKEngine.JS_FRAMEWORK_RELOAD);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver, filter);
        if (mReloadListener == null) {
            setReloadListener(new WxReloadListener() {

                @Override
                public void onReload() {
                    System.out.println("------------" + "ooooooooooooooooooo4");
                    createWeexInstance();
                    renderPage();
                }

            });
        }

        if (mRefreshListener == null) {
            setRefreshListener(new WxRefreshListener() {

                @Override
                public void onRefresh() {
                    createWeexInstance();
                    renderPage();
                }

            });
        }
    }

    protected void createWeexInstance() {
        destoryWeexInstance();
        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);
    }


    private void renderPage() {
        // 定义参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("platform", "Android");
        options.put("params", params);
        String jsPath = "index.js";
        options.put(WXSDKInstance.BUNDLE_URL, jsPath);
        mWXSDKInstance.render("WXSample", WXFileUtils.loadAsset(jsPath, this), options, null, -1, -1, WXRenderStrategy.APPEND_ASYNC);

//        mWXSDKInstance.renderByUrl("WXSample",
//                "https://raw.githubusercontent.com/w11p3333/weex-start-kit/master/Android/Demo/app/src/main/assets/weex/js/index.js",
//                options, null, WXRenderStrategy.APPEND_ASYNC);

    }

    public void setReloadListener(WxReloadListener reloadListener) {
        mReloadListener = reloadListener;
    }

    public void setRefreshListener(WxRefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public void unregisterBroadcastReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
        setReloadListener(null);
        setRefreshListener(null);
    }

    public class DefaultBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IWXDebugProxy.ACTION_DEBUG_INSTANCE_REFRESH.equals(intent.getAction())) {
                System.out.println("------------" + "ooooooooooooooooooo2");
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            } else if (WXSDKEngine.JS_FRAMEWORK_RELOAD.equals(intent.getAction())) {
                System.out.println("------------" + "ooooooooooooooooooo");
                if (mReloadListener != null) {
                    System.out.println("------------" + "ooooooooooooooooooo3");
                    mReloadListener.onReload();
                }
            }
        }
    }
}
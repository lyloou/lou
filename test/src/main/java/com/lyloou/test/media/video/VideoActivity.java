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

package com.lyloou.test.media.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.gyf.immersionbar.ImmersionBar;
import com.lyloou.test.R;

/**
 * [Home · dueeeke/DKVideoPlayer Wiki](https://github.com/dueeeke/DKVideoPlayer/wiki)
 *
 * @author lyloou
 */

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = VideoActivity.class.getSimpleName();

    public static final String[] URLS = {"http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4", "https://demovideos.oss-cn-shanghai.aliyuncs.com//时尚美妆/qiuqiu美妆/qiuqiu美妆/qiuqiu makeup/20180730/娃娃妆.mp4"};
    private VideoView videoView;
    private static int currentUrlPosition = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediavideo);

        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("视频标题");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .navigationBarDarkIcon(true)
                .statusBarColor(R.color.colorAccent)
                .statusBarAlpha(0.1f)
                .init();
    }

    private void initView() {
        videoView = findViewById(R.id.player);
        videoView.setUrl(URLS[currentUrlPosition]); //设置视频地址
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                //使用使用IjkPlayer解码
                .setPlayerFactory(IjkPlayerFactory.create())
                .build());

        StandardVideoController controller = new StandardVideoController(this);
        controller.setTitle("视频标题"); //设置视频标题
        controller.show();
        videoView.setVideoController(controller); //设置控制器，如需定制可继承BaseVideoController
        videoView.start(); //开始播放，不调用则不自动播放

        TextView tvNext = findViewById(R.id.btn_next);
        tvNext.setOnClickListener(v -> {
            videoView.release();
            int current = (++currentUrlPosition) % URLS.length;
            Log.i(TAG, "initView: currentPosition:" + current);
            videoView.setUrl(URLS[current]);
            videoView.start();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.release();
    }


    @Override
    public void onBackPressed() {
        if (!videoView.onBackPressed()) {
            super.onBackPressed();
        }
    }


}

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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.lyloou.test.R;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Uview;

import java.util.List;

/**
 * [Home · dueeeke/DKVideoPlayer Wiki](https://github.com/dueeeke/DKVideoPlayer/wiki)
 *
 * @author lyloou
 */

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = VideoActivity.class.getSimpleName();

    public static final String[] URLS = DataUtil.getVideos();
    private VideoView videoView;
    private static int lastUrlPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_video);

        initToolbar();
        initVideoView();
        initListView();
    }

    private void initListView() {
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        MyAdapter adapter = new MyAdapter(this, DataUtil.getVideoList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 4)));
        adapter.setListener(v -> {
            if (!(v.getTag() instanceof String)) {
                return;
            }
            String url = (String) v.getTag();
            playUrl(url);
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("视频");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Uview.initStatusBar(this, R.color.colorAccent);
    }

    private void initVideoView() {
        videoView = findViewById(R.id.player);
        videoView.setUrl(URLS[getCurrentPosition(false)]); //设置视频地址
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                //使用使用IjkPlayer解码
                .setPlayerFactory(IjkPlayerFactory.create())
                .build());

        StandardVideoController controller = new StandardVideoController(this);
        controller.setTitle("视频标题"); //设置视频标题
        controller.showNetWarning();
        videoView.setVideoController(controller); //设置控制器，如需定制可继承BaseVideoController
        videoView.start(); //开始播放，不调用则不自动播放

        this.<TextView>findViewById(R.id.btn_preview).setOnClickListener(v -> toChangeVideo(true));
        this.<TextView>findViewById(R.id.btn_next).setOnClickListener(v -> toChangeVideo(false));
    }

    private void toChangeVideo(boolean preview) {
        int current = getCurrentPosition(preview);
        String url = URLS[current];
        playUrl(url);
    }

    private void playUrl(String url) {
        videoView.release();
        videoView.setUrl(url);
        videoView.start();
    }

    private int getCurrentPosition(boolean preview) {
        if (preview) {
            --lastUrlPosition;
            if (lastUrlPosition < 0) {
                lastUrlPosition = URLS.length - 1;
            }
            lastUrlPosition = lastUrlPosition % URLS.length;
            return lastUrlPosition;
        }
        return (++lastUrlPosition) % URLS.length;
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

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        Context context;
        List<VideoBean> list;
        private View.OnClickListener listener;


        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        public MyAdapter(Context context, List<VideoBean> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videoprogram_list, null);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
            VideoBean videoBean = list.get(i);
            myHolder.tvTitle.setText(videoBean.getTitle());
            myHolder.tvDesc.setText(videoBean.getUrl());
            myHolder.itemView.setOnClickListener(v -> {
                v.setTag(videoBean.getUrl());
                listener.onClick(v);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class MyHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvDesc;
            View itemView;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView = itemView;
                tvDesc = itemView.findViewById(R.id.tv_desc);
                tvTitle = itemView.findViewById(R.id.tv_title);
            }
        }
    }

}

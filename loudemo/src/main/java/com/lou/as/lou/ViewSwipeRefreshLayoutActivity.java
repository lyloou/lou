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

package com.lou.as.lou;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ViewSwipeRefreshLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_swipe_refresh_layout);

        // 在布局中，使其包含在最外层
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.srl);


        // 禁用
        // srl.setEnabled(false);

        // 是否缩放
        srl.setProgressViewOffset(false, 0, 100);

        // 设置刷新时的交换颜色
        srl.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // 设置背景色
        srl.setProgressBackgroundColor(android.R.color.holo_green_light);

        // 设置大小
        srl.setSize(SwipeRefreshLayout.LARGE);

        // 3秒后停止；
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: stop");
                        srl.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private static final String TAG = "ViewSwipe";
}

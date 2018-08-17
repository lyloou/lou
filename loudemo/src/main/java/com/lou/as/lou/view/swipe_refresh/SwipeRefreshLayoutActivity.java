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

package com.lou.as.lou.view.swipe_refresh;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lou.as.lou.R;
import com.lyloou.lou.util.Uscreen;
import com.lyloou.lou.util.Usp;

public class SwipeRefreshLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_swipe_refresh_layout);

        // 在布局中，使其包含在最外层
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.srl);

        // 是否缩放
        srl.setProgressViewOffset(false, 0, 100);

        // 设置刷新时的交换颜色
        srl.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // 设置背景色
        srl.setProgressBackgroundColor(android.R.color.white);

        // 设置大小
        srl.setSize(SwipeRefreshLayout.LARGE);

        // 3秒后停止；
        srl.setOnRefreshListener(() -> srl.postDelayed(() -> {
            srl.setRefreshing(false);
            LinearLayout content = findViewById(R.id.llyt_content);
            TextView child = new TextView(this);
            child.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            int px = Uscreen.dp2Px(this, 16);
            child.setPadding(px, px, px, px);
            child.setText(String.valueOf(System.currentTimeMillis()));
            content.addView(child, 0);
            Snackbar.make(srl, "已刷新", Snackbar.LENGTH_LONG).show();
        }, 3000));
    }
}

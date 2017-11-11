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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.CrashHandler;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.webview.WebActivity;
import com.lyloou.test.util.Uanimation;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Usp;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ManActivity extends AppCompatActivity {
    private static final Map<String, String> stringMap = new LinkedHashMap<>();

    static {
        stringMap.put("阮一峰 - 未来世界的幸存者", "http://survivor.ruanyifeng.com/index.html");
        stringMap.put("阮一峰", "http://ruanyifeng.com/blog/");
        stringMap.put("陈 皓", "https://coolshell.cn/");
        stringMap.put("刘未鹏", "http://mindhacks.cn/");
        stringMap.put("廖雪峰", "https://www.liaoxuefeng.com/");
        stringMap.put("王 垠", "http://www.yinwang.org/");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        CrashHandler.getInstance().init(this.getApplicationContext());
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(this, toolbar);

        ImageView ivHeader = (ImageView) findViewById(R.id.iv_header);
        TextView tvHeader = findViewById(R.id.tv_header);
        NetWork.getKingsoftwareApi()
                .getDaily("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> {
                            Glide
                                    .with(ivHeader.getContext().getApplicationContext())
                                    .load("http://ww2.sinaimg.cn/large/7a8aed7bjw1f0cw7swd9tj20hy0qogoo.jpg")
                                    .centerCrop()
                                    .into(ivHeader);
                            tvHeader.setText(daily.getContent());
                            tvHeader.setTag(daily.getNote());
                            tvHeader.setVisibility(View.VISIBLE);
                        }
                        , Throwable::printStackTrace);

        View fab = findViewById(R.id.fab);
        fab.startAnimation(Uanimation.getRotateAnimation(3600));
        fab.setOnClickListener(view -> {
            Object tag = tvHeader.getTag();
            if (tag != null && tag instanceof String) {
                String newStr = (String) tag;
                String oldStr = tvHeader.getText().toString();
                tvHeader.setText(newStr);
                tvHeader.setTag(oldStr);
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_main);


        recyclerView.setAdapter(new MainAdapter(stringMap));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "本页既是了", Toast.LENGTH_SHORT).show();
    }

    static class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
        final Map<String, String> stringMap;
        Object[] labels;

        MainAdapter(Map<String, String> stringMap) {
            this.stringMap = stringMap;
            labels = stringMap.keySet().toArray();
        }

        @Override
        public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, null);
            return new MainAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
            String label = (String) labels[position];
            holder.tvTitle.setText(label);
            holder.view.setOnClickListener(v ->
                    WebActivity.newInstance(holder.view.getContext(), stringMap.get(label), label));
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Context context = v.getContext();
                    Usp.init(context);
                    new AlertDialog
                            .Builder(context)
                            .setTitle("清除它的历史记录：" + label)
                            .setCancelable(true)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String key = context.getClass().getSimpleName().toUpperCase() + "_" + label;
                                    Usp.getInstance().remove(key).commit();
                                }
                            })
                            .create()
                            .show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return labels.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            View view;

            ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            }
        }
    }

}

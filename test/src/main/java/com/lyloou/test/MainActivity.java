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

package com.lyloou.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.lyloou.test.common.CrashHandler;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uactivity;
import com.lyloou.test.util.Uanimation;
import com.lyloou.test.util.Uscreen;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.28 11:57
 * <p>
 * Description:
 */
public class MainActivity extends AppCompatActivity {

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
        Uscreen.setToolbarMarginTop(this, toolbar);

        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView ivHeader = (ImageView) findViewById(R.id.iv_header);
        TextView tvHeader = findViewById(R.id.tv_header);
        NetWork.getKingsoftwareApi()
                .getDaily("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> {
                            Glide
                                    .with(ivHeader.getContext().getApplicationContext())
                                    .load(daily.getPicture2())
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
        Map<String, Class> stringClassMap = Uactivity.getActivitiesMapFromManifest(this, this.getPackageName());

        recyclerView.setAdapter(new MainAdapter(stringClassMap));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "本页既是了", Toast.LENGTH_SHORT).show();
    }

    static class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
        final Map<String, Class> stringClassMap;
        Object[] labels;

        MainAdapter(Map<String, Class> stringClassMap) {
            this.stringClassMap = stringClassMap;
            labels = stringClassMap.keySet().toArray();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String label = (String) labels[position];
            holder.tvTitle.setText(label);
            holder.view.setOnClickListener(v ->
                    Uactivity.start(v.getContext(), stringClassMap.get(label)));
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

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

package com.lyloou.test.flow;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uanimation;
import com.lyloou.test.util.Uscreen;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2019.09.25 10:34
 * <p>
 * Description: 时间流列表
 */
public class FlowListActivity extends AppCompatActivity {
    private List<FlowDay> mFlowDays = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_list);

        initData();
        initView();

    }


    private void initData() {
        consumeCursorByDay(cursor -> {
            FlowDay fd = new FlowDay();
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
            String day = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DAY));
            fd.setId(id);
            fd.setDay(day);
            mFlowDays.add(fd);
        });
    }

    private void consumeCursorByDay(Consumer<Cursor> consumer) {
        SQLiteDatabase sd = new DbHelper(this).getReadableDatabase();
        Cursor cursor = sd.rawQuery("select id,day from " + DbHelper.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                consumer.accept(cursor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sd.close();
    }

    private void initView() {
        initTopPart();

        RecyclerView recyclerView = findViewById(R.id.rv_main);

        recyclerView.setAdapter(new Adapter(this, mFlowDays));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
    }

    private void initTopPart() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(this, toolbar);

        ImageView ivHeader = findViewById(R.id.iv_header);
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
            if (tag instanceof String) {
                String newStr = (String) tag;
                String oldStr = tvHeader.getText().toString();
                tvHeader.setText(newStr);
                tvHeader.setTag(oldStr);
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        final List<FlowDay> list;
        final Context context;

        Adapter(Context context, List<FlowDay> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_list, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FlowDay flowDay = list.get(position);
            holder.tvTitle.setText(flowDay.getDay());
            holder.view.setOnClickListener(v -> FlowActivity.start(context, flowDay.getDay()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            View view;

            ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                tvTitle = itemView.findViewById(R.id.tv_title);
            }
        }
    }


}

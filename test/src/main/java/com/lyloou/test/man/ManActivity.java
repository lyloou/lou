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

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uanimation;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ManActivity extends AppCompatActivity {
    private DataRepositoryHelper mDataRepositoryHelper;
    private List<Data> mDataList;
    private ManAdapter mManAdapter;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_main);
        initData();
        initView();
    }


    private void initData() {
        mDataRepositoryHelper = DataRepositoryHelper.newInstance(this);
        mDataList = mDataRepositoryHelper.readData();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, getIntentFilter());
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Const.ACTION_POSITION);
        return intentFilter;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Const.ACTION_POSITION.equals(intent.getAction())) {
                Data fromData = (Data) intent.getSerializableExtra(Const.EXTRA_DATA);

                if (fromData == null) {
                    return;
                }

                Data d = queryDataByTitle(fromData.getTitle());
                if (d != null) {
                    d.setLastUrl(fromData.getLastUrl());
                    d.setPosition(fromData.getPosition());
                    updateDataRepository();
                }
            }
        }
    };

    private Data queryDataByTitle(String title) {
        for (Data d : mDataList) {
            if (title.equals(d.getTitle())) {
                return d;
            }
        }
        return null;
    }

    @SuppressLint("CheckResult")
    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Uscreen.setToolbarMarginTop(this, toolbar);

        ImageView ivHeader = findViewById(R.id.iv_header);
        TextView tvHeader = findViewById(R.id.tv_header);
        //noinspection ResultOfMethodCallIgnored
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

        RecyclerView recyclerView = findViewById(R.id.rv_main);
        mManAdapter = new ManAdapter(mDataList);
        mManAdapter.setListener(data -> new AlertDialog.Builder(mContext)
                .setTitle("操作")
                .setItems(OperateType.toStrArray(), (dialog, which) -> {
                    switch (OperateType.indexOf(which)) {
                        case DELETE:
                            mDataList.remove(data);
                            updateDataRepository();
                            break;
                        case CLEAR_HISTORY:
                            data.setLastUrl(null);
                            data.setPosition(0);
                            updateDataRepository();
                            break;
                    }
                })
                .create()
                .show());
        recyclerView.setAdapter(mManAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
    }

    @SuppressLint("InflateParams")
    public void addAddress() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_man_address_add, null);
        new AlertDialog.Builder(mContext)
                .setTitle("添加更多网址")
                .setView(view)
                .setPositiveButton("是的", (dialog, whichButton) -> {
                    EditText etName = view.findViewById(R.id.et_name);
                    EditText etAddress = view.findViewById(R.id.et_address);

                    String name = etName.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        Utoast.show(mContext, "无效的名称");
                        return;
                    }
                    String address = etAddress.getText().toString();
                    try {
                        new URL(address);
                        mDataList.add(0, new Data().setTitle(name).setUrl(address));
                        updateDataRepository();
                    } catch (MalformedURLException e) {
                        Utoast.show(mContext, "无效的URL");
                    }

                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_man, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_man_add:
                addAddress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDataRepository() {
        mManAdapter.notifyDataSetChanged();
        mDataRepositoryHelper.writeData(mDataList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
}

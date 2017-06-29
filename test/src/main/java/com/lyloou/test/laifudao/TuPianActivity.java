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

package com.lyloou.test.laifudao;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.lyloou.test.R;
import com.lyloou.test.common.DoubleItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.LouDialog;
import com.lyloou.test.util.Uscreen;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TuPianActivity extends AppCompatActivity {
    Activity mContext;
    private TuPianAdapter mTuPianAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_laifudao);

        initView();

        loadData();
    }


    private void loadData() {

        Observable<List<TuPian>> observable = NetWork.getLaiFuDaoApi().getTuPian();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tupian -> {
                            Toast.makeText(TuPianActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                            // 减少一个元素，构成奇数个数的列表
                            tupian.remove(tupian.size() - 1);
                            mTuPianAdapter.addItems(tupian);
                        }
                        , throwable -> Toast.makeText(TuPianActivity.this, "加载失败：" + throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_laifudao);
        toolbar.setTitle("来福岛上的笑话图片");
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.coolapsing_toolbar_layout_xiaohua);
        collapsingToolbarLayout.setExpandedTitleColor(Color.YELLOW);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        ImageView ivHeader = (ImageView) findViewById(R.id.iv_header);
        NetWork.getKingsoftwareApi()
                .getDaily("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> Glide
                                .with(TuPianActivity.this)
                                .load(daily.getPicture2())
                                .centerCrop()
                                .into(ivHeader)
                        , Throwable::printStackTrace);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_laifudao);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mTuPianAdapter = new TuPianAdapter();
        mTuPianAdapter.setOnItemClickListener(new TuPianAdapter.OnItemClickListener() {
            @Override
            public void onClick(TuPian tuPian) {
                LouDialog louDialog = LouDialog
                        .newInstance(mContext, R.layout.dialog_tupian, R.style.Theme_AppCompat)
                        .setCancelable(true)
                        .setWindowAnimation(R.style.Animation_Alpha)
                        .setWH(-1, -1);
                PhotoView photoView = louDialog.getView(R.id.pv_tupian);
                Glide.with(mContext)
                        .load(tuPian.getThumburl())
                        .into(photoView);
                photoView.setMinimumScale(0.5f);
                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (photoView.getScale() <= 1) {
                            louDialog.dismiss();
                        }
                    }
                });
                photoView.setMaximumScale(3);
                photoView.setScale(0.8f);
                louDialog.show();
            }

            @Override
            public void onLongClick(TuPian tuPian) {
            }
        });
        recyclerView.setAdapter(mTuPianAdapter);
        recyclerView.addItemDecoration(new DoubleItemOffsetDecoration(Uscreen.dp2Px(this, 16)));

    }
}

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

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyloou.test.R;
import com.lyloou.test.common.DoubleItemOffsetDecoration;
import com.lyloou.test.util.Uscreen;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TuPianActivity extends AppCompatActivity {
    private TuPianAdapter mTuPianAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laifudao);

        initView();

        loadData();
    }


    private void loadData() {
        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.laifudao.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        LaiFuDaoApi laiFuDaoApi = retrofit.create(LaiFuDaoApi.class);
        Observable<List<TuPian>> observable = laiFuDaoApi.getTuPian();
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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_laifudao);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mTuPianAdapter = new TuPianAdapter();
        mTuPianAdapter.setOnItemClickListener(new TuPianAdapter.OnItemClickListener() {
            @Override
            public void onClick(TuPian tuPian) {
            }

            @Override
            public void onLongClick(TuPian tuPian) {
            }
        });
        recyclerView.setAdapter(mTuPianAdapter);
        recyclerView.addItemDecoration(new DoubleItemOffsetDecoration(Uscreen.dp2Px(this, 16)));

    }
}

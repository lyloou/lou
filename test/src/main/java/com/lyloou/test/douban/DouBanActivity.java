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

package com.lyloou.test.douban;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lyloou.test.R;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.webview.NormalWebViewActivity;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;
import com.lyloou.test.util.Uview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DouBanActivity extends AppCompatActivity {


    private static final String TAG = "DouBanActivity";
    private static final int TOTAL_ITEM_SIZE = 250;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean mIsLoading = false;
    private SubjectAdapter mSubjectAdapter;
    private Disposable mDisposable;
    private Activity mContext;
    private Type type = Type.TOP_250;

    enum Type {
        NEW,
        TOP_250
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();

            if (totalItemCount < TOTAL_ITEM_SIZE && lastVisibleItem >= totalItemCount - 4) {
                // 注意：要限制请求，否则请求太多次数，导致服务器崩溃或者服务器拒绝请求（罪过，罪过）。
                if (mIsLoading) {
                    Log.i(TAG, "加载中...");
                } else {
                    Log.i(TAG, "加载更多了.");
                    loadData();
                }

            } else if (totalItemCount >= TOTAL_ITEM_SIZE) {
                if (!mSubjectAdapter.isMaxed()) {
                    mContext.runOnUiThread(() -> {
                        // 控制footer的显示情况
                        mSubjectAdapter.setMaxed(true);
                    });
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_douban);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("看流星");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uview.initStatusBar(this, R.color.colorAccent);

        EmptyRecyclerView recyclerView = findViewById(R.id.erv_douban);
        mRefreshLayout = findViewById(R.id.srl_douban);

        mSubjectAdapter = new SubjectAdapter(this);
        mSubjectAdapter.setOnItemClickListener(subject -> {
            NormalWebViewActivity.newInstance(mContext, new JSONObject() {{
                try {
                    putOpt("url", subject.getAlt());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }});
        });
        recyclerView.setAdapter(mSubjectAdapter);
        recyclerView.setItemTypeCount(mSubjectAdapter.getItemTypeCount());
        recyclerView.setEmptyView(findViewById(R.id.rlyt_empty));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(mContext, 16)));
        recyclerView.addOnScrollListener(mOnScrollListener);


        mRefreshLayout.setOnRefreshListener(this::reloadData);

        // 开始加载数据
        loadData();
    }

    private void reloadData() {
        runOnUiThread(() -> mSubjectAdapter.setMaxed(false));
        mSubjectAdapter.clearAll();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSubjectAdapter.getListSize() == 0) {
            loadData();
        }
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }


    public void loadData() {

        // 通过取消上次加载，来防止出现加载数据混淆的问题（）
        unSubscribe();

        Observable<HttpResult<List<Subject>>> movie;
        mIsLoading = true;
        switch (type) {
            case TOP_250:
                movie = getTopMovie();
                break;
            case NEW:
            default:
                movie = getNewMovie();
        }

        mDisposable = movie
                .map(listHttpResult -> {
                    mSubjectAdapter.setTitle(listHttpResult.getTitle());
                    return listHttpResult.getSubjects();
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subjects -> {
                    mSubjectAdapter.addAll(subjects);
                    mRefreshLayout.setRefreshing(false);
                    mIsLoading = false;
                }, throwable -> {
                    Log.e(TAG, "call: error", throwable);
                    mRefreshLayout.setRefreshing(false);
                    mIsLoading = false;

                    Utoast.show(DouBanActivity.this, "网络异常:" + throwable.getMessage());
                });
    }

    private Observable<HttpResult<List<Subject>>> getTopMovie() {
        return NetWork.getDouBanApi()
                .getTopMovie(mSubjectAdapter.getListSize(), 20);
    }

    private Observable<HttpResult<List<Subject>>> getNewMovie() {
        return NetWork.getDouBanApi()
                .getNewMovie(mSubjectAdapter.getListSize(), 20);
    }

    private void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_douban_movie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new:
                type = Type.NEW;
                break;
            case R.id.menu_top500:
                type = Type.TOP_250;
                break;
        }

        reloadData();
        return super.onOptionsItemSelected(item);
    }
}

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

package com.lyloou.test.gank;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.DoubleItemWithOneHeaderOffsetDecoration;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.LouDialogProgressTips;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.webview.WebContentActivity;
import com.lyloou.test.laifudao.TuPianActivity;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GankWelfareActivity extends AppCompatActivity {
    private static final String TAG = "GankWelfareActivity";
    private final List<ActiveDay> mActiveDays = new ArrayList<>();
    LinearLayout mLlytBottom;
    private SwipeRefreshLayout mRefreshLayout;
    private ActiveDayAdapter mActiveDayAdapter;
    private Activity mContext;
    private boolean mIsLoading = false;
    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();

            int max_size = mActiveDays.size();
            // 快到底部了，数据不多了
            int nearBottom = totalItemCount - 8;
            if (totalItemCount < max_size && lastVisibleItem >= nearBottom) {
                if (mIsLoading) {
                    Log.i(TAG, "加载中...");
                } else {
                    loadMore();
                }

            }
        }
    };
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_gank_welfare);

        initView();
    }

    private List<ActiveDay> getCheckedActiveDays() {
        List<ActiveDay> activeDays = new ArrayList<>();
        if (mActiveDayAdapter != null) {
            List<ActiveDay> activeDayList = mActiveDayAdapter.getList();
            for (ActiveDay activeDay : activeDayList) {
                if (activeDay.isSelected()) {
                    activeDays.add(activeDay);
                }
            }

        }
        return activeDays;
    }

    private void loadWelfareToImageView(String activeDay) {
        String[] split = activeDay.split("-");
        if (split.length == 3) {
            String year = split[0];
            String month = split[1];
            String day = split[2];

            Call<ResponseBody> gankData = NetWork.getGankApi().getGankContent(year, month, day);
            gankData.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody body = response.body();
                            if (body == null) {
                                return;
                            }
                            String string = body.string();
                            JSONObject jsonObject = new JSONObject(string);
                            JSONArray results = jsonObject.getJSONArray("results");

                            JSONObject contentObject = results.getJSONObject(0);
                            String content = contentObject.getString("content");

                            WebContentActivity.newInstance(mContext, content);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        } else {
            Toast.makeText(mContext, "格式不对：" + activeDay, Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gank);
        toolbar.setTitle("gank.io");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_black);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(mContext, toolbar);

        EmptyRecyclerView recyclerView = (EmptyRecyclerView) findViewById(R.id.erv_gank_welfare);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_gank_welfare);
        ImageView ivEmpty = (ImageView) findViewById(R.id.iv_empty);
        RelativeLayout rlytEmpty = (RelativeLayout) findViewById(R.id.rlyt_empty);
        mLlytBottom = (LinearLayout) findViewById(R.id.llyt_bottom);

        mActiveDayAdapter = new ActiveDayAdapter(this);
        mActiveDayAdapter.setTitle("福利岛");
        mActiveDayAdapter.setOnItemClickListener(new ActiveDayAdapter.OnItemClickListener() {
            @Override
            public void onClick(int realPosition, ActiveDay activeDay) {

                int mode = mActiveDayAdapter.getMode();
                if (mode == 1) {
                    activeDay.setSelected(!activeDay.isSelected());
                    mActiveDayAdapter.notifyItemChanged(realPosition);

                    List<ActiveDay> checkedActiveDays = getCheckedActiveDays();
                    initBootomViewWithData(mLlytBottom, checkedActiveDays);

                } else if (mode == 0) {
                    loadWelfareToImageView(activeDay.getDay());
                }
            }

            @Override
            public void onLongClick(ImageView view) {


                Object tag = view.getTag(view.getId());
                if (tag != null && tag instanceof String) {
                    String url = String.valueOf(tag);
                    if (!TextUtils.isEmpty(url)) {
                        LouDialogProgressTips progressTips = LouDialogProgressTips.getInstance(mContext);
                        progressTips.show("正在设置壁纸");

                        Observable
                                .fromCallable(new Callable<Bitmap>() {
                                    @Override
                                    public Bitmap call() throws Exception {
                                        return Glide.with(mContext)
                                                .load(url)
                                                .asBitmap()
                                                .into(Uscreen.getScreenWidth(mContext), Uscreen.getScreenHeight(mContext))
                                                .get();
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Bitmap>() {
                                    @Override
                                    public void accept(@NonNull Bitmap s) throws Exception {
                                        Uscreen.setBackgroundViaBitmap(mContext, s);
                                        progressTips.hide();
                                        Snackbar.make(view, "已设壁纸", Snackbar.LENGTH_SHORT).show();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                        progressTips.hide();
                                        Snackbar.make(view, "设置壁纸失败", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Utoast.show(mContext, "URL不存在");
                }

            }
        });
        recyclerView.setAdapter(mActiveDayAdapter);
        recyclerView.setItemTypeCount(mActiveDayAdapter.getItemTypeCount());
        recyclerView.setEmptyView(rlytEmpty);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isOneLine = position == 0
                        || position >= recyclerView.getAdapter().getItemCount() - 1;
                return isOneLine ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DoubleItemWithOneHeaderOffsetDecoration(Uscreen.dp2Px(mContext, 16)));
        recyclerView.addOnScrollListener(mListener);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActiveDayAdapter.setMaxed(false);
                    }
                });
                mLlytBottom.setVisibility(View.GONE);
                mActiveDayAdapter.clearAll();
                mActiveDays.clear();
                loadDatas();
            }
        });

        // 开始加载数据
        loadDatas();
    }

    private void initBootomViewWithData(LinearLayout view, final List<ActiveDay> checkedActiveDays) {
        int size = checkedActiveDays.size();
        if (size > 0) {
            mLlytBottom.setVisibility(View.VISIBLE);
        } else {
            mLlytBottom.setVisibility(View.GONE);
            return;
        }

        TextView tvCount = view.findViewById(R.id.tv_gank_bottom_count);
        TextView tvFriend = view.findViewById(R.id.tv_gank_bottom_friend);
        TextView tvTimeline = view.findViewById(R.id.tv_gank_bottom_timeline);

        String caption = String.valueOf(checkedActiveDays.size());
        tvCount.setText(caption);

        LouDialogProgressTips progressTips = LouDialogProgressTips.getInstance(mContext);
        tvTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressTips.show("福利准备中");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Ushare.clearImageDir(mContext);
                        List<String> paths = new ArrayList<String>();
                        for (ActiveDay day : checkedActiveDays) {
                            String welfareUrl = Ushare.loadWelfareUrl(day.getDay());
                            if (TextUtils.isEmpty(welfareUrl)) {
                                continue;
                            }
                            String imageFilePathFromImageUrl = Ushare.getImageFilePathFromImageUrl(mContext, welfareUrl);
                            paths.add(imageFilePathFromImageUrl);
                        }
                        Ushare.sharePicsToWechat(mContext, "这些天的福利了", paths, Ushare.SHARE_TYPE_TIMELINE, getTipsRunnable(view));
                        progressTips.hide();
                    }
                }).start();

            }
        });
        tvFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressTips.show("福利准备中");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Ushare.clearImageDir(mContext);
                        List<String> paths = new ArrayList<String>();
                        for (ActiveDay day : checkedActiveDays) {
                            String welfareUrl = Ushare.loadWelfareUrl(day.getDay());
                            if (TextUtils.isEmpty(welfareUrl)) {
                                continue;
                            }
                            String imageFilePathFromImageUrl = Ushare.getImageFilePathFromImageUrl(mContext, welfareUrl);
                            paths.add(imageFilePathFromImageUrl);
                        }
                        Ushare.sharePicsToWechat(mContext, "", paths, Ushare.SHARE_TYPE_FRIEND, getTipsRunnable(view));
                        progressTips.hide();
                    }
                }).start();

            }
        });
    }

    @android.support.annotation.NonNull
    private Runnable getTipsRunnable(final View view) {
        return new Runnable() {
            @Override
            public void run() {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(view.getContext(), "没有安装微信", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mActiveDayAdapter.getListSize() == 0) {
            loadDatas();
        }
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    public void loadDatas() {

        // 通过取消上次加载，来防止出现加载数据混淆的问题（）
        unSubscribe();

        mDisposable = NetWork.getGankApi().getActiveDays()

                .map(new Function<ActiveDayResult, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull ActiveDayResult activeDayResult) throws Exception {
                        if (activeDayResult.isError()) {
                            return new ArrayList<>();
                        }
                        return activeDayResult.getResults();
                    }
                })
                .map(new Function<List<String>, List<ActiveDay>>() {
                    @Override
                    public List<ActiveDay> apply(@NonNull List<String> list) throws Exception {
                        List<ActiveDay> activeDays = new ArrayList<ActiveDay>();
                        for (String day : list) {
                            activeDays.add(new ActiveDay(day));
                        }
                        return activeDays;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ActiveDay>>() {
                    @Override
                    public void accept(@NonNull List<ActiveDay> activeDays) throws Exception {
                        mActiveDays.addAll(activeDays);
                        loadMore();

                        mRefreshLayout.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        mRefreshLayout.setRefreshing(false);

                        Utoast.show(mContext, "网络异常:" + throwable.getMessage());
                    }
                });
    }

    private void loadMore() {
        int maxSize = mActiveDays.size();
        int listSize = mActiveDayAdapter.getListSize();
        if (maxSize == listSize) {
            return;
        }

        mIsLoading = true;
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                int loadedSize = Math.min(maxSize, listSize + 20);
                mActiveDayAdapter.addAll(mActiveDays.subList(listSize, loadedSize));
                if (maxSize == loadedSize) {
                    if (!mActiveDayAdapter.isMaxed()) {
                        mActiveDayAdapter.setMaxed(true);
                        mActiveDayAdapter.notifyDataSetChanged();
                    }
                }
                mIsLoading = false;
            }
        }, 1600);

    }

    private void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gank, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_normal:
                if (mActiveDayAdapter.getMode() == 0) {
                    return true;
                }
                mActiveDayAdapter.setMode(0);
                mActiveDayAdapter.clearSelected();
                mActiveDayAdapter.notifyDataSetChanged();
                initBootomViewWithData(mLlytBottom, getCheckedActiveDays());
                break;
            case R.id.menu_multiple:
                if (mActiveDayAdapter.getMode() == 1) {
                    return true;
                }
                mActiveDayAdapter.setMode(1);
                mActiveDayAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

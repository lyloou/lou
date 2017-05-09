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

package com.lyloou.douban;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    @Bind(R.id.rv)
    RecyclerView mRv;
    @Bind(R.id.srl)
    SwipeRefreshLayout mSrl;
    boolean mIsLoading = false;
    List<Subject> mData;
    Retrofit mRetrofit = null;
    SubjectService mSubjectService = null;
    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
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

            if (totalItemCount < 250 && lastVisibleItem >= totalItemCount - 4) {
                // 注意：要限制请求，否则请求太多次数，导致服务器崩溃或者服务器拒绝请求（罪过，罪过）。
                if (mIsLoading) {
                    Log.i(TAG, "onScrolled: " + "加载中---------");
                } else {
                    Log.i(TAG, "onScrolled: " + "加载更多了=======》");
                    loadSubject();
                }

            }

            Log.d(TAG, "onScrolled: lastVisibleItem=" + lastVisibleItem);
            Log.d(TAG, "onScrolled: totalItemCount=" + totalItemCount);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBaseContext();
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mData = getData();
        mRv.setAdapter(new SubjectAdapter(this, mData));
        mRv.setLayoutManager(new LinearLayoutManager(this));

        mRv.addOnScrollListener(mListener);

        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mData.clear();
                mRv.getAdapter().notifyDataSetChanged();
                Utoast.show(MainActivity.this, "正在加载，请稍后。。。");
                loadSubject();
            }
        });
    }

    private List<Subject> getData() {
        ArrayList<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject());
        subjects.add(new Subject());
        subjects.add(new Subject());
        subjects.add(new Subject());
        subjects.add(new Subject());
        subjects.add(new Subject());
        return subjects;
    }

    public void loadSubject() {

        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://api.douban.com/v2/movie/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mSubjectService = mRetrofit.create(SubjectService.class);
        }

        mIsLoading = true;

        Observable<HttpResult<List<Subject>>> topMovie = mSubjectService.getTopMovie(mData.size(), 20);
        topMovie
                .map(new Func1<HttpResult<List<Subject>>, List<Subject>>() {
                    @Override
                    public List<Subject> call(HttpResult<List<Subject>> listHttpResult) {
                        return listHttpResult.getSubjects();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Subject>>() {
                    @Override
                    public void call(List<Subject> subjects) {
                        mData.addAll(subjects);

                        mRv.getAdapter().notifyDataSetChanged();
                        mSrl.setRefreshing(false);

                        mIsLoading = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "call: error", throwable);
                        mRetrofit = null;
                        mSubjectService = null;
                        mSrl.setRefreshing(false);
                        Utoast.show(MainActivity.this, "网络异常:" + throwable.getMessage());

                        mIsLoading = false;
                    }
                })
        ;
    }

    static class
    SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {
        List<Subject> mSubjects;
        private Context mContext;

        public SubjectAdapter(Context context, List<Subject> subjects) {
            mContext = context;
            mSubjects = subjects;
        }

        @Override
        public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);
            return new SubjectHolder(inflate);
        }

        @Override
        public void onBindViewHolder(SubjectHolder holder, int position) {
            Subject subject = mSubjects.get(position);
            holder.tvTitle.setText(subject.getTitle());
            holder.tvYear.setText(subject.getYear());
            Subject.ImagesBean images = subject.getImages();
            if(images != null){
                String small = images.getSmall();
                if (!TextUtils.isEmpty(small)) {
                    Glide.with(mContext)
                            .load(small)
                            .placeholder(R.mipmap.ic_launcher)
                            .centerCrop()
                            .crossFade()
                            .into(holder.ivThumb);
                }
            }


        }

        @Override
        public int getItemCount() {
            return mSubjects.size();
        }

        static class SubjectHolder extends RecyclerView.ViewHolder {
            View view;
            TextView tvTitle;
            TextView tvYear;
            ImageView ivThumb;

            public SubjectHolder(View itemView) {
                super(itemView);
                view = itemView;
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvYear = (TextView) view.findViewById(R.id.tv_year);
                ivThumb = (ImageView) view.findViewById(R.id.iv_thumb);
            }
        }
    }
}

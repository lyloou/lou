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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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


    @Bind(R.id.rv)
    RecyclerView mRv;
    @Bind(R.id.srl)
    SwipeRefreshLayout mSrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBaseContext();
        ButterKnife.bind(this);
        initView();
    }
    List<Subject> mData;
    private void initView() {
        mData = getData();
        mRv.setAdapter(new SubjectAdapter(this, mData));
        mRv.setLayoutManager(new LinearLayoutManager(this));


        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mData.clear();
                mRv.getAdapter().notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "正在加载，请稍后。。。", Toast.LENGTH_SHORT).show();

                getMovies();
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

    static class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {
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
        }

        @Override
        public int getItemCount() {
            return mSubjects.size();
        }

        static class SubjectHolder extends RecyclerView.ViewHolder {
            View view;
            TextView tvTitle;

            public SubjectHolder(View itemView) {
                super(itemView);
                view = itemView;
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
            }
        }
    }

    public void getMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        MovieService movieService = retrofit.create(MovieService.class);
        Observable<HttpResult<List<Subject>>> topMovie = movieService.getTopMovie(20, 40);
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
                        // mTv.setText(Arrays.toString(subjects.toArray()));
                        mData.addAll(subjects);

                        mRv.getAdapter().notifyDataSetChanged();
                        mSrl.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // mTv.setText(throwable.getMessage());
                    }
                })
        ;
    }
}

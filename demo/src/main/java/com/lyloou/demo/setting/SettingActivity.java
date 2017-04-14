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

package com.lyloou.demo.setting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyloou.demo.R;
import com.lyloou.lou.activity.LouActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.04.13 14:44
 * <p>
 * Description:
 */
public class SettingActivity extends LouActivity {
    @BindView(R.id.tv_show_img)
    TextView mTvShowImg;
    @BindView(R.id.iv_img)
    ImageView mIvImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvShowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showImg();
                // loadImgByGlideFromWeb();
                getIps2();
            }
        });
    }

    private void getIps2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ip-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        IpService ipService = retrofit.create(IpService.class);
        Observable<IpDetail> call = ipService.getIp2("104.168.94.8");
        call
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<IpDetail>() {
                    @Override
                    public void call(IpDetail ipDetail) {
                        System.out.println("睡眠2秒---> 开始");
                        SystemClock.sleep(2000);
                        System.out.println("睡眠2秒---> 结束");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IpDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(IpDetail ipDetail) {
                        System.out.println(Thread.currentThread().getName()+" <-------");
                        System.out.println(ipDetail.getCity());
                    }
                });
    }

    private void getIps1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ip-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
//                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        IpService ipService = retrofit.create(IpService.class);
        Call<ResponseBody> call = ipService.getIp("104.168.94.8");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.raw());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadImgByGlideFromWeb() {
        Observable.just("https://avatars3.githubusercontent.com/u/66577?v=3&s=40")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        System.out.println("1:" + Thread.currentThread().getName());
                        try {
                            return Glide.with(mContext).load(s).asBitmap().centerCrop().into(200, 200).get();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        System.out.println("2:" + Thread.currentThread().getName());
                        mIvImg.setImageBitmap(bitmap);
                    }
                });
    }

    private void showImg() {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                System.out.println("1:" + Thread.currentThread().getName());
                SystemClock.sleep(3000);
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        System.out.println("2:" + Thread.currentThread().getName());
                        mIvImg.setImageBitmap(bitmap);
                    }
                });
    }
}

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

package com.lyloou.rxjava;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.04.14 12:22
 * <p>
 * Description:
 */
public class Main {
    Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onCompleted() {
            System.out.println("observer completed");

        }

        @Override
        public void onError(Throwable e) {
            System.out.println("observer error");
        }

        @Override
        public void onNext(String s) {
            System.out.println("observer next:" + s);

        }
    };

    Subscriber<String> mSubscriber = new Subscriber<String>() {

        @Override
        public void onCompleted() {
            System.out.println("subscriber completed");
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("subscriber error");

        }

        @Override
        public void onNext(String s) {
            System.out.println("subscriber next:" + s);

        }
    };

    Observable mObservable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("Hi");
            subscriber.onNext("Hello");
            subscriber.onNext("World");
            subscriber.onCompleted();
        }
    });

    public static void main(String[] args) {
        justrestrofit3();
//        justrxjava();
    }

    private static void justrxjava() {
//        Main main = new Main();
//        main.mObservable.subscribe(main.mObserver);

//        main.mObservable.subscribe(main.mSubscriber);

//        Observable.just("1", "2", "3").subscribe(main.mObserver);
//        System.out.println();
//
//        Observable.just("a", "b", "c").subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                System.out.println(s);
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }, new Action0() {
//            @Override
//            public void call() {
//                System.out.println("===>end");
//            }
//        });
        Thread thread = Thread.currentThread();
        System.out.println("1" + thread.getName());
        System.out.println();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("2" + Thread.currentThread().getName());
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onNext("4");
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("hello:" + s);
                        System.out.println("3" + Thread.currentThread().getName());
                    }
                });
    }


    private static void justrestrofit2() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://ip-api.com")
                .build();
        IpService ipService = retrofit.create(IpService.class);
        Call<IpDetail> ipDetail = ipService.getIpDetail("104.194.84.57");
        ipDetail.enqueue(new Callback<IpDetail>() {
            @Override
            public void onResponse(Call<IpDetail> call, Response<IpDetail> response) {
                System.out.println(response.raw());
            }

            @Override
            public void onFailure(Call<IpDetail> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static void justrestrofit3() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://ip-api.com")
                .build();
        System.out.println("--------1");
        IpService ipService = retrofit.create(IpService.class);
        Observable<IpDetail> ipDetail2 = ipService.getIpDetail2("104.194.84.57");
        ipDetail2
                .doOnNext(new Action1<IpDetail>() {
                    @Override
                    public void call(IpDetail ipDetail) {
                        System.out.println("-------------next onNext  ==>" + Thread.currentThread().getName());
                        System.out.println(ipDetail);
                    }
                })
                .observeOn(Schedulers.immediate())
                .subscribe(new Observer<IpDetail>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("-------------completd  ==>" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                        System.out.println("-------------error  ==>" + Thread.currentThread().getName());
//                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(IpDetail ipDetail) {
                        System.out.println("-------------next  ==>" + Thread.currentThread().getName());
                        System.out.println(ipDetail);
                    }
                });
    }

}

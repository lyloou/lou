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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

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
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedule(new Action0() {
            @Override
            public void call() {
                System.out.println("aaaaaaaaa" + worker.now());
                try {
                worker.schedule(this);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("111111111" + worker.now());
            }
        });
    }

    private static void single2() {
        Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                printCurrentThreadName(0);
                singleSubscriber.onSuccess("Hello World");
//                singleSubscriber.onError(new NullPointerException("Who"));
            }
        })

                .mergeWith(Single.create(new Single.OnSubscribe<String>() {
                    @Override
                    public void call(SingleSubscriber<? super String> singleSubscriber) {
                        printCurrentThreadName(1);
                        singleSubscriber.onSuccess("yyy");
                    }
                }))
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        printCurrentThreadName(2);
                        System.out.println(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                });
    }

    private static void archive() {
//        single2();
        //        single1();

//        behaviorSubject();
//        justrestrofit3();
//        justrxjava();
//        testArr_Error_Retry();
//        testObject();

//        testRepeat();
//        testrepeat();

//        testInterval_toBlocking();

        // manipulateThread();

        // testCreate();

        // observeSeveralTimes();
    }

    private static void single1() {
        Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                // singleSubscriber.onSuccess("hi");
                singleSubscriber.onError(new NullPointerException("wowo"));
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("a");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(String s) {
                System.out.println(">>>" + s);
            }
        });
    }

    private static void behaviorSubject() {
        BehaviorSubject<String> aDefault = BehaviorSubject.create("11default");
        aDefault.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });
    }

    private static void observeSeveralTimes() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onStart();
                subscriber.onNext(1);
                subscriber.onNext(3);
                subscriber.onNext(2);
                subscriber.onNext(4);
                printCurrentThreadName(3);

                subscriber.onCompleted();

            }
        })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        // printCurrentThreadName(0);
                        // System.out.println("on Next");
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        printCurrentThreadName(1);
                        System.out.println("on Subscribe");
                    }
                })
                .subscribeOn(Schedulers.computation())
                // .observeOn(Schedulers.immediate())
                .toBlocking()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                })
        ;
    }

    private static void testCreate() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onStart();
                subscriber.onNext(3);
                subscriber.onNext(2);
                subscriber.onNext(-1);
                subscriber.onCompleted();
            }
        })

                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        printCurrentThreadName(0);
                        System.out.println(integer);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        printCurrentThreadName(1);
                        System.out.println(throwable.getMessage());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        printCurrentThreadName(2);
                        System.out.println("compelete");
                    }
                })
//                .subscribe(new Observer<Integer>() {
//
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        System.out.println(integer);
//                    }
//                })
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                        System.out.println("start");
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("completed");
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        System.out.println("next:" + integer);
//                    }
//                })
        ;
    }

    // 线程控制；
    private static void manipulateThread() {
        Observable.just(1)
                //.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        printCurrentThreadName(1);
                        return integer + (-1);
                    }
                })
                .observeOn(Schedulers.newThread())
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        printCurrentThreadName(2);
                        return integer + (-1);
                    }
                })
                .observeOn(Schedulers.immediate())
                .toBlocking()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        printCurrentThreadName(0);
                        System.out.println(integer);
                    }
                });
    }

    private static void printCurrentThreadName(int i) {
        System.out.println(i + "【" + Thread.currentThread().getName() + "】");
    }

    private static void testInterval_toBlocking() {
        Observable.interval(3, TimeUnit.SECONDS)
                .doOnNext(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.printf("hello %s in %s \n", aLong, Thread.currentThread().getName());
                    }
                })
                .subscribeOn(Schedulers.io())
                .toBlocking()
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        String name = Thread.currentThread().getName();
                        System.out.println("thread:" + name + ": " + aLong);
                    }
                })
        ;
    }

    private static void testrepeat() {
        String arr[] = {"a", "b", "c", "d"};
        Observable<Integer> range = Observable.range(0, arr.length);
        range.doOnNext(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                range.repeat(3).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(arr[integer]);
                    }
                });
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
            }
        });
    }

    private static void testRepeat() {
        Observable.just("StrIing")
                .repeat(3)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("-->" + s);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    private static void testArr_Error_Retry() {
        String arr[] = {"a", "b", "c", "d"};
        Observable<Integer> range = Observable.range(0, arr.length);
        range
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        if (integer == 2) {
                            return Observable.error(new Throwable("error!!!"));
                        }
                        return Observable.just(integer);
                    }
                })
                .retry(3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(arr[integer]);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable != null) {
                            range.retry(3);
                        }
                        System.out.println(throwable.getMessage());
                    }
                });
    }

    private static void testObject() {
        Observable.just("a", 1, 2, "b", "bbb", 2.3f, 2.33, new Main()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                boolean r = o instanceof Integer;
                System.out.println(o.toString() + ": " + r);
            }
        });
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
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
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

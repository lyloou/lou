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

package com.lyloou.test.rxjava2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.09 15:17
 * <p>
 * Description:
 */
public class RxJavaTest {
    public static void main(String[] args) throws InterruptedException {
        Observable.just("hello, world").subscribe(s -> System.out.println(s));
    }

    private static void sampleB() {
        Single
                .create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(SingleEmitter<String> e) throws Exception {
                        System.out.println("start:");
                        e.onSuccess("a");
                    }
                })
                .doOnSuccess(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("next:"+s);
                        System.out.println("1:"+s);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("2:"+s);
                    }
                })
        ;
    }

    private static void sampleA() throws InterruptedException {
        // subscribeOn: 只对创建 Observalbe 有影响；不可多次转换，只有第一个生效；
        // observeOn: 影响后续的 doOnNext程序段和 subscribe程序段；可多次转换，
        final CountDownLatch latch = new CountDownLatch(1);
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        String name = Thread.currentThread().getName();
                        System.out.println(name);
                        e.onNext("hello, world");
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String name = Thread.currentThread().getName();
                        System.out.println(name + " 1:" + s);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String name = Thread.currentThread().getName();
                        System.out.println(name + " 1-1:" + s);
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String name = Thread.currentThread().getName();
                        System.out.println(name + " 1-0:" + s);
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String name = Thread.currentThread().getName();
                        System.out.println(name + " 1-2:" + s);
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String name = Thread.currentThread().getName();
                        System.out.println(name + " 2:" + s);
                    }
                })

                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String name = Thread.currentThread().getName();
                        System.out.println(name + " 3:" + s);
                        latch.countDown();
                    }
                });
        latch.await();
    }

    private static void sample9() {
        // http://www.vogella.com/tutorials/RxJava/article.html#caching-values-of-completed-observables
        Single<List<String>> single = Single.create(new SingleOnSubscribe<List<String>>() {
            @Override
            public void subscribe(SingleEmitter<List<String>> e) throws Exception {
                ArrayList<String> t = new ArrayList<>();
                t.add("a");
                t.add("b");
                t.add("c");
                t.add("d");
                t.add("e");
                t.add("f");
                t.add("g");
                System.out.println("call single");
                e.onSuccess(t);
            }
        });
        single.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                System.out.println("1-0:" + Arrays.toString(list.toArray()));
            }
        });
        single.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                System.out.println("1-1:" + Arrays.toString(list.toArray()));
            }
        });
        single.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                System.out.println("1-2:" + Arrays.toString(list.toArray()));
            }
        });

        System.out.println("===================");

        Single<List<String>> cache = single.cache();
        cache.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                System.out.println("2-1:" + Arrays.toString(list.toArray()));
            }
        });
        cache.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                System.out.println("2-1:" + Arrays.toString(list.toArray()));
            }
        });
        cache.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                System.out.println("2-2:" + Arrays.toString(list.toArray()));
            }
        });
        /*
        call single
        1-0:[a, b, c, d, e, f, g]
        call single
        1-1:[a, b, c, d, e, f, g]
        call single
        1-2:[a, b, c, d, e, f, g]
        ===================
        call single
        2-1:[a, b, c, d, e, f, g]
        2-1:[a, b, c, d, e, f, g]
        2-2:[a, b, c, d, e, f, g]
        *///~
    }

    private static void sample8() throws InterruptedException {
        //        final CountDownLatch latch = new CountDownLatch(0);
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello, world");
                try {
                    Thread.sleep(1000);
                    latch.countDown();
                    System.out.println("hello, world2");
                    Thread.sleep(3000);
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        latch.await();
    }

    private static void sample7() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Single<String> s1 = Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                e.onSuccess("a");
            }
        });
        Single<String> s2 = Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                e.onSuccess("b");
            }
        });
        compositeDisposable.add(s1.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        }));
        compositeDisposable.add(s2.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        }));
        compositeDisposable.dispose();
    }

    private static void sample6() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("abcd");
            }
        });
        Disposable subscribe = observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
        Disposable subscribe2 = observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
        DisposableObserver<String> disposableObserver = observable.subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        subscribe.dispose();
        System.out.println(subscribe.isDisposed());
        System.out.println(subscribe2.isDisposed());
        System.out.println(disposableObserver.isDisposed());
    }

    private static void sample5() throws InterruptedException {
        // [rx java - How to use RxJava Interval Operator - Stack Overflow](https://stackoverflow.com/questions/37652952/how-to-use-rxjava-interval-operator)
        CountDownLatch latch = new CountDownLatch(1);
        Observable
                .interval(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("#" + aLong);
                    }
                });
        latch.await();
    }

    private static void sample4() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    int c = 1 / 0;
                    e.onComplete();
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        }).subscribe(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("Completed!");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println(throwable.getMessage());
            }
        });
    }

    private static void sample3() {
        // 没有onComplete()方法
        Single<String> single = Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                try {
                    int c = 1 / 0;
                    e.onSuccess("11");
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });

        single.subscribe(
                new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                }
                , new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }

    private static void sample2() {
        Maybe<List<String>> maybe = Maybe.create(new MaybeOnSubscribe<List<String>>() {
            @Override
            public void subscribe(MaybeEmitter<List<String>> e) throws Exception {
                try {
//                    List<String> s = new ArrayList<String>();
//                    int c = 1/0;

                    List<String> s = new ArrayList<String>();
                    s.add("1");
                    s.add("2");
                    s.add("3");

                    if (s != null && !s.isEmpty()) {
                        e.onSuccess(s);
                    } else {
                        e.onComplete();
                    }
                } catch (Exception ex) {
                    e.onError(ex);
                }

            }
        });
        maybe.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                System.out.println(Arrays.toString(list.toArray()));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("Complete!");
            }
        });
    }

    private static void sample1() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        for (int i = 0; i < 10; i++) {
                            e.onNext("#" + i);
                        }
                        e.onComplete();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                })

        ;
    }
}

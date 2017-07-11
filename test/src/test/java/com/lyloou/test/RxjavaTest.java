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

package com.lyloou.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertTrue;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.07.11 11:18
 * <p>
 * Description:
 */
public class RxjavaTest {
    String result = "";

    @Test
    public void testNull() {
        System.out.println(null instanceof Object);
    }

    // Simple subscription to a fix value
    @Test
    public void returnAValue() {
        result = "";
        Observable<String> observer = Observable.just("Hello"); // provides datea
        observer.subscribe(s -> result = s); // Callable as subscriber
        assertTrue(result.equals("Hello"));
    }

    @Test
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("0" + Thread.currentThread().getName());
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onComplete();
            }
        })

//        Observable
//                .fromCallable(new Callable<Integer>() {
//                    @Override
//                    public Integer call() throws Exception {
//                        System.out.println("a"+Thread.currentThread().getName());
//
//                        return 4;
//                    }
//                })
//                .observeOn(Schedulers.computation()) // 不涉及多线程的时候，测试是可以直接通过的（不会省略）。
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        System.out.println("b" + Thread.currentThread().getName());
                        System.out.println("aaa");
                    }
                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("c" + Thread.currentThread().getName());
                        System.out.println("latch" + latch.getCount());
//                        latch.countDown();
                        System.out.println(integer);
                    }
                });

//        latch.await();
    }

    private void waitAMoment() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("subscribe" + Thread.currentThread().getName());
    }

    @Test
    public void rxjava1() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        e.onNext("a");
                        waitAMoment();
                        e.onNext("b");
                        waitAMoment();
                        e.onNext("c");
                        waitAMoment();
                        e.onNext("d");
                        waitAMoment();
                        e.onNext("e");
                        waitAMoment();
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        System.out.println("next" + Thread.currentThread().getName());
                        System.out.println(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("ok");
                        System.out.println("complete" + Thread.currentThread().getName());
                    }
                });
        latch.await();
    }

    @Test
    public void rxjava2() throws InterruptedException {
        String item = "a,1 b,2 c,3 d,4 e,5 f,6 g,7 h,8 i,9 j,10 k,11 l,12 m,13 n,14";
        Observable.just(item)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        String[] split = s.split(" ");
                        return Observable.fromArray(split);
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return item.indexOf(s) + ":" + s;
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        return Observable.fromArray(s.split(","));
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println(s);
                    }
                });

        Observable<String> observable = Observable.fromArray(item.split(" "));
        observable.lift(new ObservableOperator<String, String>() {
            @Override
            public Observer<? super String> apply(@NonNull Observer<? super String> observer) throws Exception {
                return new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        observer.onNext("what:" + s);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    @Test
    public void rxjava3() {
        Integer numbers[] = {14, 9, 5, 2, 10, 13, 4};
        List<Integer> nums = new ArrayList<>();
        Observable.fromArray(numbers).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {
                return integer % 2 == 0;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                nums.add(integer);
                System.out.println(integer);
                System.out.println(nums.size());
            }
        });
    }
}

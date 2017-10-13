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

package com.lyloou.rxjava2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class RxJava2Test {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("say/hi");

        sample9();
    }

    private static void sample9() {
        Observable.just(1, 2).doFinally(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("finally");
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    private static void sample8() {
        Observable.just(1, 2, 3, 4, 5).doOnEach(new Consumer<Notification<Integer>>() {
            @Override
            public void accept(@NonNull Notification<Integer> integerNotification) throws Exception {
                Integer value = integerNotification.getValue();
                if (value != null) {
                    System.out.print(value % 2 == 0 ? "🙂：" : "😣：");
                }
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("Game Over！");
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    private static void sample7() {
        Observable.just(3).delay(3, TimeUnit.SECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println(integer);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void sample6() {
        Observable<Integer> ob1 = Observable.range(1, 7);
        Observable<Integer> ob2 = Observable.range(9, 8);
        Observable.combineLatest(ob1, ob2, new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                return "" + integer + integer2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    private static void sample5() {
        Observable<Integer> ob1 = Observable.range(1, 800);
        Observable<Integer> ob2 = Observable.range(1, 160);
        Observable.merge(ob2, ob1).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    private static void sample4() {
        Observable.just(1, 2, 3, 4, 5).throttleFirst(10, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("==> throttle first");
                        System.out.println(integer);
                    }
                });
    }

    private static void sample3() {
        Observable.range(1, 3).map(new Function<Integer, Long>() {
            @Override
            public Long apply(@NonNull Integer integer) throws Exception {
                return Long.valueOf(integer);
            }
        }).cast(Long.class).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    private static void sample2() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        Observable<Long> interval = Observable.interval(30, TimeUnit.MICROSECONDS);
        interval.subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                if (latch.getCount() == 0) {
                    return;
                }
                System.out.println(aLong);
                latch.countDown();
            }
        });
        latch.await();
    }

    private static void sample1() {
        Observable.just(1, 2, 3, 4).take(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }
}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.04.14 12:01
 * <p>
 * Description:
 */
public class HelloWorld {
    private static String[] IMGS = {
            "a.png",
            "b.png",
            "c.png",
            "d.png",
            "e.png",
            "f.png",
            "g.png",

            "a.jpg",
            "b.jpg",
            "c.jpg",
            "d.jpg",
            "e.jpg",
            "f.jpg",
            "g.jpg",

            "a.bpm",
            "b.bpm",
            "c.bpm",
            "d.bpm",
            "e.bpm",
            "f.bpm",
            "g.bpm",
    };

    public static void main(String[] args) {
        // hello(IMGS);
        // getJpg();
        // hello2();
        // testFlapMap();

    }

    private static void testFlapMap() {
        List<String> iterable = Arrays.asList("a", "b", "c", "d");
        Observable<List<String>> observable = Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                subscriber.onNext(new ArrayList<String>(iterable));
                subscriber.onNext(new ArrayList<String>(iterable));
                subscriber.onCompleted();
            }
        });

        observable
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> list) {
                        return Observable.from(list);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("from action1:" + s);
                    }
                });
    }

    private static void hello2() {
        Observable.just("Hello World")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });

        Observable.just("Hello World")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return Integer.toString(integer);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    private static void hello(String... names) {
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("Hello " + s + "!");
            }
        });

    }

    private static void printCurrentThreadName(String s) {

        System.out.println("Thread-[" + Thread.currentThread().getName() + "]:" + s);
    }

    private static void getJpg() {
        //printCurrentThreadName();
        Observable.from(IMGS)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        // printCurrentThreadName(s);
                        return s.endsWith(".jpg");
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        printCurrentThreadName(s);
                        System.out.println("first:" + s);
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        printCurrentThreadName(s);
                        System.out.println("second:" + s);
                    }
                })
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        printCurrentThreadName(s);
                        System.out.println("from subscribe:" + s);
                    }
                });
    }

}

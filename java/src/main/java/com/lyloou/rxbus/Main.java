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

package com.lyloou.rxbus;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Main {
    private static Disposable subscribe;

    public static void main(String[] args) {
        registe();

        RxBus.getInstance().post("今日宴客，你们猜，谁会来的最晚");

        new Thread(new Runnable() {
            @Override
            public void run() {
                RxBus.getInstance().post("👇");
            }
        }, "线程2").start();

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RxBus.getInstance().post("不是我");
            }
        });

        service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RxBus.getInstance().post("肯定不是我");
                service.shutdown();
                System.out.println("后门悄悄的被关了");
                return "嘿嘿😁";
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RxBus.getInstance().post("不好意思，我来迟了");


                // 就你最慢，把门关了
                if (subscribe != null && !subscribe.isDisposed()) {
                    subscribe.dispose();
                    System.out.println("砰的一声，大门关了");
                }
            }
        }).start();

        RxBus.getInstance().post("DJ，迪斯科，迪斯卡，D，J，跳起来");
    } /* Output:
    声音来自:main，说：今日宴客，你们猜，谁会来的最晚
    声音来自:线程2，说：👇
    声音来自:main，说：DJ，迪斯科，迪斯卡，D，J，跳起来
    声音来自:pool-1-thread-1，说：不是我
    声音来自:pool-1-thread-1，说：肯定不是我
            后门悄悄的被关了
    声音来自:Thread-0，说：不好意思，我来迟了
    砰的一声，大门关了

    Process finished with exit code 0

    *///~

    private static void registe() {
        subscribe = RxBus.getInstance().toObservalbe(String.class).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                System.out.print("声音来自:" + Thread.currentThread().getName());
                System.out.println("，说：" + s);
            }
        });
    }
}

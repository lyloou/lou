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

package com.lyloou.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    private volatile int a = 0;
    private volatile int c = 0;
    private volatile boolean b;

    public static void main(String[] args) throws Exception {
        sample8();
    }

    private static void sample8() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            String s = service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    System.out.println(Thread.currentThread().getName());
                    return "ok";
                }
            }).get();
            System.out.println(s);

            System.out.println(Thread.currentThread().getName());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void sample7() {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        class DelayObject implements Delayed {

            @Override
            public long getDelay(TimeUnit unit) {
                return 0;
            }

            @Override
            public int compareTo(Delayed o) {
                return 0;
            }
        }

        BlockingQueue<DelayObject> queue2 = new DelayQueue<DelayObject>();

        BlockingQueue<String> queue3 = new LinkedTransferQueue<>();
        BlockingQueue<String> queue4 = new ArrayBlockingQueue<String>(30);
    }


    private static void sample6() {
        Main main = new Main();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (main.c < 100) {
                    System.out.println("from1:" + main.c++);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (main.c < 100) {
                    System.out.println("from2:" + main.c++);
                }
            }
        }).start();
    }

    private static void sample5() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                System.out.println("count:" + latch.getCount());
                latch.countDown();
                System.out.println("count:" + latch.getCount());
                latch.countDown();
                System.out.println("count:" + latch.getCount());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("2222");
            }
        }).start();
        System.out.println("111");
        latch.await();
    }

    private static void sample4() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println("====2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("====>" + Thread.activeCount());
        if (Thread.activeCount() > 2) {
            Thread.yield();
        }
    }

    private static void sample3() {
        Main main = new Main();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!main.b) {
                    System.out.println("bb");
                    main.testSynchronized();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (main.a < 20) { // 如果a不用volatile修饰，那么就会进入死循环了。
                }
                main.b = true;
            }
        }).start();

    }

    private static void sample2() {
        Main main = new Main();
        for (int i = 0; i < 200; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    main.testSynchronized();
                }
            }).start();
        }
    }

    private static void sample1() throws ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        Future<String> future = service.submit(new Callable<String>() {
            @Override
            public String call() {
                System.out.println("----------1");
                // service.shutdown();
                Thread.currentThread().interrupt();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                    System.out.println("InterruptedException in thread pool Thread!");
                    return "error";
                }
                System.out.println("----------2");
                printCurrentThread();
                return "hi";
            }
        });

        Thread.currentThread().interrupt();

        try {
            String fstr = "==>future is done?";

            System.out.println(fstr + future.isDone());
            System.out.println(future.get());
            System.out.println(fstr + future.isDone());
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println("InterruptedException in main Thread!");
            Thread thread = Thread.currentThread();
            thread.resume();
        }

        String sstr = "==>pool is shutdown?";
        System.out.println(sstr + service.isShutdown());
        service.shutdown();
        System.out.println(sstr + service.isShutdown());

        printCurrentThread();
    }

    private static void printCurrentThread() {
        Thread thread = Thread.currentThread();
        System.out.println("thread:" + thread.getName());
        System.out.println("interrupted:" + thread.isInterrupted());
    }

    private synchronized void testSynchronized() {
        try {
            // System.out.println(a++);
            Thread.sleep(10);
            System.out.println(a++);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

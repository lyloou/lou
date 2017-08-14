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

package com.lyloou.java8;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.System.out;

public class ThreadMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> "a",
                () -> "b",
                () -> "c",
                () -> "d"
        );
        executor.invokeAll(callables)
                .stream()
                .map(stringFuture -> {
                    try {
                        return stringFuture.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);
    }

    private static void sample2() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                return "hi";
            }
        };
        Future<String> submit = executor.submit(task);
        out.println("done? " + submit.isDone());
        String result = null;
        try {
            result = submit.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
            try {
                result = submit.get(899, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e1) {
                e1.printStackTrace();
            }
        }
        out.println("done? " + submit.isDone());
        out.println(result);
    }

    private static void sample1() {
        Runnable runnable = () -> {
            String tName = Thread.currentThread().getName();
            out.println("Hello:" + tName);
        };

        runnable.run();

        new Thread(runnable).start();
        out.println("Done!");
    }
}

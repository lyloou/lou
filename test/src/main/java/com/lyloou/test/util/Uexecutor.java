package com.lyloou.test.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Uexecutor {

    private static final int N_THREADS = 3;

    private static ExecutorService fixedExecutorService;
    private static ExecutorService cachedExecutorService;

    public static ExecutorService getFixedThreadPool() {
        if (fixedExecutorService == null) {
            synchronized (Uexecutor.class) {
                if (fixedExecutorService == null) {
                    fixedExecutorService = Executors.newFixedThreadPool(N_THREADS);
                }
            }
        }
        return fixedExecutorService;
    }


    public static ExecutorService getCachedThreadPool() {
        if (cachedExecutorService == null) {
            synchronized (Uexecutor.class) {
                if (cachedExecutorService == null) {
                    cachedExecutorService = Executors.newCachedThreadPool();
                }
            }
        }
        return cachedExecutorService;
    }
}

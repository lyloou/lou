package com.lyloou.effective_java2;

import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class P {
    public static void main(String[] args) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("aaaaaaaaaaaaa:"+new Date());
            }
        }, 3000, 1000);
    }

    private static void p52() {
        P p = new P();
        new Thread(() -> {
            p.doThreadLocal("a");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread());
            System.out.println(p.getThreadLocal());
            System.out.println(p.getThreadLocal());
        }).start();

        new Thread(() -> {
            p.doThreadLocal("b");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread());
            System.out.println(p.getThreadLocal());
            System.out.println(p.getThreadLocal());
        }).start();
    }

    private java.lang.ThreadLocal<String> threadLocal;

    private void doThreadLocal(String a) {
        threadLocal = new java.lang.ThreadLocal<>();
        System.out.println("set : " + a);
        threadLocal.set(a);
    }

    private String getThreadLocal() {
        if (threadLocal == null) {
            return null;
        }
        return threadLocal.get();
    }

    private static void p48() {
        System.out.println(1.03 - 0.42);
    }

    private static void p49() {
        Comparator<Integer> naturalOrder = new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                return first < second ? -1 : (first == second ? 0 : 1);
            }
        };

        int compare = naturalOrder.compare(45, 45);
        System.out.println(compare);
    }

    private static void p50() {
        ThreadLocal<String> local = new ThreadLocal<>();
        local.set("hi");
        System.out.println(local.get());
    }

    public static final class ThreadLocal<T> {
        T t;

        public ThreadLocal() {
        }

        public void set(T t) {
            this.t = t;
        }

        public T get() {
            return t;
        }
    }
}

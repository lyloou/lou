package com.lyloou.effective_java2;

import java.util.Random;

public class RandomInteger {
    public static void main(String[] args) {
        int n = 2 * (Integer.MAX_VALUE / 3);
        int low = 0;
        for (int i = 0; i < 1000000; i++) {
            if (random(n) < n / 2) {
                low++;
            }
        }

        System.out.println(low);
    }

    private static final Random rnd = new Random();

    private static int random(int n) {
        return Math.abs(rnd.nextInt()) % n;
    }
}

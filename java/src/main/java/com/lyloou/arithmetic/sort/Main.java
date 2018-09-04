package com.lyloou.arithmetic.sort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] arr = {3, 2, 8, 12, 4, 9, 11, 18, 1, 0};

        Insert.sort5(arr);
        Insert.sort1(arr);
        Simple.sort(arr);
        Select.sort(arr);
        Bubble.sort(arr);

        Shell.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}

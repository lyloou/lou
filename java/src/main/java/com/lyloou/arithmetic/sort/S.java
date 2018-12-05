package com.lyloou.arithmetic.sort;

import java.util.Arrays;

public class S {
    public static void main(String[] args) {
        int[] arr = {3, 2, 8, 12, 4, 9, 11, 18, 1, 0};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    // 按增量序列来分割，分割后按照插入排序的方式执行，
    // h(k)个独立的子数组执行一次插入排序。
    static void sort(int[] arr) {
        int j;
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                int tmp = arr[i];
                for (j = i; j >= gap && tmp < arr[j - gap]; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = tmp;
            }
        }
    }
}

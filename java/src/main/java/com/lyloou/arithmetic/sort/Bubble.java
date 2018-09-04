package com.lyloou.arithmetic.sort;

import static com.lyloou.arithmetic.sort.Tool.swapArr;

public class Bubble {
    static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[j] < arr[j - 1]) {
                    swapArr(arr, j, j - 1);
                }
            }
        }
    }
}

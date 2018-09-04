package com.lyloou.arithmetic.sort;

import static com.lyloou.arithmetic.sort.Tool.swapArr;

public class Select {
    static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[min] > arr[j]) {
                    min = j;
                }
            }
            swapArr(arr, i, min);
        }
    }
}

package com.lyloou.arithmetic.sort;

import static com.lyloou.arithmetic.sort.Tool.swapArr;

public class Simple {
    static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    swapArr(arr, i, j);
                }
            }
        }
    }
}

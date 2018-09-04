package com.lyloou.arithmetic.sort;

class Tool {
    static void swapArr(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}

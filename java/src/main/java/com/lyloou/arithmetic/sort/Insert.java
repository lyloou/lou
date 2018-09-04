/*
 * Copyright  (c) 2018 Lyloou
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

package com.lyloou.arithmetic.sort;

import static com.lyloou.arithmetic.sort.Tool.swapArr;

public class Insert {

    static void sort5(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i;
            for (; j > 0 && tmp < arr[j - 1]; j--) {
                arr[j] = arr[j - 1];
            }
            arr[j] = tmp;
        }
    }

    private static void sort4(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                swapArr(arr, j, j + 1);
            }
        }
    }

    private static void sort3(int[] arr) {
        int j;
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];
            for (j = i; j > 0 && tmp > arr[j - 1]; j--) {
                arr[j] = arr[j - 1];
            }
            arr[j] = tmp;
        }
    }

    private static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i;
            for (; j > 0; j--) {
                if (tmp > arr[j - 1]) { // 等同于：j>0 && tmp <= arr[j-1]
                    break;
                }
                arr[j] = arr[j - 1];
            }
            arr[j] = tmp;
        }
    }


    private static void sort2(int[] arr) {
        int j;
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];
            for (j = i; j > 0 && tmp > arr[j - 1]; j--) {
                arr[j] = arr[j - 1];
            }
            arr[j] = tmp;
        }
    }


    static void sort1(int[] arr) {
        int j;
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];
            for (j = i; j > 0 && tmp < arr[j - 1]; j--) {
                arr[j] = arr[j - 1];
            }
            arr[j] = tmp;
        }
    }

}


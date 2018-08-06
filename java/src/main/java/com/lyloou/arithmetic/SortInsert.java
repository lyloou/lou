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

package com.lyloou.arithmetic;

import java.util.Arrays;

public class SortInsert {
    public static void main(String[] args) {
        int[] arr = {37, 56, 28, 72, 18, 29, 55};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i;
            for (; j > 0; j--) {
                if (tmp > arr[j-1]){
                        break;
                }
                arr[j] = arr[j-1];
            }
            arr[j] = tmp;
        }
    }

}


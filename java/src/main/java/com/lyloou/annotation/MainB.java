/*
 * Copyright  (c) 2017 Lyloou
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

package com.lyloou.annotation;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.05.19 11:35
 * <p>
 * Description:
 */
public class MainB {
    public static void main(String[] args) {
        try {
            Class<?> test = Class.forName("com.lyloou.annotation.UseB");
            if (test.isAnnotationPresent(AnnotationBs.class)) {
                AnnotationBs bs = test.getAnnotation(AnnotationBs.class);
                System.out.println("========");
                AnnotationB[] value = bs.value();
                for (AnnotationB b : value) {
                    System.out.println("assignee=" + b.assignee() + " effort=" + b.effort() + " finished=" + b.finished());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

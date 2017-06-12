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

package com.lyloou.java8;

class Lambda6 {
        static int outterStaticNum = 2;
        int outterNum = 1;

        void testScope() {
            Converter<Integer, String> stringConverter = (from) -> {
                System.out.println("inner outterNum:"+outterNum);
                outterNum = 2;
                return String.valueOf(from);
            };
            stringConverter.convert(1);
            System.out.println("outterNum:"+outterNum);

            Converter<Integer, String> stringStaticConverter = (from) -> {
                System.out.println("inner outterStaticNum:"+outterStaticNum);
                outterStaticNum = 3;
                return String.valueOf(from);
            };
            stringStaticConverter.convert(1);
            System.out.println("outterStaticNum:"+outterStaticNum);
        }
    }
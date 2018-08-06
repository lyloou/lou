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

package com.lyloou.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class Map {
    static List<String> LIST = Arrays.asList("one Two three Four five Six seven Eight eight seven siX".split(" "));

    public static void main(String[] args) {
        java.util.Map<String, String> collect = LIST.stream().collect(Collectors.toMap(o -> o.toUpperCase(), identity(), (a, b) -> b));
        System.out.println(collect);
    }
}

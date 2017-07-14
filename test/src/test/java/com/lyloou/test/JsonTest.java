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

package com.lyloou.test;


import com.google.gson.JsonObject;

import org.junit.Test;

import java.util.HashMap;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.07.13 19:25
 * <p>
 * Description:
 */
public class JsonTest {
    @Test
    public void test() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("a", "aaa");
        jsonObject.addProperty("b", "bbb");
        System.out.println(String.valueOf(jsonObject));
    }

    @Test
    public void testMap() {
        // 拼接网址
        String url = "http://lyloou.com?a=3";
        HashMap<String, String> maps = new HashMap<>();
        maps.put("b", "1");
        maps.put("c", "2");
        System.out.println(maps.toString());
        System.out.println(maps.entrySet());

        for (String s : maps.keySet()) {
            String s1 = maps.get(s);
            url += "&" + s + "=" + s1;
        }
        System.out.println(url);
    }

}

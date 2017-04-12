/*
 * *****************************************************************************************
 * Copyright  (c) 2017 Lou
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
 * *****************************************************************************************
 */

package com.lyloou.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        gson01();
        gson02();
        gson03_04();
    }

    // http://www.jianshu.com/p/0e40a52c0063
    // http://www.jianshu.com/p/3108f1e44155
    private static void gson03_04() {
        String str = "\"\""; // 测试 json 中的空字符串， 注意不是""
    }

    // http://www.jianshu.com/p/c88260adaf5e
    private static void gson02() {
        Gson gson = new Gson();
        gson.toJson(new User("Lou", 18, "lilou@lyloou.com"), System.out);

        User meiLi = new User("MeiLi", 17);
        gson.toJson(meiLi, System.out);
        System.out.println();
        Gson gson1 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        gson1.toJson(meiLi, System.out);
    }

    // http://www.jianshu.com/p/e740196225a4
    private static void gson01() {
        Gson gson = new Gson();
        String s = gson.toJson(new User("Lou", 18, "lyloou.com"));
        System.out.println(s);

        // 加载对象
        User user1 = gson.fromJson("{\"name\":\"怪盗kidou1\",\"age\":24,\"emailAddress\":\"ikidou@example.com\"}", User.class);
        User user2 = gson.fromJson("{\"name\":\"怪盗kidou2\",\"age\":25,\"email\":\"ikidou@example.com\"}", User.class);
        User user3 = gson.fromJson("{\"name\":\"怪盗kidou3\",\"age\":26,\"email_address\":\"ikidou@example.com\"}", User.class);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);


        // 加载数组对象
        String str = "[\"Android\",\"Java\",\"PHP\"]";
        // 解析为数组
        String[] strArray = gson.fromJson(str, String[].class);
        System.out.println(Arrays.toString(strArray));
        // 解析为list
        List<String> strList = gson.fromJson(str, new TypeToken<List<String>>() {
        }.getType());
        System.out.println(Arrays.toString(strList.toArray()));


        // 加载 Json 字符串
        Result<User> result = new Result<>(1, "ok", new User("Lou", 18, "lyloou.com"));
        System.out.println(gson.toJson(result));
        String response = "{\"code\":1,\"message\":\"ok\",\"data\":{\"name\":\"Lou\",\"age\":25,\"emailAddress\":\"lyloou.com\"}}";
        Result<User> result2 = gson.fromJson(response, new TypeToken<Result<User>>() {
        }.getType());
        System.out.println(result2);
    }

}
